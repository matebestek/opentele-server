package org.opentele.server.model.questionnaire
import dk.silverbullet.kih.api.auditlog.SkipAuditLog
import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.web.json.JSONObject
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.model.types.PermissionName

@Secured(PermissionName.NONE)
@SecurityWhiteListController
class QuestionnaireEditorController {
    def questionnaireEditorService
    def clinicianService

    static allowedMethods = [save:'POST',  editorState:'GET']

    // Databinding with commandobject and JSON works because of org.opentele.server.JSONParamsMapFilters
    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
    def save(QuestionnaireEditorCommand command) {
        def json = request.JSON
        command.nodes = json.nodes // TODO: Because Grails 2.1.x does not bind to LazyMaps correctly
        command.cloneScheduleData(json.standardSchedule)
        if(command.validate()) {
            try {
                questionnaireEditorService.createOrUpdateQuestionnaire(command, clinicianService.currentClinician)
                render([command.questionnaire.id] as JSON)
                return
            } catch (e) {
                log.warn("Could not save questionnaire due to error: ${e.message}", e)
                command.errors.reject("questionnaireeditor.unknown.error")
            }
        } else {
            log.debug("Save validation failed")
        }
        response.status = 422;
        render(template: '/questionnaireSchedule/errors', model: [errors: command.errors])
    }

    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
    def edit(Long id, QuestionnaireEditorCommand command) {
        def questionnaireHeader = QuestionnaireHeader.get(id)
        command.questionnaireHeader = questionnaireHeader
        command.title = questionnaireHeader.name
        bindData(command, command.questionnaire.standardSchedule.properties)

        if(command.validate(['questionnaireHeader.id'])) {
            command.properties
        } else {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireHeader.label', default: 'QuestionnaireHeader'), id])
            redirect(controller: "questionnaireHeader", action: "list")
        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_WRITE)
    def editorState(Long baseId) {
        def questionnaire = Questionnaire.findById(baseId)
        render new JSONObject(questionnaire?.editorState) as JSON
    }

    @Secured(PermissionName.QUESTIONNAIRE_WRITE)
    @SkipAuditLog
    def keepAlive() {
        render "pong"
    }
}
