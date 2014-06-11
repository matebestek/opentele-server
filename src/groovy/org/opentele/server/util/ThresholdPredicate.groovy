package org.opentele.server.util

import org.opentele.server.model.BloodPressureThreshold
import org.opentele.server.model.Measurement
import org.opentele.server.model.NumericThreshold
import org.opentele.server.model.Patient
import org.opentele.server.model.UrineThreshold
import org.opentele.server.model.types.MeasurementTypeName
import org.opentele.server.model.types.MeasurementTypeNameVisitor
import org.opentele.server.model.types.ProteinValue

abstract class ThresholdPredicate implements MeasurementTypeNameVisitor {
    protected Patient patient
    protected Measurement measurement
    protected boolean result

    protected abstract Float high(NumericThreshold threshold)
    protected abstract Float low(NumericThreshold threshold)
    protected abstract ProteinValue high(UrineThreshold threshold)
    protected abstract ProteinValue low(UrineThreshold threshold)
    //Multiple return values [systolicHigh, diastolicHigh]
    protected abstract high(BloodPressureThreshold threshold)
    //Multiple return values [systolicLow, diastolicLow]
    protected abstract low(BloodPressureThreshold threshold)

    @Override
    void visitBloodPressure() {
        def threshold = patient.getThreshold(MeasurementTypeName.BLOOD_PRESSURE)

        if (threshold == null) {
            result = false
        } else {
            def (Double systolic, Double diastolic) = [measurement.systolic, measurement.diastolic]


            def (Float systolicTH, Float diastolicTH) = high(threshold)
            def isAboveSystolicThreshold = systolicTH && (systolic > systolicTH)
            def isAboveDiastolicThreshold = diastolicTH && (diastolic > diastolicTH)

            def (Float systolicTHLow, Float diastolicTHLow) = low(threshold)
            def isBelowSystolicThreshold = systolicTHLow && (systolic < systolicTHLow)
            def isBelowDiastolicThreshold = diastolicTHLow && (diastolic < diastolicTHLow)

            result = isAboveSystolicThreshold || isBelowSystolicThreshold || isAboveDiastolicThreshold || isBelowDiastolicThreshold

        }
    }

    @Override
    void visitCtg() {
        // Not applicable to CTG
        result = false
    }

    @Override
    void visitTemperature() {
        visitNumericMeasurement(patient.getThreshold(MeasurementTypeName.TEMPERATURE))
    }

    @Override
    void visitUrine() {
        def threshold = patient.getThreshold(MeasurementTypeName.URINE)

        if (threshold == null) {
            result = false
        } else {
            def protein = measurement.protein
            if (protein) {
                def isAboveUrineThreshold = high(threshold) != null ? protein.ordinal() >= high(threshold).ordinal() : false
                def isBelowUrineThreshold = low(threshold) != null ? protein.ordinal() <= low(threshold).ordinal() : false
                result = isAboveUrineThreshold || isBelowUrineThreshold
            } else {
                result = false
            }
        }
    }

    @Override
    void visitPulse() {
        visitNumericMeasurement(patient.getThreshold(MeasurementTypeName.PULSE))
    }

    @Override
    void visitWeight() {
        visitNumericMeasurement(patient.getThreshold(MeasurementTypeName.WEIGHT))
    }

    @Override
    void visitHemoglobin() {
        visitNumericMeasurement(patient.getThreshold(MeasurementTypeName.HEMOGLOBIN))
    }

    @Override
    void visitSaturation() {
        visitNumericMeasurement(patient.getThreshold(MeasurementTypeName.SATURATION))
    }

    @Override
    void visitCrp() {
        visitNumericMeasurement(patient.getThreshold(MeasurementTypeName.CRP))
    }

    @Override
    void visitBloodSugar() {
        visitNumericMeasurement(patient.getThreshold(MeasurementTypeName.BLOODSUGAR))
    }

    @Override
    void visitUrineGlucose() {
        def threshold = patient.getThreshold(MeasurementTypeName.URINE_GLUCOSE)

        if (threshold == null) {
            result = false
        } else {
            def glucose = measurement.glucoseInUrine
            if (glucose) {
                def isAboveUrineGlucoseThreshold = high(threshold) != null ? glucose.ordinal() >= high(threshold).ordinal() : false
                def isBelowUrineGlucoseThreshold = low(threshold) != null ? glucose.ordinal() <= low(threshold).ordinal() : false
                result = isAboveUrineGlucoseThreshold || isBelowUrineGlucoseThreshold
            } else {
                result = false
            }
        }
    }

    @Override
    void visitLungFunction() {
        visitNumericMeasurement(patient.getThreshold(MeasurementTypeName.LUNG_FUNCTION))
    }

    @Override
    void visitContinuousBloodSugarMeasurement() {
        // Not applicable to continuous blood sugar measurements
        result = false
    }

    private void visitNumericMeasurement(NumericThreshold threshold) {
        if (threshold == null) {
            result = false
        } else {
            Double value = measurement.value
            if (value != null) {
                def isAboveThreshold = high(threshold) != null ? value > high(threshold) : false
                def isBelowThreshold = low(threshold) != null ? value < low(threshold) : false
                result = isAboveThreshold || isBelowThreshold
            } else {
                result = false
            }
        }
    }
}
