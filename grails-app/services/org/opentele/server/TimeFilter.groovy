package org.opentele.server

import org.opentele.server.model.types.MeasurementFilterType

class TimeFilter {
    MeasurementFilterType type
    Date start, end
    boolean isLimited

    static fromParams(params) {
        new TimeFilter(MeasurementFilterType.valueOf(params.filter), params.start, params.end)
    }

    static lastMonth() {
        new TimeFilter(MeasurementFilterType.MONTH, null, null)
    }

    static all() {
        new TimeFilter(MeasurementFilterType.ALL, null, null)
    }

    private TimeFilter(type, start, end) {
        this.type = type

        def startOfInterval = Calendar.getInstance()
        def endOfInterval = Calendar.getInstance()

        switch (type) {
            case MeasurementFilterType.WEEK:
                startOfInterval.add(Calendar.WEEK_OF_YEAR, -1)
                break;
            case MeasurementFilterType.MONTH:
                startOfInterval.add(Calendar.MONTH, -1)
                break;
            case MeasurementFilterType.QUARTER:
                startOfInterval.add(Calendar.MONTH, -3)
                break;
            case MeasurementFilterType.YEAR:
                startOfInterval.add(Calendar.YEAR, -1)
                break;
            case MeasurementFilterType.CUSTOM:
                startOfInterval.setTime(start)
                endOfInterval.setTime(end)
                break;
            case MeasurementFilterType.ALL:
                break;
            default:
                throw new IllegalArgumentException("Unknown filter type: '${type}'")
        }

        startOfInterval.set(Calendar.HOUR_OF_DAY, 0)
        startOfInterval.set(Calendar.MINUTE, 0)
        startOfInterval.set(Calendar.SECOND, 0)

        endOfInterval.set(Calendar.HOUR_OF_DAY, 24)
        endOfInterval.set(Calendar.MINUTE, 0)
        endOfInterval.set(Calendar.SECOND, 0)

        this.start = startOfInterval.getTime()
        this.end = endOfInterval.getTime()
        this.isLimited = type.isLimited
    }
}
