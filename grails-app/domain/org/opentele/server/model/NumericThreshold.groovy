package org.opentele.server.model

class NumericThreshold extends Threshold {
    Float alertHigh
    Float warningHigh
    Float warningLow
    Float alertLow

    static constraints = {
        alertHigh nullable: true, min: 0.0F, validator: { value, object ->
            value == null || value > [object.warningHigh, object.warningLow, object.alertLow].max()
        }
        warningHigh nullable: true, min: 0.0F, validator: { value, object ->
            value == null || value > [object.warningLow, object.alertLow].max()
        }
        warningLow nullable: true, min: 0.0F, validator: { value, object ->
            value == null || value > object.alertLow
        }
        alertLow nullable: true, min: 0.0F
    }

    @Override
    Threshold duplicate() {
        return new NumericThreshold(type: this.type, alertHigh: this.alertHigh, alertLow: this.alertLow, warningHigh: this.warningHigh, warningLow: this.warningLow)
    }
}
