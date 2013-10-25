package org.opentele.server.model.questionnaire

import org.opentele.server.model.types.Severity

class DelayNode extends QuestionnaireNode {
    
    String text
    
    Boolean countUp
    Integer countTime
    
    QuestionnaireNode alternativeNext
    
    Severity defaultSeverity
    Severity alternativeSeverity
    
    static constraints = {
        defaultNext(nullable:false)
        countUp(nullable: false)
        countTime(nullable: false)
        defaultSeverity(nullable:true)
        alternativeNext(nullable: true)
        alternativeSeverity(nullable:true)
    }
	
	String toString() {
		"DelayNode"
	}
    
    static mapping = {
        text type: 'text'
     }
}
