package org.opentele.server

import org.opentele.server.model.Clinician
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.questionnaire.EditorQuestionnaireBuilder

class QuestionnaireEditorService {
    def questionnaireHeaderService
    def questionnaireNodeService

    def createOrUpdateQuestionnaire(Map json, Clinician creator) {
        QuestionnaireHeader questionnaireHeader = questionnaireHeaderService.getOrCreateQuestionnaireDraft(json.questionnaireHeaderId, creator)
        def questionnaire = questionnaireHeader.draftQuestionnaire
        if(questionnaire.nodes) {
            questionnaireNodeService.deleteQuestionnaireNodes(questionnaireHeader.draftQuestionnaire)
        }

        new EditorQuestionnaireBuilder().buildQuestionnaire(json, questionnaire)
        questionnaire.save(failOnError: true)

        questionnaire
    }
}



