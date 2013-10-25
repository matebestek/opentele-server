package org.opentele.server

import grails.converters.JSON
import grails.plugin.spock.IntegrationSpec
import org.opentele.server.model.Clinician
import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.model.questionnaire.QuestionnaireHeader

class QuestionnaireEditorServiceIntegrationSpec extends IntegrationSpec {
    def questionnaireEditorService

    def 'can create questionnaire from JSON file'() {
        setup:
        def title = 'Test-KOL-spørgetræ (manuel)'
        QuestionnaireHeader header = new QuestionnaireHeader(name: title)
        header.questionnaires = []
        header.save(failOnError: true)
        def jsonFile = new File('grails-app/conf/resources/questionnaires/RH_kol-spoergetrae_manuel.json').text
        def json = JSON.parse(jsonFile)
        json['questionnaireHeaderId'] = header.id
        //json['title'] = title
        Clinician creator = null

        when:
        questionnaireEditorService.createOrUpdateQuestionnaire(json, creator)

        then:
        def savedHeader = QuestionnaireHeader.findByName(title)
        savedHeader.questionnaires.size() == 1
    }
}
