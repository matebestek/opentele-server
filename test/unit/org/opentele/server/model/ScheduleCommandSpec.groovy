package org.opentele.server.model

import grails.test.MockUtils
import grails.test.mixin.support.GrailsUnitTestMixin
import groovy.json.JsonOutput
import org.opentele.util.CommandCanValidateSpecification
import spock.lang.Shared
import spock.lang.Unroll

import static org.opentele.server.model.Schedule.ScheduleType.*
import static org.opentele.server.model.types.Weekday.*

@Mixin(GrailsUnitTestMixin)
class ScheduleCommandSpec extends CommandCanValidateSpecification {
    @Shared
    def date = new Date();

    def setup() {
        MockUtils.prepareForConstraintsTests(ScheduleCommand, [:])
    }

    @Unroll
    def "when ScheduleCommand does not validate, validation errors should be correct"() {
        setup:
        def command = new ScheduleCommand(type: type, daysInMonth: daysInMonth,
                reminderStartMinutes: reminder, weekdays: weekdays, dayInterval: dayInterval,
                startingDate: startingDate, specificDate: specificDate)
        when:
        command.validate()
        println command.errors

        then:
        command.errors.allErrors.size() == 1
        command.errors[field] == expectedError

        where:
        type          | field                  | reminder | weekdays | daysInMonth | dayInterval | startingDate | specificDate | expectedError
        MONTHLY       | 'reminderStartMinutes' | 0        | []       | [1]         | 1           | date         | date         | 'min'
        MONTHLY       | 'daysInMonth'          | 30       | []       | []          | 1           | date         | date         | 'error.atLeastOneDayInMonth'
        WEEKDAYS      | 'reminderStartMinutes' | 0        | [MONDAY] | []          | 1           | date         | date         | 'min'
        WEEKDAYS      | 'weekdays'             | 30       | []       | []          | 1           | date         | date         | 'error.atLeastOneWeekday'
        EVERY_NTH_DAY | 'dayInterval'          | 30       | []       | []          | 0           | date         | date         | 'min'
        EVERY_NTH_DAY | 'reminderStartMinutes' | 0        | []       | []          | 1           | date         | date         | 'min'
        EVERY_NTH_DAY | 'startingDate'         | 30       | []       | []          | 1           | null         | date         | 'nullable'
        SPECIFIC_DATE | 'specificDate'         | 30       | []       | []          | 1           | date         | null         | 'nullable'
        SPECIFIC_DATE | 'reminderStartMinutes' | 0        | []       | []          | 1           | date         | date         | 'min'
    }

    @Unroll
    def "when ScheduleCommand constraints are valid, no validation error should occur"() {
        given:
        def command = new ScheduleCommand(type: type, daysInMonth: [1],
                reminderStartMinutes: 30, weekdays: [MONDAY], dayInterval: 2,
                startingDate: date, specificDate: date, timesOfDay: [new Schedule.TimeOfDay()],
                weekdaysIntroPeriod: [MONDAY], weekdaysSecondPeriod: [MONDAY])

        expect:
        command.validate()

        where:
        type << Schedule.ScheduleType.values()
    }

    @Unroll
    def 'can correctly clone schedule from JSON'() {
        given:
        def commandToClone = [
                type: type, daysInMonth: [1],
                reminderStartMinutes: 30, weekdays: [MONDAY], dayInterval: 2,
                startingDate: jsonDate(date), specificDate: jsonDate(date), timesOfDay: [new Schedule.TimeOfDay()],
                introPeriodWeeks: '4', weekdaysIntroPeriod: [MONDAY], weekdaysSecondPeriod: [MONDAY]
        ]
        def clonedCommand = new ScheduleCommand()
        clonedCommand.cloneScheduleData(commandToClone)

        expect:
        clonedCommand.validate()

        where:
        type << Schedule.ScheduleType.values()

    }

    def "when ScheduleCommand binds its data to a Schedule implementation all information should be moved to the Schedule implementation"() {
        given: "The command properties and a new command"
        def map = [type: MONTHLY, timesOfDay: [new Schedule.TimeOfDay(hour: 10, minute: 5), new Schedule.TimeOfDay(hour: 12, minute: 0)],
                daysInMonth: [1, 28], reminderStartMinutes: 60, weekdays: [MONDAY, FRIDAY], dayInterval: 5,
                startingDate: new Date().clearTime(), specificDate: new Date().clearTime() + 1,
                weekdaysSecondPeriod: [MONDAY], weekdaysIntroPeriod: [TUESDAY], blueAlarmTime: new Schedule.TimeOfDay(hour: 23, minute: 59),
                reminderTime: new Schedule.TimeOfDay(hour: 23, minute: 59), introPeriodWeeks: 2
        ]
        def command = new ScheduleCommand(map)

        and: "a  mocked schedule"
        def schedule = Mock(QuestionnaireSchedule)

        when: "binding data to the schedule"
        command.bindScheduleData(schedule)

        then: "the following setters are called on the schedule"
        1 * schedule.setProperty('type', map.type)
        1 * schedule.setProperty('timesOfDay', map.timesOfDay)
        1 * schedule.setProperty('reminderStartMinutes', map.reminderStartMinutes)
        1 * schedule.setProperty('daysInMonth', map.daysInMonth)
        1 * schedule.setProperty('weekdays', map.weekdays)
        1 * schedule.setProperty('dayInterval', map.dayInterval)
        1 * schedule.setProperty('startingDate', map.startingDate)
        1 * schedule.setProperty('specificDate', map.specificDate)
        1 * schedule.setProperty('blueAlarmTime', map.blueAlarmTime)
        1 * schedule.setProperty('reminderTime', map.reminderTime)
        1 * schedule.setProperty('weekdaysIntroPeriod', map.weekdaysIntroPeriod)
        1 * schedule.setProperty('weekdaysSecondPeriod', map.weekdaysSecondPeriod)
        1 * schedule.setProperty('introPeriodWeeks', map.introPeriodWeeks)

        and: "Nothing else"
        0 * schedule._(* _)
    }

    private jsonDate(Date date) {
        def jsonDateIncludingOuterQuotes = JsonOutput.toJson(date)
        jsonDateIncludingOuterQuotes[1..jsonDateIncludingOuterQuotes.length() - 2]
    }
}

