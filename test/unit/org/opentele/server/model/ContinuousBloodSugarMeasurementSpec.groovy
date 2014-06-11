package org.opentele.server.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import org.opentele.server.model.types.MeasurementTypeName
import spock.lang.Specification

@TestFor(ContinuousBloodSugarMeasurement)
@Build([Measurement, MeasurementType, ContinuousBloodSugarMeasurement])
class ContinuousBloodSugarMeasurementSpec extends Specification {
    Measurement measurement

    def setup() {
        measurement = Measurement.build(measurementType: MeasurementType.build(name: MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT))
    }

    def 'requires a measurement'() {
        when:
        ContinuousBloodSugarMeasurement continuousBloodSugarMeasurement = new ContinuousBloodSugarMeasurement(time: new Date(), value: 5)

        then:
        !continuousBloodSugarMeasurement.validate()
    }

    def 'requires a time'() {
        when:
        ContinuousBloodSugarMeasurement continuousBloodSugarMeasurement = new ContinuousBloodSugarMeasurement(measurement: measurement, value: 5)

        then:
        !continuousBloodSugarMeasurement.validate()
    }

    def 'can be created with measurement, record ID, time, and value'() {
        when:
        ContinuousBloodSugarMeasurement continuousBloodSugarMeasurement = new ContinuousBloodSugarMeasurement(measurement: measurement, recordNumber: 2000, time: new Date(), value: 5)

        then:
        continuousBloodSugarMeasurement.validate()
    }

    def 'can be found through measurement'() {
        when:
        10.times {
            measurement.addToContinuousBloodPressureMeasurements(recordNumber: it, time: new Date(), value: 5)
        }

        then:
        measurement.continuousBloodPressureMeasurements.size() == 10
    }
}
