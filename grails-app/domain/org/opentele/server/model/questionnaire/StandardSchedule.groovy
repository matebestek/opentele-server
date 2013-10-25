package org.opentele.server.model.questionnaire


import org.opentele.server.model.Schedule
import org.opentele.server.model.types.Weekday


class StandardSchedule implements Schedule {

    Schedule.ScheduleType type = Schedule.ScheduleType.UNSCHEDULED
    //NOTE these internal properties are here as hibernate is using the getter, and this has to be of type string and not List<...> as the custom getters below
    String internalWeekdays = ''
    String internalTimesOfDay = ''
    String internalDaysInMonth = ''
    Date internalStartingDate = new Date() // Only relevant when type is EVERY_NTH_DAY
    int intervalInDays = 2 // Ought to be "dayInterval", since this is what the Schedule interface calls it. Alas, we have a getter and setter below
    Date internalSpecificDate = null
    int reminderStartMinutes = 30

    // Otherwise Grails will create a standard_schedule in the database.
    // Could move to src/groovy, but that will create other problems with Hibernate
    // not accepting custom setters / getters
    static mapWith = "none"

    static constraints = {
        internalSpecificDate nullable: true
    }

    static mapping = {
        internalWeekdays column: 'STANDARD_SCHEDULE_WEEKDAYS'
        internalTimesOfDay column: 'STANDARD_SCHEDULE_TIMES_OF_DAY'
        internalDaysInMonth column: 'STANDARD_SCHEDULE_DAYS_IN_MONTH'
        internalStartingDate column: 'STANDARD_SCHEDULE_STARTING_DATE'
        internalSpecificDate column: 'STANDARD_SCHEDULE_SPECIFIC_DATE'
    }
    static transients = [ 'startingDate', 'specificDate', 'dayInterval' ]

    public void setWeekdays(List<Weekday> weekdays) {
        this.internalWeekdays = weekdays.join(',')
    }

    public List<Weekday> getWeekdays() {
        if (this.internalWeekdays == null || this.internalWeekdays.empty) {
            return []
        }
        this.internalWeekdays.split(',').collect {Weekday.valueOf(it)}
    }

    public void setTimesOfDay(List<Schedule.TimeOfDay> timesOfDay) {
        this.internalTimesOfDay = timesOfDay.collect {
            "${it.hour.toString().padLeft(2,'0')}:${it.minute.toString().padLeft(2,'0')}"
        }.join(',')
    }

    public List<Schedule.TimeOfDay> getTimesOfDay() {
        if (internalTimesOfDay.empty) {
            return []
        }
        internalTimesOfDay.split(',').collect {
            def (hour, minute) = hourAndMinute(it)
            new Schedule.TimeOfDay(hour: hour, minute: minute)
        }
    }

    public void setDaysInMonth(List<Integer> daysInMonth) {
        this.internalDaysInMonth = daysInMonth.join(',')
    }

    public List<Integer> getDaysInMonth() {
        if (internalDaysInMonth.empty) {
            return []
        }
        internalDaysInMonth.split(',').collect {it as Integer}
    }

    public Schedule.StartingDate getStartingDate() {
        def calendar = Calendar.getInstance()
        calendar.setTime(internalStartingDate)

        Schedule.StartingDate.fromCalendar(calendar)
    }

    public void setStartingDate(Schedule.StartingDate startingDate) {
        def calendar = Calendar.getInstance()
        calendar.clear()
        calendar.set(Calendar.YEAR, startingDate.year)
        calendar.set(Calendar.MONTH, startingDate.month.asCalendarMonth())
        calendar.set(Calendar.DATE, startingDate.day)

        if (!equalFields(startingDate, calendar)) {
            throw new IllegalArgumentException("Invalid starting date: '${startingDate}")
        }

        internalStartingDate = calendar.getTime()
    }

    public void setDayInterval(int dayInterval) {
        intervalInDays = dayInterval
    }

    public int getDayInterval() {
        intervalInDays
    }

    public void setSpecificDate(Schedule.StartingDate specificDate) {
        if (specificDate == null) {
            internalSpecificDate = null
        } else {
            def calendar = Calendar.getInstance()
            calendar.clear()
            calendar.set(specificDate.year, specificDate.month.asCalendarMonth(), specificDate.day)
            internalSpecificDate = calendar.getTime()
        }
    }

    public Schedule.StartingDate getSpecificDate() {
        if(internalSpecificDate == null) {
            null
        } else {
            def calendar = Calendar.getInstance()
            calendar.setTime(internalSpecificDate)
            Schedule.StartingDate.fromCalendar(calendar)
        }
    }

    //TODO mss refactor / this is also in QuestionnaireSchedule
    private static def hourAndMinute(combinedHourAndMinute) {
        def hourAndMinute = combinedHourAndMinute.split(':')
        if (hourAndMinute.size() == 2) {
            def hour = hourAndMinute.first()
            def minute = hourAndMinute.last()
            if (hour.matches('^\\d+$') && minute.matches('^\\d+$')) {
                return [hour as Integer, minute as Integer]
            }
        }
        [null, null]
    }

    private boolean equalFields(Schedule.StartingDate startingDate, Calendar calendar) {
        calendar.getTime() // To recalculate all fields

        calendar.get(Calendar.YEAR) == startingDate.year &&
                calendar.get(Calendar.MONTH) == startingDate.month.asCalendarMonth() &&
                calendar.get(Calendar.DATE) == startingDate.day
    }
}
