package org.opentele.server.model.patientquestionnaire

import org.opentele.server.TimeFilter
import org.opentele.server.model.AbstractObject
import org.opentele.server.model.Clinician
import org.opentele.server.model.Patient
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.model.types.Severity


class CompletedQuestionnaire extends AbstractObject {

    static hasMany = [completedQuestions: NodeResult]

    // Date received from client, ie. can be offset from server-time
    Date uploadDate

    // Date questionnaire was received on server
    Date receivedDate

    Patient patient
    
    Clinician acknowledgedBy 
    Date acknowledgedDate
    String acknowledgedNote
    
    Severity severity // Equals worst severity in questionnaire nodes..

    QuestionnaireHeader questionnaireHeader
    PatientQuestionnaire patientQuestionnaire
    boolean _questionnaireIgnored = false //Only used in QuestionnaireController->toggleIgnoreQuestionnaire
	String questionnaireIgnoredReason
	Clinician questionnareIgnoredBy
	
    static constraints = {
        questionnaireHeader(nullable: false)
        patient(nullable: false)
        uploadDate(nullable: false)
        receivedDate(nullable: false)
        acknowledgedBy(nullable: true)
        acknowledgedDate(nullable: true)
        acknowledgedNote(nullable: true)
        severity(nullable: false)
		questionnaireIgnoredReason(nullable:true)
		questionnareIgnoredBy(nullable:true)
    }

    static mapping = {
        acknowledgedNote type: "text"
    }

    static namedQueries = {
        unacknowledgedGreenQuestionnairesByPatients  { List<Patient> patients ->
            isNull('acknowledgedBy')
            inList('patient', patients)
            eq('severity', Severity.GREEN)

        }
        unacknowledgedGreenQuestionnairesByPatient { Patient patient, TimeFilter timeFilter ->
            isNull('acknowledgedBy')
            eq('patient', patient)
            eq('severity', Severity.GREEN)

            if (timeFilter && timeFilter.isLimited) {
                between('receivedDate', timeFilter.start, timeFilter.end)
            }
        }
    }
}
