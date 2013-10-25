package org.opentele.server.model.questionnaire

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import org.opentele.builders.QuestionnaireHeaderBuilder
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(QuestionnaireGroup)
@Build([QuestionnaireHeader, Questionnaire, QuestionnaireGroup2QuestionnaireHeader,BooleanNode,PatientQuestionnaire, StandardSchedule])
class QuestionnaireGroupSpec extends Specification {


    def "test that a QuestionnaireGroup returns the active questionnaires schedule if no specific schedule is defined in the group"() {
        setup:
        def questionnaireHeader = new QuestionnaireHeaderBuilder().build()
        def questionnaireGroup = new QuestionnaireGroup(name: "Q-group")
        questionnaireGroup.addToQuestionnaireGroup2Header(questionnaireHeader: questionnaireHeader)

        expect:
        questionnaireGroup.save(failOnError: true)

        when:
        def questionnaireGroup2Header = questionnaireGroup.questionnaireGroup2Header.asList().first()

        then:
        questionnaireGroup2Header.schedule == questionnaireHeader.activeQuestionnaire.standardSchedule
    }

    def "test that a QuestionnaireGroup returns the group defined schedule if schedule is defined in the group"() {
        setup:
        def questionnaireHeader = new QuestionnaireHeaderBuilder().build()
        def questionnaireGroup = new QuestionnaireGroup(name: "Q-group")
        def schedule = new StandardSchedule()
        questionnaireGroup.addToQuestionnaireGroup2Header(questionnaireHeader: questionnaireHeader, standardSchedule: schedule)

        expect:
        questionnaireGroup.save(failOnError: true)

        when:
        def questionnaireGroup2Header = questionnaireGroup.questionnaireGroup2Header.asList().first()

        then:
        questionnaireGroup2Header.schedule == schedule
    }
}
