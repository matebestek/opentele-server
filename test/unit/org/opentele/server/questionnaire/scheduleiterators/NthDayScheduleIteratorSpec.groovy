package org.opentele.server.questionnaire.scheduleiterators

import org.opentele.server.model.Schedule
import spock.lang.Specification

class NthDayScheduleIteratorSpec extends Specification {
    def 'contains nothing if day interval is less than 1'() {
        when:
        Date startDate = Date.parse('yyyy-MM-dd', '2013-08-01')
        Date intervalStartDate = Date.parse('yyyy-MM-dd', '2013-08-01')
        int dayInterval = 0
        List<Schedule.TimeOfDay> timesOfDay = [new Schedule.TimeOfDay(hour: 8), new Schedule.TimeOfDay(hour: 10, minute: 30)]
        NthDayScheduleIterator iterator = new NthDayScheduleIterator(startDate, intervalStartDate, dayInterval, timesOfDay)

        then:
        !iterator.hasNext()
        startDate == Date.parse('yyyy-MM-dd', '2013-08-01') // Should not have been messed with
    }

    def 'contains nothing if no times of day are specified'() {
        when:
        Date startDate = Date.parse('yyyy-MM-dd', '2013-08-01')
        Date intervalStartDate = Date.parse('yyyy-MM-dd', '2013-08-01')
        int dayInterval = 3
        List<Schedule.TimeOfDay> timesOfDay = []
        NthDayScheduleIterator iterator = new NthDayScheduleIterator(startDate, intervalStartDate, dayInterval, timesOfDay)

        then:
        !iterator.hasNext()
        startDate == Date.parse('yyyy-MM-dd', '2013-08-01') // Should not have been messed with
    }

    def 'gives an infinite list of intervals in days when given valid input'() {
        when:
        Date startDate = Date.parse('yyyy-MM-dd', '2013-08-01')
        Date intervalStartDate = Date.parse('yyyy-MM-dd', '2013-08-03')
        int dayInterval = 10
        List<Schedule.TimeOfDay> timesOfDay = [new Schedule.TimeOfDay(hour: 8), new Schedule.TimeOfDay(hour: 10, minute: 30)]
        NthDayScheduleIterator iterator = new NthDayScheduleIterator(startDate, intervalStartDate, dayInterval, timesOfDay)

        then:
        iterator.hasNext()
        iterator.next() == time('2013-08-03 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-08-03 10:30:00')

        iterator.hasNext()
        iterator.next() == time('2013-08-13 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-08-13 10:30:00')

        iterator.hasNext()
        iterator.next() == time('2013-08-23 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-08-23 10:30:00')

        iterator.hasNext()
        iterator.next() == time('2013-09-02 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-09-02 10:30:00')

        iterator.hasNext()
        iterator.next() == time('2013-09-12 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-09-12 10:30:00')

        // Etc.

        startDate == Date.parse('yyyy-MM-dd', '2013-08-01') // Should not have been messed with
    }

    def 'gives no dates before actual start date'() {
        when:
        Date startDate = Date.parse('yyyy-MM-dd', '2013-08-01')
        Date intervalStartDate = Date.parse('yyyy-MM-dd', '2013-07-29')
        int dayInterval = 10
        List<Schedule.TimeOfDay> timesOfDay = [new Schedule.TimeOfDay(hour: 8), new Schedule.TimeOfDay(hour: 10, minute: 30)]
        NthDayScheduleIterator iterator = new NthDayScheduleIterator(startDate, intervalStartDate, dayInterval, timesOfDay)

        then:
        iterator.hasNext()
        iterator.next() == time('2013-08-08 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-08-08 10:30:00')

        iterator.hasNext()
        iterator.next() == time('2013-08-18 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-08-18 10:30:00')

        iterator.hasNext()
        iterator.next() == time('2013-08-28 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-08-28 10:30:00')

        iterator.hasNext()
        iterator.next() == time('2013-09-07 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-09-07 10:30:00')

        iterator.hasNext()
        iterator.next() == time('2013-09-17 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-09-17 10:30:00')

        // Etc.

        startDate == Date.parse('yyyy-MM-dd', '2013-08-01') // Should not have been messed with
    }

    private Date time(String asString) {
        Date.parse('yyyy-MM-dd HH:mm:ss', asString)
    }
}
