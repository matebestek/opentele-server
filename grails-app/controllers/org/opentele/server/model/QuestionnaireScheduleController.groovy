package org.opentele.server.model
import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.model.questionnaire.StandardSchedule
import org.opentele.server.model.types.PermissionName
import org.opentele.server.util.ScheduleViewModel
import org.springframework.dao.DataIntegrityViolationException

@Secured(PermissionName.NONE)
@SecurityWhiteListController
class QuestionnaireScheduleController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def questionnaireService
    def springSecurityService

    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_CREATE)
    def create() {
        QuestionnaireSchedule questionnaireSchedule = new QuestionnaireSchedule(params)
        questionnaireSchedule.monitoringPlan = MonitoringPlan.get(params.monitoringPlan.id)

        viewModelForCreateAndEdit(questionnaireSchedule)
    }

    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_WRITE)
    def edit() {
        def questionnaireSchedule = QuestionnaireSchedule.get(params.id)
        if (!questionnaireSchedule) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')])
            redirect(controller: "monitorPlan", action: "showplan", params: params)
            return
        }

        viewModelForCreateAndEdit(questionnaireSchedule)
    }

    @Secured([PermissionName.QUESTIONNAIRE_SCHEDULE_CREATE, PermissionName.QUESTIONNAIRE_SCHEDULE_WRITE])
    def save() {
        def questionnaireSchedule = new QuestionnaireSchedule()
        def viewModel = new ScheduleViewModel(params.viewModel)

        questionnaireSchedule.monitoringPlan = MonitoringPlan.get(viewModel.monitoringPlanId)

        def validationErrors = viewModel.validateViewModel()
        if(validationErrors) {
            // Preserves previous selected options
            questionnaireSchedule.id = -1;
            def Map selectedQuestionnaire = viewModel.get('selectedQuestionnaire')
            questionnaireSchedule.questionnaireHeader = QuestionnaireHeader.get(selectedQuestionnaire.get('id'))
            viewModel.updateSchedule(questionnaireSchedule)

            render(view: 'create', model: viewModelForCreateAndEdit(questionnaireSchedule, validationErrors))
            return
        }

        createOrUpdate(questionnaireSchedule, viewModel, 'create', 'default.created.message')
    }

    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_WRITE)
    def update() {
        def viewModel = new ScheduleViewModel(params.viewModel)
        def questionnaireSchedule = QuestionnaireSchedule.get(viewModel.id)

        def validationErrors = viewModel.validateViewModel()
        if (validationErrors) {
            render(view: 'edit', model: viewModelForCreateAndEdit(questionnaireSchedule, validationErrors))
            return
        }

        if (!questionnaireSchedule) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')])
            redirect(controller: "monitorPlan", action: "showplan", params: params)
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (questionnaireSchedule.version > version) {
                println "OPTIMISTIC LOCKING -> EDIT"
                questionnaireSchedule.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')] as Object[],
                          "Another user has updated this QuestionnaireSchedule while you were editing")

                render(view: "edit", model: [questionnaireSchedule: questionnaireSchedule])
                return
            }
        }

        createOrUpdate(questionnaireSchedule, viewModel, 'edit', 'default.updated.message')
    }

    private viewModelForCreateAndEdit(QuestionnaireSchedule questionnaireSchedule, validationErrors = []) {
        def selectableQuestionnaireHeaders = questionnaireService.getUnusedQuestionnaireHeadersForMonitoringPlan(questionnaireSchedule.monitoringPlan)
        def selectedQuestionnaireHeaderId = null

        if (questionnaireSchedule.questionnaireHeader) {
            QuestionnaireHeader currentQuestionnaireHeader = questionnaireSchedule.questionnaireHeader
            selectableQuestionnaireHeaders.add(currentQuestionnaireHeader)
            selectableQuestionnaireHeaders.sort { it.toString() }
            selectedQuestionnaireHeaderId = currentQuestionnaireHeader.id
        }

        /* If questionnaire schedule is about to be created, and monitoring plan has start date in the future
           -> set q.s. starting date to monitoring plan start date */
        def Date mpStartDate = questionnaireSchedule.monitoringPlan.startDate
        if (!questionnaireSchedule.id && mpStartDate.after(new Date())) {
            questionnaireSchedule.setStartingDate(Schedule.StartingDate.fromDate(mpStartDate))
        }

        def map = [
                id: questionnaireSchedule.id ?: '""',
                version: questionnaireSchedule.version ?: '""',
                monitoringPlanId: questionnaireSchedule.monitoringPlan.id,
                scheduleType: questionnaireSchedule.type.name(),
                timesOfDay: questionnaireSchedule.timesOfDay.collect({ [hour: it.hour, minute: it.minute] }),
                weekdays: questionnaireSchedule.weekdays*.name() as JSON,
                daysInMonth: questionnaireSchedule.daysInMonth.collect { it.toString() } as JSON,
                intervalInDays: questionnaireSchedule.dayInterval,
                startingDate: g.formatDate(date: questionnaireSchedule.startingDate.calendar.getTime(), format: "dd-MM-yyyy"),
                specificDate: questionnaireSchedule.specificDate == null ? null : g.formatDate(date: questionnaireSchedule.specificDate.calendar.getTime(), format: "dd-MM-yyyy"),
                reminderStartMinutes: questionnaireSchedule.reminderStartMinutes,
                selectableQuestionnaires: buildSelectableQuestionnaireHeadersMap(selectableQuestionnaireHeaders) as JSON,
                selectedQuestionnaireId: selectedQuestionnaireHeaderId,
                validationErrors: translateValidationErrors(validationErrors) as JSON
        ]
        return map
    }

    private buildSelectableQuestionnaireHeadersMap(List selectableQuestionnaireHeaders) {
        selectableQuestionnaireHeaders.collect { sqh ->
            def standardSchedule = sqh?.activeQuestionnaire?.standardSchedule ?: new StandardSchedule()

            [
                    id: sqh.id,
                    name: sqh.toString(),
                    hasActiveQuestionnaire: sqh.activeQuestionnaire != null,
                    type: standardSchedule.type.name(),
                    daysInMonth: standardSchedule.daysInMonth.collect { it.toString() },
                    weekdays: standardSchedule.weekdays*.name(),
                    timesOfDay: standardSchedule.timesOfDay,
                    intervalInDays: standardSchedule.intervalInDays,
                    startingDate: g.formatDate(date: standardSchedule.startingDate.calendar.getTime(), format: "dd-MM-yyyy"),
                    specificDate: standardSchedule.specificDate == null ? null : g.formatDate(date: standardSchedule.specificDate.calendar.getTime(), format: "dd-MM-yyyy"),
                    reminderStartMinutes: standardSchedule.reminderStartMinutes
            ]
        }
    }

    private translateValidationErrors(validationErrors) {
        validationErrors.collect { error ->
            [field: error.field, message: message(code: error.message)]
        }
    }

    private createOrUpdate(QuestionnaireSchedule questionnaireSchedule, ScheduleViewModel viewModel, redirectViewIfFailing, successMessageCode) {
        viewModel.updateSchedule(questionnaireSchedule)

        MonitoringPlan plan = MonitoringPlan.get(viewModel.monitoringPlanId)
        questionnaireSchedule.monitoringPlan = plan

        questionnaireSchedule.questionnaireHeader = QuestionnaireHeader.get(viewModel.selectedQuestionnaire.id)

        if (questionnaireSchedule.hasErrors() || !questionnaireSchedule.save(flush: true)) {
            render(view: redirectViewIfFailing, model: viewModelForCreateAndEdit(questionnaireSchedule))
            return
        }

		flash.message = message(code: successMessageCode, args: [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')])
        redirect(controller: "monitoringPlan", action: "show", id: questionnaireSchedule.monitoringPlan.patient.id)
    }

    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_DELETE)
    @SecurityWhiteListController
    def del() {
        def questionnaireSchedule = QuestionnaireSchedule.get(params.id)
        try {
            def pId = questionnaireSchedule?.monitoringPlan?.patient?.id
            //questionnaireSchedule.patientQuestionnaire.deleted = true
            questionnaireSchedule.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')])
            redirect(controller: "monitoringPlan", action: "show", id: pId)
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')])
            redirect(controller: "monitoringPlan", action: "show", id: pId)
        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_CREATE)
    def showAddQuestionnaireGroup(AddQuestionnaireGroup2MonitoringPlanCommand command) {
        [command: command]
    }

    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_CREATE)
    def pickQuestionnaireGroup(AddQuestionnaireGroup2MonitoringPlanCommand command) {
        command.newQuestionnaires = questionnaireService.findQuestionnaireGroup2HeadersAndOverlapWithExistingQuestionnaireSchedules(command.questionnaireGroup, command.monitoringPlan)
        [command: command]
    }

    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_CREATE)
    def addQuestionnaireGroup(AddQuestionnaireGroup2MonitoringPlanCommand command) {
        log.debug(command)
        questionnaireService.addOrUpdateQuestionnairesOnMonitoringPlan(command)
        if (command.addedQuestionnaires || command.updatedQuestionnaires) {
            appendFlash(code: 'questionnaireGroup.questionnaireAddOrUpdate.pre')
            if (command.addedQuestionnaires) {
                appendFlash(code: "questionnaireGroup.questionnaireAddOrUpdate.add", args: [command.addedQuestionnaires.size()])
            }
            if(command.addedQuestionnaires && command.updatedQuestionnaires) {
                appendFlash(code: 'questionnaireGroup.questionnaireAddOrUpdate.and')
            }
            if (command.updatedQuestionnaires) {
                appendFlash(code: "questionnaireGroup.questionnaireAddOrUpdate.update", args: [command.updatedQuestionnaires.size()])
            }
            appendFlash(code: 'questionnaireGroup.questionnaireAddOrUpdate.post', args: [command.updatedQuestionnaires.size() + command.addedQuestionnaires.size() == 1 ? '' : message(code: 'questionnaireGroup.questionnaireAddOrUpdate.plural')])
        } else {
            flash.message = message(code: 'questionnaireGroup.questionnaireAddOrUpdate.none')
        }
        flash.updated = command.addedQuestionnaires*.id + command.updatedQuestionnaires*.id
        redirect(controller: "monitoringPlan", action: "show", id: command.monitoringPlan.patient.id)
    }

    private appendFlash(Map map) {
        flash.message = "${flash.message ?: ''} ${message(map)}"
    }
}
