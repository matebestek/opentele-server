package org.opentele.server.model.questionnaire

import grails.plugins.springsecurity.Secured
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.model.types.PermissionName
import org.opentele.server.util.ScheduleViewModel
import org.springframework.dao.DataIntegrityViolationException

@Secured(PermissionName.NONE)
@SecurityWhiteListController
class QuestionnaireGroup2QuestionnaireHeaderController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def questionnaireService
    def questionnaireGroupService

    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.QUESTIONNAIRE_GROUP_CREATE)
    def create() {
        def questionnaireGroup2QuestionnaireHeader = new QuestionnaireGroup2QuestionnaireHeader(params)
        def viewModel = questionnaireGroupService.viewModelForCreateAndEdit(questionnaireGroup2QuestionnaireHeader)

        if (viewModel.selectableQuestionnaires.size == 0) {
            flash.message = message(code: 'questionnaireGroup2QuestionnaireHeader.questionnaire.none.left')
            redirect(controller: "questionnaireGroup", action: "show", id: params.questionnaireGroup.id)
        } else {
            viewModel
        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_GROUP_CREATE)
    def save() {
        def questionnaireGroup2QuestionnaireHeaderInstance = new QuestionnaireGroup2QuestionnaireHeader(params)
        def viewModel = new ScheduleViewModel(params.viewModel)

        questionnaireGroup2QuestionnaireHeaderInstance.questionnaireGroup = QuestionnaireGroup.get(viewModel.questionnaireGroupId)

        def validationErrors = viewModel.validateViewModel()
        if (viewModel.type && validationErrors) {
            render(view: 'create', model: questionnaireGroupService.viewModelForCreateAndEdit(questionnaireGroup2QuestionnaireHeaderInstance, validationErrors))
            return
        }

        if (questionnaireGroupService.createOrUpdate(questionnaireGroup2QuestionnaireHeaderInstance, viewModel)) {
            flash.message = message(code: 'default.created.message', args: [message(code: 'questionnaireGroup2QuestionnaireHeader.label', default: 'QuestionnaireSchedule')])
            redirect(controller: "questionnaireGroup", action: "show", id: questionnaireGroup2QuestionnaireHeaderInstance.questionnaireGroup.id)
        } else {
            render(view: 'create', model: questionnaireGroupService.viewModelForCreateAndEdit(questionnaireGroup2QuestionnaireHeaderInstance, domainErrorsToErrors(questionnaireGroup2QuestionnaireHeaderInstance)))
        }
    }


    @Secured(PermissionName.QUESTIONNAIRE_GROUP_WRITE)
    def edit(Long id) {
        withInstance(id) {
            questionnaireGroupService.viewModelForCreateAndEdit(it)
        }

    }

    @Secured(PermissionName.QUESTIONNAIRE_GROUP_WRITE)
    def update(Long id) {
        ScheduleViewModel viewModel = new ScheduleViewModel(params.viewModel)

        withInstance(viewModel.id) {
            def validationErrors = viewModel.validateViewModel()
            if (validationErrors) {
                render(view: 'edit', model: questionnaireGroupService.viewModelForCreateAndEdit(it, validationErrors))
            } else if (viewModel.version) {
                def version = viewModel.version.toLong()
                if (it.version > version) {
                    it.errors.rejectValue("version", "default.optimistic.locking.failure",
                            [message(code: 'questionnaireGroup2QuestionnaireHeader.label', default: 'QuestionnaireGroup2QuestionnaireHeader')] as Object[],
                            "Another user has updated this QuestionnaireGroup2QuestionnaireHeader while you were editing")
                    render(view: "edit", model: [questionnaireGroup2QuestionnaireHeaderInstance: it])
                    return
                }
            }

            if (questionnaireGroupService.createOrUpdate(it, viewModel)) {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'questionnaireGroup2QuestionnaireHeader.label', default: 'QuestionnaireSchedule')])
                redirect(controller: "questionnaireGroup", action: "show", id: it.questionnaireGroup.id)
            } else {
                render(view: 'edit', model: questionnaireGroupService.viewModelForCreateAndEdit(it, domainErrorsToErrors(it)))
            }

        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_GROUP_DELETE)
    def delete(Long id) {
        withInstance(id) {
            QuestionnaireGroup questionnaireGroup = it.questionnaireGroup
            try {
                questionnaireGroup.removeFromQuestionnaireGroup2Header(it)
                questionnaireGroup.save()
                it.delete(flush: true)
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'questionnaireGroup2QuestionnaireHeader.label', default: 'QuestionnaireGroup2QuestionnaireHeader'), questionnaireGroup.id])
                redirect(controller: "questionnaireGroup", action: "show", id: questionnaireGroup.id)
            }
            catch (DataIntegrityViolationException e) {
                flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'questionnaireGroup2QuestionnaireHeader.label', default: 'QuestionnaireGroup2QuestionnaireHeader'), questionnaireGroup.id])
                redirect(controller: "questionnaireGroup", action: "show", id: questionnaireGroup.id)
            }
        }
    }

    private domainErrorsToErrors(QuestionnaireGroup2QuestionnaireHeader questionnaireGroup2QuestionnaireHeader) {
        questionnaireGroup2QuestionnaireHeader.errors.fieldErrors.collect {
            [
                    field: it.field,
                    message: message(error: it)
            ]
        }
    }

    private withInstance(Long id, Closure closure) {
        def questionnaireGroup2Header = QuestionnaireGroup2QuestionnaireHeader.get(id)
        if (questionnaireGroup2Header) {
            return closure.call(questionnaireGroup2Header)
        } else {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireGroup2QuestionnaireHeader.label', default: 'QuestionnaireGroup2QuestionnaireHeader'), "$id"])
            redirect(controller: "questionnaireGroup", action: "list")
        }

    }


}
