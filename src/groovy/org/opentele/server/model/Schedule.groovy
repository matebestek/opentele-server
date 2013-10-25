package org.opentele.server.model

import groovy.transform.EqualsAndHashCode
import org.opentele.server.model.Schedule.ScheduleType

import org.opentele.server.model.Schedule.TimeOfDay
import org.opentele.server.model.types.Weekday

public interface Schedule {
    static enum ScheduleType { UNSCHEDULED, WEEKDAYS, WEEKDAYS_ONCE, MONTHLY, EVERY_NTH_DAY, SPECIFIC_DATE }

    @EqualsAndHashCode
    static class TimeOfDay implements Comparable<TimeOfDay>{
        int hour
        int minute

        int compareTo(TimeOfDay other) {
            this.hour <=> other?.hour ?: this.minute <=> other?.minute
        }

        @Override String toString() {
            "${hour.toString().padLeft(2,'0')}:${minute.toString().padLeft(2,'0')}"
        }

        boolean isValid() {
            this.hour >= 0 && this.hour < 24 && this.minute >= 0 && this.minute < 60
        }

        static TimeOfDay toTimeOfDay(String hour, String minute) {
           toTimeOfDay(hour.toInteger(), minute.toInteger())
        }
        static TimeOfDay toTimeOfDay(Integer hour, Integer minute) {
            new TimeOfDay(hour: hour.toInteger(), minute: minute.toInteger())
        }
        static TimeOfDay toTimeOfDay( Map defaults = [:], String hourMinute) {
            if(hourMinute) {
                try {
                    def (hour, minute) = hourMinute?.trim()?.split(':')
                    return toTimeOfDay(hour as String, minute as String)
                } catch (ignore) {}
            }
            return defaults ? new TimeOfDay(defaults) : null
        }
        static List<TimeOfDay> toTimesOfDay(String timesOfDay) {
            timesOfDay?.trim()?.split(',')?.inject([]) { list, tod ->
                def timeOfDay = TimeOfDay.toTimeOfDay(tod)
                if(timeOfDay) {
                    list << timeOfDay
                } else {
                    list
                }
            } ?: []
        }
        static String fromTimesOfDay(List<TimeOfDay> timesOfDay) {
            (timesOfDay ?: [])*.toString().join(',')
        }
    }

    static class DaysInMonth {
        static List<Integer> fromDaysInMonthString(String daysInMonth) {
            daysInMonth?.trim()?.split(',')?.inject([]) {list, number ->
                try {
                    list << (number as Integer)
                } catch (ignored) {
                    list
                }
            } ?: []
        }

        static String toDaysInMonthString(List<Integer> daysInMonth) {
            daysInMonth ? daysInMonth.join(",") : ""
        }
    }

    ScheduleType getType()
    void setType(ScheduleType type)

    List<TimeOfDay> getTimesOfDay()
    void setTimesOfDay(List<TimeOfDay> timesOfDay)

    // For WEEKDAYS
    List<Weekday> getWeekdays()
    void setWeekdays(List<Weekday> weekdays)

    // For WEEKDAYS_ONCE
    Integer getIntroPeriodWeeks()
    void setIntroPeriodWeeks(Integer introPeriodWeeks)

    TimeOfDay getReminderTime()
    void setReminderTime(TimeOfDay reminderTime)

    TimeOfDay getBlueAlarmTime()
    void setBlueAlarmTime(TimeOfDay blueAlarmTime)

    List<Weekday> getWeekdaysIntroPeriod()
    void setWeekdaysIntroPeriod(List<Weekday> weekdaysIntroPeriod)

    List<Weekday> getWeekdaysSecondPeriod()
    void setWeekdaysSecondPeriod(List<Weekday> weekdaysSecondPeriod)

    // For MONTHLY
    List<Integer> getDaysInMonth()
    void setDaysInMonth(List<Integer> daysInMonth)

    // For EVERY_NTH_DAY
    Date getStartingDate()
    void setStartingDate(Date startingDate)

    Integer getDayInterval()
    void setDayInterval(Integer dayInterval)

    // For SPECIFIC_DATE
    Date getSpecificDate()
    void setSpecificDate(Date specificDate)

    int getReminderStartMinutes()
    void setReminderStartMinutes(int reminderStartMinutes)
}
