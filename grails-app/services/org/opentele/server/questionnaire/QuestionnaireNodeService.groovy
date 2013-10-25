package org.opentele.server.questionnaire
import org.opentele.server.model.questionnaire.EndNode
import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.model.questionnaire.QuestionnaireNode
import org.springframework.transaction.annotation.Transactional

class QuestionnaireNodeService {

    @Transactional()
    def deleteQuestionnaireNodes(Questionnaire questionnaire, boolean flush = false) {
        def questionnaireNodes = QuestionnaireNode.findAllByQuestionnaire(questionnaire)
        def dummyNode = new EndNode(shortText: "Dummy").save(flush: true)
        questionnaireNodes.reverse().each {
            questionnaire.removeFromNodes(it)
            it.questionnaire = null
            it.defaultNext = dummyNode
            if (it.hasProperty('inputNode')) it.inputNode = null
            if (it.hasProperty('alternativeNext')) it.alternativeNext = dummyNode
            if (it.hasProperty('monicaMeasuringTimeInputNode')) it.monicaMeasuringTimeInputNode = null
            if (it.hasProperty('nextFail')) it.nextFail = dummyNode
            it.defaultPreviouses?.clear()
            it.alternativePreviouses?.clear()
            it.nextFails?.clear()
            it.save(flush: true, failOnError: true)
        }
        questionnaire.startNode = null
        questionnaire.save(failOnError: true)
        questionnaireNodes.each {
            it.delete()
        }
        dummyNode.delete(flush: flush)
    }

}
