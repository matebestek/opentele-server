package org.opentele.server.model
import org.opentele.server.model.types.ProteinValue

class UrineThreshold extends Threshold {
	ProteinValue alertHigh
	ProteinValue warningHigh
	ProteinValue warningLow
	ProteinValue alertLow

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
        return new UrineThreshold(type: this.type, alertHigh: this.alertHigh, alertLow: this.alertLow, warningHigh: this.warningHigh, warningLow: this.warningLow)
    }
}
