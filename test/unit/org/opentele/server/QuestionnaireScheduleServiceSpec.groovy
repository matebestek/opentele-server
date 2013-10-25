package org.opentele.server
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import org.opentele.server.model.MonitoringPlan
import org.opentele.server.model.QuestionnaireSchedule
import org.opentele.server.model.QuestionnaireScheduleCommand
import org.opentele.server.model.Schedule
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import spock.lang.Specification

@TestFor(QuestionnaireScheduleService)
@Build([QuestionnaireHeader, MonitoringPlan, QuestionnaireSchedule])
class QuestionnaireScheduleServiceSpec extends Specification {

    def "when createing a questionnaire schedule, the schedule will be added to the monitoring plan with the correct QuestionnaireHeader and schedule"() {
        given:
        def monitoringPlan = MonitoringPlan.build(questionnaireSchedules: [])

        and:
        def command = Mock(QuestionnaireScheduleCommand)
        command.validate() >> true
        command.type >> Schedule.ScheduleType.UNSCHEDULED
        command.monitoringPlan >> monitoringPlan
        command.selectedQuestionnaireHeader >> QuestionnaireHeader.build()

        expect:
        monitoringPlan.questionnaireSchedules.size() == 0

        when:
        service.save(command)

        then:
        1 * command.bindScheduleData(_ as Schedule)
        monitoringPlan.questionnaireSchedules.size() == 1
        !command.hasErrors()
    }

    def "when createing a questionnaire schedule the schedule will not be added to the monitoring plan if the command object contains errors"() {
        given:
        def monitoringPlan = MonitoringPlan.build(questionnaireSchedules: [])

        and:
        def command = Mock(QuestionnaireScheduleCommand)
        command.validate() >> false
        command.type >> Schedule.ScheduleType.UNSCHEDULED
        command.monitoringPlan >> monitoringPlan
        command.selectedQuestionnaireHeader >> QuestionnaireHeader.build()

        expect:
        monitoringPlan.questionnaireSchedules.size() == 0

        when:
        service.save(command)

        then:
        0 * command.bindScheduleData(_ as Schedule)
        monitoringPlan.questionnaireSchedules.size() == 0
    }

    def "when updating an existing questionnaire schedule with a new questionnaireHeader and schedule the schedule will be updated correct"() {
        given:
        def questionnaireHeader = QuestionnaireHeader.build(name: "Questionnaire Header")
        def monitoringPlan = MonitoringPlan.build(questionnaireSchedules: [])
        def questionnaireSchedule = QuestionnaireSchedule.build(type: Schedule.ScheduleType.UNSCHEDULED, monitoringPlan: monitoringPlan, questionnaireHeader: questionnaireHeader)
        def newQuestionnaireHeader = QuestionnaireHeader.build(name: "New QuestionnaireHeader")

        and:
        def command = Mock(QuestionnaireScheduleCommand)
        command.questionnaireSchedule >> questionnaireSchedule
        command.version >> questionnaireSchedule.version
        command.validate() >> true
        command.monitoringPlan >> monitoringPlan
        command.selectedQuestionnaireHeader >> newQuestionnaireHeader

        expect:
        monitoringPlan.questionnaireSchedules.size() == 1

        when:
        service.update(command)

        then:
        1 * command.bindScheduleData(_ as Schedule)
        monitoringPlan.questionnaireSchedules.size() == 1
        questionnaireSchedule.questionnaireHeader == newQuestionnaireHeader
    }

    def "when updating an existing questionnaire schedule with a command object that does not validate nothing is updated on the questionnaire schedule"() {
        given:
        def questionnaireHeader = QuestionnaireHeader.build(name: "Questionnaire Header")
        def monitoringPlan = MonitoringPlan.build(questionnaireSchedules: [])
        def questionnaireSchedule = QuestionnaireSchedule.build(type: Schedule.ScheduleType.UNSCHEDULED, monitoringPlan: monitoringPlan, questionnaireHeader: questionnaireHeader)
        def newQuestionnaireHeader = QuestionnaireHeader.build(name: "New QuestionnaireHeader")

        and:
        def command = Mock(QuestionnaireScheduleCommand)
        command.questionnaireSchedule >> questionnaireSchedule
        command.version >> questionnaireSchedule.version
        command.validate() >> false
        command.monitoringPlan >> monitoringPlan
        command.selectedQuestionnaireHeader >> newQuestionnaireHeader

        expect:
        monitoringPlan.questionnaireSchedules.size() == 1

        when:
        service.update(command)

        then:
        0 * command.bindScheduleData(_ as Schedule)
        monitoringPlan.questionnaireSchedules.size() == 1
        questionnaireSchedule.questionnaireHeader == questionnaireHeader
    }

}
