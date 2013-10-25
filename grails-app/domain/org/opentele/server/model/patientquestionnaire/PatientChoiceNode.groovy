package org.opentele.server.model.patientquestionnaire

import org.opentele.server.model.types.DataType; 
import org.opentele.server.model.types.Severity
import org.opentele.server.model.types.Operation;

import org.opentele.server.model.types.Operation;

class PatientChoiceNode extends PatientQuestionnaireNode {

    DataType dataType
    
    // Input (operation) value. Ie input LT value
    Operation operation
    Serializable nodeValue // Value used for GT, LT and EQUAL operations if type int or float
        
//    PatientQuestionnaireNode defaultNext
    PatientQuestionnaireNode alternativeNext
    
    PatientQuestionnaireNode inputNode
    String inputVar
    
    Severity defaultSeverity
    Severity alternativeSeverity
 
    static mapping = { 
        //nodeValue sqlType: "blob"
        nodeValue type: "serializable"
    }
     
    static constraints = {
         inputVar(nullable:true)
         inputNode(nullable:true)
         dataType(nullable:false)
         operation(nullable:false)
         nodeValue(nullable:false)
         defaultNext(nullable:true)
         alternativeNext(nullable:true)
         defaultSeverity(nullable:false)
         alternativeSeverity(nullable:false)
    }

    @Override
    void visit(PatientQuestionnaireNodeVisitor visitor) {
        visitor.visitChoiceNode(this)
    }
}
