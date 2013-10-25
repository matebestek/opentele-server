package org.opentele.server.model.patientquestionnaire

class ChoiceNodeResult extends NodeResult {

    Serializable result // Result of operation 
    
    Serializable input // From measurement or eg yes/no buttonpress
    
    static mapping = { input type: 'serializable'
                       result type: 'serializable' 
    }
    
    static constraints = {
        result(nullable:false)
    }
}
