package org.opentele.server.model

class Patient2PatientGroup extends AbstractObject {

    Patient patient
    PatientGroup patientGroup
    
    static Patient2PatientGroup link(patient, patientGroup) {
        def ref = Patient2PatientGroup.findByPatientAndPatientGroup(patient, patientGroup) 
        if (!ref) {
            ref = new Patient2PatientGroup()
            patient?.addToPatient2PatientGroups(ref) 
            patientGroup?.addToPatient2PatientGroups(ref) 
            ref.save(failOnError:true)
        } 
        ref
    }

    static void unlink(patient, patientGroup) {
        def ref = Patient2PatientGroup.findByPatientAndPatientGroup(patient, patientGroup) 
        if (ref) {
            patient?.removeFromPatient2PatientGroups(ref) 
            patientGroup?.removeFromPatient2PatientGroups(ref)
            ref.delete(failOnError:true)
        }
    }
    
    static constraints = {
        patient unique: 'patientGroup'
    }
}
