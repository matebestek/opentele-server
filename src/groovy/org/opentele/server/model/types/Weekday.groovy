package org.opentele.server.model.types

enum Weekday {
    MONDAY(Calendar.MONDAY),
    TUESDAY(Calendar.TUESDAY),
    WEDNESDAY(Calendar.WEDNESDAY),
    THURSDAY(Calendar.THURSDAY),
    FRIDAY(Calendar.FRIDAY),
    SATURDAY(Calendar.SATURDAY),
    SUNDAY(Calendar.SUNDAY)

    private final int calendarWeekday

    Weekday(int calendarWeekday) {
        this.calendarWeekday = calendarWeekday
    }

    static String fromWeekdays(List<Weekday> weekdays) {
        (weekdays ?: []).join(',')
    }

    int asCalendarWeekday() {
        return calendarWeekday
    }

    static toWeekdays(String weekdays) {
        weekdays?.size() ? weekdays.split(',')?.collect { Weekday.valueOf(it) } : []
    }
}
