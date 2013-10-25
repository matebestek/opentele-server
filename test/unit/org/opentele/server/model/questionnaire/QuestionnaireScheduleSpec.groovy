package org.opentele.server.model.questionnaire
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.opentele.server.model.MonitoringPlan
import org.opentele.server.model.QuestionnaireSchedule
import org.opentele.server.model.Schedule
import org.opentele.server.model.types.Month
import org.opentele.server.model.types.Weekday
import spock.lang.Specification
import spock.lang.Unroll

@TestMixin(GrailsUnitTestMixin)
public class QuestionnaireScheduleSpec extends Specification {
    def setup() {
        mockForConstraintsTests(QuestionnaireSchedule)
    }

    def 'behaves well when initialized'() {
        when:
        def today = new Date().clearTime()

        def schedule = new QuestionnaireSchedule()

        then:
        schedule.type == Schedule.ScheduleType.UNSCHEDULED
        schedule.timesOfDay == []
        schedule.daysInMonth == []
        schedule.weekdays == []
        schedule.startingDate == today
        schedule.dayInterval == 2
    }

    Date createDate(int day, int month, int year) {
        def date = new Date().clearTime()
        date[Calendar.DATE] = day
        date[Calendar.MONTH] = month
        date[Calendar.YEAR] = year
        return date
    }

    def 'accepts valid hours'() {
        expect:
        assertValidHours([new Schedule.TimeOfDay(hour: 8)])
        assertValidHours([new Schedule.TimeOfDay()]) // 00:00
        assertValidHours([new Schedule.TimeOfDay(hour: 23, minute: 59)])
        assertValidHours([new Schedule.TimeOfDay(hour: 8), new Schedule.TimeOfDay(hour: 16), new Schedule.TimeOfDay(hour: 12)])
    }

    def 'rejects invalid hours'() {
        expect:
        assertInvalidHours([new Schedule.TimeOfDay(hour: 24, minute: 1), new Schedule.TimeOfDay(hour: 8)])
        assertInvalidHours([new Schedule.TimeOfDay(hour: -1, minute: 1)])
        assertInvalidHours([new Schedule.TimeOfDay(hour: 7, minute: 60)])
        assertInvalidHours([new Schedule.TimeOfDay(hour: 7, minute: -1)])
    }

    def 'stores and reads times of day'() {
        when:
        def schedule = new QuestionnaireSchedule()
        schedule.timesOfDay = [new Schedule.TimeOfDay(hour: 8), new Schedule.TimeOfDay(hour: 10, minute: 30)]

        def timesOfDay = schedule.timesOfDay

        then:
        timesOfDay == [new Schedule.TimeOfDay(hour: 8), new Schedule.TimeOfDay(hour: 10, minute: 30)]
    }

    def 'accepts days in month'() {
        when:
        def schedule = new QuestionnaireSchedule()
        schedule.daysInMonth = [12, 22, 1, 3]

        then:
        schedule.daysInMonth == [12, 22, 1, 3]
    }

    def 'rejects invalid days in month'() {
        when:
        def schedule = new QuestionnaireSchedule(monitoringPlan: new MonitoringPlan())
        schedule.type = Schedule.ScheduleType.MONTHLY
        schedule.daysInMonth = [-1, 2, 100]
        schedule.validate()

        then:
        schedule.errors['internalDaysInMonth']
    }

    def 'accepts weekdays'() {
        when:
        def schedule = new QuestionnaireSchedule()
        schedule.weekdays = [Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY]

        then:
        schedule.weekdays == [Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY]
    }

    def 'rejects zero days in interval'() {
        when:
        def schedule = new QuestionnaireSchedule()
        schedule.dayInterval = 0
        schedule.validate()

        then:
        schedule.errors['dayInterval']
    }

    def 'gives no deadlines for unscheduled schedules'() {
        when:
        def schedule = new QuestionnaireSchedule()
        schedule.type = Schedule.ScheduleType.UNSCHEDULED
        Calendar calendar = calendarFromString('2013-06-07 10:46:12')

        then:
        schedule.getLatestDeadlineBefore(calendar) == null
    }

    @Unroll
    def 'gives correct past deadlines for "specific date" schedule'() {
        given: "A schedule that should fire on 2013-06-07 at 08:00 and 10:30"
        def schedule = scheduleStartingAt(startingDate)
        schedule.type = Schedule.ScheduleType.SPECIFIC_DATE
        schedule.specificDate = createDate(7, Month.JUNE.asCalendarMonth(), 2013)

        when: "The latest deadline is requested"
        def result = schedule.getLatestDeadlineBefore(calendarFromString(endTime))?.time

        then: "The correct date and time should be returned"
        result == dateFromString(deadlineDate)

        where:
        startingDate          | endTime               | deadlineDate
        '2013-06-01 00:00:00' | '2013-06-06 10:46:12' | null
        '2013-06-01 00:00:00' | '2013-06-07 07:46:12' | null
        '2013-06-01 00:00:00' | '2013-06-07 08:46:12' | '2013-06-07 08:00:00'
        '2013-06-01 00:00:00' | '2013-06-07 10:30:00' | '2013-06-07 08:00:00'
        '2013-06-01 00:00:00' | '2013-06-07 10:46:12' | '2013-06-07 10:30:00'
        '2013-06-10 00:00:00' | '2013-06-08 08:46:12' | null
    }

    @Unroll
    def 'gives correct future deadlines for "specific date" schedule'() {
        given:
        def schedule = scheduleStartingAt(startingDate)
        schedule.type = Schedule.ScheduleType.SPECIFIC_DATE
        schedule.specificDate = createDate(7, Month.JUNE.asCalendarMonth(), 2013)

        when:
        def result = schedule.getNextDeadlineAfter(calendarFromString(fromTime))?.time

        then:
        result == dateFromString(deadlineDate)

        where:
        startingDate          | fromTime              | deadlineDate
        '2013-06-01 00:00:00' | '2013-06-06 10:46:12' | '2013-06-07 08:00:00'
        '2013-06-01 00:00:00' | '2013-06-07 14:00:00' | null
    }

    @Unroll
    def 'gives correct past deadlines for monthly schedules'() {
        given:
        def schedule = scheduleStartingAt(startingDate)
        schedule.type = Schedule.ScheduleType.MONTHLY
        schedule.daysInMonth = [3, 5, 19]

        when:
        def result = schedule.getLatestDeadlineBefore(calendarFromString(endTime))?.time

        then:
        result == dateFromString(deadlineDate)

        where:
        startingDate          | endTime               | deadlineDate
        '2013-06-01 00:00:00' | '2013-06-07 10:46:12' | '2013-06-05 10:30:00'
        '2013-06-08 00:00:00' | '2013-06-07 10:46:12' | null
    }

    @Unroll
    def 'gives correct future deadlines for monthly schedules'() {
        given:
        def schedule = scheduleStartingAt(startingDate)
        schedule.type = Schedule.ScheduleType.MONTHLY
        schedule.daysInMonth = [3, 5, 19]

        when:
        def result = schedule.getNextDeadlineAfter(calendarFromString(fromTime))?.time

        then:
        result == dateFromString(deadlineDate)

        where:
        startingDate          | fromTime              | deadlineDate
        '2013-06-01 00:00:00' | '2013-05-15 10:46:12' | '2013-06-03 08:00:00'
        '2013-06-01 00:00:00' | '2013-06-03 08:00:00' | '2013-06-03 10:30:00'
        '2013-06-01 00:00:00' | '2013-06-03 08:01:00' | '2013-06-03 10:30:00'
        '2013-06-01 00:00:00' | '2013-06-07 10:46:12' | '2013-06-19 08:00:00'
    }

    @Unroll
    def 'gives correct past deadlines for nth-day schedules'() {
        given:
        def schedule = scheduleStartingAt(startingDate)
        schedule.type = Schedule.ScheduleType.EVERY_NTH_DAY
        schedule.startingDate = createDate(fromDay, fromMonth.asCalendarMonth(), fromYear)
        schedule.dayInterval = dayInterval

        when:
        def result = schedule.getLatestDeadlineBefore(calendarFromString(endTime))?.time

        then:
        result == dateFromString(deadlineDate)

        where:
        startingDate          | endTime               | dayInterval | fromYear | fromMonth  | fromDay | deadlineDate
        '2013-06-01 00:00:00' | '2013-06-07 10:46:12' | 3           | 2013     | Month.JUNE | 1       | '2013-06-07 10:30:00'
        '2013-06-01 00:00:00' | '2013-06-03 10:46:12' | 3           | 2013     | Month.MAY  | 30      | '2013-06-02 10:30:00'
    }

    @Unroll
    def 'gives correct future deadlines for nth-day schedules'() {
        given:
        def schedule = scheduleStartingAt(startingDate)
        schedule.type = Schedule.ScheduleType.EVERY_NTH_DAY
        schedule.startingDate = createDate(fromDay, fromMonth.asCalendarMonth(), fromYear)
        schedule.dayInterval = dayInterval

        when:
        def result = schedule.getNextDeadlineAfter(calendarFromString(fromTime))?.time

        then:
        result == dateFromString(deadlineDate)

        where:
        startingDate          | fromTime              | dayInterval | fromYear | fromMonth  | fromDay | deadlineDate
        '2013-06-01 00:00:00' | '2013-06-07 10:29:00' | 3           | 2013     | Month.JUNE | 1       | '2013-06-07 10:30:00'
        '2013-06-01 00:00:00' | '2013-06-07 10:30:00' | 3           | 2013     | Month.JUNE | 1       | '2013-06-10 08:00:00'
        '2013-06-01 00:00:00' | '2013-06-03 10:46:12' | 3           | 2013     | Month.MAY  | 30      | '2013-06-05 08:00:00'
    }

    @Unroll
    def 'gives correct past deadlines for weekday schedules'() {
        given:
        def schedule = scheduleStartingAt(startingDate)
        schedule.type = Schedule.ScheduleType.WEEKDAYS
        schedule.weekdays = [Weekday.MONDAY, Weekday.FRIDAY]

        when:
        def result = schedule.getLatestDeadlineBefore(calendarFromString(endTime))?.time

        then:
        result == dateFromString(deadlineDate)

        where:
        startingDate          | endTime               | deadlineDate
        '2013-06-01 00:00:00' | '2013-06-01 07:46:12' | null
        '2013-06-01 00:00:00' | '2013-06-07 10:46:12' | '2013-06-07 10:30:00'
        '2013-06-01 00:00:00' | '2013-06-11 10:46:12' | '2013-06-10 10:30:00'
    }

    @Unroll
    def 'gives correct future deadlines for weekday schedules'() {
        given:
        def schedule = scheduleStartingAt(startingDate)
        schedule.type = Schedule.ScheduleType.WEEKDAYS
        schedule.weekdays = [Weekday.MONDAY, Weekday.FRIDAY]

        when:
        def result = schedule.getNextDeadlineAfter(calendarFromString(fromTime)).time

        then:
        result == dateFromString(deadlineDate)

        where:
        startingDate          | fromTime              | deadlineDate
        '2013-06-01 00:00:00' | '2013-06-07 10:29:12' | '2013-06-07 10:30:00'
        '2013-06-01 00:00:00' | '2013-06-07 10:46:12' | '2013-06-10 08:00:00'
    }


    @Unroll
    def 'gives correct past deadlines for weekday once schedules'() {
        given:
        def schedule = scheduleStartingAt(startingDate)
        schedule.type = Schedule.ScheduleType.WEEKDAYS_ONCE
        schedule.blueAlarmTime = new Schedule.TimeOfDay(hour: 10, minute: 0)
        schedule.weekdaysIntroPeriod = [Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY]
        schedule.weekdaysSecondPeriod = [Weekday.TUESDAY]
        schedule.introPeriodWeeks = 2

        when:
        def result = schedule.getLatestDeadlineBefore(calendarFromString(endTime))?.time

        then:
        result == dateFromString(deadlineDate)

        where:
        startingDate          | endTime               | deadlineDate
        '2013-06-01 00:00:00' | '2013-06-01 07:46:12' | null
        '2013-06-01 00:00:00' | '2013-06-07 10:46:12' | '2013-06-07 10:00:00'
        '2013-06-01 00:00:00' | '2013-06-11 10:46:12' | '2013-06-11 10:00:00'
        '2013-06-01 00:00:00' | '2013-06-20 10:46:12' | '2013-06-18 10:00:00'
    }

    @Unroll
    def 'gives correct future deadlines for weekday once schedules'() {
        given:
        def schedule = scheduleStartingAt(startingDate)
        schedule.type = Schedule.ScheduleType.WEEKDAYS_ONCE
        schedule.blueAlarmTime = new Schedule.TimeOfDay(hour: 10, minute: 0)
        schedule.weekdaysIntroPeriod = [Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY]
        schedule.weekdaysSecondPeriod = [Weekday.TUESDAY]
        schedule.introPeriodWeeks = 2

        when:
        def result = schedule.getNextDeadlineAfter(calendarFromString(fromTime)).time

        then:
        result == dateFromString(deadlineDate)

        where:
        startingDate          | fromTime              | deadlineDate
        '2013-06-01 00:00:00' | '2013-06-01 10:29:12' | '2013-06-03 10:00:00'
        '2013-06-01 00:00:00' | '2013-06-03 10:29:12' | '2013-06-05 10:00:00'
        '2013-06-01 00:00:00' | '2013-06-07 10:46:12' | '2013-06-11 10:00:00'
        '2013-06-01 00:00:00' | '2013-06-11 07:46:12' | '2013-06-11 10:00:00'
        '2013-06-01 00:00:00' | '2013-06-11 18:46:12' | '2013-06-18 10:00:00'
    }

    private QuestionnaireSchedule scheduleStartingAt(String startingDate) {
        def schedule = new QuestionnaireSchedule()
        schedule.monitoringPlan = new MonitoringPlan(startDate: calendarFromString(startingDate).time)
        schedule.timesOfDay = [new Schedule.TimeOfDay(hour: 8), new Schedule.TimeOfDay(hour: 10, minute: 30)]
        schedule
    }

    private Calendar calendarFromString(String dateString) {
        if (dateString == null) {
            return null
        }
        def cal = Calendar.getInstance()
        cal.setTime(dateFromString(dateString))

        cal
    }

    private Date dateFromString(String dateString) {
        return dateString ? Date.parse("yyyy-MM-dd HH:mm:ss", dateString) : null
    }

    private boolean assertValidHours(List<Schedule.TimeOfDay> timesOfDay) {
        def schedule = new QuestionnaireSchedule(questionnaireHeader: new QuestionnaireHeader(), monitoringPlan: new MonitoringPlan(), internalDaysInMonth: "1")
        schedule.type = Schedule.ScheduleType.MONTHLY
        schedule.timesOfDay = timesOfDay
        schedule.validate()
        assert !schedule.errors.hasErrors()
        true
    }

    private boolean assertInvalidHours(List<Schedule.TimeOfDay> timesOfDay) {
        def schedule = new QuestionnaireSchedule(questionnaireHeader: new QuestionnaireHeader(), monitoringPlan: new MonitoringPlan())
        schedule.type = Schedule.ScheduleType.MONTHLY
        schedule.timesOfDay = timesOfDay
        schedule.validate()
        assert schedule.errors.hasErrors()
        true
    }
}
