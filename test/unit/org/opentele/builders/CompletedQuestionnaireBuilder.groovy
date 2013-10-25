package org.opentele.builders

import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire
import org.opentele.server.model.types.Severity

class CompletedQuestionnaireBuilder {
    def patient
    def patientQuestionnaire
    def name
    def questionnaireHeader
    def uploadDate = new Date()
    def severity = Severity.GREEN
    def createdDate = new Date()

    CompletedQuestionnaireBuilder forPatient(patient) {
        this.patient = patient
        this
    }

    CompletedQuestionnaireBuilder forName(String name) {
        this.name = name
        this
    }

    CompletedQuestionnaire build() {
        this.patientQuestionnaire = new PatientQuestionnaireBuilder().build()

        QuestionnaireHeaderBuilder questionnaireHeaderBuilder = new QuestionnaireHeaderBuilder()
        if (name != null) {
            questionnaireHeaderBuilder = questionnaireHeaderBuilder.forName(name)
        }
        this.questionnaireHeader = questionnaireHeaderBuilder.build()

        def result = new CompletedQuestionnaire(patient:patient, questionnaireHeader: questionnaireHeader,
                patientQuestionnaire: patientQuestionnaire, uploadDate: uploadDate, severity: severity, createdDate: createdDate)
        result.save(failOnError: true)

        result
    }
}
