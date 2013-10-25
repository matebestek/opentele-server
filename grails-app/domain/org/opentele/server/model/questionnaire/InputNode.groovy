package org.opentele.server.model.questionnaire

import org.opentele.server.model.types.DataType 
import org.opentele.server.model.types.Severity

class InputNode extends QuestionnaireNode {

    static hasMany = [choices: ChoiceValue]

    String text
    DataType inputType
    
    QuestionnaireNode alternativeNext
    
    Severity defaultSeverity
    Severity alternativeSeverity
    
    static constraints = {
        defaultNext(nullable:false)
        inputType(nullable: false)
        defaultSeverity(nullable:true)
        alternativeNext(nullable: true)
        alternativeSeverity(nullable:true)
    }
	
	String toString() {
		"InputNode"
	}

    static mapping = {
        text type: 'text'
        choices sort: 'ordering'
     }
}

