package org.opentele.server.model.questionnaire

import org.opentele.server.model.types.DataType  
import org.opentele.server.model.types.Operation
import org.opentele.server.model.types.Severity

import grails.util.GrailsUtil

/**
 * Choice node performs the calculation:  Input (operation) value.
 * Examples: <br>
 * input LT value <br>
 * input GT value <br>
 * input EQ value <br> 
 */
class ChoiceNode extends QuestionnaireNode {

    DataType dataType
   
    Operation operation // GT LT EQUAL // EQUAL pr default for floats
    Serializable nodeValue // Value used for GT, LT and EQUAL operations if type int or float
    
    Severity defaultSeverity
    Severity alternativeSeverity
    
    QuestionnaireNode inputNode // (circular ref :-! - redundant???)
    String inputVar// Identifies which var from earlier node we use for input
    
    QuestionnaireNode defaultNext  // If condition == true
    QuestionnaireNode alternativeNext // condition == false

    static mapping = { 
        nodeValue type: 'serializable'
    }
    
    static constraints = {
        inputVar(nullable:true)
        inputNode(nullable:true)
        dataType(nullable:false)
        operation(nullable:false)
        nodeValue(nullable:false)
        defaultNext(nullable:false)
        alternativeNext(nullable:false)
        defaultSeverity(nullable:false)
        alternativeSeverity(nullable:false)
    }
	
	String toString() {
		"ChoiceNode"
	}
}
