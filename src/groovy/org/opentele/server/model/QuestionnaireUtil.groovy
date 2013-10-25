package org.opentele.server.model

import org.opentele.server.model.questionnaire.BooleanNode
import org.opentele.server.model.questionnaire.ChoiceNode
import org.opentele.server.model.questionnaire.ChoiceValue
import org.opentele.server.model.questionnaire.DelayNode
import org.opentele.server.model.questionnaire.EndNode
import org.opentele.server.model.questionnaire.InputNode
import org.opentele.server.model.questionnaire.MeasurementNode
import org.opentele.server.model.questionnaire.NoteInputNode
import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.model.questionnaire.TextNode

class QuestionnaireUtil {

    def createEndNode(params) {
        store(new EndNode(params))
    }

    MeasurementNode createMeasurementNode(params) {
        MeasurementNode node = new MeasurementNode(params)
        store(node)
    }

    TextNode createTextNode(params) {
        store(new TextNode(params))
    }

    NoteInputNode createNoteNode(params) {
        store(new NoteInputNode(params))
    }

    InputNode createInputNode(params) {
        store(new InputNode(params))
    }

    ChoiceNode createChoiceNode(params) {
        store(new ChoiceNode(params))
    }

    BooleanNode createBooleanNode(params) {
        store(new BooleanNode(params))
    }

    DelayNode createDelayNode(params) {
        store(new DelayNode(params))
    }

    Questionnaire createQuestionnaire(params) {
        QuestionnaireHeader questionnaireHeader = createOrGetQuestionnaireHeader(params)
        params.questionnaireHeader = questionnaireHeader
        Questionnaire questionnaire = store(new Questionnaire(params))
        questionnaireHeader.addToQuestionnaires(questionnaire)
        questionnaireHeader.activeQuestionnaire = questionnaire
        questionnaireHeader.save(failOnError: true, flush: true)
        return questionnaire
    }

    ChoiceValue createChoiceValue(params) {
        store(new ChoiceValue(params))
    }

    private def store(def node) {
        Date now = new Date()
        node.createdDate = now
        node.modifiedDate = now

        node.save(failOnError:true)
        node
    }
    private createOrGetQuestionnaireHeader(params) {
        QuestionnaireHeader.findByName(params.name) ?: new QuestionnaireHeader(params).save(failOnError: true, flush: true)
    }
}
