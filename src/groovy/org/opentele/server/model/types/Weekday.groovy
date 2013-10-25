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

    int asCalendarWeekday() {
        return calendarWeekday
    }
}
