package org.opentele.server.questionnaire

import grails.plugin.spock.IntegrationSpec
import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.model.questionnaire.QuestionnaireNode

class QuestionnaireNodeServiceIntegrationSpec extends IntegrationSpec {
    static transactional = false
    def questionnaireNodeService

    void 'can delete all nodes on a questionnaire'() {
        setup:
        def questionnaire = Questionnaire.findByNameAndRevision('KOL Spørgeskema (skal ikke bruges) (manuel)','1.0')
        def nodeIds = questionnaire.nodes*.id as List

        expect:
        questionnaire

        when:
        Questionnaire.withNewTransaction {
            questionnaireNodeService.deleteQuestionnaireNodes(questionnaire)
        }

        then:
        questionnaire.nodes.empty
        !QuestionnaireNode.findAllByIdInList(nodeIds)
    }
}
