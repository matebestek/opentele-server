package org.opentele.server.model
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.types.MeasurementTypeName
import org.opentele.server.model.types.PatientState
import org.opentele.server.model.types.Sex

class Patient extends AbstractObject {

    static hasMany = [measurements: Measurement, 
        patient2PatientGroups: Patient2PatientGroup, 
        monitorKits: MonitorKit,
        completedQuestionnaires: CompletedQuestionnaire,
        conferences: Conference,
		nextOfKinPersons: NextOfKinPerson,
		endedMonitoringPlans: MonitoringPlan,
		blueAlarmQuestionnaireIDs: Long,
        notes: PatientNote,
        thresholds: Threshold,
        passiveIntervals: PassiveInterval
	]

	MonitoringPlan monitoringPlan //The current monitoring plan, null initially and if patient is discharged

	static mappedBy = [
		endedMonitoringPlans: "patient"
	]

    String firstName
    String lastName
    String cpr
    Sex sex
    String address
    String postalCode
    String city
    String phone
    String mobilePhone
    String email
    String comment

	PatientState state
    User user
    PatientGroup dataResponsible

    boolean isPaused() {
        passiveIntervals.any { it.inInterval() }
    }

    String toString() {
        name
    }
    
    @Deprecated // Use property accessor instead
    String name() {
        name
    }

    String getName() {
        "${firstName} ${lastName}"

    }

    String getFormattedCpr() {
        "${cpr[0..5]}-${cpr[6..9]}"
    }

    static transients = ['formattedCpr','name','latestQuestionnaireUploadDate','numberOfUnreadMessages','groups']
    
    static constraints = {
        cpr(validator: { val, obj ->
            def similarUser = Patient.findByCprIlike(val)
            if (similarUser && obj?.id != similarUser?.id) {
                ["validate.patient.cpr.exists", "CPR"]
            }
            if (val?.length() != 10) {
                ["validate.patient.cpr.length", "CPR"]
            }
            if (!val) {
                ["validate.patient.default.blank", "CPR"]
            }
        })

        patient2PatientGroups(validator: { val, obj ->
            if (obj.dataResponsible != null && !val*.patientGroup?.flatten()?.contains(obj.dataResponsible)) {
                ["validate.patient.dataResponsible", "dataResponsible"]
            }
        })

        firstName(validator: {val, obj -> val != null && !val.equals("") ? true : ["validate.patient.default.blank", "Fornavn"]})
        lastName(validator: {val, obj -> val != null && !val.equals("") ? true : ["validate.patient.default.blank", "Efternavn"]})
        sex(validator: {val, obj -> val != null && !val.equals("") ? true : ["validate.patient.default.blank", "Køn"]})
        address(validator: {val, obj -> val != null && !val.equals("") ? true : ["validate.patient.default.blank", "Adresse"]})
        postalCode(validator: {val, obj -> val != null && !val.equals("") ? true : ["validate.patient.default.blank", "Postnummer"]})
        city(validator: {val, obj -> val != null && !val.equals("") ? true : ["validate.patient.default.blank", "By"]})
        phone(nullable:true)
        mobilePhone(nullable:true)
        email(nullable:true)
        comment(nullable:true, maxSize: 2048)
		user(nullable:true)
        dataResponsible(nullable:true)
		state(validator: {val, obj -> val != null && !val.equals("") ? true : obj.errors.reject("validate.patient.default.blank", ["Tilstand"] as Object[], "i18n Mising")})

        monitoringPlan(nullable:true)

        //Only one threshold of each type
        thresholds( validator: { val, obj ->
            if (val == null) {
                return true
            }
            //Group thresholds by their type
            def groups = val.groupBy {it.type.name}
            //Ensure that there exists only one threshold of each type
            return groups.values().every {it.size() == 1}
        } )
    }
    
    static mapping = {
        thresholds lazy: false,  cascade: "all-delete-orphan"
    }

    static namedQueries = {
        patientSearch { PatientSearchCommand searchCommand ->
            and{
                if (searchCommand.ssn) {
                    ilike("cpr", "%${searchCommand.ssn}%")
                }
                if (searchCommand.firstName) {
                    ilike("firstName", "%${searchCommand.firstName}%")
                }
                if (searchCommand.lastName) {
                    ilike("lastName", "%${searchCommand.lastName}%")
                }
                if (searchCommand.phone) {
                    or {
                        eq("phone", searchCommand.phone)
                        eq("mobilePhone", searchCommand.phone)
                    }
                }
                if (searchCommand.status) {
                    eq("state", searchCommand.status)
                }
                if(searchCommand?.patientGroup?.id) {
                    createAlias('patient2PatientGroups', 'pg')
                    eq("pg.patientGroup", searchCommand.patientGroup)
                }
            }
        }
    }

    /**
     * Add a threshold removing the existing threshold of same type (if any) first.
     * (validation only allows for one threshold of a type to be in the list at once)
     * @param t
     */
    public void setThreshold(Threshold t) {
        thresholds.removeAll { it.type.name == t.type.name }
        thresholds.add(t)
    }

    public Threshold getThreshold(MeasurementType type) {
        getThreshold(type.name)
    }

    public Threshold getThreshold(MeasurementTypeName typeName) {
        thresholds.find {it.type.name.equals(typeName)}
    }

    Date getLatestQuestionnaireUploadDate() {
        CompletedQuestionnaire.createCriteria().get {
            eq('patient', this)
            projections {
                max('uploadDate')
            }
        }
    }

    def getGroups() {
		def patient2PatientGroups = Patient2PatientGroup.findAllByPatient(this)
		def groups = []
		patient2PatientGroups.each { group ->
			groups << group.patientGroup
		}
		groups
	}

    def getNumberOfUnreadMessages() {
        List<Message> unreadMessages = Message.findAllByPatientAndIsRead(this, false, [sort: 'sendDate', order: 'desc'])

        def unreadMessagesFromDepartment = unreadMessages.findAll { !it.sentByPatient }
        def numberOfUnreadFromDepartment = unreadMessagesFromDepartment.size()
        def oldestUnreadFromDepartment = unreadMessagesFromDepartment.empty ? null : unreadMessagesFromDepartment[0]

        def unreadMessagesFromPatient = unreadMessages.findAll { it.sentByPatient }
        def numberOfUnreadFromPatient = unreadMessagesFromPatient.size()
        def oldestUnreadFromPatient = unreadMessagesFromPatient.empty ? null : unreadMessagesFromPatient[0]

        [numberOfUnreadFromDepartment, oldestUnreadFromDepartment, numberOfUnreadFromPatient, oldestUnreadFromPatient]
    }
}
