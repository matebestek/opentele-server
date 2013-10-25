package org.opentele.server.model.types

import org.opentele.server.model.AbstractObject
import org.opentele.server.model.Clinician
import org.opentele.server.model.Patient


public enum NoteType {
    NORMAL {
        public String toString() {
            return "Normal"
        }
    },
    IMPORTANT {
        public String toString() {
            return "Vigtig"
        }
    }
}
