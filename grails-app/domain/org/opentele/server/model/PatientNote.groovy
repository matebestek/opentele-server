package org.opentele.server.model

import org.opentele.server.model.types.NoteType

class PatientNote extends AbstractObject{

    String note
    NoteType type
    Date reminderDate
    boolean remindToday = false
    Patient patient
    static hasMany = [seenBy: Clinician]

    static constraints = {

        note(nullable: false, blank: false)
        type(nullable:  false)
        reminderDate(nullable: true)
    }

    static mapping = {
        note type: "text"
    }
}
