package org.opentele.server.model.patientquestionnaire

import org.opentele.server.model.AbstractObject
import org.opentele.server.model.questionnaire.QuestionnaireNode

class PatientQuestionnaireNode extends AbstractObject {
    PatientQuestionnaire questionnaire
	PatientQuestionnaireNode defaultNext
    
    // Template node this node was instantiated "from"
    QuestionnaireNode templateQuestionnaireNode
    
    String shortText // used for added a short text, used in clinician views
    
    static hasMany = [defaultPreviouses: PatientQuestionnaireNode,
                      alternativePreviouses: PatientChoiceNode, 
                      completedQuestions: NodeResult,
                      nextFails: PatientMeasurementNode]
       
    static mappedBy = [questionnaire: "nodes", 
                       defaultPreviouses: "defaultNext", 
                       alternativePreviouses: "alternativeNext",
                       nextFails: "nextFail"]
    
    static constraints = {
        shortText(nullable:true, blank:true)
        questionnaire(nullable: true)
        templateQuestionnaireNode(nullable: true)
        defaultNext(nullable: true)
    }

    void visit(PatientQuestionnaireNodeVisitor visitor) {
        throw new RuntimeException("Implement visit for class '${this.class}'")
    }
}
