package org.opentele.taglib

import org.codehaus.groovy.grails.plugins.web.taglib.FormTagLib
import org.opentele.server.model.Schedule

class ScheduleTagLib {
	static namespace = "schedule"
    FormTagLib formTagLib = new FormTagLib()

	def humanReadableSchedule = { attrs ->
        Schedule schedule = attrs.remove('schedule')
        out << '<span '
        formTagLib.outputAttributes(attrs, out)
        out << '>'
        switch (schedule?.type) {
            case null:
                outputNoCurrentVersion()
                break
            case Schedule.ScheduleType.UNSCHEDULED:
                outputNoSchedule()
                break
            case Schedule.ScheduleType.MONTHLY:
                outputTime(schedule)
                outputMonthlySchedule(schedule)
                break
            case Schedule.ScheduleType.WEEKDAYS:
                outputTime(schedule)
                outputWeeklySchedule(schedule)
                break
            case Schedule.ScheduleType.EVERY_NTH_DAY:
                outputTime(schedule)
                outputNthDaySchedule(schedule)
                break
            case Schedule.ScheduleType.SPECIFIC_DATE:
                outputTime(schedule)
                outputSpecificDateSchedule(schedule)
                break
            default:
                throw new IllegalArgumentException("Unknown schedule type: '${schedule.type}'")
        }
        if(tooltip) {
            out << '</span>'
        }
	}

    private def outputNthDaySchedule(Schedule schedule) {
        out << g.message(code: message(code:"schedule.nthdayschedule"), args: [schedule.dayInterval])
    }

    private def outputSpecificDateSchedule(Schedule schedule) {
        def date = g.formatDate(date: schedule.specificDate.calendar.getTime(), format:  "dd/MM/yyyy")
        out << g.message(code: message(code:"schedule.specificdateschedule"), args: [date])
    }

    private void outputWeeklySchedule(Schedule schedule) {
        def sortedWeekdays = schedule.weekdays.sort { it.ordinal() }
        def daysString = sortedWeekdays.collect { g.message(code: "enum.weekday.short.${it}") }.join(', ')

        out << daysString
    }

    private void outputMonthlySchedule(Schedule schedule) {
        def days = schedule.daysInMonth.join('., ') + '.'
        out << g.message(code: 'schedule.monthlySchedule', args: [days])
    }

    private void outputNoSchedule() {
        out << g.message(code: 'schedule.scheduleType.UNSCHEDULED.label')
    }

    private void outputNoCurrentVersion() {
        out << g.message(code: 'questionnaireSchedule.noCurrentVersion.label')
    }

    private void outputTime(Schedule schedule) {
        schedule.timesOfDay.each {
            def minute = toHumanReadableTime(it.minute)
            def hour = toHumanReadableTime(it.hour)

            out << hour
            out << ":"
            out << minute
            out << " "
        }
    }

    private String toHumanReadableTime(int time) {
        def timeString = time.toString()
        if (timeString.length() == 1) {
            timeString = "0" + time
        }
        timeString
    }
}
