package org.opentele.server.model

import org.opentele.server.model.questionnaire.StandardSchedule
import spock.lang.Specification
import spock.lang.Unroll

import static org.opentele.server.model.Schedule.ScheduleType.*

class ScheduleValidatorsSpec extends Specification {
    @Unroll
    def "check timesOfDay validator"() {
        given:
        def schedule = new StandardSchedule(type: scheduleType)

        expect:
        result == ScheduleValidators.timesOfDay(WEEKDAYS, MONTHLY).call(value, schedule)

        where:
        scheduleType | value         | result
        UNSCHEDULED  | null          | true
        MONTHLY      | "10:00"       | true
        MONTHLY      | "10:00,12:00" | true
        MONTHLY      | null          | false
        MONTHLY      | ""            | false
        MONTHLY      | "25:00"       | false
        MONTHLY      | "10:00,25:00" | false
        MONTHLY      | "10:00,A:00"  | false
        MONTHLY      | "KRYF"  | false
    }

    @Unroll
    def "check weekdays validator"() {
        given:
        def schedule = new StandardSchedule(type: scheduleType)

        expect:
        result == ScheduleValidators.weekdays(WEEKDAYS).call(value, schedule)

        where:
        scheduleType | value           | result
        UNSCHEDULED  | null            | true
        WEEKDAYS     | "MONDAY"        | true
        WEEKDAYS     | "MONDAY,FRIDAY" | true
        WEEKDAYS     | null            | false
        WEEKDAYS     | ""              | false
        WEEKDAYS     | "KURT"          | false

    }

    @Unroll
    def "check daysInMonth validator"() {
        given:
        def schedule = new StandardSchedule(type: scheduleType)

        expect:
        result == ScheduleValidators.daysInMonth(MONTHLY).call(value, schedule)

        where:
        scheduleType | value | result
        UNSCHEDULED  | null  | true
        MONTHLY      | "1"   | true
        MONTHLY      | "1,2" | true
        MONTHLY      | null  | false
        MONTHLY      | ""    | false
        MONTHLY      | "A"   | false
    }

    @Unroll
    def "check timeOfDay validator"() {
        given:
        def schedule = new StandardSchedule(type: scheduleType)

        expect:
        result == ScheduleValidators.timeOfDay(WEEKDAYS_ONCE).call(value, schedule)

        where:
        scheduleType  | value         | result
        UNSCHEDULED   | null          | true
        WEEKDAYS_ONCE | "10:00"       | true
        WEEKDAYS_ONCE | null          | false
        WEEKDAYS_ONCE | ""            | false
        WEEKDAYS_ONCE | "25:00"       | false
        WEEKDAYS_ONCE | "10:75"       | false
        WEEKDAYS_ONCE | "KRYF:PLYF"   | false

    }
}
