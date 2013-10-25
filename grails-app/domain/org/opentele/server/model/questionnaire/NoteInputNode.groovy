package org.opentele.server.model.questionnaire

import org.opentele.server.model.types.DataType;

class NoteInputNode extends QuestionnaireNode {

    String text
	DataType inputType
	
    static constraints = {
        
        text(nullable:false)
		inputType(nullable:false)
        defaultNext(nullable:false)
    }
	
	String toString() {
		"NodeInputNode"
	}
    
    static mapping = {
        text type: 'text'
     }
}
