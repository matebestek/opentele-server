package org.opentele.server.questionnaire

import org.opentele.server.model.questionnaire.QuestionnaireGroup
import org.opentele.server.model.questionnaire.QuestionnaireGroup2QuestionnaireHeader
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.model.questionnaire.StandardSchedule
import org.opentele.server.util.ScheduleViewModel

class QuestionnaireGroupService {
    def i18nService

    boolean createOrUpdate(QuestionnaireGroup2QuestionnaireHeader questionnaireGroup2QuestionnaireHeader, ScheduleViewModel viewModel) {
        if (viewModel.type) {
            if (!questionnaireGroup2QuestionnaireHeader.standardSchedule) {
                questionnaireGroup2QuestionnaireHeader.standardSchedule = new StandardSchedule()
            }
            viewModel.updateSchedule(questionnaireGroup2QuestionnaireHeader.standardSchedule)
        } else {
            questionnaireGroup2QuestionnaireHeader.standardSchedule = null
        }

        QuestionnaireGroup questionnaireGroup = QuestionnaireGroup.get(viewModel.questionnaireGroupId)
        questionnaireGroup2QuestionnaireHeader.questionnaireGroup = questionnaireGroup

        questionnaireGroup.addToQuestionnaireGroup2Header(questionnaireGroup2QuestionnaireHeader)
        questionnaireGroup.save()

        if(questionnaireGroup2QuestionnaireHeader.questionnaireHeader?.id != viewModel.selectedQuestionnaire.id) {
            def newQuestionnaireHeader = QuestionnaireHeader.get(viewModel.selectedQuestionnaire.id)
            questionnaireGroup2QuestionnaireHeader.questionnaireHeader = newQuestionnaireHeader
        }

        return !questionnaireGroup2QuestionnaireHeader.hasErrors() || questionnaireGroup2QuestionnaireHeader.save(failOnError: true)
    }


    Map viewModelForCreateAndEdit(QuestionnaireGroup2QuestionnaireHeader questionnaireGroup2Header, validationErrors = []) {
        def selectableQuestionnaireHeaders = getUnusedQuestionnaireHeadersForQuestionnaireGroup(questionnaireGroup2Header.questionnaireGroup)
        def selectedQuestionnaireHeaderId = null

        if (questionnaireGroup2Header.questionnaireHeader) {
            QuestionnaireHeader currentQuestionnaireHeader = questionnaireGroup2Header.questionnaireHeader
            selectableQuestionnaireHeaders.add(currentQuestionnaireHeader)
            selectableQuestionnaireHeaders.sort { it.toString() }
            selectedQuestionnaireHeaderId = currentQuestionnaireHeader.id
        }

        StandardSchedule schedule = questionnaireGroup2Header.schedule

        return [
                id: questionnaireGroup2Header.id != null ? questionnaireGroup2Header.id : '""',
                version: questionnaireGroup2Header.version != null ? questionnaireGroup2Header.version : '""',
                questionnaireGroupId: questionnaireGroup2Header.questionnaireGroup?.id ?: '',
                scheduleType: questionnaireGroup2Header.standardSchedule?.type?.name() ?: "",
                timesOfDay: schedule?.timesOfDay?.collect({ [hour: it.hour, minute: it.minute] }) ?: [[hour: 0, minute: 0]],
                weekdays: (schedule?.weekdays*.name() ?: []),
                daysInMonth: schedule?.daysInMonth*.toString() ,
                intervalInDays: schedule?.dayInterval ?: 0,
                startingDate: (schedule?.startingDate?.toDate() ?: new Date()).format("dd-MM-yyyy"),
                specificDate: schedule?.specificDate?.toDate()?.format("dd-MM-yyyy"),
                reminderStartMinutes: schedule?.reminderStartMinutes ?: 30,
                selectableQuestionnaires: buildQuestionnaireHeaderMap(selectableQuestionnaireHeaders),
                selectedQuestionnaireId: selectedQuestionnaireHeaderId,
                validationErrors: translateValidationErrors(validationErrors)
        ]
    }

    private buildQuestionnaireHeaderMap(List<QuestionnaireHeader> selectableQuestionnaireHeaders) {
        selectableQuestionnaireHeaders.collect { sqh ->
            [
                    id: sqh.id,
                    name: sqh.name
            ]
        }
    }

    private List<QuestionnaireHeader> getUnusedQuestionnaireHeadersForQuestionnaireGroup(QuestionnaireGroup questionnaireGroup) {
        def usedQuestionnaireHeaders = (questionnaireGroup?.questionnaireGroup2Header*.questionnaireHeader*.id ?: []) as List

        def list = QuestionnaireHeader.list([sort: 'name']).findAll { !(it.id in usedQuestionnaireHeaders) }

        return list
    }

    private translateValidationErrors(validationErrors) {
        validationErrors.collect { error ->
            [field: error.field, message: i18nService.message(code: error.message)]
        }
    }

}
