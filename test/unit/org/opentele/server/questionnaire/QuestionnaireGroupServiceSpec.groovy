package org.opentele.server.questionnaire

import grails.buildtestdata.mixin.Build
import grails.converters.JSON
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import org.opentele.server.I18nService
import org.opentele.server.model.Schedule
import org.opentele.server.model.questionnaire.QuestionnaireGroup
import org.opentele.server.model.questionnaire.QuestionnaireGroup2QuestionnaireHeader
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.model.questionnaire.StandardSchedule
import org.opentele.server.model.types.Weekday
import org.opentele.server.util.ScheduleViewModel
import spock.lang.Specification

@TestFor(QuestionnaireGroupService)
@TestMixin(ControllerUnitTestMixin)
@Build([QuestionnaireHeader, QuestionnaireGroup, QuestionnaireGroup2QuestionnaireHeader])
class QuestionnaireGroupServiceSpec extends Specification {
    List<QuestionnaireHeader> questionnaireHeaders
    QuestionnaireGroup questionnaireGroup
    QuestionnaireGroup2QuestionnaireHeader questionnaireGroup2QuestionnaireHeader

    def setup() {
        questionnaireHeaders = (1..5).collect {
            QuestionnaireHeader.build(name: "QH$it")
        }
        questionnaireGroup = QuestionnaireGroup.build()
        questionnaireGroup2QuestionnaireHeader = QuestionnaireGroup2QuestionnaireHeader.build(questionnaireHeader: questionnaireHeaders.first(), questionnaireGroup: questionnaireGroup)
    }

    @SuppressWarnings("GroovyAccessibility")
    def "test that getUnusedQuestionnaireHeadersForQuestionnaireGroup returns the right questionnaire headers"() {
        when:
        def list = service.getUnusedQuestionnaireHeadersForQuestionnaireGroup(questionnaireGroup)

        then:
        list.size() == 4
        !list.contains(questionnaireHeaders.first())
    }

    def "test that viewModelForCreateAndEdit returns a valid viewModel when working with a new questionnaireGroup"() {
        setup:
        def newQuestionnaireGroup = QuestionnaireGroup.build()
        def newQuestionnaireGroup2Header = new QuestionnaireGroup2QuestionnaireHeader(questionnaireGroup: newQuestionnaireGroup)

        when:
        def viewModel = service.viewModelForCreateAndEdit(newQuestionnaireGroup2Header)

        then:
        viewModel.id == '""'
        viewModel.version == '""'
        viewModel.questionnaireGroupId == newQuestionnaireGroup.id
        !viewModel.scheduleType
        viewModel.timesOfDay == [[hour: 0, minute: 0]]
        !viewModel.weekdays
        !viewModel.daysInMonth
        !viewModel.intervalInDays
        viewModel.startingDate == new Date().format('dd-MM-yyyy')
        !viewModel.specificDate
        viewModel.reminderStartMinutes == 30
        viewModel.selectableQuestionnaires.size() == 5
        !viewModel.selectedQuestionnaireId
        !viewModel.validationErrors
    }

    def "test that viewModelForCreateAndEdit returns a valid viewModel when working with an existing questionnaireGroup"() {
        when:
        def viewModel = service.viewModelForCreateAndEdit(questionnaireGroup2QuestionnaireHeader)

        then:
        viewModel.id == 1
        viewModel.version == 0
        viewModel.questionnaireGroupId == questionnaireGroup.id
        !viewModel.scheduleType
        viewModel.timesOfDay == [[hour: 0, minute: 0]]
        !viewModel.weekdays
        !viewModel.daysInMonth
        !viewModel.intervalInDays
        viewModel.startingDate == new Date().format('dd-MM-yyyy')
        !viewModel.specificDate
        viewModel.reminderStartMinutes == 30
        viewModel.selectableQuestionnaires.size() == 5
        viewModel.selectedQuestionnaireId == 1
        !viewModel.validationErrors
    }

    def "test that viewModelForCreateAndEdit returns a valid viewModel when working with an existing questionnaireGroup with more than one questionnaireGroup2QuestionnaireHeader"() {
        setup:
        questionnaireGroup.addToQuestionnaireGroup2Header(questionnaireHeader: questionnaireHeaders.last())
        questionnaireGroup.save(failOnError: true)

        when:
        def viewModel = service.viewModelForCreateAndEdit(questionnaireGroup2QuestionnaireHeader)

        then:
        viewModel.id == 1
        viewModel.version == 1
        viewModel.questionnaireGroupId == questionnaireGroup.id
        !viewModel.scheduleType
        viewModel.timesOfDay == [[hour: 0, minute: 0]]
        !viewModel.weekdays
        !viewModel.daysInMonth
        !viewModel.intervalInDays
        viewModel.startingDate == new Date().format('dd-MM-yyyy')
        !viewModel.specificDate
        viewModel.reminderStartMinutes == 30
        viewModel.selectableQuestionnaires.size() == 4
        !viewModel.selectableQuestionnaires.any { it.name == "QH5" }
        viewModel.selectedQuestionnaireId == 1
        !viewModel.validationErrors
    }

    def "test that viewModelForCreateAndEdit returns a valid viewModel when working with an existing questionnaireGroup with standardSchedule"() {
        setup:
        def startingDay = new Date("2013/1/10")
        def schedule = new StandardSchedule(type: Schedule.ScheduleType.WEEKDAYS, internalStartingDate: startingDay, internalWeekdays: "MONDAY,TUESDAY", internalTimesOfDay: "10:00,11:00")
        questionnaireGroup2QuestionnaireHeader.standardSchedule = schedule

        when:
        def viewModel = service.viewModelForCreateAndEdit(questionnaireGroup2QuestionnaireHeader)

        then:
        viewModel.id == 1
        viewModel.version == 0
        viewModel.questionnaireGroupId == questionnaireGroup.id
        viewModel.scheduleType == "WEEKDAYS"
        viewModel.timesOfDay == [[hour: 10, minute: 0], [hour: 11, minute: 0]]
        viewModel.weekdays == ["MONDAY", "TUESDAY"]
        !viewModel.daysInMonth
        viewModel.intervalInDays == 2
        viewModel.startingDate == startingDay.format('dd-MM-yyyy')
        !viewModel.specificDate
        viewModel.reminderStartMinutes == 30
        viewModel.selectableQuestionnaires.size() == 5
        viewModel.selectedQuestionnaireId == 1
        !viewModel.validationErrors
    }

    def "test that viewModelForCreateAndEdit returns a valid viewModel when working with an existing questionnaireGroup and validationErrors"() {
        setup:
        def errors = [[field: "weekdays", message: 'weekdays.empty']]
        service.i18nService = Mock(I18nService)

        when:
        def viewModel = service.viewModelForCreateAndEdit(questionnaireGroup2QuestionnaireHeader, errors)

        then:
        1 * service.i18nService.message([code: 'weekdays.empty']) >> 'code'
        viewModel.validationErrors == [[field: 'weekdays', message: 'code']]
    }

    def "test that createOrUpdate can save a questionnaireGroup2QuestionnaireHeader with no previous questionnaireHeader and no standardSchedule before and after"() {
        setup:
        def questionnaireGroup2Header = QuestionnaireGroup2QuestionnaireHeader.build()
        def viewModel = new ScheduleViewModel(createViewModelJSON(questionnaireGroup2Header, [selectedQuestionnaire: [id: questionnaireHeaders.first().id], type: '']))

        when:
        def saved = service.createOrUpdate(questionnaireGroup2Header, viewModel)

        then:
        questionnaireGroup2Header.questionnaireHeader == questionnaireHeaders.first()
        !questionnaireGroup2Header.standardSchedule
        saved
    }

    def "test that createOrUpdate can save a new questionnaireGroup2QuestionnaireHeader with no questionnaireGroupd, no questionnaireHeader and no standardSchedule before and after"() {
        setup:
        def questionnaireGroup2Header = new QuestionnaireGroup2QuestionnaireHeader()
        def viewModel = new ScheduleViewModel(createViewModelJSON(questionnaireGroup2Header, [questionnaireGroupId: questionnaireGroup.id, selectedQuestionnaire: [id: questionnaireHeaders.first().id], type: '']))

        when:
        def saved = service.createOrUpdate(questionnaireGroup2Header, viewModel)

        then:
        questionnaireGroup2Header.questionnaireGroup == questionnaireGroup
        questionnaireGroup2Header.questionnaireHeader == questionnaireHeaders.first()
        !questionnaireGroup2Header.standardSchedule
        saved
    }

    def "test that createOrUpdate can save a questionnaireGroup2QuestionnaireHeader with no previous questionnaireHeader and new standardSchedule after"() {
        setup:
        def questionnaireGroup2Header = QuestionnaireGroup2QuestionnaireHeader.build()
        def viewModel = new ScheduleViewModel(createViewModelJSON(questionnaireGroup2Header, [selectedQuestionnaire: [id: questionnaireHeaders.first().id], type: 'UNSCHEDULED']))

        when:
        def saved = service.createOrUpdate(questionnaireGroup2Header, viewModel)

        then:
        questionnaireGroup2Header.questionnaireHeader == questionnaireHeaders.first()
        questionnaireGroup2Header.standardSchedule.type == Schedule.ScheduleType.UNSCHEDULED
        saved
    }

    def "test that createOrUpdate can save a questionnaireGroup2QuestionnaireHeader with no previous questionnaireHeader and standardSchedule before and none after"() {
        setup:
        def questionnaireGroup2Header = QuestionnaireGroup2QuestionnaireHeader.build(standardSchedule: new StandardSchedule(type: Schedule.ScheduleType.UNSCHEDULED))
        def viewModel = new ScheduleViewModel(createViewModelJSON(questionnaireGroup2Header, [selectedQuestionnaire: [id: questionnaireHeaders.first().id], type: '']))

        when:
        def saved = service.createOrUpdate(questionnaireGroup2Header, viewModel)

        then:
        questionnaireGroup2Header.questionnaireHeader == questionnaireHeaders.first()
        !questionnaireGroup2Header.standardSchedule
        saved
    }

    def "test that createOrUpdate can save a questionnaireGroup2QuestionnaireHeader with no previous questionnaireHeader and standardSchedule before and changed after"() {
        setup:
        def questionnaireGroup2Header = QuestionnaireGroup2QuestionnaireHeader.build(standardSchedule: new StandardSchedule(type: Schedule.ScheduleType.UNSCHEDULED))
        questionnaireGroup.addToQuestionnaireGroup2Header(questionnaireGroup2Header)
        questionnaireGroup.save(failOnError: true)

        def viewModel = new ScheduleViewModel(createViewModelJSON(questionnaireGroup2Header,
                [selectedQuestionnaire: [id: questionnaireHeaders.first().id], type: 'WEEKDAYS', weekdays: ["MONDAY", "SATURDAY"], timesOfDay: [[hour: "10", minute: "00"], [hour: "12",minute: "00"]]]))

        when:
        def saved = service.createOrUpdate(questionnaireGroup2Header, viewModel)

        then:
        questionnaireGroup2Header.questionnaireHeader == questionnaireHeaders.first()
        questionnaireGroup2Header.standardSchedule.type == Schedule.ScheduleType.WEEKDAYS
        questionnaireGroup2Header.standardSchedule.weekdays == [Weekday.MONDAY, Weekday.SATURDAY]
        questionnaireGroup2Header.standardSchedule.internalTimesOfDay == "10:00,12:00"
        saved
    }

    def "test that createOrUpdate can save a questionnaireGroup2QuestionnaireHeader with previous questionnaireHeader that changes"() {
        setup:
        def questionnaireGroup2Header = QuestionnaireGroup2QuestionnaireHeader.build(questionnaireHeader: questionnaireHeaders.last())
        def viewModel = new ScheduleViewModel(createViewModelJSON(questionnaireGroup2Header, [selectedQuestionnaire: [id: questionnaireHeaders.first().id], type: '']))

        expect:
        questionnaireGroup2Header.questionnaireHeader == questionnaireHeaders.last()

        when:
        def saved = service.createOrUpdate(questionnaireGroup2Header, viewModel)

        then:
        questionnaireGroup2Header.questionnaireHeader == questionnaireHeaders.first()
        !questionnaireGroup2Header.standardSchedule
        saved
    }

    def createViewModelJSON(QuestionnaireGroup2QuestionnaireHeader questionnaireGroup2Header, Map params = [:]) {
        def result = service.viewModelForCreateAndEdit(questionnaireGroup2Header)
        result << params
        return new JSON(result).toString()
    }
}
