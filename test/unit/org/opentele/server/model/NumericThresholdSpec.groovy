package org.opentele.server.model
import grails.test.mixin.TestFor
import org.opentele.server.model.types.MeasurementTypeName
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(NumericThreshold)
class NumericThresholdSpec extends Specification {
    @Shared
    def type = new MeasurementType(MeasurementTypeName.PULSE)

    def setup() {
        mockForConstraintsTests(NumericThreshold)
    }

    @Unroll
    def "when a NumericThreshold contains valid data, it can validate without errors"() {
        given:
        def threshold = new NumericThreshold(type: type, alertHigh: alertHigh, warningHigh: warningHigh, warningLow: warningLow, alertLow: alertLow)

        when:
        threshold.validate()

        then:
        !threshold.hasErrors()

        where:
        alertHigh | warningHigh | warningLow | alertLow
        null      | null        | null       | null
        11        | 10          | 9          | 8
        null      | 10          | 9          | 8
        11        | null        | 9          | 8
        11        | 10          | null       | 8
        11        | 10          | 9          | null
        11        | 10          | null       | null
        11        | null        | null       | null
        null      | null        | 9          | 8
        null      | null        | null       | 8
        null      | 10          | null       | 8
        11        | null        | null       | 8
    }

    @Unroll
    def "when a NumericThreshold contains invalid data, it cannot validate"() {
        given:
        def threshold = new NumericThreshold(type: type, alertHigh: alertHigh, warningHigh: warningHigh, warningLow: warningLow, alertLow: alertLow)

        when:
        threshold.validate()

        then:
        threshold.hasErrors()
        threshold.errors.allErrors.size() == 1
        threshold.errors[field] == expectedError

        where:
        alertHigh | warningHigh | warningLow | alertLow | field         | expectedError
        9         | 10          | 9          | 8        | "alertHigh"   | "validator"
        -1        | null        | null       | null     | "alertHigh"   | "min"
        10        | 7           | 9          | 8        | "warningHigh" | "validator"
        10        | -1          | null       | null     | "warningHigh" | "min"
        10        | 9           | 7          | 8        | "warningLow"  | "validator"
        10        | 9           | -1         | null     | "warningLow"  | "min"
        10        | 9           | 7          | -1       | "alertLow"    | "min"
    }
}
