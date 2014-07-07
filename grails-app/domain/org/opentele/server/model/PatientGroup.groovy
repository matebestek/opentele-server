package org.opentele.server.model

class PatientGroup extends AbstractObject {

    String name
    boolean disableMessaging
    boolean showGestationalAge
    boolean showRunningCtgMessaging

    static belongsTo = [department: Department]
    
    static hasMany = [patient2PatientGroups: Patient2PatientGroup, clinician2PatientGroups: Clinician2PatientGroup, patients: Patient]  //patients are the patients for whom this patientgroup is the dataresponsible (reverse mapping on patient is: PatientGroup dataResponsible)

    StandardThresholdSet standardThresholdSet

    static constraints = {
        name(nullable:false, blank: false, unique: 'department')
        disableMessaging(nullable: false)
        showGestationalAge(nullable: false)
        showRunningCtgMessaging nullable: false
        standardThresholdSet(validator: {val, obj ->
            if (val == null) {
                ["validate.patientgroup.nothresholdset", "Standard tærskelværdier"]
            }
        })
    }

    static mapping = {
        department(lazy:false)
    }

	@Override
	String toString () {
        "${name} (${department?.name})"
    }   // + (department name?)
}
