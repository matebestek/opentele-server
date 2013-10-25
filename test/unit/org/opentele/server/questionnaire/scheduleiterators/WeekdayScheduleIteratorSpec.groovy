package org.opentele.server.questionnaire.scheduleiterators

import org.opentele.server.model.Schedule
import org.opentele.server.model.types.Weekday
import spock.lang.Specification

class WeekdayScheduleIteratorSpec extends Specification {
    def 'contains nothing if no weekdays are specified'() {
        when:
        Date startDate = Date.parse('yyyy-MM-dd', '2013-08-01')
        List<Weekday> weekdays = []
        List<Schedule.TimeOfDay> timesOfDay = [new Schedule.TimeOfDay(hour: 8), new Schedule.TimeOfDay(hour: 10, minute: 30)]
        WeekdayScheduleIterator iterator = new WeekdayScheduleIterator(startDate, weekdays, timesOfDay)

        then:
        !iterator.hasNext()
    }

    def 'contains nothing if no times of day are specified'() {
        when:
        Date startDate = Date.parse('yyyy-MM-dd', '2013-08-01')
        List<Weekday> weekdays = [Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY]
        List<Schedule.TimeOfDay> timesOfDay = []
        WeekdayScheduleIterator iterator = new WeekdayScheduleIterator(startDate, weekdays, timesOfDay)

        then:
        !iterator.hasNext()
    }

    def 'gives an infinite list of weekdays when given valid input'() {
        when:
        Date startDate = Date.parse('yyyy-MM-dd', '2013-08-01')
        List<Weekday> weekdays = [Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY]
        List<Schedule.TimeOfDay> timesOfDay = [new Schedule.TimeOfDay(hour: 8), new Schedule.TimeOfDay(hour: 10, minute: 30)]
        WeekdayScheduleIterator iterator = new WeekdayScheduleIterator(startDate, weekdays, timesOfDay)

        then:
        iterator.hasNext()
        iterator.next() == time('2013-08-02 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-08-02 10:30:00')

        iterator.hasNext()
        iterator.next() == time('2013-08-05 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-08-05 10:30:00')

        iterator.hasNext()
        iterator.next() == time('2013-08-07 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-08-07 10:30:00')

        iterator.hasNext()
        iterator.next() == time('2013-08-09 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-08-09 10:30:00')

        iterator.hasNext()
        iterator.next() == time('2013-08-12 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-08-12 10:30:00')

        // Etc.
    }

    private Date time(String asString) {
        Date.parse('yyyy-MM-dd HH:mm:ss', asString)
    }
}
