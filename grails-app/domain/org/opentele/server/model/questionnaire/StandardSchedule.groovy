package org.opentele.server.model.questionnaire
import org.opentele.server.model.Schedule
import org.opentele.server.model.Schedule.TimeOfDay
import org.opentele.server.model.Schedule.ScheduleType
import org.opentele.server.model.ScheduleValidators
import org.opentele.server.model.types.Weekday

import static org.opentele.server.model.Schedule.ScheduleType.*

class StandardSchedule implements Schedule {

    ScheduleType type = UNSCHEDULED
    //NOTE these internal properties are here as hibernate is using the getter, and this has to be of type string and not List<...> as the custom getters below
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
    Integer dayInterval = 2 // Mapped to INTERVAL_IN_DAYS in database

    // SPECIFIC DATE
    Date specificDate = null

    Integer reminderStartMinutes = 30

    // Otherwise Grails will create a standard_schedule in the database.
    // Could move to src/groovy, but that will create other problems with Hibernate
    // not accepting custom setters / getters
    static mapWith = "none"

    static constraints = {
        internalTimesOfDay nullable: true, validator: ScheduleValidators.timesOfDay(WEEKDAYS, MONTHLY, EVERY_NTH_DAY, SPECIFIC_DATE)
        internalWeekdays nullable: true, validator: ScheduleValidators.weekdays(WEEKDAYS)
        introPeriodWeeks nullable: false, min: 1, max: 8
        internalReminderTime nullable: true, validator: ScheduleValidators.timeOfDay(WEEKDAYS_ONCE)
        internalBlueAlarmTime nullable: true, validator: ScheduleValidators.timeOfDay(WEEKDAYS_ONCE)
        internalWeekdaysIntroPeriod nullable: true, validator: ScheduleValidators.weekdays(WEEKDAYS_ONCE)
        internalWeekdaysSecondPeriod nullable: true, validator: ScheduleValidators.weekdays(WEEKDAYS_ONCE)
        internalDaysInMonth nullable: true, validator: ScheduleValidators.daysInMonth(MONTHLY)
        reminderStartMinutes nullable : true
        startingDate nullable: false
        dayInterval nullable: false, min: 1
        specificDate nullable: true
    }

    static mapping = {
        internalTimesOfDay column: 'STANDARD_SCHEDULE_TIMES_OF_DAY'
        internalWeekdays column: 'STANDARD_SCHEDULE_WEEKDAYS'
        internalWeekdaysIntroPeriod column: 'STANDARD_SCHEDULE_WEEKDAYS_INTRO_PERIOD'
        internalWeekdaysSecondPeriod column: 'STANDARD_SCHEDULE_WEEKDAYS_SECOND_PERIOD'
        internalReminderTime column: 'STANDARD_SCHEDULE_REMINDER_TIME'
        internalBlueAlarmTime column: 'STANDARD_SCHEDULE_BLUE_ALARM_TIME'
        internalDaysInMonth column: 'STANDARD_SCHEDULE_DAYS_IN_MONTH'
        dayInterval column: 'STANDARD_SCHEDULE_INTERVAL_IN_DAYS'
        startingDate column: 'STANDARD_SCHEDULE_STARTING_DATE'
        specificDate column: 'STANDARD_SCHEDULE_SPECIFIC_DATE'
    }

    static transients = ['timesOfDay','weekdays','weekdaysIntroPeriod','weekdaysSecondPeriod','reminderTime','blueAlarmTime']

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
}
