package org.opentele.server.model.questionnaire
import grails.buildtestdata.mixin.Build
import org.opentele.server.model.User
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Build([QuestionnaireHeader,Questionnaire, User])
class QuestionnaireHeaderSpec extends Specification {
    QuestionnaireHeader sut
    @Shared Questionnaire questionnaire1
    @Shared Questionnaire questionnaire2


    def setup() {
        questionnaire1 = Questionnaire.build(questionnaireHeader: QuestionnaireHeader.build(name: "q1"))
        questionnaire2 = Questionnaire.build(questionnaireHeader: QuestionnaireHeader.build(name: "q2"))

        sut = QuestionnaireHeader.build(name: "name"+System.nanoTime())
        sut.addToQuestionnaires(questionnaire1)
    }

    @Unroll
    def "test activeQuestionnaire constraint sunshine scenario"() {
        when:
        sut.addToQuestionnaires(questionnaire2)
        sut.activeQuestionnaire = questionnaire

        then:
        sut.validate()

        where:
        questionnaire << [questionnaire2, null]
    }

    @Unroll
    def "test draftQuestionnaire constraint sunshine scenario"() {
        when:
        sut.addToQuestionnaires(questionnaire2)
        sut.draftQuestionnaire = questionnaire

        then:
        sut.validate()

        where:
        questionnaire << [questionnaire2, null]
    }

    def "test activeQuestionnaire constraint with errors"() {
        when:
        sut.activeQuestionnaire = questionnaire2

        then:
        !sut.validate()

        sut.errors[ "activeQuestionnaire" ].code == "activeQuestionnaire.not.in.questionnaires"
    }

    @Unroll
    def "test draftQuestionnaire constraint with errors"() {
        when:
        sut.activeQuestionnaire = questionnaire1

        sut.draftQuestionnaire = questionnaire

        then:
        !sut.validate()

        sut.errors['draftQuestionnaire'].code == expectedError

        where:
        questionnaire  | expectedError
        questionnaire1 | "draftQuestionnaire.cannot.be.equal.to.activeQuestionnaire"
        questionnaire2 | "draftQuestionnaire.not.in.questionnaires"
    }

}
