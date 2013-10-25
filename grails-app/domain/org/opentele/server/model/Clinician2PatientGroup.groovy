package org.opentele.server.model

class Clinician2PatientGroup extends AbstractObject {

    Clinician clinician
    PatientGroup patientGroup
    
	String toString() {
		"${patientGroup?.name}"
	}
	
    static Clinician2PatientGroup link(Clinician clinician, PatientGroup patientGroup) {
        def ref = Clinician2PatientGroup.findByClinicianAndPatientGroup(clinician, patientGroup)
        
        if (!ref) {
            ref = new Clinician2PatientGroup()
            clinician?.addToClinician2PatientGroups(ref)
            patientGroup?.addToClinician2PatientGroups(ref)
            ref.save(flush: true, failOnError:true)
        }
        ref
    }

    static void unlink(Clinician clinician, PatientGroup patientGroup) {
        def ref = Clinician2PatientGroup.findByClinicianAndPatientGroup(clinician, patientGroup)
        if (ref) {
            clinician?.removeFromClinician2PatientGroups(ref)
            patientGroup?.removeFromClinician2PatientGroups(ref)
            ref.delete(flush: true, failOnError:true)
        }
    }
    
    static constraints = {
        clinician unique: 'patientGroup'
    }

    static mapping = {
        patientGroup(lazy: false)
    }
}

