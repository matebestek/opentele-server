package org.opentele.server.questionnaire.scheduleiterators

import org.opentele.server.model.Schedule
import org.opentele.server.model.types.Weekday

/**
 * Iterates through specific weekdays.
 */
class WeekdayScheduleIterator implements Iterator<Date> {
    private final Set<Integer> weekdays
    private final List<Schedule.TimeOfDay> timesOfDay

    private int timeOfDayIndex
    private Calendar currentDate

    public WeekdayScheduleIterator(Date startingDate, List<Weekday> weekdays, List<Schedule.TimeOfDay> timesOfDay) {
        this.weekdays = (weekdays*.asCalendarWeekday()).toSet()
        this.timesOfDay = timesOfDay

        if (!weekdays.empty && !timesOfDay.empty) {
            currentDate = Calendar.getInstance()
            currentDate.setTime(startingDate)
            goToMatchingWeekday()
        }
    }

    @Override
    boolean hasNext() {
        !weekdays.empty && !timesOfDay.empty
    }

    @Override
    Date next() {
        setTimeOfDay(timesOfDay[timeOfDayIndex])
        Date result = currentDate.time

        incrementTimeOfDay()

        result
    }

    @Override
    void remove() {
        throw new UnsupportedOperationException()
    }

    private setTimeOfDay(Schedule.TimeOfDay timeOfDay) {
        currentDate.set(Calendar.HOUR_OF_DAY, timeOfDay.hour)
        currentDate.set(Calendar.MINUTE, timeOfDay.minute)
    }

    private incrementTimeOfDay() {
        timeOfDayIndex++
        if (timeOfDayIndex >= timesOfDay.size()) {
            timeOfDayIndex = 0;
            currentDate.add(Calendar.DATE, 1)
            goToMatchingWeekday()
        }
    }

    private void goToMatchingWeekday() {
        while (!weekdays.contains(currentDate.get(Calendar.DAY_OF_WEEK))) {
            currentDate.add(Calendar.DATE, 1)
        }
    }
}
