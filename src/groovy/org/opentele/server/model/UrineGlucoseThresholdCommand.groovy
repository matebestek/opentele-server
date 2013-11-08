package org.opentele.server.model
import grails.validation.Validateable
import org.opentele.server.model.types.GlucoseInUrineValue

@Validateable
class UrineGlucoseThresholdCommand extends ThresholdCommand {

    GlucoseInUrineValue alertHigh
    GlucoseInUrineValue warningHigh
    GlucoseInUrineValue warningLow
    GlucoseInUrineValue alertLow

    List<String> getBindableProperties() {
        ["alertHigh","warningHigh","warningLow","alertLow"]
    }

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

}
