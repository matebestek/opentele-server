package org.opentele.server.util

import org.opentele.server.model.BloodPressureThreshold
import org.opentele.server.model.Measurement
import org.opentele.server.model.NumericThreshold
import org.opentele.server.model.UrineGlucoseThreshold
import org.opentele.server.model.UrineThreshold
import org.opentele.server.model.types.GlucoseInUrineValue
import org.opentele.server.model.types.ProteinValue

class AboveAlertThresholdPredicate extends ThresholdPredicate {
    static boolean isTrueFor(Measurement measurement) {
        def visitor = new AboveAlertThresholdPredicate(patient: measurement.patient, measurement: measurement)
        measurement.measurementType.name.visit(visitor)
        visitor.result
    }

    @Override
    Float high(NumericThreshold threshold) {
        threshold.alertHigh
    }

    @Override
    Float low(NumericThreshold threshold) {
        threshold.alertLow
    }

    @Override
    ProteinValue high(UrineThreshold threshold) {
        threshold.alertHigh
    }

    @Override
    ProteinValue low(UrineThreshold threshold) {
        threshold.alertLow
    }

    @Override
    GlucoseInUrineValue high(UrineGlucoseThreshold threshold) {
        threshold.alertHigh
    }

    @Override
    GlucoseInUrineValue low(UrineGlucoseThreshold threshold) {
        threshold.alertLow
    }

    @Override
    List<Float> high(BloodPressureThreshold threshold) {
        [threshold.systolicAlertHigh, threshold.diastolicAlertHigh]
    }

    @Override
    List<Float> low(BloodPressureThreshold threshold) {
        [threshold.systolicAlertLow, threshold.diastolicAlertLow]
    }

    @Override
    void visitUrineCombi() {
        // TODO KM: Har ingen ide om hvad der skal stå her....
    }
}
