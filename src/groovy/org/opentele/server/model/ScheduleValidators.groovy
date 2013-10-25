package org.opentele.server.model

import org.opentele.server.model.Schedule.ScheduleType
import org.opentele.server.model.types.Weekday


class ScheduleValidators {
    static Closure timesOfDay(ScheduleType... types) {
        return { String value, Schedule schedule ->
            if (schedule.type in types) {
                return value && value?.split(',')?.every {
                    isTimeOfDayValid(it)
                }
            }
            return true
        }
    }

    static Closure weekdays(ScheduleType... types) {
        return { String value, Schedule schedule ->
            if (schedule.type in types) {
                return value && value?.split(',')?.every {
                    try {
                        Weekday.valueOf(it)
                    } catch (ignored) {
                        false
                    }
                }
            }
            return true
        }
    }

    static Closure daysInMonth(ScheduleType... types) {
        return { String value, Schedule schedule ->
            if (schedule.type in types) {
                return value && value.split(',').every {
                    it && within(it, 1, 28)
                }
            }
            return true
        }
    }

    static Closure timeOfDay(ScheduleType... types) {
        return { String value, Schedule schedule ->
            if (schedule.type in types) {
                return isTimeOfDayValid(value)
            }
            return true
        }
    }

    private static boolean within(def number, int lowerBound, int upperBound) {
        try {
            number = number.toInteger()
            number >= lowerBound && number <= upperBound
        } catch (ignored) {
            false
        }
    }

    private static def hourAndMinute(String combinedHourAndMinute) {
        try {
            combinedHourAndMinute.split(':').collect { it.toInteger() }
        } catch (ignored) {
            []
        }
    }

    private static boolean isTimeOfDayValid(String combinedHourAndMinute) {
        def (hour, minute) = hourAndMinute(combinedHourAndMinute)
        return within(hour as Integer, 0, 23) && within(minute as Integer, 0, 59)
    }


}
