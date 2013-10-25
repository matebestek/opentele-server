package org.opentele.server

import org.opentele.server.model.QuestionnaireSchedule
import org.opentele.server.model.QuestionnaireScheduleCommand

class QuestionnaireScheduleService {
    void update(QuestionnaireScheduleCommand command) {
        if (command.validate()) {
            def questionnaireSchedule = command.questionnaireSchedule
            questionnaireSchedule.questionnaireHeader = command.selectedQuestionnaireHeader

            command.bindScheduleData(questionnaireSchedule)
            questionnaireSchedule.save(failOnError: true)
        }

    }

    void save(QuestionnaireScheduleCommand command) {
        if (command.validate()) {
            def questionnaireSchedule = new QuestionnaireSchedule()
            command.bindScheduleData(questionnaireSchedule)
            questionnaireSchedule.monitoringPlan = command.monitoringPlan
            questionnaireSchedule.questionnaireHeader = command.selectedQuestionnaireHeader
            questionnaireSchedule.save(failOnError: true)
        }
    }
}
