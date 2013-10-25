package org.opentele.server.model
import grails.buildtestdata.mixin.Build
import grails.converters.JSON
import grails.test.mixin.TestFor
import org.opentele.builders.QuestionnaireHeaderBuilder
import org.opentele.builders.QuestionnaireScheduleBuilder
import org.opentele.server.model.Schedule.ScheduleType
import org.opentele.server.model.Schedule.StartingDate
import org.opentele.server.model.Schedule.TimeOfDay
import org.opentele.server.model.patientquestionnaire.PatientBooleanNode
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire
import org.opentele.server.model.questionnaire.BooleanNode
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.model.types.Month
import org.opentele.server.model.types.Weekday
import org.opentele.server.questionnaire.QuestionnaireService

import java.text.SimpleDateFormat

@TestFor(QuestionnaireScheduleController)
@Build([QuestionnaireSchedule, Patient, PatientQuestionnaire, QuestionnaireHeader, PatientBooleanNode, BooleanNode, MonitoringPlan])
class QuestionnaireScheduleControllerTests {
    def questionnaireSevice = new QuestionnaireService()

    def setQuestionnaireScheduleIdParam(schedule) {
        params["id"] = schedule.id
    }

    void setUp() {
        controller.questionnaireService = this.questionnaireSevice
    }

    void testEditViewModelHasVersionAndIdInViewModel() {
        def schedule = new QuestionnaireScheduleBuilder().build()
        setQuestionnaireScheduleIdParam(schedule)

        def viewModel = controller.edit()

        assert viewModel.id == schedule.id
        assert viewModel.version == schedule.version
    }

    void testEditViewModelHasMonitoringPlanId() {
        def schedule = new QuestionnaireScheduleBuilder().build()
        setQuestionnaireScheduleIdParam(schedule)

        assert controller.edit().monitoringPlanId == schedule.monitoringPlan.id
    }

    void testEditViewModelHasTimesOfDay() {
        def schedule = new QuestionnaireScheduleBuilder().forTimesOfDay([new TimeOfDay(hour: 12, minute: 0), new TimeOfDay(hour: 23, minute: 13)]).build();
        setQuestionnaireScheduleIdParam(schedule)

        assert controller.edit().timesOfDay == [[hour:12,minute:0],[hour:23,minute:13]]
    }

    void testEditViewModelHasSelectableQuestionnairesAsJSON() {
        def schedule = new QuestionnaireScheduleBuilder().build();
        setQuestionnaireScheduleIdParam(schedule)

        assert controller.edit().selectableQuestionnaires.toString() =~ /\[\{"id":1,"name":"TestQuestionnaireHeader[\d]+".*\}\]/
    }

    void testEditViewModelHasScheduleTypeUnscheduled() {
        def schedule = new QuestionnaireScheduleBuilder().forScheduleType(ScheduleType.UNSCHEDULED).build();
        setQuestionnaireScheduleIdParam(schedule)

        assert controller.edit().scheduleType == ScheduleType.UNSCHEDULED.name()
    }

    void testEditViewModelForWeekdaysHasWeekdays() {
        def schedule = new QuestionnaireScheduleBuilder().forScheduleType(ScheduleType.WEEKDAYS).forWeekdays([Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY]).build();
        setQuestionnaireScheduleIdParam(schedule)

        def viewModel = controller.edit()
        assert viewModel.scheduleType == ScheduleType.WEEKDAYS.name()
        assert viewModel.weekdays.toString() == '["MONDAY","WEDNESDAY","FRIDAY"]'
    }

    void testEditViewModelForMonthlyHasDaysInMonth() {
        def schedule = new QuestionnaireScheduleBuilder().forScheduleType(ScheduleType.MONTHLY).forDaysInMonth([2,4,6,8,19]).build();
        setQuestionnaireScheduleIdParam(schedule)

        assert controller.edit().scheduleType == ScheduleType.MONTHLY.name()
        assert "java.util.ArrayList" == [].class.name
        assertEquals "[\"2\",\"4\",\"6\",\"8\",\"19\"]", controller.edit().daysInMonth.toString()
    }

    void testEditViewModelForNthDayHasStartingDateAndIntervalInDays() {
        def schedule = new QuestionnaireScheduleBuilder().forScheduleType(ScheduleType.EVERY_NTH_DAY).forStartingDate(new Schedule.StartingDate(day: 2, month: Month.DECEMBER, year: 2012)).forIntervalInDays(4).build();
        setQuestionnaireScheduleIdParam(schedule)

        def viewModel = controller.edit()

        assert viewModel.startingDate == '02-12-2012'
        assert viewModel.intervalInDays == 4
        assert viewModel.scheduleType == ScheduleType.EVERY_NTH_DAY.name()
    }

    void testUpdateWillUpdateScheduleType() {

        Clinician clinician = Clinician.build()
        clinician.user = new User()

        def mockSpringSecurityService = mockFor(grails.plugins.springsecurity.SpringSecurityService)
        mockSpringSecurityService.metaClass.getCurrentUser = { ->
            clinician.getUser()
        }

        controller.springSecurityService = mockSpringSecurityService

        def schedule = new QuestionnaireScheduleBuilder().forScheduleType(ScheduleType.WEEKDAYS).build();
        def viewModel = buildViewModel(schedule, ScheduleType.UNSCHEDULED)
        params["viewModel"] = viewModel

        controller.update()

        assert schedule.type == ScheduleType.UNSCHEDULED
    }

    void testUpdateWillUpdateDaysOfWeek() {

        Clinician clinician = Clinician.build()
        clinician.user = new User()

        def mockSpringSecurityService = mockFor(grails.plugins.springsecurity.SpringSecurityService)
        mockSpringSecurityService.metaClass.getCurrentUser = { ->
            clinician.getUser()
        }

        controller.springSecurityService = mockSpringSecurityService

        def schedule = new QuestionnaireScheduleBuilder().forScheduleType(ScheduleType.WEEKDAYS).forWeekdays([Weekday.MONDAY, Weekday.FRIDAY]).build();
        def viewModel = buildViewModel(schedule, [Weekday.TUESDAY], schedule.timesOfDay)
        params["viewModel"] = viewModel

        controller.update()

        assert schedule.weekdays == [Weekday.TUESDAY]
    }

    void testUpdateWillUpdateDaysInMonth() {

        Clinician clinician = Clinician.build()
        clinician.user = new User()

        def mockSpringSecurityService = mockFor(grails.plugins.springsecurity.SpringSecurityService)
        mockSpringSecurityService.metaClass.getCurrentUser = { ->
            clinician.getUser()
        }

        controller.springSecurityService = mockSpringSecurityService

        def schedule = new QuestionnaireScheduleBuilder().forScheduleType(ScheduleType.MONTHLY).forDaysInMonth([1,4,6,8]).build()
        def viewModel = buildViewModelForDaysInMonth(schedule, [1,2,3,4,5,6,7,8,9,28])

        params["viewModel"] = viewModel
        controller.update()

        assert schedule.daysInMonth == [1,2,3,4,5,6,7,8,9,28]
    }

    void testUpdateWillUpdateIntervalInDatesAndStartingDate() {
        Clinician clinician = Clinician.build()
        clinician.user = new User()

        def mockSpringSecurityService = mockFor(grails.plugins.springsecurity.SpringSecurityService)
        mockSpringSecurityService.metaClass.getCurrentUser = { ->
            clinician.getUser()
        }

        controller.springSecurityService = mockSpringSecurityService

        def schedule = new QuestionnaireScheduleBuilder().forScheduleType(ScheduleType.EVERY_NTH_DAY).forIntervalInDays(4).forStartingDate(new StartingDate(day: 15, month: Month.DECEMBER, year: 2012)).build()
        def viewModel = buildViewModelForStartingDate(schedule, 4, new StartingDate(day: 15, month: Month.JANUARY, year: 2013))

        params.viewModel = viewModel
        controller.update()

        assert schedule.dayInterval == 4
        assert schedule.startingDate == new StartingDate(day: 15, month: Month.JANUARY, year: 2013)
    }

    void testUpdateWillUpdateSelectedQuestionnaire() {
        Clinician clinician = Clinician.build()
        clinician.user = new User()

        def mockSpringSecurityService = mockFor(grails.plugins.springsecurity.SpringSecurityService)
        mockSpringSecurityService.metaClass.getCurrentUser = { ->
            clinician.getUser()
        }

        controller.springSecurityService = mockSpringSecurityService

        def schedule = new QuestionnaireScheduleBuilder().build()
        def questionnaireHeader = new QuestionnaireHeaderBuilder().forName("NewQuestionnaire").build()
        def viewModel = buildViewModelForSelectedQuestionnaire(schedule, questionnaireHeader)

        params.viewModel = viewModel
        controller.update()

        assert schedule.questionnaireHeader == questionnaireHeader
    }

    void testUpdateWillCatchIllegalTimesOfDay() {
        def schedule = new QuestionnaireScheduleBuilder().forScheduleType(ScheduleType.WEEKDAYS).forWeekdays([Weekday.TUESDAY]).build()
        def viewModel = buildViewModelWithIllegalTimesOfDay(schedule, [[hour: "xx", minute: "00"], [hour: 12, minute: "xx"]])

        params.viewModel = viewModel
        controller.update()

        assert model.validationErrors.target.field.first() == "timesOfDay"
    }

    void testUpdateWillCatchEmptyTimesOfDay() {
        def schedule = new QuestionnaireScheduleBuilder().forScheduleType(ScheduleType.WEEKDAYS).forWeekdays([Weekday.TUESDAY]).build()
        def viewModel = buildViewModelWithEmptyTimesOfDay(schedule)

        params.viewModel = viewModel
        controller.update()

        assert model.validationErrors.target.field.first() == "timesOfDay"
    }

    void testUpdateWillCatchEmptyDaysInMonth() {
        def schedule = new QuestionnaireScheduleBuilder().forScheduleType(ScheduleType.MONTHLY).build()
        def viewModel = buildViewModelWithEmptyDaysInMonth(schedule)

        params.viewModel = viewModel
        controller.update()

        assert model.validationErrors.target.field.first() == "daysInMonth"
    }

    void testUpdateWillCatchEmptyIntervalInDays() {
        def schedule = new QuestionnaireScheduleBuilder().forScheduleType(ScheduleType.EVERY_NTH_DAY).build()
        def viewModel = buildViewModelWithEmptyIntervalInDays(schedule)

        params.viewModel = viewModel
        controller.update()

        assert model.validationErrors.target.field.first() == "intervalInDays"
    }

    void testUpdateWillCatchIllegalIntervalInDays() {
        def schedule = new QuestionnaireScheduleBuilder().forScheduleType(ScheduleType.EVERY_NTH_DAY).build()
        def viewModel = buildViewModelWithIllegalIntervalInDays(schedule)

        params.viewModel = viewModel
        controller.update()

        assert model.validationErrors.target.field.first() == "intervalInDays"
    }

    void testUpdateWillCatchNegativeIntervalInDays() {
        def schedule = new QuestionnaireScheduleBuilder().forScheduleType(ScheduleType.EVERY_NTH_DAY).build()
        def viewModel = buildViewModelWithNegativeIntervalInDays(schedule)

        params.viewModel = viewModel
        controller.update()

        assert model.validationErrors.target.field.first() == "intervalInDays"
    }

    void testUpdateWillCatchEmptyStartingDate() {
        def schedule = new QuestionnaireScheduleBuilder().forScheduleType(ScheduleType.EVERY_NTH_DAY).forIntervalInDays(4).build()
        def viewModel = buildViewModelWithEmptyStartingDate(schedule)

        params.viewModel = viewModel
        controller.update()

        assert model.validationErrors.target.field.first() == "startingDate"
    }

    void testUpdateWillCatchIllegalStartingDate() {
        def schedule = new QuestionnaireScheduleBuilder().forScheduleType(ScheduleType.EVERY_NTH_DAY).forIntervalInDays(4).build()
        def viewModel = buildViewModelWithIllegalStartingDate(schedule)

        params.viewModel = viewModel
        controller.update()

        assert model.validationErrors.target.field.first() == "startingDate"
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/monitorPlan/showplan'
    }

    private def buildViewModelWithIllegalStartingDate(QuestionnaireSchedule schedule) {
        buildViewModel(schedule.id, schedule.version, schedule.type, schedule.weekdays, schedule.timesOfDay, schedule.daysInMonth, schedule.dayInterval, "31/14/NotADate", schedule.reminderStartMinutes, schedule.questionnaireHeader)
    }

    private def buildViewModelWithEmptyStartingDate(QuestionnaireSchedule schedule) {
        buildViewModel(schedule.id, schedule.version, schedule.type, schedule.weekdays, schedule.timesOfDay, schedule.daysInMonth, schedule.dayInterval, '', schedule.reminderStartMinutes, schedule.questionnaireHeader)
    }

    private def buildViewModelWithNegativeIntervalInDays(QuestionnaireSchedule schedule) {
        buildViewModel(schedule.id, schedule.version, schedule.type, schedule.weekdays, schedule.timesOfDay, schedule.daysInMonth, -1, schedule.startingDate, schedule.reminderStartMinutes, schedule.questionnaireHeader)
    }

    private def buildViewModelWithIllegalIntervalInDays(QuestionnaireSchedule schedule) {
        buildViewModel(schedule.id, schedule.version, schedule.type, schedule.weekdays, schedule.timesOfDay, schedule.daysInMonth, "hej", schedule.startingDate, schedule.reminderStartMinutes, schedule.questionnaireHeader)
    }

    private def buildViewModelWithEmptyIntervalInDays(QuestionnaireSchedule schedule) {
        buildViewModel(schedule.id, schedule.version, schedule.type, schedule.weekdays, schedule.timesOfDay, schedule.daysInMonth, [], schedule.startingDate, schedule.reminderStartMinutes, schedule.questionnaireHeader)
    }

    private def buildViewModelWithEmptyDaysInMonth(QuestionnaireSchedule schedule) {
        buildViewModel(schedule.id, schedule.version, schedule.type, schedule.weekdays, schedule.timesOfDay, [], schedule.dayInterval, schedule.startingDate, schedule.reminderStartMinutes, schedule.questionnaireHeader)
    }

    private def buildViewModelWithIllegalTimesOfDay(QuestionnaireSchedule schedule, illegalTimesOfDay) {
        buildViewModel(schedule.id, schedule.version, schedule.type, schedule.weekdays, illegalTimesOfDay, schedule.daysInMonth, schedule.dayInterval, schedule.startingDate, schedule.reminderStartMinutes, schedule.questionnaireHeader)
    }

    private def buildViewModelWithEmptyTimesOfDay(QuestionnaireSchedule schedule) {
        def viewModel = "{" +
                "\"id\":${schedule.id}," +
                "\"version\":${schedule.version}," +
                "\"monitoringPlanId\":1," +
                "\"type\":\"${schedule.type.name()}\"," +
                "\"timesOfDay\":[]," +
                "\"weekdays\":${schedule.weekdays.collect({it.name()}) as JSON}," +
                "\"daysInMonth\":${schedule.daysInMonth as JSON}," +
                "\"intervalInDays\":${schedule.dayInterval}," +
                "\"startingDate\":\"${schedule.startingDate}\"," +
                "\"selectedQuestionnaire\":${[id:schedule.questionnaireHeader.id, name:schedule.questionnaireHeader.name] as JSON}" +
                "}"

        viewModel
    }

    private def buildViewModelForSelectedQuestionnaire(QuestionnaireSchedule schedule, QuestionnaireHeader questionnaireHeader) {
        buildViewModel(schedule.id, schedule.version, schedule.type, schedule.weekdays, schedule.timesOfDay, schedule.daysInMonth, schedule.dayInterval, schedule.startingDate, schedule.reminderStartMinutes, questionnaireHeader)
    }

    private def buildViewModelForDaysInMonth(QuestionnaireSchedule schedule, List<Integer> daysInMonth) {
        buildViewModel(schedule.id, schedule.version, schedule.type, schedule.weekdays, schedule.timesOfDay, daysInMonth, schedule.dayInterval, schedule.startingDate, schedule.reminderStartMinutes, schedule.questionnaireHeader)
    }

    private def buildViewModelForStartingDate(QuestionnaireSchedule schedule, Integer intervalInDays, StartingDate startingDate) {
        buildViewModel(schedule.id, schedule.version, schedule.type, schedule.weekdays, schedule.timesOfDay, schedule.daysInMonth, intervalInDays, startingDate, schedule.reminderStartMinutes, schedule.questionnaireHeader)
    }

    private def buildViewModel(QuestionnaireSchedule schedule, List<Weekday> weekdays, List<TimeOfDay> timesOfDay) {
        buildViewModel(schedule.id, schedule.version, schedule.type, weekdays, timesOfDay, schedule.daysInMonth, schedule.dayInterval, schedule.startingDate, schedule.reminderStartMinutes, schedule.questionnaireHeader)
    }

    private def buildViewModel(QuestionnaireSchedule schedule, ScheduleType type) {
        buildViewModel(schedule.id, schedule.version, type, schedule.weekdays, schedule.timesOfDay, schedule.daysInMonth, schedule.dayInterval, schedule.startingDate, schedule.reminderStartMinutes, schedule.questionnaireHeader)
    }

    private def buildViewModel(long id, long version, ScheduleType type, List<Weekday> weekdays, timesOfDay, List<Integer> daysInMonth, intervalInDays, StartingDate startingDate, reminderStartMinutes, QuestionnaireHeader questionnaireHeader) {
        buildViewModel(id, version, type, weekdays, timesOfDay, daysInMonth, intervalInDays, formatDate(startingDate.calendar.getTime(), "dd/MM/yyyy"), reminderStartMinutes, questionnaireHeader)
    }

    private def buildViewModel(long id, long version, ScheduleType type, List<Weekday> weekdays, timesOfDay, List<Integer> daysInMonth, intervalInDays, String startingDate, reminderStartMinutes, QuestionnaireHeader questionnaireHeader) {
        def viewModel = """
        {
            "id":${id},
            "version":${version},
            "monitoringPlanId":1,
            "type":"${type.name()}",
            "timesOfDay":${timesOfDay.collect({ [hour: it.hour, minute: it.minute] }) as JSON},
            "weekdays":${weekdays.collect({ it.name() }) as JSON},
            "daysInMonth":${daysInMonth as JSON},
            "intervalInDays":${intervalInDays},
            "startingDate":"${startingDate}",
            "reminderStartMinutes":"${reminderStartMinutes}",
            "selectedQuestionnaire":${[id: questionnaireHeader.id, name: questionnaireHeader.name] as JSON}
        }
        """.stripIndent().trim()

        viewModel
    }

    private def formatDate(date, format) {
        new SimpleDateFormat(format).format(date)
    }
}
