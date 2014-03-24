package org.opentele.server.questionnaire.scheduleiterators

import org.opentele.server.model.Schedule
import spock.lang.Specification

class SpecificDateScheduleIteratorSpec extends Specification {
    def 'contains nothing if start date lies after the specific date'() {
        when:
        Date startDate = date('2013-08-01')
        Date specificDate = date('2013-07-30')
        List<Schedule.TimeOfDay> timesOfDay = [new Schedule.TimeOfDay(hour: 8), new Schedule.TimeOfDay(hour: 10, minute: 30)]
        SpecificDateScheduleIterator iterator = new SpecificDateScheduleIterator(startDate, specificDate, timesOfDay)

        then:
        !iterator.hasNext()
        startDate == date('2013-08-01') // Should not have been messed with
        specificDate == date('2013-07-30') // Should not have been messed with
    }

    def 'contains nothing if no times of day specified'() {
        when:
        Date startDate = date('2013-08-01')
        Date specificDate = date('2013-08-02')
        List<Schedule.TimeOfDay> timesOfDay = []
        SpecificDateScheduleIterator iterator = new SpecificDateScheduleIterator(startDate, specificDate, timesOfDay)

        then:
        !iterator.hasNext()
        startDate == date('2013-08-01') // Should not have been messed with
        specificDate == date('2013-08-02') // Should not have been messed with
    }

    def 'contains times of day on specific date'() {
        when:
        Date startDate = date('2013-08-01')
        Date specificDate = date('2013-08-02')
        List<Schedule.TimeOfDay> timesOfDay = [new Schedule.TimeOfDay(hour: 8), new Schedule.TimeOfDay(hour: 10, minute: 30)]
        SpecificDateScheduleIterator iterator = new SpecificDateScheduleIterator(startDate, specificDate, timesOfDay)

        then:
        iterator.hasNext()
        iterator.next() == time('2013-08-02 08:00:00')

        iterator.hasNext()
        iterator.next() == time('2013-08-02 10:30:00')

        !iterator.hasNext()

        startDate == date('2013-08-01') // Should not have been messed with
        specificDate == date('2013-08-02') // Should not have been messed with
    }

    def 'gives no dates if specific date is before start date'() {
        when:
        Date startDate = date('2013-08-03')
        Date specificDate = date('2013-08-02')
        List<Schedule.TimeOfDay> timesOfDay = [new Schedule.TimeOfDay(hour: 8), new Schedule.TimeOfDay(hour: 10, minute: 30)]
        SpecificDateScheduleIterator iterator = new SpecificDateScheduleIterator(startDate, specificDate, timesOfDay)

        then:
        !iterator.hasNext()
        startDate == date('2013-08-03') // Should not have been messed with
        specificDate == date('2013-08-02') // Should not have been messed with
    }

    private Date date(String asString) {
        Date.parse('yyyy-MM-dd', asString)
    }

    private Date time(String asString) {
        Date.parse('yyyy-MM-dd HH:mm:ss', asString)
    }
}
