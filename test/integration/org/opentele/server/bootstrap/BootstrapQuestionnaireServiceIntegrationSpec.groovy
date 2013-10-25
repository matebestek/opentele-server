package org.opentele.server.bootstrap

import grails.plugin.spock.IntegrationSpec
import org.opentele.server.model.Clinician
import org.opentele.server.model.User
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire
import org.opentele.server.model.questionnaire.QuestionnaireHeader

class BootstrapQuestionnaireServiceIntegrationSpec extends IntegrationSpec {
    Clinician creator
    def bootstrapQuestionnaireService
    def sessionFactory

    def setup() {
        def user = new User(username: 'bootstrapQuestionnaire', password: 'abcd1234')
        creator = new Clinician(user: user, firstName: 'Bootstrap', lastName: 'Questionnaire')
        [user, creator]*.save(failOnError: true)
    }

    def 'can create and publish a questionnaire from JSON'() {
        setup:
        assert QuestionnaireHeader.findByName('Test-KOL-spørgetræ') == null

        when:
        bootstrapQuestionnaireService.ensureQuestionnaireExists(creator, 'Test-KOL-spørgetræ', 'RH_kol-spoergetrae_manuel.json')

        then:
        def savedHeader = QuestionnaireHeader.findByName('Test-KOL-spørgetræ')
        savedHeader.questionnaires.size() == 1
        savedHeader.activeQuestionnaire.name == 'Test-KOL-spørgetræ'
        PatientQuestionnaire.findByTemplateQuestionnaire(savedHeader.activeQuestionnaire) != null
    }

    def 'does nothing if questionnaire header already exists'() {
        setup:
        def oldVersionNumber = QuestionnaireHeader.findByName('Blodtryk og puls').version

        when:
        bootstrapQuestionnaireService.ensureQuestionnaireExists(creator, 'Blodtryk og puls', 'RH_kol-spoergetrae_manuel.json')

        then:
        QuestionnaireHeader.findByName('Blodtryk og puls').version == oldVersionNumber
    }
}
