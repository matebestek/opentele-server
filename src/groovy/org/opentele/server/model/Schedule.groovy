package org.opentele.server.model

import org.opentele.server.model.Schedule.ScheduleType
import org.opentele.server.model.Schedule.StartingDate
import org.opentele.server.model.Schedule.TimeOfDay
import org.opentele.server.model.types.Month
import org.opentele.server.model.types.Weekday

public interface Schedule {
    static enum ScheduleType { UNSCHEDULED, WEEKDAYS, MONTHLY, EVERY_NTH_DAY, SPECIFIC_DATE }
    static class TimeOfDay {
        int hour
        int minute

        @Override boolean equals(Object other) {
            other.class == TimeOfDay &&
            other.hour == hour &&
            other.minute == minute
        }

        @Override int hashCode() {
            hour.hashCode() ^ minute.hashCode()
        }

        @Override String toString() {
            "${hour.toString().padLeft(2,'0')}:${minute.toString().padLeft(2,'0')}"
        }

        static TimeOfDay toTimeOfDay(String hour, String minute) {
           toTimeOfDay(hour.toInteger(), minute.toInteger())
        }
        static TimeOfDay toTimeOfDay(Integer hour, Integer minute) {
            new TimeOfDay(hour: hour.toInteger(), minute: minute.toInteger())
        }
    }

    static class StartingDate {
        int day
        Month month
        int year

        @Override boolean equals(Object other) {
            other.class == StartingDate &&
            other.day == day &&
            other.month == month &&
            other.year == year
        }

        @Override int hashCode() {
            day.hashCode() ^ month.hashCode() ^ year.hashCode()
        }


        @Override String toString() {
            "StartingDate[day=${day},month=${month},year=${year}]"
        }

        static StartingDate fromCalendar(Calendar calendar) {
            new StartingDate(day: calendar.get(Calendar.DATE), month: Month.fromCalendarMonth(calendar.get(Calendar.MONTH)), year: calendar.get(Calendar.YEAR))
        }

        def getCalendar() { //is used from _nthDaySchedule.gsp
            Calendar calendar = Calendar.getInstance()
            calendar.clear()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month.asCalendarMonth())
            calendar.set(Calendar.DATE, day)

            calendar
        }

        def asType(Class clazz) {
            switch(clazz) {
                case java.util.Date:
                    return calendar.time
                    break;
                default:
                    return this
            }

        }

        Date toDate() {
            calendar.time
        }

        static StartingDate fromDate(Date date) {
            if(date) {
                def calendar = Calendar.getInstance()
                calendar.setTime(date)
                StartingDate.fromCalendar(calendar)
            } else {
                return null
            }
        }
    }

    ScheduleType type
    List<TimeOfDay> timesOfDay

    // For WEEKDAYS
    List<Weekday> weekdays

    // For MONTHLY
    List<Integer> daysInMonth

    // For EVERY_NTH_DAY
    StartingDate startingDate
    int dayInterval

    // For SPECIFIC_DATE
    StartingDate specificDate

    int reminderStartMinutes
}
