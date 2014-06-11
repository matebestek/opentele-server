package org.opentele.server.model

class ContinuousBloodSugarMeasurement extends AbstractObject {
    Measurement measurement
    long recordNumber
    Date time
    double value

    static constraints = {
        measurement(nullable: false)
        recordNumber(nullable: false)
        time(nullable: false)
        value(nullable: false)
    }
}
