package org.opentele.server.model

import org.opentele.server.model.types.ProteinValue
import org.opentele.server.util.ThresholdValidationUtil

class UrineThreshold extends Threshold {
	ProteinValue alertHigh
	ProteinValue warningHigh
	ProteinValue warningLow
	ProteinValue alertLow

	private convertToProteinValue(Float f) {
		return convertToProteinValue((int)f)
	}

	private convertToProteinValue(int v) {
		switch(v) {
			case 0: return ProteinValue.NEGATIVE
			case 1: return ProteinValue.PLUSMINUS
			case 2: return ProteinValue.PLUS_ONE
			case 3: return ProteinValue.PLUS_TWO
			case 4: return ProteinValue.PLUS_THREE
			case 5: return ProteinValue.PLUS_FOUR
			default:
                throw new RuntimeException("Invalid protein value: '${v}'")
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
        return new UrineThreshold(type: this.type, alertHigh: this.alertHigh, alertLow: this.alertLow, warningHigh: this.warningHigh, warningLow: this.warningLow)
    }
}
