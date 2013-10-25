package org.opentele.server.model

import spock.lang.Specification
import spock.lang.Unroll



class ScheduleSpec extends Specification {
    @Unroll
    def "test that days in month can deserialize the sting correct"() {
        expect:
        expected == Schedule.DaysInMonth.fromDaysInMonthString(input)

        where:
        input     | expected
        "1,2,3,4" | [1, 2, 3, 4]
        "A,2,4"   | [2, 4]
        " ,21"    | [21]
        " "       | []
        ""        | []
    }

    @Unroll
    def "test that days in month can serialize to a string"() {
        expect:
        expected == Schedule.DaysInMonth.toDaysInMonthString(input)

        where:
        input  | expected
        []     | ""
        [1]    | "1"
        [1, 2] | "1,2"
    }

    @Unroll
    def "test that timeOfDay can deserialize a string correct"() {
        expect:
        expected == Schedule.TimeOfDay.toTimeOfDay(defaults, timeOfDay)

        where:
        timeOfDay | defaults              | expected
        ""        | [:]                   | null
        ""        | [hour: 10, minute: 5] | new Schedule.TimeOfDay(hour: 10, minute: 5)
        "1"       | [:]                   | null
        "1"       | [hour: 10, minute: 5] | new Schedule.TimeOfDay(hour: 10, minute: 5)
        "1:10"    | [:]                   | new Schedule.TimeOfDay(hour: 1, minute: 10)
        "1:10"    | [hour: 10, minute: 5] | new Schedule.TimeOfDay(hour: 1, minute: 10)
    }

    @Unroll
    def "test that timesOfDay can deserialisize a string correct"() {
        expect:
        expected == Schedule.TimeOfDay.toTimesOfDay(input)

        where:
        input        | expected
        ""           | []
        "1"          | []
        "1,10:00"    | [new Schedule.TimeOfDay(hour: 10, minute: 0)]
        "9:00,10:00" | [new Schedule.TimeOfDay(hour: 9, minute: 0), new Schedule.TimeOfDay(hour: 10, minute: 0)]
    }

    @Unroll
    def "test that fromTimesOfDay can serialize to a string"() {
        expect:
        expected == Schedule.TimeOfDay.fromTimesOfDay(input)

        where:
        input                                                                                     | expected
        []                                                                                        | ""
        [new Schedule.TimeOfDay(hour: 10, minute: 0)]                                             | "10:00"
        [new Schedule.TimeOfDay(hour: 9, minute: 9), new Schedule.TimeOfDay(hour: 10, minute: 0)] | "09:09,10:00"
    }
}
