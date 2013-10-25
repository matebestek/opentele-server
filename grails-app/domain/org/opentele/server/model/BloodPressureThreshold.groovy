package org.opentele.server.model

import org.opentele.server.util.ThresholdValidationUtil

class BloodPressureThreshold extends Threshold{
    Float diastolicAlertHigh
    Float diastolicWarningHigh
    Float diastolicWarningLow
    Float diastolicAlertLow

    Float systolicAlertHigh
    Float systolicWarningHigh
    Float systolicWarningLow
    Float systolicAlertLow

    static constraints = {
        diastolicAlertHigh(nullable:true)
        diastolicWarningHigh(nullable:true)
        diastolicWarningLow(nullable:true)
        diastolicAlertLow(nullable:true)

        systolicAlertHigh(nullable:true)
        systolicWarningHigh(nullable:true)
        systolicWarningLow(nullable:true)
        systolicAlertLow(nullable:true)

        diastolicAlertHigh(validator: {val, obj ->
            ThresholdValidationUtil.validateAlertHigh(val, obj, ThresholdValidationUtil.bloodPressureThresholdValueProxy(obj)("diastolic"))
        })
        diastolicWarningHigh(validator: {val, obj ->
            ThresholdValidationUtil.validateWarningHigh(val, obj, ThresholdValidationUtil.bloodPressureThresholdValueProxy(obj)("diastolic"))
        })
        diastolicWarningLow(validator: {val, obj ->
            ThresholdValidationUtil.validateWarningLow(val, obj, ThresholdValidationUtil.bloodPressureThresholdValueProxy(obj)("diastolic"))
        })
        diastolicAlertLow(validator: {val, obj ->
            ThresholdValidationUtil.validateAlertLow(val, obj, ThresholdValidationUtil.bloodPressureThresholdValueProxy(obj)("diastolic"))
        })

        systolicAlertHigh(validator: {val, obj ->
            ThresholdValidationUtil.validateAlertHigh(val, obj, ThresholdValidationUtil.bloodPressureThresholdValueProxy(obj)("systolic"))
        })
        systolicWarningHigh(validator: {val, obj ->
            ThresholdValidationUtil.validateWarningHigh(val, obj, ThresholdValidationUtil.bloodPressureThresholdValueProxy(obj)("systolic"))
        })
        systolicWarningLow(validator: {val, obj ->
            ThresholdValidationUtil.validateWarningLow(val, obj, ThresholdValidationUtil.bloodPressureThresholdValueProxy(obj)("systolic"))
        })
        systolicAlertLow(validator: {val, obj ->
            ThresholdValidationUtil.validateAlertLow(val, obj, ThresholdValidationUtil.bloodPressureThresholdValueProxy(obj)("systolic"))
        })

    }

    @Override
    Threshold duplicate() {
        return new BloodPressureThreshold(type: this.type, diastolicAlertHigh: this.diastolicAlertHigh, diastolicAlertLow: this.diastolicAlertLow, diastolicWarningHigh: this.diastolicWarningHigh, diastolicWarningLow: this.diastolicWarningLow, systolicAlertHigh: this.systolicAlertHigh, systolicAlertLow: this.systolicAlertLow, systolicWarningHigh: this.systolicWarningHigh, systolicWarningLow: this.systolicWarningLow)
    }
}
