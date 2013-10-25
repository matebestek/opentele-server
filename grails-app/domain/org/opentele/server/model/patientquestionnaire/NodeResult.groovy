package org.opentele.server.model.patientquestionnaire

import org.opentele.server.model.AbstractObject 
import org.opentele.server.model.Clinician
import org.opentele.server.model.types.Severity

class NodeResult extends AbstractObject {
	
    Date completionTime
    
    Clinician acknowledgedBy
    Date acknowledgedDate
    String acknowledgedNote
    
    PatientQuestionnaireNode patientQuestionnaireNode
    CompletedQuestionnaire completedQuestionnaire

    Boolean wasOmitted = Boolean.FALSE
    String note
    Severity severity
    
    String thresholdMessage
    
	boolean nodeIgnored = false
	String nodeIgnoredReason
	Clinician nodeIgnoredBy
	
    static constraints = {
        completionTime(nullable:false)
        acknowledgedBy(nullable:true)
        acknowledgedDate(nullable:true)
        acknowledgedNote(nullable:true)
        completedQuestionnaire(nullable: false)
        patientQuestionnaireNode(nullable: false)
        wasOmitted(nullable: false)
        severity(nullable: true)
        thresholdMessage(nullable: true)
		note(nullable:true)
		nodeIgnoredReason(nullable:true)
		nodeIgnoredBy(nullable:true)
		nodeIgnoredBy (nullable: true, validator: {val, obj ->
			if (obj != null && obj.nodeIgnored) {
				return val != null
			}
		})
    }
    
    static mapping = {
       // tablePerHierarchy false
    }
}
