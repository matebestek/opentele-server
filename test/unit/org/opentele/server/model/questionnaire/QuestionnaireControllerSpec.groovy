package org.opentele.server.model.questionnaire
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import org.opentele.server.ClinicianService
import org.opentele.server.CompletedQuestionnaireService
import org.opentele.server.MessageService
import org.opentele.server.PatientService
import org.opentele.server.model.*
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.types.Severity
import spock.lang.Specification

@TestFor(QuestionnaireController)
@Build([CompletedQuestionnaire, Clinician, User, Patient, Department, PatientGroup, Patient2PatientGroup, Clinician2PatientGroup, QuestionnaireHeader])
class QuestionnaireControllerSpec extends Specification{
    Patient patientA
    Clinician clinicianA
    PatientGroup pgB
    CompletedQuestionnaire questionnaire

    def setup() {
        controller.completedQuestionnaireService = Mock(CompletedQuestionnaireService)
        controller.patientService = Mock(PatientService)
        controller.clinicianService = Mock(ClinicianService)

        Department depA = Department.build(name: "A")
        Department depB = Department.build(name: "B")
        PatientGroup pgA = PatientGroup.build(department: depA)
        pgB = PatientGroup.build(department: depB)
        patientA = Patient.build(firstName: "PatientA")
        Patient2PatientGroup.build(patient: patientA, patientGroup: pgA)

        clinicianA = Clinician.build(firstName: "ClinicianA")
        Clinician2PatientGroup.build(clinician: clinicianA, patientGroup: pgA)

        controller.clinicianService.currentClinician >> clinicianA

        questionnaire = CompletedQuestionnaire.build(patient: patientA, acknowledgedDate: new Date(), severity:  Severity.GREEN)
    }

    def "test acknowledge sends new auto message"() {
        setup:
        def cq = questionnaire
        params.id = cq.id
        params.note = ""
        params.withAutoMessage = "true"
        controller.completedQuestionnaireService.acknowledge(_, _) >> cq

        when:
        controller.acknowledge()

        then:
        flash.message == "completedquestionnaire.acknowledged"
        1 * controller.completedQuestionnaireService.sendAcknowledgeAutoMessage(_)
    }
}
