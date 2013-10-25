package org.opentele.server.measurement

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.junit.Before
import org.opentele.server.model.BloodPressureThreshold
import org.opentele.server.model.Measurement
import org.opentele.server.model.MeasurementType
import org.opentele.server.model.NumericThreshold
import org.opentele.server.model.Patient
import org.opentele.server.model.Threshold
import org.opentele.server.model.UrineGlucoseThreshold
import org.opentele.server.model.UrineThreshold
import org.opentele.server.model.types.GlucoseInUrineValue
import org.opentele.server.model.types.MeasurementTypeName
import org.opentele.server.model.types.ProteinValue
import org.opentele.server.util.AboveAlertThresholdPredicate

@TestMixin(GrailsUnitTestMixin)
class AboveAlertThresholdPredicateTests {
    Patient patient = new Patient();

    @Before
    void setUp() {
        patient.thresholds = new ArrayList<Threshold>();
        patient.thresholds.add(new BloodPressureThreshold(type: new MeasurementType(MeasurementTypeName.BLOOD_PRESSURE),	diastolicAlertHigh: 100, diastolicAlertLow: 40, systolicAlertHigh: 180,	systolicAlertLow: 80));
        patient.thresholds.add(new NumericThreshold(type:  new MeasurementType(MeasurementTypeName.TEMPERATURE), alertHigh: 39, alertLow: 36));
        patient.thresholds.add(new UrineThreshold(type:  new MeasurementType(MeasurementTypeName.URINE), alertHigh: ProteinValue.PLUS_TWO, alertLow: ProteinValue.NEGATIVE));
        patient.thresholds.add(new UrineGlucoseThreshold(type:  new MeasurementType(MeasurementTypeName.URINE_GLUCOSE), alertHigh: GlucoseInUrineValue.PLUS_TWO, alertLow: GlucoseInUrineValue.NEGATIVE));
    }

    void testKnowsWhenSystolicPressureIsAboveAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.BLOOD_PRESSURE), systolic: 190, diastolic: 60)
        assert AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenSystolicPressureIsWithinAlertThresholds() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.BLOOD_PRESSURE), systolic: 175, diastolic: 60)
        assert !AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenSystolicPressureIsBelowAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.BLOOD_PRESSURE), systolic: 75, diastolic: 60)
        assert AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenDiastolicPressureIsAboveAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.BLOOD_PRESSURE), systolic: 120, diastolic: 110)
        assert AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenDiastolicPressureIsWithinAlertThresholds() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.BLOOD_PRESSURE), systolic: 120, diastolic: 95)
        assert !AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenDiastolicPressureIsBelowAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.BLOOD_PRESSURE), systolic: 120, diastolic: 35)
        assert AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsNothingAboutCtgAndThusReturnsFalse() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.CTG), systolic: 120, diastolic: 35)
        assert !AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenTemperatureIsAboveAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.TEMPERATURE), value: 40)
        assert AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenTemperatureIsWithinAlertThresholds() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.TEMPERATURE), value: 37)
        assert !AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenTemperatureIsBelowAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.TEMPERATURE), value: 35.9)
        assert AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testLetsMissingTemperatureBeWithinAlertThresholds() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.TEMPERATURE))
        assert !AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenUrineIsAboveAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE), protein: ProteinValue.PLUS_THREE)
        assert AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenUrineIsWithinAlertThresholds() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE), protein: ProteinValue.PLUS_ONE)
        assert !AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenUrineIsBelowAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE), protein: ProteinValue.NEGATIVE)
        assert AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testLetsMissingUrineMeasurementBeWithinAlertThresholds() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE))
        assert !AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenUrineGlucoseIsAboveAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE_GLUCOSE), glucoseInUrine: GlucoseInUrineValue.PLUS_THREE)
        assert AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenUrineGlucoseIsWithinAlertThresholds() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE_GLUCOSE), glucoseInUrine: GlucoseInUrineValue.PLUS_ONE)
        assert !AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testKnowsWhenUrineGlucoseIsBelowAlertThreshold() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE_GLUCOSE), glucoseInUrine: GlucoseInUrineValue.NEGATIVE)
        assert AboveAlertThresholdPredicate.isTrueFor(measurement)
    }

    void testLetsMissingUrineGlucoseMeasurementBeWithinAlertThresholds() {
        def measurement = new Measurement(patient: patient, measurementType: createType(MeasurementTypeName.URINE_GLUCOSE))
        assert !AboveAlertThresholdPredicate.isTrueFor(measurement)
    }



    private MeasurementType createType(MeasurementTypeName typeName) {
        new MeasurementType(name: typeName)
    }
}
