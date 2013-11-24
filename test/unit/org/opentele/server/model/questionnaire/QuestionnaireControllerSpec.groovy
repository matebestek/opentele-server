package org.opentele.server.model.questionnaire
import grails.buildtestdata.mixin.Build
import grails.converters.JSON
import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.opentele.server.ClinicianService
import org.opentele.server.CompletedQuestionnaireService
import org.opentele.server.I18nService
import org.opentele.server.PatientService
import org.opentele.server.model.*
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire
import org.opentele.server.model.types.Severity
import org.springframework.security.core.Authentication
import spock.lang.Specification

@TestFor(QuestionnaireController)
@Build([CompletedQuestionnaire, Clinician, User, Patient, Department, PatientGroup, Patient2PatientGroup, Clinician2PatientGroup, QuestionnaireHeader])
class QuestionnaireControllerSpec extends Specification{
    Patient patientA
    Patient patientB
    Clinician clinicianA
    PatientGroup pgB
    CompletedQuestionnaire questionnaireA

    def setup() {
        controller.completedQuestionnaireService = Mock(CompletedQuestionnaireService)
        controller.patientService = Mock(PatientService)
        controller.clinicianService = Mock(ClinicianService)
        controller.springSecurityService = Mock(SpringSecurityService)
        controller.i18nService = Mock(I18nService)
        controller.i18nService.message( { it.code == 'completedquestionnaire.message.body' } ) >> "Hey"
        controller.i18nService.message([code: "default.date.format.notime.short"]) >> "d-MM-yyyy"
        controller.i18nService.message([code: "default.time.format.noseconds"]) >> "HH:mm"

        Department depA = Department.build(name: "A")
        Department depB = Department.build(name: "B")
        PatientGroup pgA = PatientGroup.build(department: depA)
        pgB = PatientGroup.build(department: depB)
        patientA = Patient.build(firstName: "PatientA")
        Patient2PatientGroup.build(patient: patientA, patientGroup: pgA)

        patientB = Patient.build(firstName: "PatientB")

        clinicianA = Clinician.build(firstName: "ClinicianA")
        Clinician2PatientGroup.build(clinician: clinicianA, patientGroup: pgA)

        controller.clinicianService.currentClinician >> clinicianA

        Authentication authentication = Mock(Authentication)
        authentication.authenticated >> true
        controller.springSecurityService.currentUser >> patientA.user
        controller.springSecurityService.authentication >> authentication

        questionnaireA = CompletedQuestionnaire.build(patient: patientA, acknowledgedDate: new Date(), severity:  Severity.GREEN)
    }

    def "test acknowledge makes acknowledgement available to patient"() {
        setup:
        def cq = questionnaireA
        params.id = cq.id
        params.note = ""
        params.withAutoMessage = "true"
        controller.completedQuestionnaireService.acknowledge(_, _, true) >> cq

        when:
        controller.acknowledge()

        then:
        flash.message == "completedquestionnaire.acknowledged"
    }

    def 'can give acknowledgement list as JSON'() {
        setup:

        QuestionnaireHeader qh = QuestionnaireHeader.build(name: "Testskemaet")
        def q = Questionnaire.build(questionnaireHeader: qh)
        qh.activeQuestionnaire = q

        def pq = PatientQuestionnaire.build(templateQuestionnaire: q)
        CompletedQuestionnaire.build(patient: patientA, patientQuestionnaire: pq, acknowledgedDate: new Date(), receivedDate: new Date(), showAcknowledgementToPatient: true)
        CompletedQuestionnaire.build(patient: patientB, patientQuestionnaire: pq, acknowledgedDate: new Date(), receivedDate: new Date(), showAcknowledgementToPatient: true)

        when:
        response.format = 'json'
        controller.acknowledgements()
        def model = JSON.parse(response.contentAsString)

        then:
        model.acknowledgements.size() == 1
        model.acknowledgements[0].message == "Hey"
    }

    def 'only find acknowledgements acknowledged within the last 30 days'() {
        setup:

        QuestionnaireHeader qh = QuestionnaireHeader.build(name: "Testskemaet")
        def q = Questionnaire.build(questionnaireHeader: qh)
        qh.activeQuestionnaire = q

        def cal = Calendar.instance
        cal.roll (Calendar.DATE, -30)
        cal.roll (Calendar.MINUTE, 1)
        def thirtyDaysMinusAMinuteAgo = cal.getTime()

        def pq = PatientQuestionnaire.build(templateQuestionnaire: q)

        CompletedQuestionnaire.build(patient: patientA, patientQuestionnaire: pq, acknowledgedDate: new Date()-29, receivedDate: new Date(), showAcknowledgementToPatient: true)
        CompletedQuestionnaire.build(patient: patientA, patientQuestionnaire: pq, acknowledgedDate: thirtyDaysMinusAMinuteAgo, receivedDate: new Date(), showAcknowledgementToPatient: true)
        CompletedQuestionnaire.build(patient: patientA, patientQuestionnaire: pq, acknowledgedDate: new Date()-31, receivedDate: new Date(), showAcknowledgementToPatient: true)

        when:
        response.format = 'json'
        controller.acknowledgements()
        def model = JSON.parse(response.contentAsString)

        then:
        model.acknowledgements.size() == 2
        model.acknowledgements[0].message == "Hey"
    }
}
