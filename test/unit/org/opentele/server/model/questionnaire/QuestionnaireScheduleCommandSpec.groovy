package org.opentele.server.model.questionnaire

import grails.test.MockUtils
import grails.test.mixin.support.GrailsUnitTestMixin
import org.opentele.server.model.MonitoringPlan
import org.opentele.server.model.QuestionnaireSchedule
import org.opentele.server.model.QuestionnaireScheduleCommand
import org.opentele.server.model.Schedule
import org.opentele.util.CommandCanValidateSpecification

import static org.opentele.server.model.Schedule.ScheduleType.MONTHLY
import static org.opentele.server.model.types.Weekday.FRIDAY
import static org.opentele.server.model.types.Weekday.MONDAY

@Mixin(GrailsUnitTestMixin)
class QuestionnaireScheduleCommandSpec extends CommandCanValidateSpecification {
    def setup() {
        MockUtils.prepareForConstraintsTests(QuestionnaireScheduleCommand, [:])
    }

    def "when a valid QuestionnaireScheduleCommand is validated, everything runs smooth"() {
        given:
        def command = validCommandObject
        command.questionnaireSchedule = questionnaireSchedule

        expect:
        command.validate()

        where:
        questionnaireSchedule << [null, new QuestionnaireSchedule(version: 0)]
    }

    def "when the questionnaireScheduleCommand.questionnaireSchedule version is ahead of the command version validation fails"() {
        given:
        def command = validCommandObject
        command.questionnaireSchedule.version = 2

        when:
        command.validate()

        then:
        command.hasErrors()
        command.errors['questionnaireSchedule'] == 'optimistic.locking.failure'

    }
    def "when the questionnaireScheduleCommand.monitoringPlan is not set validation fails"() {
        given:
        def command = validCommandObject
        command.monitoringPlan = null

        when:
        command.validate()

        then:
        command.hasErrors()
        command.errors['monitoringPlan'] == 'nullable'

    }

    private getValidCommandObject() {
        new QuestionnaireScheduleCommand(monitoringPlan: new MonitoringPlan(), questionnaireSchedule: new QuestionnaireSchedule(version: 0),
                        version: 0, type: MONTHLY,
                        timesOfDay: [new Schedule.TimeOfDay(hour: 10, minute: 5), new Schedule.TimeOfDay(hour: 12, minute: 0)],
                        daysInMonth: [1, 28], reminderStartMinutes: 60, weekdays: [MONDAY, FRIDAY], dayInterval: 5,
                        startingDate: new Date().clearTime(), specificDate: new Date().clearTime() + 1)
    }
}
