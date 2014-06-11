package org.opentele.server.model

import org.opentele.server.model.types.Severity

class PatientOverview extends AbstractObject {
    Patient patient

    String name
    String cpr
    int numberOfUnacknowledgedQuestionnaires
    Severity questionnaireSeverity
    String mostSevereQuestionnaireName
    Date mostSevereQuestionnaireDate
    String blueAlarmText
    String greenQuestionnaireIds

    int numberOfUnreadMessagesToPatient
    Date dateOfOldestUnreadMessageToPatient
    int numberOfUnreadMessagesFromPatient
    Date dateOfOldestUnreadMessageFromPatient

    boolean important

    static constraints = {
        patient(nullable:false, unique: true)

        mostSevereQuestionnaireName(nullable: true)
        mostSevereQuestionnaireDate(nullable: true)
        blueAlarmText(nullable: true)
        greenQuestionnaireIds(nullable: true)

        dateOfOldestUnreadMessageToPatient(nullable: true)
        dateOfOldestUnreadMessageFromPatient(nullable: true)
    }
}
