package org.opentele.server.questionnaire.scheduleiterators

import org.opentele.server.model.Schedule
import org.opentele.server.model.types.Weekday

import static java.util.Calendar.*

/**
 * Iterates through specific weekdays depending on period.
 */
class WeekdayOnceScheduleIterator implements Iterator<Date> {
    final SortedSet<Integer> weekdaysIntroPeriod
    final SortedSet<Integer> weekdaysSecondPeriod
    final int introPeriodWeeks
    final Schedule.TimeOfDay blueAlarmTime

    private Date currentDate
    private startWeek = 1
    private List<Integer> weekdaysInCurrentWeek

    public WeekdayOnceScheduleIterator(Date startingDate, int introPeriodWeeks,
                                       List<Weekday> weekdaysIntroPeriod, List<Weekday> weekdaysSecondPeriod,
                                       Schedule.TimeOfDay blueAlarmTime) {
        this.introPeriodWeeks = introPeriodWeeks
        // Get weekday numbers as integers from Monday(1) to Sunday(7)
        this.weekdaysIntroPeriod = weekdaysIntroPeriod.collect { adjustDayOfWeek(it.asCalendarWeekday()) } as SortedSet
        this.weekdaysSecondPeriod = weekdaysSecondPeriod.collect { adjustDayOfWeek(it.asCalendarWeekday()) } as SortedSet
        this.blueAlarmTime = blueAlarmTime

        if (this.weekdaysIntroPeriod && this.weekdaysSecondPeriod) {
            currentDate = startingDate
            currentDate[HOUR_OF_DAY] = blueAlarmTime.hour
            currentDate[MINUTE] = blueAlarmTime.minute
            startWeek = currentDate[WEEK_OF_YEAR]
            // Find the weekdays after startingDate
            weekdaysInCurrentWeek = this.weekdaysIntroPeriod.findAll { it >= currentDayOfWeek } as List
        }
    }

    @Override
    boolean hasNext() {
        weekdaysIntroPeriod && weekdaysSecondPeriod
    }

    @Override
    Date next() {
        moveToNextWeekday()
        currentDate.clone() as Date
    }

    @Override
    void remove() {
        throw new UnsupportedOperationException()
    }

    private void moveToNextWeekday() {
        if (weekdaysInCurrentWeek) {
            def nextDayOfWeek = weekdaysInCurrentWeek.remove(0)
            def delta = nextDayOfWeek - currentDayOfWeek
            currentDate[DATE] = currentDate[DATE] + delta
        } else {
            moveToNextWeek()
            moveToNextWeekday()
        }
    }

    private void moveToNextWeek() {
        // Move to the first day of next week
        def delta = 7 - currentDayOfWeek + 1

        currentDate[DATE] = currentDate[DATE] + delta

        // Find the weekdays for the week to come
        if (currentDate[WEEK_OF_YEAR] - startWeek < introPeriodWeeks) {
            weekdaysInCurrentWeek = weekdaysIntroPeriod.collect() as List
        } else {
            weekdaysInCurrentWeek = weekdaysSecondPeriod.collect() as List
        }
    }

    private int getCurrentDayOfWeek() {
        adjustDayOfWeek(currentDate[DAY_OF_WEEK])
    }

    // Make day of week go from 1 (Monday) to 7 (Sunday), Calendar.MONDAY == 2, Calendar.SUNDAY == 1
    private int adjustDayOfWeek(int dayOfWeek) {
        dayOfWeek - 1 ?: 7
    }

}
