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
class AboveWarningThresholdPredicateTests {
    Patient patient = new Patient();

    @Before
    void setUp() {
        patient.thresholds = new ArrayList<Threshold>();
        patient.thresholds.add(new BloodPressureThreshold(type: new MeasurementType(MeasurementTypeName.BLOOD_PRESSURE),	diastolicWarningHigh: 100, diastolicWarningLow: 40, systolicWarningHigh: 180,	systolicWarningLow: 80));
        patient.thresholds.add(new NumericThreshold(type:  new MeasurementType(MeasurementTypeName.TEMPERATURE), warningHigh: 39, warningLow: 36));
        patient.thresholds.add(new UrineThreshold(type:  new MeasurementType(MeasurementTypeName.URINE), warningHigh: ProteinValue.PLUS_TWO, warningLow: ProteinValue.NEGATIVE));
        patient.thresholds.add(new UrineGlucoseThreshold(type:  new MeasurementType(MeasurementTypeName.URINE_GLUCOSE), warningHigh: GlucoseInUrineValue.PLUS_TWO, warningLow: GlucoseInUrineValue.NEGATIVE));

    }

    void testKnowsWhenSystolicPressureIsAboveWarningThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.BLOOD_PRESSURE), systolic: 190, diastolic: 60)
        assert AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenSystolicPressureIsWithinAlertThresholds() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.BLOOD_PRESSURE), systolic: 175, diastolic: 60)
        assert !AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenSystolicPressureIsBelowAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.BLOOD_PRESSURE), systolic: 75, diastolic: 60)
        assert AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenDiastolicPressureIsAboveAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.BLOOD_PRESSURE), systolic: 120, diastolic: 110)
        assert AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenDiastolicPressureIsWithinAlertThresholds() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.BLOOD_PRESSURE), systolic: 120, diastolic: 95)
        assert !AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenDiastolicPressureIsBelowAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.BLOOD_PRESSURE), systolic: 120, diastolic: 35)
        assert AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenTemperatureIsAboveAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.TEMPERATURE), value: 40)
        assert AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenTemperatureIsWithinAlertThresholds() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.TEMPERATURE), value: 37)
        assert !AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenTemperatureIsBelowAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.TEMPERATURE), value: 35.9)
        assert AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenUrineIsAboveAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE), protein: ProteinValue.PLUS_THREE)
        assert AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenUrineIsWithinAlertThresholds() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE), protein: ProteinValue.PLUS_ONE)
        assert !AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenUrineIsBelowAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE), protein: ProteinValue.NEGATIVE)
        assert AboveWarningThresholdPredicate.isTrueFor(measurement)
    }


    void testKnowsWhenUrineGlucoseIsAboveAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE_GLUCOSE), glucoseInUrine: GlucoseInUrineValue.PLUS_THREE)
        assert AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenUrineGlucoseIsWithinAlertThresholds() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE_GLUCOSE), glucoseInUrine: GlucoseInUrineValue.PLUS_ONE)
        assert !AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenUrineGlucoseIsBelowAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE_GLUCOSE), glucoseInUrine: GlucoseInUrineValue.NEGATIVE)
        assert AboveWarningThresholdPredicate.isTrueFor(measurement)
    }

    MeasurementType createType(MeasurementTypeName typeName) {
        new MeasurementType(name: typeName)
    }
}
