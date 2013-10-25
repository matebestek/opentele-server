package org.opentele.server.questionnaire
import grails.plugin.spock.IntegrationSpec
import org.opentele.server.model.Clinician
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire
import org.opentele.server.model.questionnaire.Questionnaire

class QuestionnaireServiceIntegrationSpec extends IntegrationSpec {
    def questionnaireService
    def grailsApplication

    def setup() {
        // Avoid conflicts with objects in session created earlier. E.g. in bootstrap
        grailsApplication.mainContext.sessionFactory.currentSession.clear()
    }

    def "test create patient questionnaire"() {
        setup:
        Clinician helle = Clinician.findByFirstNameAndLastName("Helle", "Andersen")

            // Retrieve q from db
        Questionnaire theq = Questionnaire.findByNameAndRevision("Radioknap test", "0.1")

        when:
        PatientQuestionnaire pq = questionnaireService.createPatientQuestionnaire(helle, theq);

        then:
        pq.templateQuestionnaire.id == theq.id
    }


    def "test remove draft questionnaire"() {
    }
}
