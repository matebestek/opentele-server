package org.opentele.server.model

import grails.validation.Validateable

@Validateable
abstract class ThresholdCommand {
    Long version
    Threshold threshold

    abstract List<String> getBindableProperties()

    static constraints = {
        threshold nullable: false
        version nullable: true, validator: { value, object ->
            value == null || value == object.threshold.version
        }
    }
}
