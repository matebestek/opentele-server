package org.opentele.server.model.questionnaire

class TextNode extends QuestionnaireNode {

    String headline
    String text

    static constraints = {
        headline(nullable:true)
        text(nullable:false)
        defaultNext(nullable:false)
    }
	
	String toString() {
		"TextNode"
	}
    
    static mapping = {
        text type: 'text'
     }
}
