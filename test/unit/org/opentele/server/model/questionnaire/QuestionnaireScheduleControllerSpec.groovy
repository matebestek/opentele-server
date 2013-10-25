package org.opentele.server.model.questionnaire

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import org.opentele.builders.QuestionnaireScheduleBuilder
import org.opentele.server.QuestionnaireScheduleService
import org.opentele.server.model.AddQuestionnaireGroup2MonitoringPlanCommand
import org.opentele.server.model.MonitoringPlan
import org.opentele.server.model.Patient
import org.opentele.server.model.QuestionnaireSchedule
import org.opentele.server.model.QuestionnaireScheduleCommand
import org.opentele.server.model.Schedule
import org.opentele.server.model.ScheduleWindow
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.patientquestionnaire.PatientBooleanNode
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire
import org.opentele.server.questionnaire.QuestionnaireService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.validation.Errors
import spock.lang.Specification
import static org.opentele.server.constants.Constants.SESSION_PATIENT_ID


@SuppressWarnings("GroovyAssignabilityCheck") // For controller calls
@TestFor(QuestionnaireScheduleController)
@Build([StandardSchedule, QuestionnaireGroup, QuestionnaireGroup2QuestionnaireHeader, QuestionnaireHeader, Questionnaire, QuestionnaireNode, QuestionnaireSchedule, PatientBooleanNode, BooleanNode, ScheduleWindow, PatientQuestionnaire, MonitoringPlan, CompletedQuestionnaire, Patient])
class QuestionnaireScheduleControllerSpec extends Specification{
    def setup() {
        controller.questionnaireService = Mock(QuestionnaireService)
        controller.questionnaireScheduleService = Mock(QuestionnaireScheduleService)
    }

    def "when calling create the model will be the properties from the QuestionnaireScheduleCommand object"() {
        given:
        def monitoringPlan = MonitoringPlan.build()
        params.type = Schedule.ScheduleType.MONTHLY.toString()
        params.'monitoringPlan.id' = monitoringPlan.id

        when:
        def model = controller.create()

        then:
        model.type ==Schedule.ScheduleType.MONTHLY
    }

    def "when calling save with a valid command, the service save will be called, and a redirect is issued"() {
        given:
        def command = Mock(QuestionnaireScheduleCommand)
        command.hasErrors() >> false
        command.monitoringPlan >> mockMonitoringPlan()

        when:
        controller.save(command)

        then:
        1 * controller.questionnaireScheduleService.save(command)
        flash.message == 'default.created.message'
        response.redirectUrl == "/monitoringPlan/show/${command.monitoringPlan.patient.id}"
    }

    def "when calling save with an invalid command, the service save will be called, invalidating the command, and the create view will be rerendered"() {
        given:
        def command = Mock(QuestionnaireScheduleCommand)
        command.hasErrors() >> true
        command.properties >> [:]
        command.errors >> Mock(Errors)

        when:
        controller.save(command)

        then:
        1 * controller.questionnaireScheduleService.save(command)
        view == '/questionnaireSchedule/create'
        model.errors == command.errors
    }

    def "when calling edit with an existing id, the questionnaireSchedule is added to the command and the command properties is the model for the view"() {
        given:
        def questionnaireSchedule = new QuestionnaireScheduleBuilder().forMonitoringPlan(mockMonitoringPlan())
                .forScheduleType(Schedule.ScheduleType.MONTHLY).forDaysInMonth([1]).build()
        params.id = questionnaireSchedule.id

        when:
        def model = controller.edit()

        then:
        model.questionnaireSchedule == questionnaireSchedule
        model.type == questionnaireSchedule.type
    }

    def "when calling edit with a non-existing id, a redirect to monitoringPlan/showPlan is issued"() {
        given:
        params.id = 10
        session[SESSION_PATIENT_ID] = 5

        when:
        controller.edit()

        then:
        flash.message == 'default.not.found.message'
        response.redirectUrl == "/monitoringPlan/show/5"
    }

    def "when calling update with a valid command, the service update will be called and a redirect is issued"() {
        given:
        def monitoringPlan = mockMonitoringPlan()
        def questionnaireSchedule = new QuestionnaireScheduleBuilder().forMonitoringPlan(monitoringPlan)
                .forScheduleType(Schedule.ScheduleType.MONTHLY).forDaysInMonth([1]).build()

        and:
        def command = Mock(QuestionnaireScheduleCommand)
        command.questionnaireSchedule >> questionnaireSchedule
        command.hasErrors() >> false
        command.monitoringPlan >> monitoringPlan

        when:
        controller.update(command)

        then:
        1 * controller.questionnaireScheduleService.update(command)
        flash.message == 'default.updated.message'
        response.redirectUrl == "/monitoringPlan/show/${command.monitoringPlan.patient.id}"
    }

    def "when calling update with a command without a questionnaireSchedule, a redirect to the monitoring plan is expected"() {
        given:
        def command = Mock(QuestionnaireScheduleCommand)

        and:
        session[SESSION_PATIENT_ID] = 5

        when:
        controller.update(command)

        then:
        0 * controller.questionnaireScheduleService.update(command)
        flash.message == 'default.not.found.message'
        response.redirectUrl == "/monitoringPlan/show/5"
    }

    def "when calling update with an invalid command, the service update will be called, invalidating the command, and the create view will be rerendered"() {
        given:
        def questionnaireSchedule = new QuestionnaireScheduleBuilder().forMonitoringPlan(mockMonitoringPlan())
                .forScheduleType(Schedule.ScheduleType.MONTHLY).forDaysInMonth([1]).build()

        and:
        def command = Mock(QuestionnaireScheduleCommand)
        command.questionnaireSchedule >> questionnaireSchedule
        command.hasErrors() >> true
        command.properties >> [type: Schedule.ScheduleType.MONTHLY]
        command.errors >> Mock(Errors)

        when:
        controller.update(command)

        then:
        1 * controller.questionnaireScheduleService.update(command)
        view == '/questionnaireSchedule/edit'
        model.errors == command.errors
    }

    def "when calling del with an existing questionnaireSchedule id will be deleted"() {
        given:
        def questionnaireSchedule = new QuestionnaireScheduleBuilder().forMonitoringPlan(mockMonitoringPlan())
                .forScheduleType(Schedule.ScheduleType.MONTHLY).forDaysInMonth([1]).build()
        def id = questionnaireSchedule.id

        and:
        params.id = id
        session[SESSION_PATIENT_ID] = 5

        when:
        controller.del()

        then:
        !QuestionnaireSchedule.get(id)
        flash.message == 'default.deleted.message'
        response.redirectUrl == '/monitoringPlan/show/5'
    }

    def "when calling del with a non existing questionnaireSchedule id flash is shown with not found"() {
        given:
        params.id = 10
        session[SESSION_PATIENT_ID] = 5

        when:
        controller.del()

        then:
        flash.message == 'default.not.found.message'
        response.redirectUrl == '/monitoringPlan/show/5'
    }

    def "when calling del with an existing questionnaireSchedule id that can not be deleted, a flash will be shown"() {
        given:
        def questionnaireSchedule = new QuestionnaireScheduleBuilder().forMonitoringPlan(mockMonitoringPlan())
                        .forScheduleType(Schedule.ScheduleType.MONTHLY).forDaysInMonth([1]).build()

        def id = questionnaireSchedule.id

        and:
        questionnaireSchedule.metaClass.delete = { Map m ->
            throw new DataIntegrityViolationException("boom")  // Instead of delete, throw up
        }

        and:
        params.id = id
        session[SESSION_PATIENT_ID] = 5

        when:
        controller.del()

        then:
        QuestionnaireSchedule.get(id)
        flash.message == 'default.not.deleted.message'
        response.redirectUrl == '/monitoringPlan/show/5'
    }

    def "when calling pickQuestionnaireGroup additional newQuestionnaries will be added to the command object"() {
        given:
        def questionnaireGroup = new QuestionnaireGroup()
        def monitoringPlan = new MonitoringPlan()
        def listOfMaps = [[test: 'value']]
        and:
        def command =  new AddQuestionnaireGroup2MonitoringPlanCommand(questionnaireGroup: questionnaireGroup, monitoringPlan: monitoringPlan)

        when:
        def model = controller.pickQuestionnaireGroup(command)

        then:
        1 * controller.questionnaireService.findQuestionnaireGroup2HeadersAndOverlapWithExistingQuestionnaireSchedules(questionnaireGroup, monitoringPlan) >> listOfMaps
        model.command.newQuestionnaires == listOfMaps
    }

    def "when calling addQustionnaireGroup, questionaireService addOrUpdateQuestionnairesOnMonitoringPlan is called an an appropiate flash message is generated"() {
        given:
        def command = Mock(AddQuestionnaireGroup2MonitoringPlanCommand)
        def monitoringPlan = mockMonitoringPlan()
        command.monitoringPlan >> monitoringPlan
        command.addedQuestionnaires >> [[id: 1], [id: 3]]  // Fake objects with an id
        command.updatedQuestionnaires >> [[id: 2]] // Fake object with an id

        when:
        controller.addQuestionnaireGroup(command)

        then:
        1 * controller.questionnaireService.addOrUpdateQuestionnairesOnMonitoringPlan(command)
        flash.message == 'questionnaireGroup.questionnaireAddOrUpdate.pre questionnaireGroup.questionnaireAddOrUpdate.add questionnaireGroup.questionnaireAddOrUpdate.and questionnaireGroup.questionnaireAddOrUpdate.update questionnaireGroup.questionnaireAddOrUpdate.post'
        flash.updated == [1,3,2]
        response.redirectUrl == "/monitoringPlan/show/${monitoringPlan.patient.id}"

    }
    
    private mockMonitoringPlan() {
        return MonitoringPlan.build(patient: Patient.build())
    }

}
