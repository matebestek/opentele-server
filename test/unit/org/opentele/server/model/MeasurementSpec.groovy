package org.opentele.server.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.opentele.server.model.patientquestionnaire.MeasurementNodeResult
import org.opentele.server.model.types.MeasurementTypeName
import org.opentele.server.model.types.Unit
import spock.lang.Specification

@TestFor(Measurement)
@Mock(Measurement)
@Build([Patient, MeasurementNodeResult, Conference, MeasurementType])
class MeasurementSpec extends Specification {
    Patient patient
    MeasurementType measurementType

    def setup() {
        measurementType = new MeasurementType(name: MeasurementTypeName.LUNG_FUNCTION)
        patient = Patient.build()
        [measurementType, patient]*.save(failOnError: true)
    }

    def 'can be placed on a MeasurementNodeResult'() {
        setup:
        def measurementNodeResult = MeasurementNodeResult.build()
        measurementNodeResult.save(failOnError: true)

        when:
        def measurement = new Measurement(patient: patient,
                measurementType: measurementType,
                unit: Unit.LITER,
                time: new Date(),
                value: 3.65)
        measurement.measurementNodeResult = measurementNodeResult
        measurement.save(failOnError: true)

        then:
        measurementNodeResult.measurements.asList() == [measurement]
    }

    def 'can be placed on a Conference'() {
        setup:
        def conference = Conference.build()
        conference.save(failOnError: true)

        when:
        def measurement = new Measurement(patient: patient,
                measurementType: measurementType,
                unit: Unit.LITER,
                time: new Date(),
                value: 3.65)
        measurement.conference = conference
        measurement.save(failOnError: true)

        then:
        conference.measurements.asList() == [measurement]
    }
}
