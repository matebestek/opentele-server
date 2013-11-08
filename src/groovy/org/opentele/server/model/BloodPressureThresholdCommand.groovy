package org.opentele.server.model

import grails.validation.Validateable

@Validateable
class BloodPressureThresholdCommand extends ThresholdCommand {

    Float diastolicAlertHigh
    Float diastolicWarningHigh
    Float diastolicWarningLow
    Float diastolicAlertLow

    Float systolicAlertHigh
    Float systolicWarningHigh
    Float systolicWarningLow
    Float systolicAlertLow

    List<String> getBindableProperties() {
        ["diastolicAlertHigh", "diastolicWarningHigh", "diastolicWarningLow", "diastolicAlertLow", "systolicAlertHigh","systolicWarningHigh","systolicWarningLow","systolicAlertLow"]
    }

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
}
