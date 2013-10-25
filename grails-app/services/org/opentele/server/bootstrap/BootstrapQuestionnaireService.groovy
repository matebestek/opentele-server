package org.opentele.server.bootstrap

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONElement
import org.opentele.server.model.Clinician
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.model.types.Severity

class BootstrapQuestionnaireService {
    def questionnaireEditorService
    def questionnaireHeaderService
    def grailsApplication

    def ensureQuestionnaireExists(Clinician creator, String title, String jsonFileName) {
        def questionnaireHeader = QuestionnaireHeader.findByName(title)

        if (questionnaireHeader == null) {
            println "Creating questionnaire '${title}'"

            QuestionnaireHeader header = createQuestionnaireHeader(title)
            JSONElement json = readQuestionnaireJson(jsonFileName, title, header)

            questionnaireEditorService.createOrUpdateQuestionnaire(json, creator)
            questionnaireHeaderService.publishDraft(header, creator)
        }
    }

    private createQuestionnaireHeader(String title) {
        QuestionnaireHeader header = new QuestionnaireHeader(name: title, requiresManualInspection: title.contains('CTG'))
        header.questionnaires = []
        header.save(failOnError: true)
        header
    }

    private readQuestionnaireJson(String jsonFileName, String title, QuestionnaireHeader header) {
        def jsonFile = grailsApplication.getParentContext().getResource("classpath:resources/questionnaires/${jsonFileName}").inputStream.getText('UTF-8')
        def json = JSON.parse(jsonFile)
        json['title'] = title
        json['questionnaireHeaderId'] = header.id
        json
    }
}
