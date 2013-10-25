package org.opentele.server.model
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import spock.lang.Specification

@TestFor(Patient)
@Build([Patient,Measurement, MeasurementType, CompletedQuestionnaire])
class PatientSpec extends Specification {
    Patient patient
    def setup() {
        patient = Patient.build()
    }


    def "test that getLatestQuestionnaireUploadDate works as expected"() {
        setup:
        [2,4,1].each { day ->
            def completedQuestionnaire = CompletedQuestionnaire.build(uploadDate: new Date())
            completedQuestionnaire.uploadDate[Calendar.DATE] = day
            patient.addToCompletedQuestionnaires(completedQuestionnaire)
        }
        patient.save()

        expect:
        patient.latestQuestionnaireUploadDate[Calendar.DATE] == 4
    }

    def "test that getLatestQuestionnaireUploadDate works as expected when there is no completed questionnaires"() {
        expect:
        !patient.latestQuestionnaireUploadDate
    }

}
