package org.opentele.server.model
import grails.plugins.springsecurity.Secured
import org.opentele.server.TimeFilter
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.constants.Constants
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.types.PatientState
import org.opentele.server.model.types.PermissionName

@Secured(PermissionName.NONE)
@SecurityWhiteListController
class PatientOverviewController {
    def sessionService
    def patientService
    def completedQuestionnaireService
    def questionnaireService
    def clinicianService

    static allowedMethods = [index: 'GET', details: 'GET', acknowledgeQuestionnaireAndRenderDetails: 'POST', acknowledgeAll: 'GET', acknowledgeAllForAll:'GET']

    @Secured(PermissionName.PATIENT_READ_ALL)
    def index() {
        Clinician clinician = clinicianService.currentClinician
        sessionService.setNoPatient(session)

        if (params['patientgroup.filter.id'] != null) {
            session[Constants.SESSION_PATIENT_GROUP_ID] = params.long('patientgroup.filter.id')
        }

        PatientGroup patientGroupFilter = session[Constants.SESSION_PATIENT_GROUP_ID] ? PatientGroup.get(session[Constants.SESSION_PATIENT_GROUP_ID]) : null

        def allPatientsForClinician = patientService.getPatientsForClinician(clinician)
        def activePatients = allPatientsForClinician.findAll { it.state == PatientState.ACTIVE }
        def activePatientsInGroupFilter = activePatients.findAll { patientGroupFilter == null || it.groups.contains(patientGroupFilter) }

        Map<Patient, List<CompletedQuestionnaire>> patientsToCompletedQuestionnaires = findPatientsToCompletedQuestionnaires(activePatientsInGroupFilter)
        Map<Patient, List<PatientNote>> patientsToNotes = findPatientsToNotes(activePatientsInGroupFilter)

        def patientsToShow = activePatientsInGroupFilter.grep { Patient patient ->
            if (!patientsToCompletedQuestionnaires[patient].empty) {
                return true
            }

            def unreadMessageForPatient = Message.findByPatientAndIsRead(patient, false)
            if (unreadMessageForPatient != null) {
                return true
            }

            if (!patient.blueAlarmQuestionnaireIDs.empty) {
                return true
            }

            def hasUnseenReminders = patientsToNotes[patient].any {note ->
                note.remindToday && !patientService.isNoteSeenByUser(note)
            }

            return hasUnseenReminders
        }


        //Sort patient list by severity
        patientsToShow.sort { a, b ->
            def (questionnairesForA, questionnairesForB) = [patientsToCompletedQuestionnaires[a], patientsToCompletedQuestionnaires[b]]
            def worstSeverityForA = questionnaireService.worstSeverityOfUnacknowledgedQuestionnaires(a, questionnairesForA)
            def worstSeverityForB = questionnaireService.worstSeverityOfUnacknowledgedQuestionnaires(b, questionnairesForB)
            worstSeverityForB <=> worstSeverityForA
        }

        [
            patientList: patientsToShow,
            patientsToCompletedQuestionnaires: patientsToCompletedQuestionnaires,
            questionPreferences: questionPreferencesForClinician(clinician),
            notes: patientsToNotes,
            clinicianPatientGroups: Clinician2PatientGroup.findAllByClinician(clinician).collect { it.patientGroup }
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

        completedQuestionnaireService.acknowledge(questionnaire, null)
        if (withAutoMessage) {
            completedQuestionnaireService.sendAcknowledgeAutoMessage(questionnaire)
        }

        def unacknowledgedQuestionnaires = CompletedQuestionnaire.findAllByPatientAndAcknowledgedDateIsNull(patient, [sort: 'uploadDate', order: 'desc'])

        def completedQuestionnaireResultModel = questionnaireService.extractMeasurements(patient.id, true, TimeFilter.all())

        def notes = patient.notes.sort { it.createdDate }

        render(view: '/patientOverview/detailsWithHeader', model: [
            patient: patient,
            completedQuestionnaireResultModel: completedQuestionnaireResultModel,
            unacknowledgedQuestionnaires: unacknowledgedQuestionnaires,
            notes: notes,
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

        println session.lastParams


        redirect(controller: session.lastController, action: session.lastAction, params: session.lastParams)
    }

    @Secured(PermissionName.QUESTIONNAIRE_ACKNOWLEDGE)
    def acknowledgeAllForAll(boolean withAutoMessage) {
        //Same method for getting the patientList that is passed to overview.gps
        def clinician = clinicianService.currentClinician
        PatientGroup patientGroupFilter = session[Constants.SESSION_PATIENT_GROUP_ID] ? PatientGroup.get(session[Constants.SESSION_PATIENT_GROUP_ID]) : null

        def patientList = patientService.getPatientsForClinician(clinician)
        //If overviewfilter is used, filter our patientlist aswell
        if (patientGroupFilter) {
            patientList = patientList.findAll { it.groups.contains(patientGroupFilter)}
        }

        //Only get CQs that are green has have no blue alarms and are not previously acknowledged
        def unacknowledgedGreenCompletedQuestionnaires = CompletedQuestionnaire.unacknowledgedGreenQuestionnairesByPatients(patientList).list()

        completedQuestionnaireService.acknowledge(unacknowledgedGreenCompletedQuestionnaires, withAutoMessage)

        redirect(action: 'index')
    }

    private Map<Patient, List<CompletedQuestionnaire>> findPatientsToCompletedQuestionnaires(List<Patient> patients) {
        Map<Patient, List<CompletedQuestionnaire>> result = [:]
        if (patients.empty) {
            return result
        }

        patients.each { result[it] = [] }
        def allUnacknowledgedQuestionnaires = CompletedQuestionnaire.findAllByPatientInListAndAcknowledgedDateIsNull(patients, [sort: 'uploadDate', order: 'desc'])
        allUnacknowledgedQuestionnaires.each {
            result[it.patient] << it
        }

        result
    }

    private Map<Patient, List<PatientNote>> findPatientsToNotes(List<Patient> patients) {
        Map<Patient, List<PatientNote>> result = [:]
        if (patients.empty) {
            return result
        }

        patients.each { result[it] = [] }
        def allPatientNotes = PatientNote.findAllByPatientInList(patients, [sort: 'createdDate', order: 'asc'])
        allPatientNotes.each {
            result[it.patient] << it
        }

        result
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
}
