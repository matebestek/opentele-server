package org.opentele.server.questionnaire.scheduleiterators

import org.opentele.server.model.Schedule
import spock.lang.Specification

class MonthlyScheduleIteratorSpec extends Specification {
    def 'contains nothing if no days in month are specified'() {
        when:
        Date startDate = Date.parse('yyyy-MM-dd', '2013-08-01')
        List<Integer> daysInMonth = []
        List<Schedule.TimeOfDay> timesOfDay = [new Schedule.TimeOfDay(hour: 8), new Schedule.TimeOfDay(hour: 10, minute: 30)]
        MonthlyScheduleIterator iterator = new MonthlyScheduleIterator(startDate, daysInMonth, timesOfDay)

        then:
        !iterator.hasNext()
    }

    def 'contains nothing if no times of day are specified'() {
        when:
        Date startDate = Date.parse('yyyy-MM-dd', '2013-08-01')
        List<Integer> daysInMonth = [1, 15, 30]
        List<Schedule.TimeOfDay> timesOfDay = []
        MonthlyScheduleIterator iterator = new MonthlyScheduleIterator(startDate, daysInMonth, timesOfDay)

        then:
        !iterator.hasNext()
    }

    def 'gives an infinite list of days in month when given valid input'() {
        when:
        Date startDate = Date.parse('yyyy-MM-dd', '2013-08-01')
        List<Integer> daysInMonth = [1, 15, 30]
        List<Schedule.TimeOfDay> timesOfDay = [new Schedule.TimeOfDay(hour: 8), new Schedule.TimeOfDay(hour: 10, minute: 30)]
        MonthlyScheduleIterator iterator = new MonthlyScheduleIterator(startDate, daysInMonth, timesOfDay)

        then:
        iterator.hasNext()
        iterator.next() == time('2013-08-01 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-08-01 10:30:00')

        iterator.hasNext()
        iterator.next() == time('2013-08-15 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-08-15 10:30:00')

        iterator.hasNext()
        iterator.next() == time('2013-08-30 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-08-30 10:30:00')

        iterator.hasNext()
        iterator.next() == time('2013-09-01 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-09-01 10:30:00')

        iterator.hasNext()
        iterator.next() == time('2013-09-15 08:00:00')
        iterator.hasNext()
        iterator.next() == time('2013-09-15 10:30:00')

        // Etc.
    }

    private Date time(String asString) {
        Date.parse('yyyy-MM-dd HH:mm:ss', asString)
    }
}
