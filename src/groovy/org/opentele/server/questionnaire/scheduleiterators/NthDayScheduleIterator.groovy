package org.opentele.server.questionnaire.scheduleiterators

import org.opentele.server.model.Schedule

/**
 * Iterates through all times in an "nth-day" schedule.
 *
 * The "every nth day" schedule has a starting date from which it calculates the "nth days", but the iterator makes
 * sure not to output dates before the actual starting date given in the constructor.
 */
class NthDayScheduleIterator implements Iterator<Date> {
    private final int dayInterval
    private final List<Schedule.TimeOfDay> timesOfDay

    private int timeOfDayIndex
    private Calendar currentDate

    public NthDayScheduleIterator(Date startingDate, Date intervalStartingDate, int dayInterval, List<Schedule.TimeOfDay> timesOfDay) {
        this.dayInterval = dayInterval
        this.timesOfDay = timesOfDay

        if (!timesOfDay.empty && dayInterval > 0) {
            currentDate = Calendar.getInstance()
            currentDate.setTime(intervalStartingDate)
            goToFirstValidDate(startingDate)
        }
    }

    @Override
    boolean hasNext() {
        !timesOfDay.empty && dayInterval > 0
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
            moveToNextDate()
        }
    }

    private void goToFirstValidDate(Date startingDate) {
        while (currentDate.time.before(startingDate)) {
            moveToNextDate()
        }
    }

    private void moveToNextDate() {
        currentDate.add(Calendar.DATE, dayInterval)
    }
}
