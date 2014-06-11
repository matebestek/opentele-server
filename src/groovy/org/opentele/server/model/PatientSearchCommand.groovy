package org.opentele.server.model

import grails.validation.Validateable
import org.opentele.server.model.types.PatientState


@Validateable
class PatientSearchCommand {
    String ssn
    String phone
    String firstName
    String lastName
    String username
    PatientState status = PatientState.ACTIVE
    PatientGroup patientGroup

    static List<PatientGroup> getAllPatientGroups() {
        PatientGroup.list([sort: 'name'])
    }

    String getSsn() {
        this.ssn?.replace('-','')
    }
}
