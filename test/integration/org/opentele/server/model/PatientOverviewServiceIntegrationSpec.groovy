package org.opentele.server.model

import grails.plugin.spock.IntegrationSpec
import org.opentele.server.model.types.NoteType
import org.opentele.server.model.types.PatientState

class PatientOverviewServiceIntegrationSpec extends IntegrationSpec {
    def patientOverviewService

    def 'can find patients for clinician overview'() {
        setup:
        PatientGroup clinicianPatientGroup = PatientGroup.build()
        PatientGroup otherPatientGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianPatientGroup)
        clinician.save(failOnError: true)

        Patient activePatientInPatientGroup = createPatientWithMessage(PatientState.ACTIVE, clinicianPatientGroup)
        createPatientWithMessage(PatientState.DECEASED, clinicianPatientGroup)
        createPatientWithMessage(PatientState.ACTIVE, otherPatientGroup)
        createPatientWithMessage(PatientState.DECEASED, otherPatientGroup)

        when:
        def patientOverviews = patientOverviewService.getPatientsForClinicianOverview(clinician)

        then:
        patientOverviews*.patient == [activePatientInPatientGroup]
    }

    def 'includes patients with reminders not seen by clinician'() {
        setup:
        PatientGroup clinicianPatientGroup = PatientGroup.build()
        PatientGroup otherPatientGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianPatientGroup)
        clinician.save(failOnError: true)

        Patient activePatientInPatientGroupWithoutReminder = createPatient(PatientState.ACTIVE, clinicianPatientGroup)
        Patient activePatientInPatientGroupWithUnseenReminder = createPatient(PatientState.ACTIVE, clinicianPatientGroup)
        Patient activePatientInPatientGroupWithSeenReminder = createPatient(PatientState.ACTIVE, clinicianPatientGroup)
        Patient inactivePatientInPatientGroup = createPatient(PatientState.DECEASED, clinicianPatientGroup)
        Patient activePatientInOtherPatientGroupWithReminder = createPatient(PatientState.ACTIVE, otherPatientGroup)

        activePatientInPatientGroupWithoutReminder.addToNotes(type: NoteType.IMPORTANT, note: 'Note', reminderDate: new Date()+1, seenBy: [])
        activePatientInPatientGroupWithUnseenReminder.addToNotes(type: NoteType.NORMAL, note: 'Note', reminderDate: new Date()-1, seenBy: [])
        activePatientInPatientGroupWithSeenReminder.addToNotes(type: NoteType.NORMAL, note: 'Note', reminderDate: new Date()-1, seenBy: [clinician])
        activePatientInOtherPatientGroupWithReminder.addToNotes(type: NoteType.IMPORTANT, note: 'Note', reminderDate: new Date()-1, seenBy: [])

        when:
        def patientOverviews = patientOverviewService.getPatientsForClinicianOverview(clinician)

        then:
        patientOverviews*.patient == [activePatientInPatientGroupWithUnseenReminder]
    }

    def 'can find patients for specific patient group for clinician overview'() {
        setup:
        PatientGroup group1 = PatientGroup.build()
        PatientGroup group2 = PatientGroup.build()
        PatientGroup group3 = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: group1)
        clinician.addToClinician2PatientGroups(patientGroup: group2)
        clinician.addToClinician2PatientGroups(patientGroup: group3)
        clinician.save(failOnError: true)

        Patient activePatientInPatientGroup1 = createPatientWithMessage(PatientState.ACTIVE, group1)
        createPatientWithMessage(PatientState.DECEASED, group1)
        Patient activePatientInPatientGroup2 = createPatientWithMessage(PatientState.ACTIVE, group2)
        createPatientWithMessage(PatientState.DECEASED, group2)

        when:
        def activePatientOverviewsInGroup1 = patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, group1)
        def activePatientOverviewsInGroup2 = patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, group2)
        def activePatientOverviewsInGroup3 = patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, group3)

        then:
        activePatientOverviewsInGroup1*.patient == [activePatientInPatientGroup1]
        activePatientOverviewsInGroup2*.patient == [activePatientInPatientGroup2]
        activePatientOverviewsInGroup3*.patient == []
    }

    def 'includes patients with reminders not seen by clinician when searching in specific patient group'() {
        setup:
        PatientGroup group1 = PatientGroup.build()
        PatientGroup group2 = PatientGroup.build()
        PatientGroup group3 = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: group1)
        clinician.addToClinician2PatientGroups(patientGroup: group2)
        clinician.addToClinician2PatientGroups(patientGroup: group3)
        clinician.save(failOnError: true)

        Patient activePatientInPatientGroup1WithoutReminder = createPatient(PatientState.ACTIVE, group1)
        Patient activePatientInPatientGroup1WithNormalPatientNote = createPatient(PatientState.ACTIVE, group1)
        Patient activePatientInPatientGroup1WithUnseenReminder = createPatient(PatientState.ACTIVE, group1)
        Patient activePatientInPatientGroup1WithSeenReminder = createPatient(PatientState.ACTIVE, group1)
        Patient inactivePatientInPatientGroup2WithReminder = createPatient(PatientState.DECEASED, group1)
        Patient activePatientInPatientGroup2WithUnseenReminder = createPatient(PatientState.ACTIVE, group2)

        activePatientInPatientGroup1WithNormalPatientNote.addToNotes(type: NoteType.IMPORTANT, note: 'Note', reminderDate: new Date()+1, seenBy: [])
        activePatientInPatientGroup1WithUnseenReminder.addToNotes(type: NoteType.IMPORTANT, note: 'Note', reminderDate: new Date()-1, seenBy: [])
        activePatientInPatientGroup1WithSeenReminder.addToNotes(type: NoteType.NORMAL, note: 'Note', reminderDate: new Date()-1, seenBy: [clinician])
        inactivePatientInPatientGroup2WithReminder.addToNotes(type: NoteType.IMPORTANT, note: 'Note', reminderDate: new Date()-1, seenBy: [])
        activePatientInPatientGroup2WithUnseenReminder.addToNotes(type: NoteType.IMPORTANT, note: 'Note', reminderDate: new Date()-1, seenBy: [])

        when:
        def activePatientOverviewsInGroup1 = patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, group1)
        def activePatientOverviewsInGroup2 = patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, group2)
        def activePatientOverviewsInGroup3 = patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, group3)

        then:
        activePatientOverviewsInGroup1*.patient == [activePatientInPatientGroup1WithUnseenReminder]
        activePatientOverviewsInGroup2*.patient == [activePatientInPatientGroup2WithUnseenReminder]
        activePatientOverviewsInGroup3*.patient == []
    }

    def 'complains if finding patients for patient group not assigned to clinician'() {
        setup:
        PatientGroup clinicianGroup = PatientGroup.build()
        PatientGroup otherGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianGroup)

        when:
        patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, otherGroup)

        then:
        thrown(IllegalArgumentException)
    }

    def 'can identify patients for which messaging is enabled'() {
        setup:
        PatientGroup patientGroupWithMessaging = PatientGroup.build(disableMessaging: false)
        PatientGroup patientGroupWithoutMessaging = PatientGroup.build(disableMessaging: true)

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: patientGroupWithMessaging)
        clinician.addToClinician2PatientGroups(patientGroup: patientGroupWithoutMessaging)
        clinician.save(failOnError: true)

        Patient patient1WithMessagingEnabled = createPatientWithMessage(PatientState.ACTIVE, patientGroupWithMessaging)
        Patient patient2WithMessagingEnabled = createPatientWithMessage(PatientState.ACTIVE, patientGroupWithMessaging)
        Patient patient1WithMessagingDisabled = createPatientWithMessage(PatientState.ACTIVE, patientGroupWithoutMessaging)
        Patient patient2WithMessagingDisabled = createPatientWithMessage(PatientState.ACTIVE, patientGroupWithoutMessaging)

        when:
        def patientIds = patientOverviewService.getIdsOfPatientsWithMessagingEnabled(clinician, [patient1WithMessagingEnabled, patient2WithMessagingEnabled, patient1WithMessagingDisabled, patient2WithMessagingDisabled]*.patientOverview)

        then:
        patientIds.size() == 2
        patientIds.contains(patient1WithMessagingEnabled.id)
        patientIds.contains(patient2WithMessagingEnabled.id)
    }

    def 'can identify patients for which alarms triggered by unread messages *to* the patient is disabled'() {
        setup:
        PatientGroup patientGroupWithMessaging = PatientGroup.build(disableMessaging: false)
        PatientGroup patientGroupWithoutMessaging = PatientGroup.build(disableMessaging: true)

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: patientGroupWithMessaging)
        clinician.addToClinician2PatientGroups(patientGroup: patientGroupWithoutMessaging)
        clinician.save(failOnError: true)

        Patient patient1WithNoAlarmIfUnreadMessages = createPatientWithMessageAndNoAlarmIfUnreadMessages(PatientState.ACTIVE, patientGroupWithoutMessaging, true)
        Patient patient2WithNoAlarmIfUnreadMessages = createPatientWithMessageAndNoAlarmIfUnreadMessages(PatientState.ACTIVE, patientGroupWithMessaging, true)
        Patient patient3WithNoAlarmIfUnreadMessages = createPatientWithMessageAndNoAlarmIfUnreadMessages(PatientState.ACTIVE, patientGroupWithoutMessaging, false)
        Patient patient4WithNoAlarmIfUnreadMessages = createPatientWithMessageAndNoAlarmIfUnreadMessages(PatientState.ACTIVE, patientGroupWithMessaging, false)

        when:
        def patientIds = patientOverviewService.getIdsOfPatientsWithAlarmIfUnreadMessagesDisabled(clinician, [patient1WithNoAlarmIfUnreadMessages, patient2WithNoAlarmIfUnreadMessages, patient3WithNoAlarmIfUnreadMessages, patient4WithNoAlarmIfUnreadMessages]*.patientOverview)

        then:
        patientIds.size() == 2
        patientIds.contains(patient1WithNoAlarmIfUnreadMessages.id)
        patientIds.contains(patient2WithNoAlarmIfUnreadMessages.id)
    }

    def 'can find all patient notes not seen by clinician for a list of patients'() {
        setup:
        PatientGroup patientGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: patientGroup)
        clinician.save(failOnError: true)

        Patient patient1 = createPatient(PatientState.ACTIVE, patientGroup)
        Patient patient2 = createPatient(PatientState.ACTIVE, patientGroup)
        Patient patient3 = createPatient(PatientState.ACTIVE, patientGroup)
        Patient patient4 = createPatient(PatientState.ACTIVE, patientGroup)
        Patient patient5 = createPatient(PatientState.ACTIVE, patientGroup)

        patient1.addToNotes(type: NoteType.IMPORTANT, note: 'Note 1', reminderDate: new Date()+1, seenBy: [])
        patient1.addToNotes(type: NoteType.IMPORTANT, note: 'Note 2', reminderDate: new Date()+1, seenBy: [])
        patient1.addToNotes(type: NoteType.NORMAL, note: 'Note 3', reminderDate: new Date()-1, seenBy: [])
        patient2.addToNotes(type: NoteType.IMPORTANT, note: 'Note 4', reminderDate: new Date()-1, seenBy: [])
        patient3.addToNotes(type: NoteType.IMPORTANT, note: 'Note 5', reminderDate: new Date()+1, seenBy: [])
        patient5.addToNotes(type: NoteType.IMPORTANT, note: 'Irrelevant note', reminderDate: new Date()+1, seenBy: [])

        when:
        def patientNotes = patientOverviewService.fetchUnseenNotesForPatients(clinician, [patient1, patient2, patient3, patient4]*.patientOverview)

        then:
        patientNotes[patient1.id]*.note.toSet() == ['Note 1', 'Note 2', 'Note 3'].toSet()
        patientNotes[patient2.id]*.note.toSet() == ['Note 4'].toSet()
        patientNotes[patient3.id]*.note.toSet() == ['Note 5'].toSet()
        patientNotes[patient4.id]*.note.toSet() == [].toSet()
    }

    private Patient createPatientWithMessage(PatientState state, PatientGroup group) {
        createPatientWithMessageAndNoAlarmIfUnreadMessages(state, group, false)
    }

    private Patient createPatientWithMessageAndNoAlarmIfUnreadMessages(PatientState state, PatientGroup group, boolean noAlarmIfUnreadMessages) {
        Patient patient = Patient.build(state: state, blueAlarmQuestionnaireIDs: [], noAlarmIfUnreadMessagesToPatient: noAlarmIfUnreadMessages)
        patient.addToPatient2PatientGroups(patientGroup: group)

        // Create unread note, in order to make this patient important enough to show in patient overview
        Message.build(patient: patient)

        patientOverviewService.createOverviewFor(patient)

        patient
    }

    private Patient createPatient(PatientState state, PatientGroup group) {
        Patient patient = Patient.build(state: state, blueAlarmQuestionnaireIDs: [])
        patient.addToPatient2PatientGroups(patientGroup: group)
        patientOverviewService.createOverviewFor(patient)

        patient
    }
}
