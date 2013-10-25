package org.opentele.server.questionnaire.scheduleiterators

import org.opentele.server.model.Schedule

/**
 * Iterates through all specified times of a specific date.
 *
 * Gives nothing if the specific date is before the specified starting date for the schedule.
 */
class SpecificDateScheduleIterator implements Iterator<Date> {
    Iterator<Date> iterator

    public SpecificDateScheduleIterator(Date startingDate, Date specificDate, List<Schedule.TimeOfDay> timesOfDay) {
        List<Date> dates = []

        if (!startingDate.after(specificDate)) {
            Calendar c = Calendar.getInstance()
            c.setTime(specificDate)

            for (Schedule.TimeOfDay timeOfDay: timesOfDay) {
                setTimeOfDay(c, timeOfDay)
                dates << c.time
            }
        }

        iterator = dates.iterator()
    }

    @Override
    boolean hasNext() {
        iterator.hasNext()
    }

    @Override
    Date next() {
        iterator.next()
    }

    @Override
    void remove() {
        throw new UnsupportedOperationException()
    }

    private setTimeOfDay(Calendar calendar, Schedule.TimeOfDay timeOfDay) {
        calendar.set(Calendar.HOUR_OF_DAY, timeOfDay.hour)
        calendar.set(Calendar.MINUTE, timeOfDay.minute)
    }
}
