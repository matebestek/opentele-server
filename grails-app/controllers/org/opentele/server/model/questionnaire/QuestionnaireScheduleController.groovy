package org.opentele.server.model.questionnaire

import grails.plugins.springsecurity.Secured
import org.opentele.server.QuestionnaireScheduleService
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.model.AddQuestionnaireGroup2MonitoringPlanCommand
import org.opentele.server.model.QuestionnaireSchedule
import org.opentele.server.model.QuestionnaireScheduleCommand
import org.opentele.server.model.types.PermissionName
import org.opentele.server.questionnaire.QuestionnaireService
import org.springframework.dao.DataIntegrityViolationException

import static org.opentele.server.constants.Constants.SESSION_PATIENT_ID

@Secured(PermissionName.NONE)
@SecurityWhiteListController
class QuestionnaireScheduleController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    QuestionnaireService questionnaireService
    QuestionnaireScheduleService questionnaireScheduleService

    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_CREATE)
    def create(QuestionnaireScheduleCommand command) {
        adjustScheduleStartDate(command)
        command.properties
    }



    @Secured([PermissionName.QUESTIONNAIRE_SCHEDULE_CREATE, PermissionName.QUESTIONNAIRE_SCHEDULE_WRITE])
    def save(QuestionnaireScheduleCommand command) {
        questionnaireScheduleService.save(command)
        if (command.hasErrors()) {
            render(view: "create", model: modelWithErrors(command))
        } else {
            flash.message = message(code: 'default.created.message', args: [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')])
            redirect(controller: "monitoringPlan", action: "show", id: command.monitoringPlan.patient.id)
        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_WRITE)
    def edit(Long id, QuestionnaireScheduleCommand command) {
        def questionnaireSchedule = QuestionnaireSchedule.get(id)
        if (!questionnaireSchedule) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')])
            redirect(controller: "monitoringPlan", action: "show", id: session[SESSION_PATIENT_ID])
            return
        }
        command.questionnaireSchedule = questionnaireSchedule
        bindData(command, questionnaireSchedule.properties)
        return command.properties
    }


    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_WRITE)
    def update(QuestionnaireScheduleCommand command) {
        if (!command.questionnaireSchedule) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')])
            redirect(controller: "monitoringPlan", action: "show", id: session[SESSION_PATIENT_ID])
            return
        }
        questionnaireScheduleService.update(command)

        if (command.hasErrors()) {
            render(view: "edit", model: modelWithErrors(command))
        } else {
            flash.message = message(code: 'default.updated.message', args: [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')])
            redirect(controller: "monitoringPlan", action: "show", id: command.monitoringPlan.patient.id)
        }
    }

    private modelWithErrors(def command) {
        def model = command.properties.collectEntries { key, value -> [key, value] }
        model.errors = command.errors
        model
    }


    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_DELETE)
    @SecurityWhiteListController
    def del(Long id) {
        def questionnaireSchedule = QuestionnaireSchedule.get(id)
        if (!questionnaireSchedule) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')])
        } else {
            try {
                questionnaireSchedule.delete(flush: true)
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')])
            }
            catch (DataIntegrityViolationException ignored) {
                flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')])
            }
        }
        redirect(controller: "monitoringPlan", action: "show", id: session[SESSION_PATIENT_ID])
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
            if (command.addedQuestionnaires && command.updatedQuestionnaires) {
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
        flash.message = "${flash.message ?: ''} ${message(map)}".trim()
    }

    private adjustScheduleStartDate(QuestionnaireScheduleCommand command) {
        def monitoringPlanStartDate = command.monitoringPlan.startDate
        if (monitoringPlanStartDate.after(new Date())) {
            command.startingDate = monitoringPlanStartDate
        }
    }
}


