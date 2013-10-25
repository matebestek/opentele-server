package org.opentele.server.model.patientquestionnaire

import org.opentele.server.model.types.DataType;
import org.opentele.server.model.types.Severity;

class PatientInputNode extends PatientQuestionnaireNode {

    static hasMany = [choices: PatientChoiceValue]
    
    String text
    DataType inputType
    
    // If inputType Boolean..
    PatientQuestionnaireNode alternativeNext
    Severity defaultSeverity
    Severity alternativeSeverity
    
    static constraints = {
        defaultNext(nullable:true) // Hvorfor nullable?
        alternativeNext(nullable:true)
        defaultSeverity(nullable:true)
        alternativeSeverity(nullable:true)
        inputType(nullable: false)
    }

    static mapping = {
        text type: 'text'
        choices sort: 'ordering'
    }

    @Override
    void visit(PatientQuestionnaireNodeVisitor visitor) {
        visitor.visitInputNode(this)
    }
}
