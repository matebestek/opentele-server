package org.opentele.server.model

class Consultation extends AbstractObject {

    static hasMany = [
            measurements: Measurement
    ]

    Clinician clinician
    Patient patient
    Date consultationDate

    static constraints = {
        clinician(nullable:false)
        patient(nullable:false)
        consultationDate(nullable:false)
    }
}