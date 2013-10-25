package org.opentele.server.model

import org.opentele.server.model.types.GlucoseInUrineValue
import org.opentele.server.util.ThresholdValidationUtil

class UrineGlucoseThreshold extends Threshold {
    GlucoseInUrineValue alertHigh
    GlucoseInUrineValue warningHigh
    GlucoseInUrineValue warningLow
    GlucoseInUrineValue alertLow

	private convertToUrineGlucoseValue(Float f) {
		return convertToUrineGlucoseValue((int)f)
	}

	private convertToUrineGlucoseValue(int v) {
		switch(v) {
			case 0: return GlucoseInUrineValue.NEGATIVE
			case 1: return GlucoseInUrineValue.PLUS_ONE
			case 2: return GlucoseInUrineValue.PLUS_TWO
			case 3: return GlucoseInUrineValue.PLUS_THREE
			case 4: return GlucoseInUrineValue.PLUS_FOUR
			default:
                throw new RuntimeException("Invalid Urine Glucose value: '${v}'")
		}
	}

    static constraints = {
		alertHigh(nullable:true)
		alertLow(nullable:true)
		warningHigh(nullable:true)
		warningLow(nullable:true)

        alertHigh(validator: {val, obj ->
            return ThresholdValidationUtil.validateAlertHigh(val, obj, ThresholdValidationUtil.thresholdValueProxy(obj))
        })
        warningHigh(validator: {val, obj ->
            return ThresholdValidationUtil.validateWarningHigh(val, obj, ThresholdValidationUtil.thresholdValueProxy(obj))
        })
        warningLow(validator: {val, obj ->
            return ThresholdValidationUtil.validateWarningLow(val, obj, ThresholdValidationUtil.thresholdValueProxy(obj))
        })
        alertLow(validator: {val, obj ->
            return ThresholdValidationUtil.validateAlertLow(val, obj, ThresholdValidationUtil.thresholdValueProxy(obj))
        })
    }

    @Override
    Threshold duplicate() {
        return new UrineGlucoseThreshold(type: this.type, alertHigh: this.alertHigh, alertLow: this.alertLow, warningHigh: this.warningHigh, warningLow: this.warningLow)
    }
}
