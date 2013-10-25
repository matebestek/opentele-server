package org.opentele.server.questionnaire.scheduleiterators

import org.opentele.server.model.Schedule

/**
 * Iterates through all times in a monthly schedule.
 *
 * Gives no times before the specified starting date.
 */
class MonthlyScheduleIterator implements Iterator<Date> {
    private final List<Integer> daysInMonth
    private final List<Schedule.TimeOfDay> timesOfDay

    private int dayInMonthIndex
    private int timeOfDayIndex
    private Calendar currentDate

    public MonthlyScheduleIterator(Date startingDate, List<Integer> daysInMonth, List<Schedule.TimeOfDay> timesOfDay) {
        this.daysInMonth = daysInMonth
        this.timesOfDay = timesOfDay

        if (!daysInMonth.empty && !timesOfDay.empty) {
            currentDate = Calendar.getInstance()
            currentDate.setTime(startingDate)
            goToMatchingDayInMonth()
        }
    }

    @Override
    boolean hasNext() {
        !daysInMonth.empty && !timesOfDay.empty
    }

    @Override
    Date next() {
        setDayInMonth(daysInMonth[dayInMonthIndex])
        setTimeOfDay(timesOfDay[timeOfDayIndex])
        Date result = currentDate.time

        incrementTimeOfDay()

        result
    }

    @Override
    void remove() {
        throw new UnsupportedOperationException()
    }

    private setDayInMonth(int dayInMonth) {
        currentDate.set(Calendar.DAY_OF_MONTH, dayInMonth)
    }

    private setTimeOfDay(Schedule.TimeOfDay timeOfDay) {
        currentDate.set(Calendar.HOUR_OF_DAY, timeOfDay.hour)
        currentDate.set(Calendar.MINUTE, timeOfDay.minute)
    }

    private incrementTimeOfDay() {
        timeOfDayIndex++
        if (timeOfDayIndex >= timesOfDay.size()) {
            timeOfDayIndex = 0;
            incrementDayInMonth()
        }
    }

    private incrementDayInMonth() {
        dayInMonthIndex++
        if (dayInMonthIndex >= daysInMonth.size()) {
            dayInMonthIndex = 0
            moveToNextMonth()
        }
    }

    private void goToMatchingDayInMonth() {
        while (!daysInMonth.contains(currentDate.get(Calendar.DAY_OF_MONTH))) {
            currentDate.add(Calendar.DATE, 1)
        }
        dayInMonthIndex = daysInMonth.indexOf(currentDate.get(Calendar.DAY_OF_MONTH))
    }

    private moveToNextMonth() {
        currentDate.set(Calendar.DAY_OF_MONTH, 1)
        currentDate.add(Calendar.MONTH, 1)
    }
}
