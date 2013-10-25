package org.opentele.server

import grails.plugins.springsecurity.Secured
import org.opentele.server.model.PatientGroup
import org.opentele.server.model.types.PermissionName
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.transaction.annotation.Transactional

class PatientGroupService {

    @Secured(PermissionName.PATIENT_GROUP_DELETE)
    public boolean deletePatientGroup(Long patientGroupID) {
        return deletePatientGroup(PatientGroup.get(patientGroupID))
    }

    @Secured(PermissionName.PATIENT_GROUP_DELETE)
    @Transactional
    public boolean deletePatientGroup(PatientGroup patientGroup) {
        if (patientGroup == null) {
            throw new IllegalArgumentException("Cannot delete PatientGroup: "+patientGroup)
        }

        patientGroup.clinician2PatientGroups.each {
            it.delete(failOnError: true)
        }
        patientGroup.patient2PatientGroups.each {
            it.delete(failOnError: true)
        }
        patientGroup.standardThresholdSet.delete()

        try {
            patientGroup.delete(flush: true)
        }
        catch (DataIntegrityViolationException e) {
            return false
        }

        return true
    }
}
