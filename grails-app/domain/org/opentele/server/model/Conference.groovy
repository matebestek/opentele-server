package org.opentele.server.model

class Conference extends AbstractObject {
    static hasMany = [
        measurementDrafts: ConferenceMeasurementDraft,
        measurements: Measurement
    ]

    Clinician clinician
    Patient patient
    boolean completed

    static constraints = {
    }
}
