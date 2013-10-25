package org.opentele.server.questionnaire

import org.opentele.server.model.Clinician
import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.model.questionnaire.QuestionnaireGroup2QuestionnaireHeader
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.springframework.transaction.annotation.Transactional

class QuestionnaireHeaderService {
    def questionnaireService

    List<Questionnaire> findHistoricQuestionnaires(QuestionnaireHeader questionnaireHeaderInstance) {
        def excludedQuestionnaires = [questionnaireHeaderInstance.activeQuestionnaire?.id, questionnaireHeaderInstance.draftQuestionnaire?.id]

        return questionnaireHeaderInstance.questionnaires.findAll {
            !(it.id in excludedQuestionnaires)
        }.sort {it.revision}
    }

    void unpublishActive(QuestionnaireHeader questionnaireHeaderInstance) {
        questionnaireHeaderInstance.activeQuestionnaire = null
    }

    void publishDraft(QuestionnaireHeader questionnaireHeader, Clinician creator) {
        if (questionnaireHeader.draftQuestionnaire) {
            questionnaireService.createPatientQuestionnaire(creator, questionnaireHeader.draftQuestionnaire)
            questionnaireHeader.activeQuestionnaire = questionnaireHeader.draftQuestionnaire
            questionnaireHeader.draftQuestionnaire = null
            questionnaireHeader.save()
        }
    }

    @Transactional
    void deleteDraft(QuestionnaireHeader questionnaireHeader) {
        if (questionnaireHeader.draftQuestionnaire) {
            questionnaireService.deleteQuestionnaire(questionnaireHeader.draftQuestionnaire)
        }
    }

    @Transactional
    void delete(QuestionnaireHeader questionnaireHeader) {
        if (!(questionnaireHeader.activeQuestionnaire || findHistoricQuestionnaires(questionnaireHeader).any())) {
            deleteDraft(questionnaireHeader)

            // TODO: This fails.
            // questionnaireHeader.delete(flush: true)

            // So do this instead:
            // Delete this questionnaire from all questionnaire groups.
            QuestionnaireGroup2QuestionnaireHeader.executeUpdate("delete from QuestionnaireGroup2QuestionnaireHeader qg2qh where questionnaireHeader=?", [questionnaireHeader])

            // Then delete the questionnaire header itself.
            QuestionnaireHeader.executeUpdate("delete from QuestionnaireHeader qh where qh=?",[questionnaireHeader])
        }
    }

    @Transactional
    QuestionnaireHeader getOrCreateQuestionnaireDraft(QuestionnaireHeader questionnaireHeader, Clinician creator) {
        if(questionnaireHeader.draftQuestionnaire) {
            questionnaireHeader
        } else {
            def nextRevision = calculateNextRevision(questionnaireHeader)
            def questionnaire = new Questionnaire(questionnaireHeader: questionnaireHeader, creator: creator, revision: nextRevision, creationDate: new Date())
            questionnaire.save(failOnError: true)
            questionnaireHeader.draftQuestionnaire = questionnaire
            questionnaireHeader.addToQuestionnaires(questionnaire)
            questionnaireHeader.save(failOnError: true)
        }
    }

    String calculateNextRevision(QuestionnaireHeader questionnaireHeader) {
        def latestQuestionnaire = questionnaireHeader.questionnaires.max { it.creationDate }
        def nextRevision = latestQuestionnaire?.revision?.toBigDecimal() ?: 0
        nextRevision = Math.floor(nextRevision) + 1
        nextRevision
    }

}
