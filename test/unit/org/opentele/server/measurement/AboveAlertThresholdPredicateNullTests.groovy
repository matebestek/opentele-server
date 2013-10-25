package org.opentele.server.measurement

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.junit.Before
import org.opentele.server.model.*
import org.opentele.server.model.types.GlucoseInUrineValue
import org.opentele.server.model.types.MeasurementTypeName
import org.opentele.server.model.types.ProteinValue
import org.opentele.server.util.AboveAlertThresholdPredicate

@TestMixin(GrailsUnitTestMixin)
class AboveAlertThresholdPredicateNullTests {
    Patient patient = new Patient();

    @Before
    void setUp() {
        // Everything is null, thus there are NO measurements that are above ANY thresholds
        patient.thresholds = new ArrayList<Threshold>();
        patient.thresholds.add(new BloodPressureThreshold(type: new MeasurementType(MeasurementTypeName.BLOOD_PRESSURE),	diastolicAlertHigh: null, diastolicAlertLow: null, systolicAlertHigh:null,	systolicAlertLow: null, diastolicWarningHigh: null, diastolicWarningLow: null, systolicWarningHigh:null, systolicWarningLow: null));
        patient.thresholds.add(new NumericThreshold(type:  new MeasurementType(MeasurementTypeName.TEMPERATURE), alertHigh: null, alertLow: null));
        patient.thresholds.add(new UrineThreshold(type:  new MeasurementType(MeasurementTypeName.URINE), alertHigh: null, alertLow: null));
        patient.thresholds.add(new UrineThreshold(type:  new MeasurementType(MeasurementTypeName.URINE_GLUCOSE), alertHigh: null, alertLow: null));
    }

    void testMeasurementBloodPressureIsNotBelowWhenNull() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.BLOOD_PRESSURE), systolic: 190, diastolic: 60)
        assert !AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testMeasurementNumericIsNotBelowWhenNull() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.TEMPERATURE), value: 35.9)
        assert !AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenProteinIsAboveAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE), protein: ProteinValue.PLUS_THREE)
        assert !AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenGlucoseIsAboveAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE_GLUCOSE), protein: GlucoseInUrineValue.PLUS_THREE)
        assert !AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    private MeasurementType createType(MeasurementTypeName typeName) {
        new MeasurementType(name: typeName)
    }
}
