package org.opentele.server.model
import org.opentele.server.model.Schedule.ScheduleType
import org.opentele.server.model.Schedule.StartingDate
import org.opentele.server.model.Schedule.TimeOfDay
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.model.types.Weekday
import org.opentele.server.questionnaire.scheduleiterators.MonthlyScheduleIterator
import org.opentele.server.questionnaire.scheduleiterators.NthDayScheduleIterator
import org.opentele.server.questionnaire.scheduleiterators.SpecificDateScheduleIterator
import org.opentele.server.questionnaire.scheduleiterators.WeekdayScheduleIterator

class QuestionnaireSchedule extends AbstractObject implements Schedule {
    MonitoringPlan monitoringPlan

    QuestionnaireHeader questionnaireHeader
    ScheduleType type = ScheduleType.UNSCHEDULED
    String internalTimesOfDay = ''
    String internalWeekdays = ''
    String internalDaysInMonth = ''
    Date internalStartingDate = today() // Only relevant when type is EVERY_NTH_DAY
    int dayInterval = 2
    Date internalSpecificDate = null
    int reminderStartMinutes = 30

    static mapping = {
        internalTimesOfDay column: 'TIMES_OF_DAY'
        internalWeekdays column: 'WEEKDAYS'
        internalDaysInMonth column: 'DAYS_IN_MONTH'
        internalStartingDate column: 'STARTING_DATE'
        internalSpecificDate column: 'SPECIFIC_DATE'
    }
    static transients = [ 'startingDate', 'specificDate' ]

    static QuestionnaireSchedule everyWeekdayAt(TimeOfDay timeOfDay) {
        atWeekdays(timeOfDay, new ArrayList((Collection<Weekday>)Weekday.values()))
    }

    static QuestionnaireSchedule everyNthDay(dayInterval, TimeOfDay timeOfDay) {
        def schedule = new QuestionnaireSchedule()
        schedule.type = ScheduleType.EVERY_NTH_DAY
        schedule.timesOfDay = [timeOfDay]
        schedule.dayInterval = dayInterval

        schedule
    }

    static QuestionnaireSchedule atWeekdays(TimeOfDay timeOfDay, List<Weekday> weekdays) {
        def schedule = new QuestionnaireSchedule()
        schedule.type = ScheduleType.WEEKDAYS
        schedule.weekdays = weekdays
        schedule.timesOfDay = [timeOfDay]

        schedule
    }

    static QuestionnaireSchedule unscheduled() {
        def schedule = new QuestionnaireSchedule()
        schedule.type = ScheduleType.UNSCHEDULED

        schedule
    }

    public void setDaysInMonth(List<Integer> daysInMonth) {
        this.internalDaysInMonth = daysInMonth.join(',')
    }

    public List<Integer> getDaysInMonth() {
        if (!internalDaysInMonth) {
            return []
        }
        internalDaysInMonth.split(',').collect {it as Integer}
    }

    public void setTimesOfDay(List<TimeOfDay> timesOfDay) {
        this.internalTimesOfDay = timesOfDay.collect { "${it.hour}:${it.minute}" }.join(',')
    }

    public List<TimeOfDay> getTimesOfDay() {
        if (!internalTimesOfDay) {
            return []
        }
        internalTimesOfDay.split(',').collect {
            def (hour, minute) = hourAndMinute(it)
            new TimeOfDay(hour: hour, minute: minute)
        }
    }

    public void setWeekdays(List<Weekday> weekdays) {
        this.internalWeekdays = weekdays.join(',')
    }

    public List<Weekday> getWeekdays() {
        if (!internalWeekdays) {
            return []
        }
        internalWeekdays.split(',').collect {Weekday.valueOf(it)}
    }

    public void setDayInterval(int dayInterval) {
        if (dayInterval <= 0) {
            throw new IllegalArgumentException("Non-positive day interval: ${dayInterval}")
        }
        this.dayInterval = dayInterval
    }

    public StartingDate getStartingDate() {
        def calendar = Calendar.getInstance()
        calendar.setTime(internalStartingDate)

        StartingDate.fromCalendar(calendar)

    }

    public void setStartingDate(StartingDate startingDate) {
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

    public void setSpecificDate(StartingDate specificDate)
    {
        if (specificDate == null) {
            internalSpecificDate = null
        } else {
            def calendar = Calendar.getInstance()
            calendar.clear()
            calendar.set(specificDate.year, specificDate.month.asCalendarMonth(), specificDate.day)
            internalSpecificDate = calendar.getTime()
        }
    }

    public StartingDate getSpecificDate()
    {
        if(internalSpecificDate == null) {
            null
        } else {
            def calendar = Calendar.getInstance()
            calendar.setTime(internalSpecificDate)
            StartingDate.fromCalendar(calendar)
        }
    }

    boolean hasTimeSchedule() {
        type != ScheduleType.UNSCHEDULED
    }

    boolean isMonthlySchedule() {
        type == ScheduleType.MONTHLY
    }

    static constraints = {
        internalTimesOfDay(nullable: false, validator: {
            if (it.empty) {
                return true
            }
            it.split(',').every {
                def (hour, minute) = hourAndMinute(it)
                hour != null && minute != null && within(hour, 0, 23) && within(minute, 0, 59)
            }
        })
        internalWeekdays(nullable: false)
        internalDaysInMonth(nullable: false, validator: {
            if (it.empty) {
                return true
            }
            it.split(',').every {
                within(it as Integer, 1, 28)
            }

        })
        internalStartingDate(nullable: false)
        internalSpecificDate(nullable: true)
        monitoringPlan(nullable: false)
        monitoringPlan(unique: 'questionnaireHeader')
        questionnaireHeader(nullable: false)
    }

    private static boolean within(i, lowerBound, upperBound) {
        i >= lowerBound && i <= upperBound
    }

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

    private Date today() {
        def result = Calendar.getInstance()
        result.set(Calendar.HOUR_OF_DAY, 0)
        result.set(Calendar.MINUTE, 0)
        result.set(Calendar.SECOND, 0)
        result.set(Calendar.MILLISECOND, 0)
        result.getTime()
    }

    private boolean equalFields(StartingDate startingDate, Calendar calendar) {
        calendar.getTime() // To recalculate all fields

        calendar.get(Calendar.YEAR) == startingDate.year &&
        calendar.get(Calendar.MONTH) == startingDate.month.asCalendarMonth() &&
        calendar.get(Calendar.DATE) == startingDate.day
    }

    Calendar getLatestDeadlineBefore(Calendar endTime) {
        Iterator<Date> iterator = iterator()
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
        Iterator<Date> iterator = iterator()
        Date fromTimeAsDate = fromTime.time

        while (iterator.hasNext()) {
            Date date = iterator.next()
            if (date.after(fromTimeAsDate)) {
                return date?.toCalendar()
            }
        }
        null
    }

    private Iterator<Date> iterator() {
        switch (type) {
            case Schedule.ScheduleType.UNSCHEDULED:
                return [].iterator()
            case Schedule.ScheduleType.WEEKDAYS:
                return new WeekdayScheduleIterator(monitoringPlan.startDate, weekdays, timesOfDay)
            case Schedule.ScheduleType.MONTHLY:
                return new MonthlyScheduleIterator(monitoringPlan.startDate, daysInMonth, timesOfDay)
            case Schedule.ScheduleType.EVERY_NTH_DAY:
                return new NthDayScheduleIterator(monitoringPlan.startDate, startingDate.toDate(), dayInterval, timesOfDay)
            case Schedule.ScheduleType.SPECIFIC_DATE:
                return new SpecificDateScheduleIterator(monitoringPlan.startDate, specificDate.toDate(), timesOfDay)
            default:
                throw new RuntimeException("Unknown schedule type: '${type}'")
        }
    }
}
