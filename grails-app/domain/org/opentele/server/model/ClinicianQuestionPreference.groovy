package org.opentele.server.model

import org.opentele.server.model.questionnaire.QuestionnaireNode

class ClinicianQuestionPreference extends AbstractObject{
	
	Clinician clinician
	QuestionnaireNode question
	
    static constraints = {
		question(nullable:false)
		clinician(nullable:false)
    }
}
