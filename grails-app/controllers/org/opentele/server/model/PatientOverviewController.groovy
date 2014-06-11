package org.opentele.server.model
import grails.plugins.springsecurity.Secured
import org.opentele.server.TimeFilter
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.constants.Constants
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.types.PermissionName

@Secured(PermissionName.NONE)
@SecurityWhiteListController
class PatientOverviewController {
    def sessionService
    def patientService
    def patientNoteService
    def completedQuestionnaireService
    def questionnaireService
    def messageService
    def clinicianService
    def patientOverviewService

    static allowedMethods = [index: 'GET', details: 'GET', acknowledgeQuestionnaireAndRenderDetails: 'POST', acknowledgeAll: 'GET', acknowledgeAllForAll:'GET']

    @Secured(PermissionName.PATIENT_READ_ALL)
    def index() {
        Clinician clinician = clinicianService.currentClinician
        sessionService.setNoPatient(session)

        if (params['patientgroup.filter.id'] != null) {
            session[Constants.SESSION_PATIENT_GROUP_ID] = params.long('patientgroup.filter.id')
        }

        List<PatientOverview> patientOverviews = fetchPatientOverviews(clinician)
        Set<Long> idsOfPatientsWithMessaging = patientOverviewService.getIdsOfPatientsWithMessagingEnabled(clinician, patientOverviews)
        Set<Long> idsOfPatientsWithAlarmIfUnreadMessagesDisabled = patientOverviewService.getIdsOfPatientsWithAlarmIfUnreadMessagesDisabled(clinician, patientOverviews)
        Map<Long, List<PatientNote>> patientNotes = patientOverviewService.fetchUnseenNotesForPatients(clinician, patientOverviews)

        sortPatientOverviews(patientOverviews, patientNotes)

        [
            patients: patientOverviews,
            patientNotes: patientNotes,
            idsOfPatientsWithMessaging: idsOfPatientsWithMessaging,
            idsOfPatientsWithAlarmIfUnreadMessagesDisabled: idsOfPatientsWithAlarmIfUnreadMessagesDisabled,
            questionPreferences: questionPreferencesForClinician(clinician),
            clinicianPatientGroups: clinicianService.patientGroupsForCurrentClinician
        ]
    }

    @Secured(PermissionName.PATIENT_READ_ALL)
    def details () {
        def clinician = clinicianService.currentClinician
        def patient = Patient.get(params.id)
        checkAccessToPatient(patient)

        def completedQuestionnaireResultModel = questionnaireService.extractMeasurements(patient.id, true, TimeFilter.all())

        def noUnacknowledgedQuestionnaires = completedQuestionnaireResultModel.columnHeaders.empty

        [
            patient: patient,
            noUnacknowledgedQuestionnaires: noUnacknowledgedQuestionnaires,
            questionPreferences: questionPreferencesForClinician(clinician),
            completedQuestionnaireResultModel: completedQuestionnaireResultModel
        ]
    }

    @Secured(PermissionName.QUESTIONNAIRE_ACKNOWLEDGE)
    def acknowledgeQuestionnaireAndRenderDetails() {
        def withAutoMessage = params.boolean('withAutoMessage')
        def questionnaire = CompletedQuestionnaire.get(params.id)
        def patient = questionnaire.patient
        def clinician = clinicianService.currentClinician
        checkAccessToPatient(patient)

        completedQuestionnaireService.acknowledge(questionnaire, null, withAutoMessage)

        def unacknowledgedQuestionnaires = CompletedQuestionnaire.findAllByPatientAndAcknowledgedDateIsNull(patient, [sort: 'uploadDate', order: 'desc'])
        def completedQuestionnaireResultModel = questionnaireService.extractMeasurements(patient.id, true, TimeFilter.all())
        def patientNotes = patientOverviewService.fetchUnseenNotesForPatients(clinician, [patient.patientOverview])[patient.id]

        render(view: '/patientOverview/detailsWithHeader', model: [
            patient: patient,
            patientOverview: patient.patientOverview,
            messagingEnabled: messageService.clinicianCanSendMessagesToPatient(clinician, patient),
            completedQuestionnaireResultModel: completedQuestionnaireResultModel,
            unacknowledgedQuestionnaires: unacknowledgedQuestionnaires,
            patientNotes: patientNotes,
            questionPreferences: questionPreferencesForClinician(clinician)
        ])
    }

    @Secured(PermissionName.QUESTIONNAIRE_ACKNOWLEDGE)
    def acknowledgeAll(Long id, boolean withAutoMessage) {
        def patient = Patient.get(id)
        if(!patient) {
            // Setting up session values
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient')])
        } else {

            def completedQuestionnaireIds = params.list('ids').collect {it as Long}

            def questionnaires = CompletedQuestionnaire.findAllByIdInList(completedQuestionnaireIds)
            questionnaires.each { questionnaire ->
                checkAccessToPatient(questionnaire.patient)
            }

            completedQuestionnaireService.acknowledge(questionnaires, withAutoMessage)

            def msg = questionnaires.collect { questionnaire ->

                g.message(code: "completedquestionnaire.acknowledged",
                     args: [questionnaire.patientQuestionnaire?.name, g.formatDate(date: questionnaire.acknowledgedDate)])
            }

            if (msg) {
                flash.message = msg.join('<br/>')
            }
        }
        redirect(controller: session.lastController, action: session.lastAction, params: session.lastParams)
    }

    @Secured(PermissionName.QUESTIONNAIRE_ACKNOWLEDGE)
    def acknowledgeAllForAll(boolean withAutoMessage) {
        Clinician clinician = clinicianService.currentClinician
        List<PatientOverview> patients = fetchPatientOverviews(clinician)

        List<Long> unacknowledgedGreenQuestionnaireIds = []
        patients.findAll {
            it.greenQuestionnaireIds != null
        }.each {
            it.greenQuestionnaireIds.split(',').each {
                unacknowledgedGreenQuestionnaireIds << it.toLong()
            }
        }
        List<CompletedQuestionnaire> unacknowledgedGreenQuestionnaires = CompletedQuestionnaire.findAllByIdInList(unacknowledgedGreenQuestionnaireIds)
        completedQuestionnaireService.acknowledge(unacknowledgedGreenQuestionnaires, withAutoMessage)

        redirect(action: 'index')
    }

    private void checkAccessToPatient(Patient patient) {
        if (!patientService.allowedToView(patient)) {
            throw new IllegalStateException("User not allowed to view patient ${patient.id}")
        }
    }

    private def questionPreferencesForClinician(Clinician clinician) {
        if (clinician != null) {
            ClinicianQuestionPreference.findAllByClinicianAndQuestionIsNotNull(clinician, [sort:'id', order:'asc'])?.collect { it.questionId }
        } else {
            []
        }
    }

    private List<PatientOverview> fetchPatientOverviews(Clinician clinician) {
        PatientGroup patientGroupFilter = session[Constants.SESSION_PATIENT_GROUP_ID] ? PatientGroup.get(session[Constants.SESSION_PATIENT_GROUP_ID]) : null

        patientGroupFilter == null ?
            patientOverviewService.getPatientsForClinicianOverview(clinician) :
            patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, patientGroupFilter)
    }

    private void sortPatientOverviews(List<PatientOverview> patientOverviews, Map<Long, List<PatientNote>> patientNotes) {
        //Sort patient list by severity

        patientOverviews.sort { a, b ->

            def result = b.questionnaireSeverity <=> a.questionnaireSeverity

            if (result == 0) {
                result = b.numberOfUnreadMessagesFromPatient <=> a.numberOfUnreadMessagesFromPatient

                if (result == 0) {
                    result = b.numberOfUnreadMessagesToPatient <=> a.numberOfUnreadMessagesToPatient

                    if (result == 0) {
                        result = patientNoteService.countImportantWithReminder(patientNotes[b.id]) <=> patientNoteService.countImportantWithReminder(patientNotes[a.id])

                        if (result == 0) {

                            result = patientNoteService.countNormalWithReminder(patientNotes[b.id]) <=> patientNoteService.countNormalWithReminder(patientNotes[a.id])
                            if (result == 0) {

                                result = patientNoteService.countImportantWithoutDeadline(patientNotes[b.id]) <=> patientNoteService.countImportantWithoutDeadline(patientNotes[a.id])
                                if (result == 0) {

                                    result = patientNotes[b.id].size() <=> patientNotes[a.id].size()
                                    if (result == 0) {
                                        result = a.name <=> b.name
                                    }
                                }
                            }
                        }
                    }
                }
            }
            result
        }
    }
}



