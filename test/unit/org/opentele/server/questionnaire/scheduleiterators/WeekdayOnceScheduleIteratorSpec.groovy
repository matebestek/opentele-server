package org.opentele.server.questionnaire.scheduleiterators

import org.opentele.server.model.Schedule
import org.opentele.server.model.Schedule.TimeOfDay
import org.opentele.server.model.types.Weekday
import spock.lang.Specification

class WeekdayOnceScheduleIteratorSpec extends Specification {
    def 'contains nothing if no weekdays are specified in the intro period'() {
        when:
        Date startDate = Date.parse('yyyy-MM-dd', '2013-08-01') // Starts on a Thursday
        List<Weekday> weekdaysIntroPeriod = []
        List<Weekday> weekdaysSecondPeriod = [Weekday.MONDAY]
        Schedule.TimeOfDay blueAlarmTime = new TimeOfDay(hour: 23, minute: 59)
        WeekdayOnceScheduleIterator iterator = new WeekdayOnceScheduleIterator(startDate, 2, weekdaysIntroPeriod, weekdaysSecondPeriod, blueAlarmTime)

        then:
        !iterator.hasNext()
    }

    def 'contains nothing if no weekdays are specified in the second period'() {
        when:
        Date startDate = Date.parse('yyyy-MM-dd', '2013-08-01') // Starts on a Thursday
        List<Weekday> weekdaysIntroPeriod = [Weekday.MONDAY, Weekday.WEDNESDAY]
        List<Weekday> weekdaysSecondPeriod = []
        Schedule.TimeOfDay blueAlarmTime = new TimeOfDay(hour: 23, minute: 59)
        WeekdayOnceScheduleIterator iterator = new WeekdayOnceScheduleIterator(startDate, 2, weekdaysIntroPeriod, weekdaysSecondPeriod, blueAlarmTime)

        then:
        !iterator.hasNext()
    }

    def 'gives an infinite list of weekdays when given valid input'() {
        when:
        Date startDate = Date.parse('yyyy-MM-dd', '2013-08-01') // Starts on a Thursday
        List<Weekday> weekdaysIntroPeriod = [Weekday.TUESDAY, Weekday.FRIDAY, Weekday.SUNDAY]
        List<Weekday> weekdaysSecondPeriod = [Weekday.MONDAY]
        Schedule.TimeOfDay blueAlarmTime = new TimeOfDay(hour: 23, minute: 59)
        WeekdayOnceScheduleIterator iterator = new WeekdayOnceScheduleIterator(startDate, 3, weekdaysIntroPeriod, weekdaysSecondPeriod, blueAlarmTime)

        then:
        iterator.hasNext()
        iterator.next() == asDate('2013-08-02 23:59:00') // Friday

        iterator.hasNext()
        iterator.next() == asDate('2013-08-04 23:59:00') // Sunday - end of first week

        iterator.hasNext()
        iterator.next() == asDate('2013-08-06 23:59:00') // Tuesday

        iterator.hasNext()
        iterator.next() == asDate('2013-08-09 23:59:00') // Friday

        iterator.hasNext()
        iterator.next() == asDate('2013-08-11 23:59:00') // Sunday - end of second week

        iterator.hasNext()
        iterator.next() == asDate('2013-08-13 23:59:00') // Tuesday

        iterator.hasNext()
        iterator.next() == asDate('2013-08-16 23:59:00') // Friday

        iterator.hasNext()
        iterator.next() == asDate('2013-08-18 23:59:00') // Sunday - end of third week

        iterator.hasNext()
        iterator.next() == asDate('2013-08-19 23:59:00') // Change to second period - Monday

        iterator.hasNext()
        iterator.next() == asDate('2013-08-26 23:59:00') // Monday

        iterator.hasNext()
        iterator.next() == asDate('2013-09-02 23:59:00') // Monday

        iterator.hasNext()
        iterator.next() == asDate('2013-09-09 23:59:00') // Monday

        iterator.hasNext()
        iterator.next() == asDate('2013-09-16 23:59:00') // Monday
        // Etc.
    }
    def 'gives an infinite list of weekdays when given valid input and the first week does not produce output'() {
        when:
        Date startDate = Date.parse('yyyy-MM-dd', '2013-06-01') // Starts on a Thursday
        List<Weekday> weekdaysIntroPeriod = [Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY]
        List<Weekday> weekdaysSecondPeriod = [Weekday.TUESDAY]
        Schedule.TimeOfDay blueAlarmTime = new TimeOfDay(hour: 23, minute: 59)
        WeekdayOnceScheduleIterator iterator = new WeekdayOnceScheduleIterator(startDate, 2, weekdaysIntroPeriod, weekdaysSecondPeriod, blueAlarmTime)

        then:
        iterator.hasNext()
        iterator.next() == asDate('2013-06-03 23:59:00')  // Second week Monday

        iterator.hasNext()
        iterator.next() == asDate('2013-06-05 23:59:00')  // Second week Wednesday

        iterator.hasNext()
        iterator.next() == asDate('2013-06-07 23:59:00')  // Second weekd Friday

        iterator.hasNext()
        iterator.next() == asDate('2013-06-11 23:59:00')  // Third week Tuesday

        iterator.hasNext()
        iterator.next() == asDate('2013-06-18 23:59:00')  // Fourth week Tuesday

        // Etc.
    }

    private Date asDate(String asString) {
        Date.parse('yyyy-MM-dd HH:mm:ss', asString)
    }
}
