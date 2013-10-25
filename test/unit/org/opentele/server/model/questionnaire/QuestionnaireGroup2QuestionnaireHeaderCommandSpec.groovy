package org.opentele.server.model.questionnaire
import grails.test.MockUtils
import grails.test.mixin.support.GrailsUnitTestMixin
import org.opentele.server.model.Schedule
import org.opentele.server.questionnaire.QuestionnaireGroup2QuestionnaireHeaderCommand
import org.opentele.util.CommandCanValidateSpecification

import static org.opentele.server.model.Schedule.ScheduleType.MONTHLY
import static org.opentele.server.model.types.Weekday.FRIDAY
import static org.opentele.server.model.types.Weekday.MONDAY

@Mixin(GrailsUnitTestMixin)
class QuestionnaireGroup2QuestionnaireHeaderCommandSpec extends CommandCanValidateSpecification {
    def setup() {
        MockUtils.prepareForConstraintsTests(QuestionnaireGroup2QuestionnaireHeaderCommand, [:])
    }

    def "when a valid QuestionnaireGroup2QuestionnaireHeaderCommand is validated, everything runs smooth"() {
        given:
        def command = validCommandObject
        command.questionnaireGroup2QuestionnaireHeader = questionnaireGroup2QuestionnaireHeader

        expect:
        command.validate()

        where:
        questionnaireGroup2QuestionnaireHeader << [null, new QuestionnaireGroup2QuestionnaireHeader(version: 0)]
    }

    def "when the questionnaireGroup2QuestionnaireHeaderCommand.questionnaireGroup2QuestionnaireHeader version is ahead of the command version validation fails"() {
        given:
        def command = validCommandObject
        command.questionnaireGroup2QuestionnaireHeader.version = 2

        when:
        command.validate()

        then:
        command.hasErrors()
        command.errors['questionnaireGroup2QuestionnaireHeader'] == 'optimistic.locking.failure'

    }

    def "when the questionnaireScheduleCommand.questionnaireGroup is not set validation fails"() {
        given:
        def command = validCommandObject
        command.questionnaireGroup = null

        when:
        command.validate()

        then:
        command.hasErrors()
        command.errors['questionnaireGroup'] == 'nullable'

    }

    private getValidCommandObject() {
        new QuestionnaireGroup2QuestionnaireHeaderCommand(questionnaireGroup: new QuestionnaireGroup(),
                questionnaireGroup2QuestionnaireHeader: new QuestionnaireGroup2QuestionnaireHeader(version: 0),
                version: 0, type: MONTHLY,
                timesOfDay: [new Schedule.TimeOfDay(hour: 10, minute: 5), new Schedule.TimeOfDay(hour: 12, minute: 0)],
                daysInMonth: [1, 28], reminderStartMinutes: 60, weekdays: [MONDAY, FRIDAY], dayInterval: 5,
                startingDate: new Date().clearTime(), specificDate: new Date().clearTime() + 1)
    }
}
