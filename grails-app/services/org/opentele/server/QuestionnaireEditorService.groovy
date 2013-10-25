package org.opentele.server

import org.opentele.server.model.Clinician
import org.opentele.server.model.questionnaire.QuestionnaireEditorCommand
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.questionnaire.EditorQuestionnaireBuilder

class QuestionnaireEditorService {
    def questionnaireHeaderService
    def questionnaireNodeService

    void createOrUpdateQuestionnaire(QuestionnaireEditorCommand command, Clinician creator) {
        QuestionnaireHeader questionnaireHeader = questionnaireHeaderService.getOrCreateQuestionnaireDraft(command.questionnaireHeader, creator)

        def questionnaire = questionnaireHeader.draftQuestionnaire
        if(questionnaire.nodes) {
            questionnaireNodeService.deleteQuestionnaireNodes(questionnaireHeader.draftQuestionnaire)
        }

        new EditorQuestionnaireBuilder().buildQuestionnaire(command, questionnaire)
        questionnaire.save(failOnError: true)
    }
}



