package org.opentele.server.model.questionnaire

import org.opentele.server.model.types.DataType
import org.opentele.server.model.types.Severity


// transient.. Not stored as result!
class BooleanNode extends QuestionnaireNode {

    String variableName
    Boolean value
    
    static constraints = {
        defaultNext(nullable:false)
        variableName(nullable:false)
        value(nullable:false)
    }
	
	String toString() {
		"BooleanNode"
	}
}
