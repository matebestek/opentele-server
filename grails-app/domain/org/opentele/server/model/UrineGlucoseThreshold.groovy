package org.opentele.server.model
import org.opentele.server.model.types.GlucoseInUrineValue

class UrineGlucoseThreshold extends Threshold {
    GlucoseInUrineValue alertHigh
    GlucoseInUrineValue warningHigh
    GlucoseInUrineValue warningLow
    GlucoseInUrineValue alertLow

    static constraints = {
        alertHigh nullable: true, validator: { value, object ->
            value == null || value > [object.warningHigh, object.warningLow, object.alertLow].max()
        }
        warningHigh nullable: true, validator: { value, object ->
            value == null || value > [object.warningLow, object.alertLow].max()
        }
        warningLow nullable: true, validator: { value, object ->
            value == null || value > object.alertLow
        }
        alertLow nullable: true
    }

    @Override
    Threshold duplicate() {
        return new UrineGlucoseThreshold(type: this.type, alertHigh: this.alertHigh, alertLow: this.alertLow, warningHigh: this.warningHigh, warningLow: this.warningLow)
    }
}
