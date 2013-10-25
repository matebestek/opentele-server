package org.opentele.server.model.questionnaire

import org.opentele.server.model.AbstractObject;

class QuestionnaireNode extends AbstractObject {

    Questionnaire questionnaire
	QuestionnaireNode defaultNext

    String shortText // used for added a short text, used in clinician views

    static hasMany = [defaultPreviouses: QuestionnaireNode, alternativePreviouses: ChoiceNode, nextFails: MeasurementNode]
        
    static mappedBy = [questionnaire: "nodes", defaultPreviouses: "defaultNext", alternativePreviouses: "alternativeNext", nextFails: "nextFail"]
    
    static constraints = {
        shortText(nullable:true, blank:true)
        questionnaire(nullable: true)
        defaultNext(nullable: true) // Actually cannot have a value, but can we model that?
    }
}
