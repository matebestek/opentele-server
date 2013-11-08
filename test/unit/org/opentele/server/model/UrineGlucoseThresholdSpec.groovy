package org.opentele.server.model

import grails.test.mixin.TestFor
import org.opentele.server.model.types.MeasurementTypeName
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static org.opentele.server.model.types.GlucoseInUrineValue.*

@TestFor(UrineGlucoseThreshold)
class UrineGlucoseThresholdSpec extends Specification {
    @Shared
    def type = new MeasurementType(MeasurementTypeName.URINE)

    def setup() {
        mockForConstraintsTests(UrineGlucoseThreshold)
    }

    @Unroll
    def "when a UrineGlucoseThreshold contains valid data, it can validate without errors"() {
        given:
        def threshold = new UrineGlucoseThreshold(type: type, alertHigh: alertHigh, warningHigh: warningHigh, warningLow: warningLow, alertLow: alertLow)

        when:
        threshold.validate()

        then:
        !threshold.hasErrors()

        where:
        alertHigh | warningHigh | warningLow | alertLow
        null      | null        | null       | null
        PLUS_FOUR | PLUS_THREE  | PLUS_TWO   | NEGATIVE
        null      | PLUS_THREE  | PLUS_TWO   | NEGATIVE
        PLUS_FOUR | null        | PLUS_TWO   | NEGATIVE
        PLUS_FOUR | PLUS_THREE  | null       | NEGATIVE
        PLUS_FOUR | PLUS_THREE  | PLUS_TWO   | null
        PLUS_FOUR | PLUS_THREE  | null       | null
        PLUS_FOUR | null        | null       | null
        null      | null        | PLUS_TWO   | NEGATIVE
        null      | null        | null       | NEGATIVE
        null      | PLUS_THREE  | null       | NEGATIVE
        PLUS_FOUR | null        | null       | NEGATIVE
    }

    @Unroll
    def "when a UrineGlucoseThreshold contains invalid data, it cannot validate"() {
        given:
        def threshold = new UrineGlucoseThreshold(type: type, alertHigh: alertHigh, warningHigh: warningHigh, warningLow: warningLow, alertLow: alertLow)

        when:
        threshold.validate()

        then:
        threshold.hasErrors()
        threshold.errors.allErrors.size() == 1
        threshold.errors[field] == expectedError

        where:
        alertHigh  | warningHigh | warningLow | alertLow | field         | expectedError
        PLUS_THREE | PLUS_FOUR   | PLUS_THREE | PLUS_ONE | "alertHigh"   | "validator"
        PLUS_FOUR  | NEGATIVE    | PLUS_THREE | PLUS_ONE | "warningHigh" | "validator"
        PLUS_FOUR  | PLUS_THREE  | NEGATIVE   | PLUS_ONE | "warningLow"  | "validator"
    }
}
