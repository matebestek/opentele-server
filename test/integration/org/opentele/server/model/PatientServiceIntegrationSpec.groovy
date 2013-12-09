package org.opentele.server.model

import grails.plugin.spock.IntegrationSpec
import org.opentele.server.model.types.PatientState

class PatientServiceIntegrationSpec extends IntegrationSpec {
    def patientService

    def 'can find active patients for clinician'() {
        setup:
        PatientGroup clinicianPatientGroup = PatientGroup.build()
        PatientGroup otherPatientGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianPatientGroup)

        Patient activePatientInPatientGroup = Patient.build(state: PatientState.ACTIVE)
        activePatientInPatientGroup.addToPatient2PatientGroups(patientGroup: clinicianPatientGroup)

        Patient inactivePatientInPatientGroup = Patient.build(state: PatientState.DECEASED)
        inactivePatientInPatientGroup.addToPatient2PatientGroups(patientGroup: clinicianPatientGroup)

        Patient activePatientInOtherPatientGroup = Patient.build(state: PatientState.ACTIVE)
        activePatientInOtherPatientGroup.addToPatient2PatientGroups(patientGroup: otherPatientGroup)

        Patient inactivePatientInOtherPatientGroup = Patient.build(state: PatientState.DECEASED)
        inactivePatientInOtherPatientGroup.addToPatient2PatientGroups(patientGroup: otherPatientGroup)

        [clinician, activePatientInPatientGroup, inactivePatientInPatientGroup, activePatientInOtherPatientGroup, inactivePatientInOtherPatientGroup]*.save(failOnError: true)

        when:
        def patients = patientService.getActivePatientsForClinician(clinician)

        then:
        patients == [activePatientInPatientGroup]
    }

    def 'can find active patients for specific patient group'() {
        setup:
        PatientGroup group1 = PatientGroup.build()
        PatientGroup group2 = PatientGroup.build()
        PatientGroup group3 = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: group1)
        clinician.addToClinician2PatientGroups(patientGroup: group2)
        clinician.addToClinician2PatientGroups(patientGroup: group3)

        Patient activePatientInPatientGroup1 = Patient.build(state: PatientState.ACTIVE)
        activePatientInPatientGroup1.addToPatient2PatientGroups(patientGroup: group1)

        Patient inactivePatientInPatientGroup1 = Patient.build(state: PatientState.DECEASED)
        inactivePatientInPatientGroup1.addToPatient2PatientGroups(patientGroup: group1)

        Patient activePatientInPatientGroup2 = Patient.build(state: PatientState.ACTIVE)
        activePatientInPatientGroup2.addToPatient2PatientGroups(patientGroup: group2)

        Patient inactivePatientInPatientGroup2 = Patient.build(state: PatientState.DECEASED)
        inactivePatientInPatientGroup2.addToPatient2PatientGroups(patientGroup: group2)

        [clinician, activePatientInPatientGroup1, inactivePatientInPatientGroup1, activePatientInPatientGroup2, inactivePatientInPatientGroup2]*.save(failOnError: true)

        when:
        def activePatientsInGroup1 = patientService.getActivePatientsForClinicianAndPatientGroup(clinician, group1)
        def activePatientsInGroup2 = patientService.getActivePatientsForClinicianAndPatientGroup(clinician, group2)
        def activePatientsInGroup3 = patientService.getActivePatientsForClinicianAndPatientGroup(clinician, group3)

        then:
        activePatientsInGroup1 == [activePatientInPatientGroup1]
        activePatientsInGroup2 == [activePatientInPatientGroup2]
        activePatientsInGroup3 == []
    }

    def 'complains if finding patients for patient group not assigned to clinician'() {
        setup:
        PatientGroup clinicianGroup = PatientGroup.build()
        PatientGroup otherGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianGroup)

        when:
        patientService.getActivePatientsForClinicianAndPatientGroup(clinician, otherGroup)

        then:
        thrown(IllegalArgumentException)
    }
}
