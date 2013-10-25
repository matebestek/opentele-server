package org.opentele.server.measurement

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.junit.Before
import org.opentele.server.model.*
import org.opentele.server.model.types.GlucoseInUrineValue
import org.opentele.server.model.types.MeasurementTypeName
import org.opentele.server.model.types.ProteinValue
import org.opentele.server.util.AboveWarningThresholdPredicate

/**
 * Doesn't test as much as AboveAlertThresholdPredicateTests, since the two classes share most of the functionality
 * through the base class.
 */
@TestMixin(GrailsUnitTestMixin)
class AboveWarningThresholdPredicateNullTests {
    Patient patient = new Patient();

    @Before
    void setUp() {
        // Everything is null, thus there are NO measurements that are above ANY thresholds
        patient.thresholds = new ArrayList<Threshold>();
        patient.thresholds.add(new BloodPressureThreshold(type: new MeasurementType(MeasurementTypeName.BLOOD_PRESSURE), diastolicAlertHigh: null, diastolicAlertLow: null, systolicAlertHigh:null,	systolicAlertLow: null, diastolicWarningHigh: null, diastolicWarningLow: null, systolicWarningHigh:null, systolicWarningLow: null));
        patient.thresholds.add(new NumericThreshold(type:  new MeasurementType(MeasurementTypeName.TEMPERATURE), warningHigh: null, warningLow: null));
        patient.thresholds.add(new UrineThreshold(type:  new MeasurementType(MeasurementTypeName.URINE), warningHigh: null, warningLow: null));
        patient.thresholds.add(new UrineThreshold(type:  new MeasurementType(MeasurementTypeName.URINE_GLUCOSE), warningHigh: null, warningLow: null));
    }

    void testMeasurementBloodPressureIsNotAboveWhenNull() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.BLOOD_PRESSURE), systolic: 190, diastolic: 60)
        assert !AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    void testMeasurementNumericIsNotAboveWhenNull() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.TEMPERATURE), value: 40)
        assert !AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    void testMeasurementProteinIsNotAboveWhenNull() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE), protein: ProteinValue.PLUS_THREE)
        assert !AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    void testMeasurementUrineGlucoseIsNotAboveWhenNull() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE_GLUCOSE), protein: GlucoseInUrineValue.PLUS_THREE)
        assert !AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    private MeasurementType createType(MeasurementTypeName typeName) {
        new MeasurementType(name: typeName)
    }
}
