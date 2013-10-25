package org.opentele.server

import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.opentele.builders.CompletedQuestionnaireBuilder
import org.opentele.builders.MonitoringPlanBuilder
import org.opentele.builders.PatientBuilder
import org.opentele.builders.QuestionnaireScheduleBuilder
import org.opentele.server.model.MonitoringPlan
import org.opentele.server.model.QuestionnaireSchedule
import org.opentele.server.model.Schedule
import org.opentele.server.model.ScheduleWindow
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.patientquestionnaire.PatientBooleanNode
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire
import org.opentele.server.model.questionnaire.BooleanNode
import org.opentele.server.model.types.Month
import org.opentele.server.model.types.Weekday
import org.opentele.server.questionnaire.QuestionnaireService
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(QuestionnaireService)
@Build([QuestionnaireSchedule, PatientQuestionnaire, PatientBooleanNode, BooleanNode, ScheduleWindow])
@Mock(CompletedQuestionnaire)
class ScheduleWindowSpec extends Specification {

    QuestionnaireService service
    def patient
    def completedQuestionnaire
    def timesOfDay

    def setup() {
        ScheduleWindow.build(scheduleType: Schedule.ScheduleType.WEEKDAYS, windowSizeMinutes: 30)
        ScheduleWindow.build(scheduleType: Schedule.ScheduleType.MONTHLY, windowSizeMinutes: 30)
        ScheduleWindow.build(scheduleType: Schedule.ScheduleType.EVERY_NTH_DAY, windowSizeMinutes: 30)

        service = new QuestionnaireService()
        patient = new PatientBuilder().build()

        def createdDate = Date.parse("yyyy/M/d H:m:s", "2013/6/5 15:29:12").toCalendar()
        completedQuestionnaire = new CompletedQuestionnaireBuilder(createdDate: createdDate.getTime()).forPatient(patient).build()
        timesOfDay = [new Schedule.TimeOfDay(hour: 16, minute: 0)]
    }

    @Unroll
    def "Test that the window size can be adjusted."() {
        when:
        def checkWindow = ScheduleWindow.findByScheduleType(scheduleType)
        if (checkWindow != null) {
            checkWindow.windowSizeMinutes = windowSizeMinutes
            checkWindow.save()
        }

        Date startDate = Date.parse("yyyy/M/d", "2013/6/5")
        MonitoringPlan monitoringPlan = new MonitoringPlanBuilder().forPatient(patient).forStartDate(startDate).build();
        Schedule.StartingDate nthDayStartingDate = new Schedule.StartingDate(year: 2013, month: Month.JUNE, day: 5)
        new QuestionnaireScheduleBuilder(questionnaireHeader: completedQuestionnaire.questionnaireHeader).forMonitoringPlan(monitoringPlan).forScheduleType(scheduleType).forDaysInMonth([5]).forStartingDate(nthDayStartingDate).forWeekdays(Weekday.values().collect()).forTimesOfDay(timesOfDay).build()

        def checkDate = Date.parse("yyyy/M/d H:m:s", "2013/6/5 16:00:02").toCalendar()
        def blueAlarms = service.checkForBlueAlarms(patient, subtractOneMinute(checkDate), checkDate)

        then:
        blueAlarms.any() == hasBlueAlarm

        where:

        scheduleType                        | windowSizeMinutes | hasBlueAlarm
      //  Schedule.ScheduleType.WEEKDAYS      | 30                | true
        Schedule.ScheduleType.WEEKDAYS      | 35                | false
      //  Schedule.ScheduleType.EVERY_NTH_DAY | 30                | true
        Schedule.ScheduleType.EVERY_NTH_DAY | 35                | false
      //  Schedule.ScheduleType.MONTHLY       | 30                | true
        Schedule.ScheduleType.MONTHLY       | 35                | false
        Schedule.ScheduleType.UNSCHEDULED   | 30                | false
        Schedule.ScheduleType.UNSCHEDULED   | 35                | false
    }

    Calendar subtractOneMinute(Calendar c) {
        def result = c.clone()
        result.add(Calendar.MINUTE, -1)
        result
    }
}
