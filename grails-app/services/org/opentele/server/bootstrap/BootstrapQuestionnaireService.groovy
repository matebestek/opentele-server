package org.opentele.server.bootstrap
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONElement
import org.opentele.server.model.Clinician
import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.model.questionnaire.QuestionnaireEditorCommand
import org.opentele.server.model.questionnaire.QuestionnaireHeader

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
            QuestionnaireEditorCommand command = new QuestionnaireEditorCommand()
            command.cloneScheduleData(json.standardSchedule)

            // TODO: Maps does not bind well before Grails 2.3 and binding fails in integration tests
            command.questionnaireHeader = header
            command.nodes = json.nodes
            command.connections = json.connections
            command.title = json.title

            questionnaireEditorService.createOrUpdateQuestionnaire(command, creator)
            questionnaireHeaderService.publishDraft(header, creator)
        }
    }

    def ensureQuestionnaireVersionExists(Clinician creator, String title, String revision, String jsonFileName) {
        def questionnaireHeader = QuestionnaireHeader.findByName(title)

        if (questionnaireHeader == null) {
            println "Creating questionnaire header'${title}'"
            questionnaireHeader = createQuestionnaireHeader(title)
        }

        def questionnaireVersion = Questionnaire.findByNameAndRevision(title, revision)

        if (questionnaireVersion == null) {

            println "Adding new questionnaire version '${title}'"

            JSONElement json = readQuestionnaireJson(jsonFileName, title, questionnaireHeader)

            QuestionnaireEditorCommand command = new QuestionnaireEditorCommand()
            command.cloneScheduleData(json.standardSchedule)

            // TODO: Maps does not bind well before Grails 2.3 and binding fails in integration tests
            command.questionnaireHeader = questionnaireHeader
            command.nodes = json.nodes
            command.connections = json.connections
            command.title = json.title

            questionnaireEditorService.createOrUpdateQuestionnaire(command, creator)
            questionnaireHeaderService.publishDraft(questionnaireHeader, creator)
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
        json['questionnaireHeader'] = header
        json
    }
}
