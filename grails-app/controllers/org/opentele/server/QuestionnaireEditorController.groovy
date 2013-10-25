package org.opentele.server

import dk.silverbullet.kih.api.auditlog.SkipAuditLog
import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.web.json.JSONObject
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.model.types.PermissionName

@Secured(PermissionName.NONE)
@SecurityWhiteListController
class QuestionnaireEditorController {
    def questionnaireEditorService
    def clinicianService

    static allowedMethods = [save:'POST',  editorState:'GET']

    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
    def save() {
        try {
            def questionnaire = questionnaireEditorService.createOrUpdateQuestionnaire(request.JSON, clinicianService.currentClinician)
            render([questionnaire.id] as JSON)
        } catch (e) {
            response.status = 422
            log.warn("Could not save questionnaire", e)
            render(text: e.message)
        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
    def edit(Long id, Long baseId) {
        def questionnaireHeader = QuestionnaireHeader.get(id)
        if(questionnaireHeader) {
            def questionnaire = questionnaireHeader.draftQuestionnaire ?: questionnaireHeader.questionnaires.find { it.id == baseId } ?: new Questionnaire()
            [questionnaireHeader: questionnaireHeader, questionnaire: questionnaire ]
        } else {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireHeader.label', default: 'QuestionnaireHeader'), id])
            redirect(controller: "questionnaireHeader", action: "list")
        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_WRITE)
    def editorState(Long id, Long baseId) {
        def questionnaire = Questionnaire.findById(baseId)
        render new JSONObject(questionnaire?.editorState) as JSON
    }

    @Secured(PermissionName.QUESTIONNAIRE_WRITE)
    @SkipAuditLog
    def keepAlive() {
        render "pong"
    }
}
