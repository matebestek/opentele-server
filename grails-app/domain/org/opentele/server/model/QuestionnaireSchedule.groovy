package org.opentele.server.model

import org.opentele.server.model.Schedule.ScheduleType
import org.opentele.server.model.Schedule.TimeOfDay
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.model.types.Weekday
import org.opentele.server.questionnaire.scheduleiterators.*

import static org.opentele.server.model.Schedule.ScheduleType.*

class QuestionnaireSchedule extends AbstractObject implements Schedule {
    MonitoringPlan monitoringPlan

    QuestionnaireHeader questionnaireHeader
    ScheduleType type = UNSCHEDULED
    String internalTimesOfDay = ''

    // WEEKDAYS
    String internalWeekdays = ''

    // WEEKDAYS_ONCE
    Integer introPeriodWeeks = 4
    String internalReminderTime = ''
    String internalBlueAlarmTime = ''
    String internalWeekdaysIntroPeriod = ''
    String internalWeekdaysSecondPeriod = ''

    // MONTHLY
    String internalDaysInMonth = ''

    // EVERY_NTH_DAY
    Date startingDate = new Date().clearTime() // Only relevant when type is EVERY_NTH_DAY
    Integer dayInterval = 2

    // SPECIFIC_DATE
    Date specificDate = null

    int reminderStartMinutes = 30

    static constraints = {
        internalTimesOfDay nullable: true, validator: ScheduleValidators.timesOfDay(WEEKDAYS, MONTHLY, EVERY_NTH_DAY, SPECIFIC_DATE)
        internalWeekdays nullable: true, validator: ScheduleValidators.weekdays(WEEKDAYS)
        introPeriodWeeks nullable: false, min: 1, max: 8
        internalReminderTime nullable: true, validator: ScheduleValidators.timeOfDay(WEEKDAYS_ONCE)
        internalBlueAlarmTime nullable: true, validator: ScheduleValidators.timeOfDay(WEEKDAYS_ONCE)
        internalWeekdaysIntroPeriod nullable: true, validator: ScheduleValidators.weekdays(WEEKDAYS_ONCE)
        internalWeekdaysSecondPeriod nullable: true, validator: ScheduleValidators.weekdays(WEEKDAYS_ONCE)
        internalDaysInMonth nullable: true, validator: ScheduleValidators.daysInMonth(MONTHLY)
        startingDate nullable: false
        dayInterval nullable: false, min: 1
        specificDate nullable: true
        monitoringPlan nullable: false, unique: "questionnaireHeader"
        questionnaireHeader nullable: false
    }

    static mapping = {
        internalTimesOfDay column: 'TIMES_OF_DAY'
        internalDaysInMonth column: 'DAYS_IN_MONTH'
        internalWeekdays column: 'WEEKDAYS'
        internalWeekdaysIntroPeriod column: 'WEEKDAYS_INTRO_PERIOD'
        internalWeekdaysSecondPeriod column: 'WEEKDAYS_SECOND_PERIOD'
        internalReminderTime column: 'REMINDER_TIME'
        internalBlueAlarmTime column: 'BLUE_ALARM_TIME'
    }

    static transients = ['timesOfDay', 'weekdays', 'weekdaysIntroPeriod', 'weekdaysSecondPeriod', 'reminderTime', 'blueAlarmTime']

    public List<TimeOfDay> getTimesOfDay() {
        TimeOfDay.toTimesOfDay(internalTimesOfDay)
    }

    public void setTimesOfDay(List<TimeOfDay> timesOfDay) {
        internalTimesOfDay = TimeOfDay.fromTimesOfDay(timesOfDay)
    }


    public List<Weekday> getWeekdays() {
        return Weekday.toWeekdays(internalWeekdays)
    }

    public void setWeekdays(List<Weekday> weekdays) {
        internalWeekdays = Weekday.fromWeekdays(weekdays)
    }

    TimeOfDay getReminderTime() {
        return TimeOfDay.toTimeOfDay(internalReminderTime, hour: 10, minute: 0)

    }

    void setReminderTime(TimeOfDay reminderTime) {
        internalReminderTime = reminderTime.toString()
    }

    TimeOfDay getBlueAlarmTime() {
        return TimeOfDay.toTimeOfDay(internalBlueAlarmTime, hour: 23, minute: 59)
    }

    void setBlueAlarmTime(TimeOfDay blueAlarmTime) {
        internalBlueAlarmTime = blueAlarmTime.toString()
    }

    List<Weekday> getWeekdaysIntroPeriod() {
        return Weekday.toWeekdays(internalWeekdaysIntroPeriod)
    }

    void setWeekdaysIntroPeriod(List<Weekday> weekdaysIntroPeriod) {
        internalWeekdaysIntroPeriod = Weekday.fromWeekdays(weekdaysIntroPeriod)
    }

    List<Weekday> getWeekdaysSecondPeriod() {
        return Weekday.toWeekdays(internalWeekdaysSecondPeriod)
    }

    void setWeekdaysSecondPeriod(List<Weekday> weekdaysSecondPeriod) {
        internalWeekdaysSecondPeriod = Weekday.fromWeekdays(weekdaysSecondPeriod)
    }


    public void setDaysInMonth(List<Integer> daysInMonth) {
        internalDaysInMonth = Schedule.DaysInMonth.toDaysInMonthString(daysInMonth)
    }

    public List<Integer> getDaysInMonth() {
        Schedule.DaysInMonth.fromDaysInMonthString(internalDaysInMonth)
    }


    boolean hasTimeSchedule() {
        type != UNSCHEDULED
    }

    boolean isMonthlySchedule() {
        type == MONTHLY
    }

    Calendar getLatestDeadlineBefore(Calendar endTime) {
        Iterator<Date> iterator = this.scheduleIterator
        Date endTimeAsDate = endTime.time

        Date latest = null

        while (iterator.hasNext()) {
            Date date = iterator.next()

            // If we passed the end time then return the previous deadline.
            if (!date.before(endTimeAsDate)) {
                return latest?.toCalendar()
            }

            latest = date
        }

        latest?.toCalendar()
    }

    Calendar getNextDeadlineAfter(Calendar fromTime) {
        scheduleIterator.find { it.after(fromTime.time) }?.toCalendar()
    }

    private Iterator<Date> getScheduleIterator() {
        switch (type) {
            case UNSCHEDULED:
                return [].iterator()
            case WEEKDAYS:
                return new WeekdayScheduleIterator(monitoringPlan.startDate, weekdays, timesOfDay)
            case WEEKDAYS_ONCE:
                return new WeekdayOnceScheduleIterator(monitoringPlan.startDate, introPeriodWeeks, weekdaysIntroPeriod, weekdaysSecondPeriod, blueAlarmTime)
            case MONTHLY:
                return new MonthlyScheduleIterator(monitoringPlan.startDate, daysInMonth, timesOfDay)
            case EVERY_NTH_DAY:
                return new NthDayScheduleIterator(monitoringPlan.startDate, startingDate, dayInterval, timesOfDay)
            case SPECIFIC_DATE:
                return new SpecificDateScheduleIterator(monitoringPlan.startDate, specificDate, timesOfDay)
            default:
                throw new RuntimeException("Unknown schedule type: '${type}'")
        }
    }
}
