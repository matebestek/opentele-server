package org.opentele.server.model

import org.opentele.server.util.ThresholdValidationUtil

class NumericThreshold extends Threshold {
	Float alertHigh
	Float warningHigh
	Float warningLow
	Float alertLow

    static constraints = {
		alertHigh(nullable:true)
		alertLow(nullable:true)
		warningHigh(nullable:true)
		warningLow(nullable:true)

		alertHigh(validator: {val, obj ->
            ThresholdValidationUtil.validateAlertHigh(val, obj, ThresholdValidationUtil.thresholdValueProxy(obj))
		})
		warningHigh(validator: {val, obj ->
            ThresholdValidationUtil.validateWarningHigh(val, obj, ThresholdValidationUtil.thresholdValueProxy(obj))
		})
		warningLow(validator: {val, obj ->
            ThresholdValidationUtil.validateWarningLow(val, obj, ThresholdValidationUtil.thresholdValueProxy(obj))
		})
		alertLow(validator: {val, obj ->
            ThresholdValidationUtil.validateAlertLow(val, obj, ThresholdValidationUtil.thresholdValueProxy(obj))
		})
    }

    @Override
    Threshold duplicate() {
        return new NumericThreshold(type: this.type, alertHigh: this.alertHigh, alertLow: this.alertLow, warningHigh: this.warningHigh, warningLow: this.warningLow)
    }
}
