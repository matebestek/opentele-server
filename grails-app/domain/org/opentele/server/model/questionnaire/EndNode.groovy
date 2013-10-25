package org.opentele.server.model.questionnaire


class EndNode extends QuestionnaireNode {

    static constraints = {
        
        defaultNext(nullable: true) // Actually cannot have a value, but can we model that?
    }
	
	String toString() {
		"EndNode"
	}
}
