package org.opentele.server.model.patientquestionnaire

import org.opentele.server.model.Measurement
import org.opentele.server.model.MeasurementType

class MeasurementNodeResult extends NodeResult {

	MeasurementType measurementType

    // Kan pr. definition generere en eller mange målinger
    static hasMany = [measurements: Measurement]
    
    static constraints = {
    }
    
    public String toString() {
        "MasurementNodeResult id:${id}, type:${measurementType?.name?.value()}"
    }
}
