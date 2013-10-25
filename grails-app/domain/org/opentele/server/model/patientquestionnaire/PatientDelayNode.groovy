package org.opentele.server.model.patientquestionnaire

import org.opentele.server.model.types.Severity

class PatientDelayNode extends PatientQuestionnaireNode {
    
    String text
    Boolean countUp
    Integer countTime
    
    // If inputType Boolean..
    PatientQuestionnaireNode alternativeNext
    Severity defaultSeverity
    Severity alternativeSeverity
    
    static constraints = {
        defaultNext(nullable:true) // Hvorfor nullable?
        alternativeNext(nullable:true)
        defaultSeverity(nullable:true)
        alternativeSeverity(nullable:true)
        countUp(nullable: false)
        countTime(nullable:false)
    }
    
    static mapping = {
        text type: 'text'
    }

    @Override
    void visit(PatientQuestionnaireNodeVisitor visitor) {
        visitor.visitDelayNode(this)
    }
}
