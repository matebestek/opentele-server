package org.opentele.server.model
import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import grails.util.Environment
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.constants.Constants
import org.opentele.server.cpr.CPRPerson
import org.opentele.server.exception.OptimisticLockingException
import org.opentele.server.exception.PasswordException
import org.opentele.server.exception.PatientException
import org.opentele.server.exception.PatientNotFoundException
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.questionnaire.QuestionnaireNode
import org.opentele.server.model.types.GlucoseInUrineValue
import org.opentele.server.model.types.PatientState
import org.opentele.server.model.types.PermissionName
import org.opentele.server.model.types.ProteinValue
import org.springframework.validation.Errors

import static org.opentele.server.model.types.MeasurementTypeName.*

@Secured(PermissionName.NONE)
class PatientController {

    def springSecurityService
    def patientService
    def questionnaireService
    def sessionService
    def passwordService
    def messageService
    def cprLookupService

	static allowedMethods = [index: "GET", save: "POST", update: "POST", delete: "POST", overview:["GET","POST"]]

    @Secured(PermissionName.PATIENT_READ_ALL)
	def index() {
		redirect(action: "overview")
	}

    @Secured(PermissionName.PATIENT_READ)
	def showJson = {
		render Patient.list() as JSON
	}

    @Secured(PermissionName.PATIENT_READ)
	def json = {
		def patientInstance = Patient.get(params.id)
		render patientInstance as JSON
	}

	@Secured(PermissionName.PATIENT_LOGIN)
    @SecurityWhiteListController
	def login() {
        def user = springSecurityService.currentUser
		def patient = Patient.findByUser(user)

        def requiredClientVersion = grailsApplication.metadata['app.requiredClientVersion']
        def actualClientVersion = request.getHeader("Client-version") as String
        def minimumClientVersion = new SemanticVersion(requiredClientVersion)
        def clientVersion = new SemanticVersion(actualClientVersion)

        //Check client version
        def versionChecksOut = minimumClientVersion > new SemanticVersion("0.0.0") && minimumClientVersion <= clientVersion

        if (actualClientVersion == '${version}' || Environment.getCurrent().getName().equals(Environment.DEVELOPMENT.getName())) {
            versionChecksOut = true //version was not set in android app. Most likely because client wasn't built by Maven
        }

        if (versionChecksOut) {
            //Everything seems to be in order
            boolean changePassword = patient.user.cleartextPassword
            def result = [id: patient.id, firstName: patient.firstName, lastName: patient.lastName, user: [id: patient.user.id, changePassword: changePassword]]
            render result as JSON
        } else {
            render (status: 403, text: "Client version too old for this server. Update the client.") as JSON
        }
    }

    @Secured(PermissionName.PATIENT_CREATE)
    @SecurityWhiteListController
    def createPatientFlow = {
        session[Constants.SESSION_ACCESS_VALIDATED] = "false"

        startMagic {
            action {

                def cleartextPassword = passwordService.generateTempPassword()
                flow.patientInstance = new CreatePatientCommand(cleartextPassword: cleartextPassword)
            }
            on("success").to "basicPatientInformation"
        }
        basicPatientInformation {
            on("next") {
                flow.patientInstance.setBasicInformation(params)
                !flow.patientInstance.validate(['firstName', 'lastName', 'cpr', 'sex', 'address', 'postalCode', 'city']) ? error() : success()
            }.to "setAuthentication"
            on("lookupCPR") {
                def cpr = params.cpr
                cpr = cpr?.replaceAll("-","")
                cpr = cpr?.replaceAll(" ","")
                flow.patientInstance.cpr = cpr
                if (flow.patientInstance.validate(['cpr'])) {
                    CPRPerson cprLookupResult = cprLookupService.getPersonDetails(flow.patientInstance.cpr)

                    if (!cprLookupResult.hasErrors) {
                        def firstAndMiddlename = cprLookupResult.firstName ? cprLookupResult.firstName + " "+cprLookupResult.middleName : null
                        flow.patientInstance.cpr = cprLookupResult.civilRegistrationNumber
                        flow.patientInstance.firstName = firstAndMiddlename
                        flow.patientInstance.lastName = cprLookupResult.lastName
                        flow.patientInstance.sex = cprLookupResult.sex
                        flow.patientInstance.address = cprLookupResult.address
                        flow.patientInstance.postalCode = cprLookupResult.postalCode
                        flow.patientInstance.city = cprLookupResult.city

                        if (!cprLookupResult.hasErrors && cprLookupResult.civilRegistrationNumber == null) {
                            //Call to DetGodeCPROpslag was good, but empty result
                            flash.error = message(code: 'patient.create.flow.CPRLookup.emptyResponse')
                        }

                        success()
                    } else {
                        //Call to DetGodeCPROpslag was erroneous
                        flash.error = message(code: 'patient.create.flow.CPRLookup.SOAPError')
                        error()
                    }

                } else {
                    error()
                }
            }.to "basicPatientInformation"
        }
        setAuthentication {
            on("next") {
                flow.patientInstance.setAuthentication(params.username, params.cleartextPassword)
                flow.patientInstance.validate(['username', 'cleartextPassword']) ? success() : error()
            }.to "findPatientGroups"
            on("previous") {flow.patientInstance.clearErrors()}.to "basicPatientInformation"
        }
        findPatientGroups {
            action {
                def user = springSecurityService?.currentUser
                def clinician = Clinician.findByUser(user)
                def cpgs = Clinician2PatientGroup.findAllByClinician(clinician)

                flow.patientGroups = cpgs.collect { it.patientGroup}
            }
            on ("success").to "choosePatientGroup"
        }
        choosePatientGroup {
            on("next") {flow.patientInstance.clearErrors();flow.nextStateName = "thresholdValues"}.to "writePatientGroup"
            on("previous") {flow.patientInstance.clearErrors()}.to "setAuthentication"
            on("saveAndShow") {flow.nextStateName = "summary"}.to "writePatientGroup"
            on("saveAndGotoMonplan") {flow.nextStateName = "writePatientToDB"; flow.endRedirectToMonplan = true}.to "writePatientGroup"
        }
        writePatientGroup {
            action {
                if(params.groupIds) {
                    flow.patientInstance.setPatientGroups(params.groupIds)
                }
                !flow.patientInstance.validate(['groupIds']) ? error() : action.result(flow.nextStateName)
            }
            on("writePatientToDB").to"writePatientToDB"
            on("thresholdValues").to "thresholdValues"
            on("error").to "choosePatientGroup"
            on("summary").to "summary"
        }
        thresholdValues {
            on("next") {flow.nextStateName = "addComment"}.to "updateThresholdValues"
            on("previous") {flow.patientInstance.clearErrors()}.to "choosePatientGroup"
            on("saveAndShow") {flow.nextStateName = "summary"}.to "updateThresholdValues"
            on("saveAndGotoMonplan") {flow.nextStateName = "writePatientToDB"; flow.endRedirectToMonplan = true}.to "updateThresholdValues"
        }
        updateThresholdValues {
            action {
                flow.patientInstance.updateThresholds(params)
                !flow.patientInstance.validate(['thresholds']) ? error() : action.result(flow.nextStateName)
                flow.patientInstance.thresholds.any{!it.validate()} ? error() : action.result(flow.nextStateName)
            }
            on("addComment").to "addComment"
            on("summary").to "summary"
            on("writePatientToDB").to "writePatientToDB"
            on("error").to "thresholdValues"
        }
        addComment {
            on("next") {flow.nextStateName = "addNextOfKin"}.to "writeComment"
            on ("previous") {flow.patientInstance.clearErrors()}.to "thresholdValues"
            on ("saveAndShow") {flow.nextStateName = "summary"}.to "writeComment"
            on ("saveAndGotoMonplan") {flow.nextStateName = "writePatientToDB"; flow.endRedirectToMonplan = true}.to "writeComment"
        }
        writeComment {
            action {
                flow.patientInstance.setComment(params.comment)
                !flow.patientInstance.validate(['comment']) ? error() : action.result(flow.nextStateName)
            }
            on("addNextOfKin").to "addNextOfKin"
            on("summary").to "summary"
            on("writePatientToDB").to "writePatientToDB"
            on("error").to "addComment"
        }
        addNextOfKin {
            on("next").to "summary"
            on("create").to "createNextOfKin"
            on("previous") {flow.patientInstance.clearErrors()}.to "addComment"
            on("saveAndShow").to "summary"
            on("saveAndGotoMonplan") {flow.endRedirectToMonplan = true}.to "writePatientToDB"
        }
        createNextOfKin {
            on("done") {
                flow.nextOfKinPersonInstance = null ///Might have been set during validation error
                def nextOfKinPersonInstance = new NextOfKinPerson(params)
                if(!nextOfKinPersonInstance.save()) {
                    flow.nextOfKinPersonInstance = nextOfKinPersonInstance //Make sure view has access to instance with validation errors
                    error()
                } else {
                    flow.patientInstance.addNextOfKinPerson(nextOfKinPersonInstance)
                    success()
                }
            }.to "addNextOfKin"
        }
        summary {
            on("save").to "writePatientToDB"
            on("saveAndGotoMonplan") { flow.endRedirectToMonplan = true }.to "writePatientToDB"
            on("editBasic").to "basicPatientInformation"
            on("editAuth").to "setAuthentication"
            on("editPG").to "choosePatientGroup"
            on("editThresholds").to "thresholdValues"
            on("editComment").to "addComment"
            on("editNok").to "addNextOfKin"
            on("quitNoSaving").to "quitNoSave"
        }
        writePatientToDB {
           action {
               Patient patientInstance
               try {
                   patientInstance = patientService.buildAndSavePatient(flow.patientInstance)
                   if(patientInstance.hasErrors()) {
                       return error()
                   }
                } catch (PatientException e) {
                   flow.hasErrors = true
                   flow.errorMessage = g.message(code: e.getMessage())
                   return error()
		        }

               flow.savedPatient = patientInstance
           }
           on("success").to "finish"
           on("error").to "summary"
        }
        finish {
            action {
                if (flow.endRedirectToMonplan) {
                    redirect(controller: "monitoringPlan", action: "show", id: flow.savedPatient.id)
                } else {
                    redirect(controller: "patient", action: "show", id: flow.savedPatient.id)
                }
            }
            on("success").to "end"
        }
        quitNoSave {
            action {
                redirect(controller: "patientOverview", action: "index")
            }
            on("success").to "end"
        }
        end()
    }

    @Secured(PermissionName.PATIENT_READ)
	def show() {
		def patientInstance = Patient.get(params.id)
        if (!patientInstance) {
            // Setting up session values
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient')])
            sessionService.setNoPatient(session)
            redirect(action: "overview")
            return
        }
        sessionService.setPatient(session, patientInstance)
        [patientInstance: patientInstance, groups:getPatientGroups(patientInstance), nextOfKin: getNextOfKin(patientInstance)]
	}

    @Secured(PermissionName.PATIENT_WRITE)
    def edit() {
        def patientInstance = Patient.get(params.id)

        if (!patientInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient')])
            sessionService.setNoPatient(session)
            redirect(action: "overview")
            return
        }

        sessionService.setPatient(session, patientInstance)
        ArrayList groups = PatientGroup.list(sort:"name")
        [patientInstance: patientInstance, groups: groups]
    }

	private def getPatientGroups(def patientInstance) {
		def p2pg = Patient2PatientGroup.findAllByPatient(patientInstance)
		def groups = []
		p2pg.each {map ->
			groups << map.patientGroup
		}
		groups
	}
	
	private def getNextOfKin(patientInstance) {
		NextOfKinPerson.findAllByPatient(patientInstance)
	}

    @Secured(PermissionName.PATIENT_WRITE)
    @SecurityWhiteListController
    def update() {
		def patientInstance = Patient.findById(params.id)
        flash.message = null
		try {
			patientService.updatePatient(params, patientInstance)

            if(!patientInstance.hasErrors()) {
                // Setting up session values
                sessionService.setPatient(session, patientInstance)

                //Clear kits from patient if patient treatment is stopped, or if status is dischanged and
                //equipment is handed in
                if (patientInstance.state == PatientState.DISCHARGED_EQUIPMENT_DELIVERED) {
                    MonitorKit.findAllByPatient(patientInstance).each { kit ->
                        def meters = kit.meters
                        meters.each {meter ->
                            meter.patient = null
                            meter.save()
                        }

                        patientInstance.removeFromMonitorKits(kit)
                        patientInstance.save()
                    }
                }

                if (patientInstance.state == PatientState.DISCHARGED_EQUIPMENT_DELIVERED || patientInstance.state == PatientState.DISCHARGED_EQUIPMENT_NOT_DELIVERED || patientInstance.state == PatientState.DECEASED) {
                    clearDataFromInactivePatient(patientInstance)
                }

                flash.message = message(code: 'default.updated.message', args: [message(code: 'patient.label', default: 'Patient')])
                sessionService.setPatient(session, patientInstance)
                redirect(action:  "show", params: [id: params.id])
            } else {
                render(view: "edit", model: [patientInstance: patientInstance, groups: PatientGroup.list(sort:"name")])
            }

		} catch (PatientNotFoundException e) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient')])
			redirect(action: "overview")
		} catch (PasswordException e) {
            //If exception, then update has exited with exception and patientInstance is null
			patientInstance = Patient.get(params.id)
			flash.error = message(code: e.message)
			render(view: "edit", model: [patientInstance: patientInstance, groups: PatientGroup.list(sort:"name")])
		} catch (OptimisticLockingException e) {
            patientInstance = Patient.findById(params.id)
            patientInstance.errors.reject("default.optimistic.locking.failure", [message(code:"patientNote.label")] as Object[] , "i18n error")
            render(view: "edit", model: [patientInstance: patientInstance, groups: PatientGroup.list(sort:"name")])
        } catch (Exception e) {
            //Transaction has rolledback
            log.warn("patientUpdate failed with exception: "+e)
            Errors errors = patientInstance.getErrors()
            patientInstance = Patient.findById(params.id)
            patientInstance.setErrors(errors)

            render(view: "edit", model: [patientInstance: patientInstance, groups: PatientGroup.list(sort:"name")])
        }
	}

    private void clearDataFromInactivePatient(Patient patient) {
        // If patient is 'udmeldt' (discharged), or if patient is deceased (afdød)
        //  * Clear monitoring plan from patient.
        //  * Remove blue alarms from the patient.
        //  * Acknowledge all the patient's completed questionnaires.
        //  * Remove reminders from notes.
        patient.endedMonitoringPlans.add(patient.monitoringPlan)
        patient.monitoringPlan = null
        patient.blueAlarmQuestionnaireIDs = []
        patient.save(failOnError: true, flush: true)

        acknowledgeAllQuestionnaires(patient)
        removeRemindersFromNotes(patient)
    }

    private void acknowledgeAllQuestionnaires(Patient patient) {
        def user = springSecurityService.currentUser
        def clinician = Clinician.findByUser(user)
        for (questionnaire in patient.completedQuestionnaires) {
            questionnaire.acknowledgedBy = clinician
            questionnaire.acknowledgedDate = new Date()
        }
    }

    private void removeRemindersFromNotes(Patient patient) {
        for (note in patient.notes) {
            note.reminderDate = null
            note.remindToday = false
        }
    }

    @Secured(PermissionName.PATIENT_READ_ALL)
    @SecurityWhiteListController
    def search(PatientSearchCommand searchCommand) {
        def patientList = patientService.searchPatient(searchCommand)

		//For g:sortableColumns
        params.sort = params.sort ?: 'firstName'
        params.order = params.order ?: 'asc'
		if (params.sort) {
			if (params.sort == 'severity') {
                patientList.sort { questionnaireService.severity(it) }

			} else {

				patientList.sort{ normalizeSortValue(it."${params.sort}") }
			}
            if (params.order.equals("desc")) {
                patientList.reverse(true)
            }
		}
		[searchCommand: searchCommand, patients:patientList]
	}


    @Secured(PermissionName.PATIENT_READ_ALL)
    @SecurityWhiteListController
    def resetSearch() {
        redirect(action: 'search')
    }

    /**
	 * Provides an overview of a single questionnaire.
	 */
    @Secured(PermissionName.COMPLETED_QUESTIONNAIRE_READ)
	def questionnaire() {
		def user = springSecurityService.currentUser

		def completedQuestionnaire = CompletedQuestionnaire.get(params.id)

		if (user.isPatient()) {
			def myPatient = Patient.findByUser(user)
			if (completedQuestionnaire.patient != myPatient) {
				redirect(controller: "login", action: "denied")
			}
		}


		sessionService.setPatient(session, completedQuestionnaire.patient)

		[completedQuestionnaire:completedQuestionnaire]
	}


    @Secured(PermissionName.CONFERENCE_READ)
    def conference() {
        def conference = Conference.get(params.id)
        sessionService.setPatient(session, conference.patient)
        [conference:conference]
    }

	/**
	 * Closure for presenting the results of a patients response to questionnaires
	 */
    @Secured(PermissionName.COMPLETED_QUESTIONNAIRE_READ_ALL)
    @SecurityWhiteListController
    def questionnaires(Long id) {

		def user = springSecurityService.currentUser

		def patient
		if (user.isPatient()) {
			patient = Patient.findByUser(user)
		} else  if (user.isClinician() &&id) {
            patient = Patient.get(id)
		}

		def questionPreferences = questionPreferencesForClinician(Clinician.findByUser(user))
		def questionnairesNumber = []
		def completed
        def greenCompletedQuestionnaires = []
		if (patient) {
			// Setting up session values
			sessionService.setPatient(session, patient)
            if(patient.getMonitoringPlan()) {
                questionnairesNumber = QuestionnaireSchedule.countByMonitoringPlan(patient.getMonitoringPlan())
            } else {
                questionnairesNumber = 0
            }
			completed = CompletedQuestionnaire.findAllByPatient(patient).size()
            greenCompletedQuestionnaires = CompletedQuestionnaire.unacknowledgedGreenQuestionnairesByPatient(patient).list()

        }

        [patientInstance: patient, questionnairesNumber: questionnairesNumber, questionPreferences: questionPreferences, completedNumber: completed, greenCompletedAndUnacknowledgedQuestionnaires: greenCompletedQuestionnaires]
	}

    @Secured([PermissionName.METER_READ_ALL, PermissionName.MONITOR_KIT_READ_ALL])
	def equipment () {

		def p = Patient.get(params.id)
        sessionService.setPatient(session,p)

		def kits = MonitorKit.findAllByPatient(p)
		def meters = Meter.findAllByPatient(p)
		[patientInstance: p, kits:kits,meters:meters]
	}

    @Secured(PermissionName.PATIENT_WRITE)
    @SecurityWhiteListController
	def removeNextOfKin(Long id, Long nextOfKin) {
		def patientInstance = Patient.get(id)

		if (!patientInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient')])
			redirect(action: "edit", params: [id: id])
			return
		}
		
        def nextOfKinPersonInstance = NextOfKinPerson.get(nextOfKin)
		if (!nextOfKinPersonInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient2NextOfKinPerson.label', default: 'Next of kin')])
			redirect(action: "edit", params: [id: id])
			return
		}
		def name = nextOfKinPersonInstance.nameAndRelation
        patientInstance.nextOfKinPersons.remove(nextOfKinPersonInstance)
		nextOfKinPersonInstance.delete()

        flash.message = message(code: "patient.nextOfKin.removed", args:[name])

		redirect(action: "edit", id: id)
	}

    @Secured(PermissionName.PATIENT_WRITE)
    @SecurityWhiteListController
	def removeKit() {
		// TODO: Add to service
		def kit = MonitorKit.get(params.id)
		String name = kit.name
		def p = Patient.get(session.patientId)
		def meters = kit.meters
		meters.each {meter ->
			meter.patient = null
			meter.save()
		}

		p.removeFromMonitorKits(kit)
		p.save()

		flash.message = message(code: "patient.equipment.kit.removed", args:[name])
		redirect(action: "equipment", id: session.patientId)
	}

    @Secured(PermissionName.PATIENT_WRITE)
    @SecurityWhiteListController
	def removeMeter() {
		// TODO: Add to service
		def meter = Meter.get(params.id)
		def msg
		if (!meter?.monitorKit) {
			meter.patient = null
			meter.save()
			msg = message(code: "patient.equipment.meter.removed", args:[meter?.model])
		} else {
			def name = (meter?.monitorKit?.name?: "No meter")
			msg = message(code: "patient.equipment.meter.error.in.kit", args:[name])
		}

		flash.message = msg
		redirect(action: "equipment", id: session.patientId)
	}

    @Secured(PermissionName.PATIENT_WRITE)
    @SecurityWhiteListController
	def addKit() {
		// TODO: Add to service
		def kits = MonitorKit.findAllWhere(patient:null)

		if (params.id) {
			def p = Patient.get(session.patientId)
			def kit = MonitorKit.get(params.id)
			p.addToMonitorKits(kit)

			kit.meters.each { meter ->
				meter.patient = p
				meter.save()
			}

			flash.message = message(code: "patient.equipment.kit.added", args:[kit.name])
			redirect(action: "equipment", id: session.patientId)
		}

		[kits:kits]
	}

    @Secured(PermissionName.PATIENT_WRITE)
    @SecurityWhiteListController
	def addMeter() {
		// TODO: Add to service
		def meters = Meter.findAllWhere(patient:null, monitorKit:null)

		if (params.id) {
			def p = Patient.get(session.patientId)
			def meter = Meter.get(params.id)
			meter.patient = p
			meter.save()

			flash.message = message(code: "patient.equipment.meter.added", args:[meter.model])
			redirect(action: "equipment", id: session.patientId)
		}

		[meters:meters]
	}

    @Secured(PermissionName.MESSAGE_READ)
	def messages() {
		def patient = Patient.get(params.id)
        // Setting up session values
        sessionService.setPatient(session, patient)
        def clinician = Clinician.findByUser(springSecurityService.currentUser)

        if(messageService.clinicianCanSendMessagesToPatient(clinician, patient)) {
            def messageList = Message.findAll("from Message as m where m.patient=(:patient) order by m.sendDate desc", [patient: patient])
            [canSendMessages: true, patientInstance: patient, messages: messageList]
        } else {
            [canSendMessages: false, patientInstance: patient]
        }
	}

    @Secured(PermissionName.PATIENT_PREFERENCES_WRITE)
    @SecurityWhiteListController
	def savePrefs() {
		def clinician = Clinician.findByUser(springSecurityService.currentUser)
		def patient = Patient.get(params.patientID)
		
		def preferredQuestionIds = params.preferredQuestionId 
		
		savePref(clinician, preferredQuestionIds)
		
		redirect(action: "questionnaires", params: [id: patient.id])
	}
	
	private def savePref(Clinician c, def preferredQuestionIds) {
		//Clear old prefs, ignore 'empty' submit
		if (preferredQuestionIds) {
			def query = "from ClinicianQuestionPreference as cqp where cqp.clinician.id=(:c_id)"
			def questionPrefs = ClinicianQuestionPreference.findAll(query, [c_id: c.id])
			questionPrefs.each {
				it.delete()
			}
		}
		
		preferredQuestionIds.each {
			//Question id - ignore id=-1 as that would be the 'V��lg..' select
			try {
				def id = Integer.parseInt(it);
				if (id > 0) {
					def pref = new ClinicianQuestionPreference()
					pref.question = QuestionnaireNode.get(it)
					pref.clinician = c
					pref.save(failOnError:true)
				}
			} catch (Exception e) {
				//Ignore
			}
		}
	}

    @Secured(PermissionName.PATIENT_REMOVE_BLUE_ALARMS)
    @SecurityWhiteListController
	def removeAllBlue() {
		Patient p = Patient.get(params.patientID)
        p.blueAlarmQuestionnaireIDs = []
        p.save()

		redirect(controller: session.lastController, action: session.lastAction)
	}

    @Secured(PermissionName.PATIENT_WRITE)
    @SecurityWhiteListController
    def chooseThreshold(String type) {
        log.debug("ChooseThreshold $type")
        switch (type) {
            case BLOOD_PRESSURE.name():
                render(template: '/bloodPressureThreshold/form', model: [standardThresholdInstance: new BloodPressureThreshold()])
                break
            case URINE.name():
                render(template: '/urineThreshold/form', model: [standardThresholdInstance: new UrineThreshold()])
                break
            case URINE_GLUCOSE.name():
                render(template: '/urineGlucoseThreshold/form', model: [standardThresholdInstance: new UrineGlucoseThreshold()])
                break
            default:
                render(template: '/numericThreshold/form', model: [standardThresholdInstance: new NumericThreshold()])
        }
    }



    @Secured(PermissionName.PATIENT_WRITE)
    @SecurityWhiteListController
    def addThreshold(Long id) {
        def patientInstance = Patient.get(id)
        [patientInstance: patientInstance, notUsedThresholds: getUnusedThresholds(patientInstance)]
    }

    private getUnusedThresholds(Patient patientInstance) {
        def currentMeasurementTypes = patientInstance.thresholds*.type*.name


        def list = (currentMeasurementTypes.size() < 1 ? MeasurementType.list() : MeasurementType.withCriteria {
            not {
                inList('name', currentMeasurementTypes)
            }
        })*.name.sort { it.name() }

        list
    }



    @Secured(PermissionName.PATIENT_WRITE)
    @SecurityWhiteListController
    def removeThreshold(Long id, Long threshold) {
        def patientInstance = Patient.get(id)
        def thresholdInstance = Threshold.get(threshold)

        patientInstance.removeFromThresholds(thresholdInstance)
        patientInstance.save()
        thresholdInstance.delete()
        redirect(action: "show", id: patientInstance.id)
    }

    @Secured(PermissionName.PATIENT_WRITE)
    @SecurityWhiteListController
    def saveThresholdToPatient(Long id) {
        def patientInstance = Patient.get(id)

        Threshold threshold

        MeasurementType measurementType = MeasurementType.findByName(params.type)
        params.type = measurementType
        switch(measurementType.name) {
            case BLOOD_PRESSURE:
                threshold = new BloodPressureThreshold(params)
                break;
            case URINE:
                threshold = createUrineThreshold(params)
                break;

            case URINE_GLUCOSE:
                threshold = createUrineGlucoseThreshold()
                break;
            default: //Numeric
                threshold = new NumericThreshold(params)
                break;
        }

        //If the threshold is OK, then update the patient
        if (threshold.validate() && threshold.save(flush: true)) {
            patientInstance.addToThresholds(threshold)
            if(!patientInstance.validate()) {
                patientInstance.removeFromThresholds(threshold)
                threshold.delete()
                patientInstance.clearErrors()
                patientInstance.errors.reject('patientThreshold.add.error', [threshold.prettyToString(), patientInstance.firstName] as Object[], 'Kunne ikke tilføje {0} til {1}: Der findes allerede en tærskelværdi af denne type for denne patient.')
                render(view: "edit", model:  [patientInstance: patientInstance, groups: PatientGroup.list(sort:"name")])
            } else {
                patientInstance.save(flush: true)
                render(view:  "edit", model: [patientInstance: patientInstance, groups: PatientGroup.list(sort:"name")])
            }
        } else {
            render(view: "addThreshold", model: [patientInstance: patientInstance, standardThresholdInstance: threshold, thresholdType: measurementType.name, notUsedThresholds: getUnusedThresholds(patientInstance)])
        }
    }

    private createUrineGlucoseThreshold() {
        ['alertHigh', 'warningHigh', 'warningLow', 'alertLow'].inject(new UrineGlucoseThreshold(type: params.type)) { threshold, prop ->
            threshold."$prop" = params."$prop" ? GlucoseInUrineValue.fromString(params."$prop") : null
            return threshold
        } as UrineGlucoseThreshold
    }

    private createUrineThreshold(GrailsParameterMap params) {
        ['alertHigh', 'warningHigh', 'warningLow', 'alertLow'].inject(new UrineThreshold(type: params.type)) { threshold, prop ->
            threshold."$prop" = params."$prop" ? ProteinValue.fromString(params."$prop") : null
            return threshold
        } as UrineThreshold
    }

    @Secured(PermissionName.SET_PATIENT_RESPONSIBILITY)
    @SecurityWhiteListController
    def updateDataResponsible() {

        Patient patientInstance = null
        if (params.id) {
            patientInstance = Patient.findById(params.id)
        }

        PatientGroup patientGroupInstance = null
        if (params.dataResponsible) {
            patientGroupInstance = PatientGroup.findById(params.dataResponsible)
        }

        if (!patientInstance) {
            render(view: "editResponsability", model: [patientInstance: patientInstance])
            return
        }

        patientInstance.dataResponsible = patientGroupInstance

        if(!patientInstance.save(flush: true)) {
            render(view: "editResponsability", model: [patientInstance: patientInstance])
            return
        }

        redirect(action: "edit", id: patientInstance.id)
    }

    @Secured(PermissionName.SET_PATIENT_RESPONSIBILITY)
    def editResponsability() {
        Patient patientInstance = Patient.findById(params.id)
        sessionService.setPatient(session, patientInstance)
        render(view: "editResponsability", model: [patientInstance: patientInstance])
    }

    @Secured(PermissionName.PATIENT_WRITE)
    @SecurityWhiteListController
    def resetPassword(Long id) {
        def patientInstance = Patient.get(id)
        if (!patientInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient')])
            redirect(action: "list")
            return
        }

        patientService.resetPassword(patientInstance)
        flash.message = message(code: 'patient.reset-password.done', args: [patientInstance.name])
        redirect(action: "show", id: id)
    }

    @Secured(PermissionName.PATIENT_WRITE)
    @SecurityWhiteListController
    def sendPassword(Long id) {
        def patientInstance = Patient.get(id)
        if (!patientInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient')])
            redirect(action: "list")
            return
        }

        patientService.sendPassword(patientInstance)
        flash.message = message(code: 'patient.send-password.done', args: [patientInstance.email])
        redirect(action: "show", id: id)
    }

    @Secured(PermissionName.PATIENT_WRITE)
    @SecurityWhiteListController
    def unlockAccount(Long id) {
        def patientInstance = Patient.get(id)
        if (!patientInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient')])
            redirect(action: "list")
            return
        }

        passwordService.unlockAccount(patientInstance.user)
        flash.message = message(code: 'patient.unlock-account.done', args: [patientInstance.name])
        redirect(action: "show", id: id)

    }

    private def questionPreferencesForClinician(Clinician clinician) {
        if (clinician != null) {
            ClinicianQuestionPreference.findAllByClinicianAndQuestionIsNotNull(clinician, [sort:'id', order:'asc'])?.collect { it.questionId }

        } else {
            []
        }
    }

    private normalizeSortValue(def value) {
        value instanceof String ? value.toLowerCase() : value
    }
}
