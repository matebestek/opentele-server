package org.opentele.server.model

class PatientGroup extends AbstractObject {

    String name
    boolean disableMessaging
    boolean showGestationalAge
	
    static belongsTo = [department: Department]
    
    static hasMany = [patient2PatientGroups: Patient2PatientGroup, clinician2PatientGroups: Clinician2PatientGroup, patients: Patient]

    StandardThresholdSet standardThresholdSet

    static constraints = {
        name(nullable:false, blank: false, unique: 'department')
        disableMessaging(nullable: false)
        showGestationalAge(nullable: false)
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
