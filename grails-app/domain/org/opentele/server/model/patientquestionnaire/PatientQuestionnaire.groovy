package org.opentele.server.model.patientquestionnaire
import org.opentele.server.model.AbstractObject
import org.opentele.server.model.Clinician
import org.opentele.server.model.questionnaire.Questionnaire

class PatientQuestionnaire extends AbstractObject {
	static hasMany = [nodes: PatientQuestionnaireNode,
		completedQuestionnaires: CompletedQuestionnaire]

	Questionnaire templateQuestionnaire

	Clinician creator
	Date creationDate

	PatientQuestionnaireNode startNode

    String toString() {
		name
	}

    String getName() {
        templateQuestionnaire.name
    }

    static transients = ['name']

	static constraints = {
		startNode(nullable: true)
		creator(nullable:false)
		creationDate(nullable:false)

	}
}
