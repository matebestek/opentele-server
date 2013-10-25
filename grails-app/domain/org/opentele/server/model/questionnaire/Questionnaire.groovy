package org.opentele.server.model.questionnaire

import org.opentele.server.model.AbstractObject; 
import org.opentele.server.model.Clinician
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire

class Questionnaire extends AbstractObject {
    
    static hasMany = [nodes: QuestionnaireNode, 
                      questionnaire2MeterTypes : Questionnaire2MeterType,
                      patientQuestionnaires: PatientQuestionnaire]
    String revision
    Clinician creator
    Date creationDate
    //QuestionnaireHeader questionnaireHeader
	QuestionnaireNode startNode
    String editorState
    
    //NOTE at the moment we have a double representation / the standardSchedule is also saved in the editorState
    StandardSchedule standardSchedule = new StandardSchedule()

	String toString() {
		name + " version : " + revision
	}

    String getName() {
        questionnaireHeader.name
    }

    static transients = ['name']

    static belongsTo = [questionnaireHeader: QuestionnaireHeader]

    static constraints = {
        questionnaireHeader(nullable: false)
        startNode(nullable: true)
        revision(nullable:false)
        creator(nullable:true)
        creationDate(nullable:false)
        editorState(nullable: true)
        standardSchedule(nullable: false )
        //name unique: 'revision'
    }

    static mapping = {
        editorState type: 'text'
        questionnaire2MeterTypes cascade: 'all-delete-orphan'
    }

    static embedded = [ 'standardSchedule' ]

    static Questionnaire findByNameAndRevision(String name, String revision) {
        QuestionnaireHeader questionnaireHeader = QuestionnaireHeader.findByName(name)
        questionnaireHeader?.questionnaires.find { it.revision == revision }
    }

}
