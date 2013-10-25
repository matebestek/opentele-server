package org.opentele.server.model.types

public enum Month {
    JANUARY(Calendar.JANUARY),
    FEBRUARY(Calendar.FEBRUARY),
    MARCH(Calendar.MARCH),
    APRIL(Calendar.APRIL),
    MAY(Calendar.MAY),
    JUNE(Calendar.JUNE),
    JULY(Calendar.JULY),
    AUGUST(Calendar.AUGUST),
    SEPTEMBER(Calendar.SEPTEMBER),
    OCTOBER(Calendar.OCTOBER),
    NOVEMBER(Calendar.NOVEMBER),
    DECEMBER(Calendar.DECEMBER)

    private final int calendarMonth

    Month(int calendarMonth) {
        this.calendarMonth = calendarMonth
    }

    public static Month fromCalendarMonth(int calendarMonth) {
        for (Month month : Month.values()) {
            if (month.asCalendarMonth() == calendarMonth) {
                return month
            }
        }
        throw new IllegalArgumentException("Invalid calendar month: ${calendarMonth}")
    }

    int asCalendarMonth() {
        return calendarMonth
    }
}