package org.opentele.server

import org.opentele.server.model.Clinician
import org.opentele.server.model.Message
import org.opentele.server.model.Patient
import org.opentele.server.model.PatientGroup
import org.opentele.server.model.PatientNote
import org.opentele.server.model.PatientOverview
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire
import org.opentele.server.model.types.PatientState
import org.opentele.server.model.types.Severity

class PatientOverviewService {
    def questionnaireService

    void createOverviewFor(Patient patient) {
        PatientOverview patientOverview = new PatientOverview(patientOverviewProperties(patient)).save(failOnError: true)
        patient.addToPatientOverviews(patientOverview)
    }

    void updateOverviewFor(Patient patient) {
        PatientOverview existingOverview = patient.patientOverview
        existingOverview.setProperties(patientOverviewProperties(patient))
        existingOverview.save(failOnError: true)
    }

    boolean overviewDetailsAreWrongFor(Patient patient) {
        Map correctProperties = patientOverviewProperties(patient)
        Map actualProperties = patient.patientOverview.getProperties().findAll { it.key in correctProperties.keySet() }
        actualProperties != correctProperties
    }

    List<PatientOverview> getPatientsForClinicianOverview(Clinician activeClinician) {

        def importantPatientOverviewIds = PatientOverview.executeQuery(
            'select po.id ' +
            'from PatientOverview as po ' +
            'inner join po.patient as p ' +
            'inner join p.patient2PatientGroups as p2pg ' +
            'inner join p2pg.patientGroup as pg ' +
            'inner join pg.clinician2PatientGroups as c2pg ' +
            'where po.important = true ' +
            '  and c2pg.clinician.id = ?', [activeClinician.id])

        // Find all patients with reminders not seen by clinician. Since this depends on the current clinician,
        // it cannot be embedded in the PatientOverview object for each patient.
        def idsOfPatientOverviewsWithRemindersNotSeenByClinician = PatientOverview.executeQuery(
            'select po.id ' +
            'from Patient as p ' +
            'inner join p.patientOverviews as po ' +
            'inner join p.patient2PatientGroups as p2pg ' +
            'inner join p2pg.patientGroup as pg ' +
            'inner join pg.clinician2PatientGroups as c2pg ' +
            'where c2pg.clinician.id = ? ' +
            '  and p.state = ? '+
            '  and exists (from p.notes note where note.reminderDate < ? and ? not in (select id from note.seenBy))',
            [activeClinician.id, PatientState.ACTIVE, new Date(), activeClinician.id])

        importantPatientOverviewIds.addAll(idsOfPatientOverviewsWithRemindersNotSeenByClinician)
        PatientOverview.findAllByIdInList(importantPatientOverviewIds)
    }

    List<PatientOverview> getPatientsForClinicianOverviewInPatientGroup(Clinician activeClinician, PatientGroup activePatientGroup) {
        if (!activeClinician.clinician2PatientGroups.find { it.patientGroup == activePatientGroup }) {
            throw new IllegalArgumentException("Clinician ${activeClinician} is not part of given patient group (${activePatientGroup})")
        }

        def importantPatientOverviewIds = PatientOverview.executeQuery(
                'select po.id ' +
                'from PatientOverview as po ' +
                'inner join po.patient as p ' +
                'inner join p.patient2PatientGroups as p2pg ' +
                'where po.important = true ' +
                '  and p2pg.patientGroup.id = ?',
                [activePatientGroup.id]
        )

        // Find all patients with reminders not seen by clinician. Since this depends on the current clinician,
        // it cannot be embedded in the PatientOverview object for each patient.
        def idsOfPatientOverviewsWithRemindersNotSeenByClinician = PatientOverview.executeQuery(
                'select po.id ' +
                'from Patient as p ' +
                'inner join p.patientOverviews as po ' +
                'inner join p.patient2PatientGroups as p2pg ' +
                'inner join p2pg.patientGroup as pg ' +
                'where pg.id = ? ' +
                '  and p.state = ? '+
                '  and exists (from p.notes note where note.reminderDate < ? and ? not in (select id from note.seenBy))',
                [activePatientGroup.id, PatientState.ACTIVE, new Date(), activeClinician.id]
        )

        importantPatientOverviewIds.addAll(idsOfPatientOverviewsWithRemindersNotSeenByClinician)
        PatientOverview.findAllByIdInList(importantPatientOverviewIds)
    }

    Set<Long> getIdsOfPatientsWithMessagingEnabled(Clinician clinician, Collection<PatientOverview> patientOverviews) {
        if (patientOverviews.empty) {
            return Collections.emptySet()
        }

        PatientOverview.executeQuery(
                'select patient.id ' +
                'from Patient as patient ' +
                'inner join patient.patient2PatientGroups as p2pg ' +
                'inner join p2pg.patientGroup as pg ' +
                'inner join pg.clinician2PatientGroups as c2pg ' +
                'where c2pg.clinician = :clinician ' +
                '  and patient.id in :patientIds '+
                '  and pg.disableMessaging = false',
                [clinician: clinician, patientIds: patientOverviews*.patientId]
        ).toSet()
    }

    Set<Long> getIdsOfPatientsWithAlarmIfUnreadMessagesDisabled(Clinician clinician, Collection<PatientOverview> patientOverviews) {
        if (patientOverviews.empty) {
            return Collections.emptySet()
        }

        PatientOverview.executeQuery(
                'select patient.id ' +
                        'from Patient as patient ' +
                        'inner join patient.patient2PatientGroups as p2pg ' +
                        'inner join p2pg.patientGroup as pg ' +
                        'inner join pg.clinician2PatientGroups as c2pg ' +
                        'where c2pg.clinician = :clinician ' +
                        '  and patient.id in :patientIds '+
                        '  and patient.noAlarmIfUnreadMessagesToPatient = true',
                [clinician: clinician, patientIds: patientOverviews*.patientId]
        ).toSet()
    }

    Map<Long, List<PatientNote>> fetchUnseenNotesForPatients(Clinician clinician, Collection<PatientOverview> patientOverviews) {
        if (patientOverviews.empty) {
            return [:]
        }

        List<PatientNote> patientNotes = PatientNote.executeQuery(
                'select note ' +
                'from PatientNote as note ' +
                'inner join note.patient as patient ' +
                'where patient.id in :patientIds ' +
                '  and :clinicianId not in (select id from note.seenBy)',
                [patientIds: patientOverviews*.patientId, clinicianId: clinician.id]
        )

        Map<Long, List<PatientNote>> result = [:]
        patientOverviews.each { result[it.patientId] = [] }
        patientNotes.each { result[it.patientId] << it }
        result
    }

    private def patientOverviewProperties(Patient patient) {
        String name = patient.name
        String cpr = patient.cpr

        List<CompletedQuestionnaire> unacknowledgedQuestionnaires = findUnacknowledgedQuestionnaires(patient)
        int numberOfUnacknowledgedQuestionnaires = unacknowledgedQuestionnaires.size()
        Severity worstSeverity = questionnaireService.worstSeverityOfUnacknowledgedQuestionnaires(patient, unacknowledgedQuestionnaires)

        CompletedQuestionnaire questionnaireOfWorstSeverity = findQuestionnaireOfWorstSeverity(worstSeverity, unacknowledgedQuestionnaires)
        String blueAlarmText = collectBlueAlarmText(patient)
        String greenQuestionnaireIds = collectGreenQuestionnaireIds(unacknowledgedQuestionnaires)

        List<Message> unreadMessages = Message.findAllByPatientAndIsRead(patient, false, [sort: 'sendDate', order: 'asc'])
        List<Message> unreadMessagesToPatient = unreadMessages.findAll { !it.sentByPatient }
        Date dateOfOldestUnreadMessageToPatient = unreadMessagesToPatient.empty ? null : unreadMessagesToPatient.first().sendDate
        List<Message> unreadMessagesFromPatient = unreadMessages.findAll { it.sentByPatient }
        Date dateOfOldestUnreadMessageFromPatient = unreadMessagesFromPatient.empty ? null : unreadMessagesFromPatient.first().sendDate


        boolean unreadMessagesToPatientTriggersAlarm = patient.noAlarmIfUnreadMessagesToPatient ? !unreadMessagesFromPatient.empty : !unreadMessages.empty

        boolean important = (patient.state == PatientState.ACTIVE) && (
                numberOfUnacknowledgedQuestionnaires > 0 ||
                worstSeverity > Severity.NONE ||
                unreadMessagesToPatientTriggersAlarm)

        [
            patient: patient,
            name: name,
            cpr: cpr,
            numberOfUnacknowledgedQuestionnaires: numberOfUnacknowledgedQuestionnaires,
            questionnaireSeverity: worstSeverity,
            mostSevereQuestionnaireName: questionnaireOfWorstSeverity?.questionnaireHeader?.name,
            mostSevereQuestionnaireDate: questionnaireOfWorstSeverity?.uploadDate,
            blueAlarmText: blueAlarmText,
            greenQuestionnaireIds: greenQuestionnaireIds,

            numberOfUnreadMessagesToPatient: unreadMessagesToPatient.size(),
            dateOfOldestUnreadMessageToPatient: dateOfOldestUnreadMessageToPatient,
            numberOfUnreadMessagesFromPatient: unreadMessagesFromPatient.size(),
            dateOfOldestUnreadMessageFromPatient: dateOfOldestUnreadMessageFromPatient,

            important: important
        ]
    }

    private List<CompletedQuestionnaire> findUnacknowledgedQuestionnaires(Patient patient) {
        CompletedQuestionnaire.findAllByPatientAndAcknowledgedDateIsNull(patient, [sort: 'uploadDate', order: 'desc'])
    }

    private CompletedQuestionnaire findQuestionnaireOfWorstSeverity(Severity worstSeverity, List<CompletedQuestionnaire> questionnaires) {
        questionnaires.find { it.severity == worstSeverity }
    }

    private String collectBlueAlarmText(Patient patient) {
        if (patient.blueAlarmQuestionnaireIDs == null || patient.blueAlarmQuestionnaireIDs.empty) {
            return null
        }
        patient.blueAlarmQuestionnaireIDs.collect { PatientQuestionnaire.get(it).name }.join('\n')
    }

    private String collectGreenQuestionnaireIds(List<CompletedQuestionnaire> completedQuestionnaires) {
        List<CompletedQuestionnaire> greenQuestionnaires = completedQuestionnaires.findAll { it.severity == Severity.GREEN }
        greenQuestionnaires.empty ? null : greenQuestionnaires.collect { it.id }.join(',')
    }
}
