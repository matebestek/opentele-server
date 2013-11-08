package org.opentele.server.model

class BloodPressureThreshold extends Threshold {
    Float diastolicAlertHigh
    Float diastolicWarningHigh
    Float diastolicWarningLow
    Float diastolicAlertLow

    Float systolicAlertHigh
    Float systolicWarningHigh
    Float systolicWarningLow
    Float systolicAlertLow

    static constraints = {
        diastolicAlertHigh nullable: true, min: 0.0F, validator: { value, object ->
            value == null || value > [object.diastolicWarningHigh, object.diastolicWarningLow, object.diastolicAlertLow].max()
        }
        diastolicWarningHigh nullable: true, min: 0.0F, validator: { value, object ->
            value == null || value > [object.diastolicWarningLow, object.diastolicAlertLow].max()
        }
        diastolicWarningLow nullable: true, min: 0.0F, validator: { value, object ->
            value == null || value > object.diastolicAlertLow
        }
        diastolicAlertLow nullable: true, min: 0.0F

        systolicAlertHigh nullable: true, min: 0.0F, validator: { value, object ->
            value == null || value > [object.systolicWarningHigh, object.systolicWarningLow, object.systolicAlertLow].max()
        }
        systolicWarningHigh nullable: true, min: 0.0F, validator: { value, object ->
            value == null || value > [object.systolicWarningLow, object.systolicAlertLow].max()
        }
        systolicWarningLow nullable: true, min: 0.0F, validator: { value, object ->
            value == null || value > object.systolicAlertLow
        }
        systolicAlertLow nullable: true, min: 0.0F
    }

    @Override
    Threshold duplicate() {
        return new BloodPressureThreshold(type: this.type, diastolicAlertHigh: this.diastolicAlertHigh, diastolicAlertLow: this.diastolicAlertLow, diastolicWarningHigh: this.diastolicWarningHigh, diastolicWarningLow: this.diastolicWarningLow, systolicAlertHigh: this.systolicAlertHigh, systolicAlertLow: this.systolicAlertLow, systolicWarningHigh: this.systolicWarningHigh, systolicWarningLow: this.systolicWarningLow)
    }
}
