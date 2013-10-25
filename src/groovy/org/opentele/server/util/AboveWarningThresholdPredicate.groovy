package org.opentele.server.util

import org.opentele.server.model.BloodPressureThreshold
import org.opentele.server.model.Measurement
import org.opentele.server.model.NumericThreshold
import org.opentele.server.model.UrineGlucoseThreshold
import org.opentele.server.model.UrineThreshold
import org.opentele.server.model.types.GlucoseInUrineValue
import org.opentele.server.model.types.ProteinValue

class AboveWarningThresholdPredicate extends ThresholdPredicate {
    static boolean isTrueFor(Measurement measurement) {
        def visitor = new AboveWarningThresholdPredicate(patient: measurement.patient, measurement: measurement)
        measurement.measurementType.name.visit(visitor)
        visitor.result
    }

    @Override
    Float high(NumericThreshold threshold) {
        threshold.warningHigh
    }

    @Override
    Float low(NumericThreshold threshold) {
        threshold.warningLow
    }

    @Override
    ProteinValue high(UrineThreshold threshold) {
        threshold.warningHigh
    }

    @Override
    ProteinValue low(UrineThreshold threshold) {
        threshold.warningLow
    }

    @Override
    GlucoseInUrineValue high(UrineGlucoseThreshold threshold) {
        threshold.warningHigh
    }

    @Override
    GlucoseInUrineValue low(UrineGlucoseThreshold threshold) {
        threshold.warningLow
    }

    @Override
    List<Float> high(BloodPressureThreshold threshold) {
        [threshold.systolicWarningHigh, threshold.diastolicWarningHigh]
    }

    @Override
    List<Float> low(BloodPressureThreshold threshold) {
        [threshold.systolicWarningLow, threshold.diastolicWarningLow]
    }
}
