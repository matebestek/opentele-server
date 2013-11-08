package org.opentele.server.model
import grails.test.mixin.TestFor
import org.opentele.server.model.types.MeasurementTypeName
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(BloodPressureThreshold)
class BloodPressureThresholdSpec extends Specification {
    @Shared
    def type = new MeasurementType(MeasurementTypeName.BLOOD_PRESSURE)

    def setup() {
        mockForConstraintsTests(BloodPressureThreshold)
    }

    @Unroll
    def "when a BloodPressureThreshold contains valid data, it can validate without errors"() {
        given:
        def threshold = new BloodPressureThreshold(type: type,
                diastolicAlertHigh: alertHigh, diastolicWarningHigh: warningHigh, diastolicWarningLow: warningLow, diastolicAlertLow: alertLow,
                systolicAlertHigh: alertHigh, systolicWarningHigh: warningHigh, systolicWarningLow: warningLow, systolicAlertLow: alertLow)

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
    def "when a BloodPressureThreshold diastolic contains invalid data, it cannot validate"() {
        given:
        def threshold = new BloodPressureThreshold(type: type,
                diastolicAlertHigh: alertHigh, diastolicWarningHigh: warningHigh, diastolicWarningLow: warningLow, diastolicAlertLow: alertLow)

        when:
        threshold.validate()

        then:
        threshold.hasErrors()
        threshold.errors.allErrors.size() == 1
        threshold.errors[field] == expectedError

        where:
        alertHigh | warningHigh | warningLow | alertLow | field         | expectedError
        9         | 10          | 9          | 8        | "diastolicAlertHigh"   | "validator"
        -1        | null        | null       | null     | "diastolicAlertHigh"   | "min"
        10        | 7           | 9          | 8        | "diastolicWarningHigh" | "validator"
        10        | -1          | null       | null     | "diastolicWarningHigh" | "min"
        10        | 9           | 7          | 8        | "diastolicWarningLow"  | "validator"
        10        | 9           | -1         | null     | "diastolicWarningLow"  | "min"
        10        | 9           | 7          | -1       | "diastolicAlertLow"    | "min"
    }

    @Unroll
    def "when a BloodPressureThreshold systolic contains invalid data, it cannot validate"() {
        given:
        def threshold = new BloodPressureThreshold(type: type,
                systolicAlertHigh: alertHigh, systolicWarningHigh: warningHigh, systolicWarningLow: warningLow, systolicAlertLow: alertLow)

        when:
        threshold.validate()

        then:
        threshold.hasErrors()
        threshold.errors.allErrors.size() == 1
        threshold.errors[field] == expectedError

        where:
        alertHigh | warningHigh | warningLow | alertLow | field         | expectedError
        9         | 10          | 9          | 8        | "systolicAlertHigh"   | "validator"
        -1        | null        | null       | null     | "systolicAlertHigh"   | "min"
        10        | 7           | 9          | 8        | "systolicWarningHigh" | "validator"
        10        | -1          | null       | null     | "systolicWarningHigh" | "min"
        10        | 9           | 7          | 8        | "systolicWarningLow"  | "validator"
        10        | 9           | -1         | null     | "systolicWarningLow"  | "min"
        10        | 9           | 7          | -1       | "systolicAlertLow"    | "min"
    }
}
