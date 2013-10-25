package org.opentele.server.util

import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import org.junit.Test
import org.junit.Before
import org.opentele.server.model.BloodPressureThreshold
import org.opentele.server.model.MeasurementType
import org.opentele.server.model.NumericThreshold
import org.opentele.server.model.UrineThreshold
import org.opentele.server.model.types.MeasurementTypeName

@TestMixin(DomainClassUnitTestMixin)
class ThresholdValidationUtilTest {
    NumericThreshold numericThreshold
    BloodPressureThreshold bloodPressureThreshold
    UrineThreshold urineThreshold
    ThresholdValidationUtil thresholdValidationUtil

    @Before
    void setUp() {
        mockDomain(MeasurementType)
        mockDomain(NumericThreshold)
        mockDomain(BloodPressureThreshold)
        mockDomain(UrineThreshold)

        thresholdValidationUtil = new ThresholdValidationUtil()
        numericThreshold = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.WEIGHT),
                                                    alertHigh: 10.0F, warningHigh: 9.0F,
                                                    warningLow: 8.0F, alertLow: 7.0F)

        bloodPressureThreshold = new BloodPressureThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),
                                                            diastolicAlertHigh: 10F, diastolicWarningHigh: 9F,
                                                            diastolicWarningLow: 8F, diastolicAlertLow: 7F,
                                                            systolicAlertHigh: 10F, systolicWarningHigh: 9F,
                                                            systolicWarningLow: 8F, systolicAlertLow: 7F)
    }

	@Test
    void testNumericCanValidateOK() {
        def numericProxy = ThresholdValidationUtil.thresholdValueProxy(numericThreshold)

        assert ThresholdValidationUtil.validateAlertHigh(11.0F, numericThreshold, numericProxy)
        assert ThresholdValidationUtil.validateWarningHigh(9.5F, numericThreshold, numericProxy)
        assert ThresholdValidationUtil.validateWarningLow(8.5F, numericThreshold, numericProxy)
        assert ThresholdValidationUtil.validateAlertLow(6.0F, numericThreshold, numericProxy)

        assert !numericThreshold.hasErrors()
	}

    @Test
    void testNumericCanFailWrong() {
        def numericProxy = ThresholdValidationUtil.thresholdValueProxy(numericThreshold)

        assert ThresholdValidationUtil.validateAlertHigh(9.0F, numericThreshold, numericProxy) == null
        assert ThresholdValidationUtil.validateWarningHigh(10.0F, numericThreshold, numericProxy) == null
        assert ThresholdValidationUtil.validateWarningHigh(8.0F, numericThreshold, numericProxy) == null
        assert ThresholdValidationUtil.validateWarningLow(9.5F, numericThreshold, numericProxy) == null
        assert ThresholdValidationUtil.validateWarningLow(7.0F, numericThreshold, numericProxy) == null
        assert ThresholdValidationUtil.validateAlertLow(8.0F, numericThreshold, numericProxy) == null

        assert numericThreshold.hasErrors()
    }

    @Test
    void testBloodPressureCanValidateOK() {
        def bpProxy = ThresholdValidationUtil.bloodPressureThresholdValueProxy(bloodPressureThreshold)("systolic")

        assert ThresholdValidationUtil.validateAlertHigh(11.0F, bloodPressureThreshold, bpProxy)
        assert ThresholdValidationUtil.validateWarningHigh(9.5F, bloodPressureThreshold, bpProxy)
        assert ThresholdValidationUtil.validateWarningLow(8.5F, bloodPressureThreshold, bpProxy)
        assert ThresholdValidationUtil.validateAlertLow(7.5F, bloodPressureThreshold, bpProxy)

        bpProxy = ThresholdValidationUtil.bloodPressureThresholdValueProxy(bloodPressureThreshold)("diastolic")

        assert ThresholdValidationUtil.validateAlertHigh(11.0F, bloodPressureThreshold, bpProxy)
        assert ThresholdValidationUtil.validateWarningHigh(9.5F, bloodPressureThreshold, bpProxy)
        assert ThresholdValidationUtil.validateWarningLow(8.5F, bloodPressureThreshold, bpProxy)
        assert ThresholdValidationUtil.validateAlertLow(7.5F, bloodPressureThreshold, bpProxy)

        assert !bloodPressureThreshold.hasErrors()
    }

    @Test
    void testBloodPressureCanFailWrong() {
        def bpProxy = ThresholdValidationUtil.bloodPressureThresholdValueProxy(bloodPressureThreshold)("systolic")

        assert ThresholdValidationUtil.validateAlertHigh(9.0F, numericThreshold, bpProxy) == null
        assert ThresholdValidationUtil.validateWarningHigh(10.0F, numericThreshold, bpProxy) == null
        assert ThresholdValidationUtil.validateWarningHigh(8.0F, numericThreshold, bpProxy) == null
        assert ThresholdValidationUtil.validateWarningLow(9.5F, numericThreshold, bpProxy) == null
        assert ThresholdValidationUtil.validateWarningLow(7.0F, numericThreshold, bpProxy) == null
        assert ThresholdValidationUtil.validateAlertLow(8.0F, numericThreshold, bpProxy) == null

        bpProxy = ThresholdValidationUtil.bloodPressureThresholdValueProxy(bloodPressureThreshold)("diastolic")

        assert ThresholdValidationUtil.validateAlertHigh(9.0F, numericThreshold, bpProxy) == null
        assert ThresholdValidationUtil.validateWarningHigh(10.0F, numericThreshold, bpProxy) == null
        assert ThresholdValidationUtil.validateWarningHigh(8.0F, numericThreshold, bpProxy) == null
        assert ThresholdValidationUtil.validateWarningLow(9.5F, numericThreshold, bpProxy) == null
        assert ThresholdValidationUtil.validateWarningLow(7.0F, numericThreshold, bpProxy) == null
        assert ThresholdValidationUtil.validateAlertLow(8.0F, numericThreshold, bpProxy) == null

        assert numericThreshold.hasErrors()
    }

    @Test
    void testNullIntervalCanValidateOnOK1() {
        //New numericThreshold with null values
        numericThreshold = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.WEIGHT),
                alertHigh: 10.0F, warningHigh: null,
                warningLow: 8.0F, alertLow: 7.0F)

        def numericProxy = ThresholdValidationUtil.thresholdValueProxy(numericThreshold)

        assert ThresholdValidationUtil.validateAlertHigh(11.0F, numericThreshold, numericProxy)
        assert ThresholdValidationUtil.validateWarningLow(8.5F, numericThreshold, numericProxy)
        assert ThresholdValidationUtil.validateAlertLow(6.0F, numericThreshold, numericProxy)

        assert !numericThreshold.hasErrors()
    }

    @Test
    void testNullIntervalCanFailOnWrong1() {
        //New numericThreshold with null values
        numericThreshold = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.WEIGHT),
                alertHigh: 10.0F, warningHigh: null,
                warningLow: 8.0F, alertLow: 7.0F)

        def numericProxy = ThresholdValidationUtil.thresholdValueProxy(numericThreshold)

        assert ThresholdValidationUtil.validateAlertHigh(4.0F, numericThreshold, numericProxy) == null
        assert ThresholdValidationUtil.validateWarningLow(15.5F, numericThreshold, numericProxy) == null
        assert ThresholdValidationUtil.validateWarningLow(0.5F, numericThreshold, numericProxy) == null
        assert ThresholdValidationUtil.validateAlertLow(16.0F, numericThreshold, numericProxy) == null

        assert numericThreshold.hasErrors()
    }

    @Test
    void testNullIntervalCanValidateOnOK2() {
        //New numericThreshold with null values
        numericThreshold = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.WEIGHT),
                alertHigh: 10.0F, warningHigh: 9.0F,
                warningLow: null, alertLow: 7.0F)

        def numericProxy = ThresholdValidationUtil.thresholdValueProxy(numericThreshold)

        assert ThresholdValidationUtil.validateAlertHigh(11.0F, numericThreshold, numericProxy)
        assert ThresholdValidationUtil.validateWarningHigh(8.5F, numericThreshold, numericProxy)
        assert ThresholdValidationUtil.validateAlertLow(6.0F, numericThreshold, numericProxy)

        assert !numericThreshold.hasErrors()
    }

    @Test
    void testNullIntervalCanFailOnWrong2() {
        //New numericThreshold with null values
        numericThreshold = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.WEIGHT),
                alertHigh: 10.0F, warningHigh: 9.0F,
                warningLow: null, alertLow: 7.0F)

        def numericProxy = ThresholdValidationUtil.thresholdValueProxy(numericThreshold)

        assert ThresholdValidationUtil.validateAlertHigh(4.0F, numericThreshold, numericProxy) == null
        assert ThresholdValidationUtil.validateWarningLow(15.5F, numericThreshold, numericProxy) == null
        assert ThresholdValidationUtil.validateWarningLow(0.5F, numericThreshold, numericProxy) == null
        assert ThresholdValidationUtil.validateAlertLow(16.0F, numericThreshold, numericProxy) == null

        assert numericThreshold.hasErrors()
    }


}
