package org.opentele.server.model.types

public enum MeasurementFilterType {
    WEEK(true),
    MONTH(true),
    QUARTER(true),
    YEAR(true),
    CUSTOM(true),
    ALL(false);

    boolean isLimited

    private MeasurementFilterType(boolean isLimited) {
        this.isLimited = isLimited
    }

    static valueOf(String type) {
        if (type != null) {
            MeasurementFilterType.valueOf(MeasurementFilterType.class, type)
        } else {
            ALL;
        }
    }
}