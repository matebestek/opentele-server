package org.opentele.server.model.patientquestionnaire

class InputNodeResult extends NodeResult {

    Serializable result
    
    static mapping = { result type: 'serializable' }
    
    static constraints = {
        result(nullable: true) // Hvis aktivt fravalgt
    }
}
