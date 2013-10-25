package org.opentele.server.questionnaire

import grails.validation.Validateable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.opentele.server.model.Schedule
import org.opentele.server.model.ScheduleCommand
import org.opentele.server.model.questionnaire.QuestionnaireGroup
import org.opentele.server.model.questionnaire.QuestionnaireGroup2QuestionnaireHeader
import org.opentele.server.model.questionnaire.QuestionnaireHeader

@Validateable
@ToString(includeFields = true)
@EqualsAndHashCode
class QuestionnaireGroup2QuestionnaireHeaderCommand extends ScheduleCommand {

    Schedule.ScheduleType type
    QuestionnaireGroupService questionnaireGroupService

    QuestionnaireGroup2QuestionnaireHeader questionnaireGroup2QuestionnaireHeader
    Long version

    QuestionnaireGroup questionnaireGroup

    List<QuestionnaireHeader> getSelectableQuestionnaireHeaders() {
        def list = questionnaireGroupService.getUnusedQuestionnaireHeadersForQuestionnaireGroup(questionnaireGroup)
        if (questionnaireGroup2QuestionnaireHeader) {
            list << questionnaireGroup2QuestionnaireHeader.questionnaireHeader
            list = list.sort { it.toString().toLowerCase() }
        }
        return list
    }

    QuestionnaireHeader selectedQuestionnaireHeader

    static constraints = {
        type nullable: true
        questionnaireGroup nullable: false
        questionnaireGroup2QuestionnaireHeader nullable: true, validator: { value, obj ->
            if (value && value.version > obj.version) {
                return "optimistic.locking.failure"
            }
        }
    }
}
