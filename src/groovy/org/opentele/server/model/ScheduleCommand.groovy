package org.opentele.server.model

import grails.validation.Validateable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.util.logging.Log4j
import org.apache.commons.collections.ListUtils
import org.opentele.server.model.types.Weekday

import java.text.ParseException

@Validateable
@Log4j
@ToString(includeFields = true)
@EqualsAndHashCode
class ScheduleCommand implements Schedule {

    Schedule.ScheduleType type = Schedule.ScheduleType.UNSCHEDULED
    List<Schedule.TimeOfDay> timesOfDay = ListUtils.lazyList([], {
        new Schedule.TimeOfDay()
    } as org.apache.commons.collections.Factory)
    Integer reminderStartMinutes = 30

    // MONTHLY
    List<Integer> daysInMonth = []

    // WEEKDAYS
    List<Weekday> weekdays = []

    // WEEKDAYS_ONCE
    Schedule.TimeOfDay reminderTime = new Schedule.TimeOfDay(hour: 10, minute: 00)
    Schedule.TimeOfDay blueAlarmTime = new Schedule.TimeOfDay(hour: 23, minute: 59)
    Integer introPeriodWeeks = 4
    List<Weekday> weekdaysIntroPeriod = []
    List<Weekday> weekdaysSecondPeriod = []

    // EVERY_NTH_DAY
    Integer dayInterval = 2
    Date startingDate = new Date()

    // SPECIFIC_DATE
    Date specificDate

    List<Schedule.TimeOfDay> getTimesOfDay() {
        def day = timesOfDay
        if (!timesOfDay) {
            timesOfDay << new Schedule.TimeOfDay(hour: 10, minute: 0)
        }
        return day
    }

    static getPropertyNames() {
        ScheduleCommand.metaClass.properties*.name - ['propertyNames', 'class', 'metaClass', 'constraints', 'errors', 'properties']
    }

    static constraints = {
        type nullable: false
        reminderStartMinutes min: 1
        dayInterval nullable: false, min: 1

        timesOfDay validator: { value, obj ->
            if (obj.type in [Schedule.ScheduleType.UNSCHEDULED, Schedule.ScheduleType.WEEKDAYS_ONCE]) {
                return true
            } else {
                if (value.size() > 0) {
                    if(value.every { it.valid }) {
                        return true
                    } else {
                        return ["contains.error", value.findAll {!it.valid}.join(", ")]
                    }
                } else {
                    return "error.atLeastOneTimeOfDay"
                }
            }
        }
        daysInMonth validator: { value, obj ->
            if (obj.type == Schedule.ScheduleType.MONTHLY) {
                if (!value.size()) {
                    return "error.atLeastOneDayInMonth"
                }
            }
        }
        weekdays validator: { value, obj ->
            if (obj.type == Schedule.ScheduleType.WEEKDAYS) {
                if (!value?.size()) {
                    return "error.atLeastOneWeekday"
                }
            }
        }
        introPeriodWeeks min: 1, max: 8
        weekdaysIntroPeriod validator: { value, obj ->
            if (obj.type == Schedule.ScheduleType.WEEKDAYS_ONCE) {
                if (!value?.size()) {
                    return "error.atLeastOneWeekdayIntroPeriod"
                }
            }
        }
        weekdaysSecondPeriod validator: { value, obj ->
            if (obj.type == Schedule.ScheduleType.WEEKDAYS_ONCE) {
                if (!value?.size()) {
                    return "error.atLeastOneWeekdaySecondPeriod"
                }
            }
        }
        reminderTime nullable: false, validator: { value, obj ->
            if(obj.type == Schedule.ScheduleType.WEEKDAYS_ONCE) {
                value < obj.blueAlarmTime
            }
        }
        blueAlarmTime nullable: false
        startingDate validator: { value, obj ->
            if (obj.type == Schedule.ScheduleType.EVERY_NTH_DAY) {
                if (!value) {
                    return "nullable"
                }
            }

        }
        specificDate nullable: true, validator: { value, obj ->
            if (obj.type == Schedule.ScheduleType.SPECIFIC_DATE) {
                if (!value) {
                    return "nullable"
                }
            }
        }
    }

    void bindScheduleData(Schedule target) {
        // TODO: Consider grails databinder for Grails 2.3
        def names = ScheduleCommand.propertyNames
        names.each { property ->
            target."$property" = this."$property"
        }
    }

    void cloneScheduleData(def source) {
        // TODO: Consider grails databinder for Grails 2.3
        if (source instanceof Map) {
            this.type = source.type as Schedule.ScheduleType
            this.weekdays = source.weekdays.collect { Weekday.valueOf(it as String) }
            this.reminderTime = parseTimeOfDay(source, 'reminderTime', hour: 10, minute: 0)
            this.blueAlarmTime = parseTimeOfDay(source, 'blueAlarmTime', hour: 23, minute: 59)
            this.introPeriodWeeks = source.introPeriodWeeks ? Integer.parseInt(source.introPeriodWeeks) : 4
            this.weekdaysIntroPeriod = source.weekdaysIntroPeriod?.collect { Weekday.valueOf(it as String) }
            this.weekdaysSecondPeriod = source.weekdaysSecondPeriod?.collect { Weekday.valueOf(it as String) }
            this.daysInMonth = source.daysInMonth.collect { it as Integer }
            this.dayInterval = source.dayInterval as Integer
            this.startingDate = parseDate(source.startingDate as String, new Date())
            this.specificDate = parseDate(source.specificDate as String)
            this.timesOfDay = source.timesOfDay.collect { new Schedule.TimeOfDay(hour: it.hour as Integer, minute: it.minute as Integer) }
            this.reminderStartMinutes = source.reminderStartMinutes as Integer

        } else if (source instanceof Schedule) {
            ScheduleCommand.propertyNames.each { property ->
                this."$property" = source."$property"
            }
        } else {
            throw new IllegalArgumentException("Unknown type to clone from")
        }
    }

    private Schedule.TimeOfDay parseTimeOfDay(Map defaults = [:], def source, String field) {
        if (source."${field}.hour" && source."${field}.minute") {
            return Schedule.TimeOfDay.toTimeOfDay(source."${field}.hour", source."${field}.minute")
        } else {
            try {
                return new Schedule.TimeOfDay(hour: source."$field"?.hour?.toInteger() ?: defaults.hour ?: 10, minute: source."$field"?.minute?.toInteger() ?: defaults.minute ?: 0)
            } catch (ignore)  {
                return null
            }
        }
    }

    private Date parseDate(String stringDate, Date defaultDate = null) {
        if (!stringDate) return defaultDate
        try {
            return Date.parse("dd/MM/yyyy", stringDate)
        } catch (ParseException ignored) {
            return Date.parse("dd-MM-yyyy", stringDate)
        }
    }
}
