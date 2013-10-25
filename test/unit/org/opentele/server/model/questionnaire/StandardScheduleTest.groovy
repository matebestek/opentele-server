package org.opentele.server.model.questionnaire
import grails.test.mixin.Mock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.opentele.server.model.Schedule

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

@TestMixin(GrailsUnitTestMixin)
@Mock([Questionnaire])
class StandardScheduleTest {
    def questionnaire

    void setUp() {
        questionnaire = new Questionnaire(revision: 1, creationDate: new Date(), questionnaireHeader: new QuestionnaireHeader())
        questionnaire.standardSchedule = new StandardSchedule()
    }

    void testDefaultValuesForStandardSchedule() {
        def standardSchedule = new StandardSchedule()
        assertEquals(Schedule.ScheduleType.UNSCHEDULED, standardSchedule.type)
    }

    void testQuestionnaireValidWithoutStandardSchedule()
    {
        assertTrue questionnaire.validate()
    }

    void testAnyStandardScheduleIsValid()
    {
        questionnaire.standardSchedule = new StandardSchedule()
        assertTrue questionnaire.validate()
    }

    void testSaveAndGet() {
        def questionnaire = new Questionnaire(revision: 1, creationDate: new Date(), questionnaireHeader: new QuestionnaireHeader())
        def standardSchedule = new StandardSchedule(
                type: Schedule.ScheduleType.WEEKDAYS
        )
        questionnaire.standardSchedule = standardSchedule
        questionnaire.save(failOnError: true)
        questionnaire = Questionnaire.get(questionnaire.id)

        assertEquals(Schedule.ScheduleType.WEEKDAYS, questionnaire.standardSchedule.type)
    }
}
