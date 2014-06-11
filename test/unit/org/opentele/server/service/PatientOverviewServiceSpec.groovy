package org.opentele.server.service

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import org.opentele.builders.CompletedQuestionnaireBuilder
import org.opentele.builders.QuestionnaireHeaderBuilder
import org.opentele.server.PatientOverviewService
import org.opentele.server.model.Clinician
import org.opentele.server.model.Message
import org.opentele.server.model.Patient
import org.opentele.server.model.PatientOverview
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.patientquestionnaire.PatientBooleanNode
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire
import org.opentele.server.model.questionnaire.BooleanNode
import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.model.types.Severity
import org.opentele.server.questionnaire.QuestionnaireService
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(PatientOverviewService)
@Build([Patient, PatientOverview, Clinician, PatientQuestionnaire, BooleanNode, PatientBooleanNode, CompletedQuestionnaire, Questionnaire, Message])
class PatientOverviewServiceSpec extends Specification {
    Patient patient

    def setup() {
        patient = Patient.build(firstName: 'Nancy Ann', lastName: 'Berggreen', cpr: '1234567890')
        service.questionnaireService = Mock(QuestionnaireService)
        service.questionnaireService.worstSeverityOfUnacknowledgedQuestionnaires(_, []) >> Severity.NONE
    }

    def 'fills out first name and last name'() {
        when:
        service.createOverviewFor(patient)

        then:
        createdOverview.name == 'Nancy Ann Berggreen'
        createdOverview.patient == patient
        !createdOverview.important
    }

    def 'can update existing patient overview'() {
        setup:
        service.createOverviewFor(patient)
        patient.firstName = 'Olav'

        when:
        service.updateOverviewFor(patient)

        then:
        createdOverview.name == 'Olav Berggreen'
    }

    def 'fills out cpr number'() {
        when:
        service.createOverviewFor(patient)

        then:
        createdOverview.cpr == '1234567890'
        !createdOverview.important
    }

    def 'calculates number of unacknowledged questionnaires'() {
        setup:
        new CompletedQuestionnaireBuilder().forPatient(patient).build()
        new CompletedQuestionnaireBuilder().forPatient(patient).build()
        new CompletedQuestionnaireBuilder().forPatient(patient).acknowledged().build()
        service.questionnaireService.worstSeverityOfUnacknowledgedQuestionnaires(_, _) >> Severity.NONE

        when:
        service.createOverviewFor(patient)

        then:
        createdOverview.numberOfUnacknowledgedQuestionnaires == 2
        createdOverview.important
    }

    def 'finds worst severity of unacknowledged questionnaires'() {
        setup:
        def unacknowledgedQuestionnaire1 = new CompletedQuestionnaireBuilder().forPatient(patient).ofSeverity(Severity.ORANGE).build()
        def unacknowledgedQuestionnaire2 = new CompletedQuestionnaireBuilder().forPatient(patient).build()
        new CompletedQuestionnaireBuilder().forPatient(patient).acknowledged().build()
        service.questionnaireService.worstSeverityOfUnacknowledgedQuestionnaires(patient, { it.size() == 2 && it.contains(unacknowledgedQuestionnaire1) && it.contains(unacknowledgedQuestionnaire2)}) >> Severity.ORANGE

        when:
        service.createOverviewFor(patient)

        then:
        createdOverview.questionnaireSeverity == Severity.ORANGE
        createdOverview.important
    }

    @Unroll
    def 'finds questionnaire name and date for most recent, most severe unacknowledged questionnaire'() {
        setup:
        questionnaires.each {
            String name = it[0]
            Severity severity = it[1]
            Date date = Date.parse('yyyy-MM-dd', it[2])

            new CompletedQuestionnaireBuilder().forPatient(patient).forName(name).ofSeverity(severity).uploadedAt(date).build()
        }
        service.questionnaireService.worstSeverityOfUnacknowledgedQuestionnaires(patient, _) >> worstSeverity

        when:
        service.createOverviewFor(patient)

        then:
        createdOverview.mostSevereQuestionnaireName == expectedQuestionnaireName
        createdOverview.mostSevereQuestionnaireDate == parseDate(expectedQuestionnaireDate)
        createdOverview.important == important

        where:
        questionnaires                                                                                                        | worstSeverity   | expectedQuestionnaireName | expectedQuestionnaireDate | important
        []                                                                                                                    | Severity.NONE   |                      null |                      null |     false
        [['Q1', Severity.GREEN, '2012-05-13']]                                                                                | Severity.GREEN  |                      'Q1' |              '2012-05-13' |      true
        [['Q1', Severity.RED,   '2013-08-11']]                                                                                | Severity.RED    |                      'Q1' |              '2013-08-11' |      true
        [['Q1', Severity.ORANGE, '2012-05-13'], ['Q2', Severity.ORANGE, '2013-03-14'], ['Q3', Severity.ORANGE, '2013-02-12']] | Severity.ORANGE |                      'Q2' |              '2013-03-14' |      true
        [['Q1', Severity.RED, '2012-05-13'], ['Q2', Severity.ORANGE, '2013-03-14'], ['Q3', Severity.ORANGE, '2013-02-12']]    | Severity.RED    |                      'Q1' |              '2012-05-13' |      true
        [['Q1', Severity.ORANGE, '2012-05-13'], ['Q2', Severity.ORANGE, '2013-03-14']]                                        | Severity.BLUE   |                      null |                      null |      true
        [['Q1', Severity.ORANGE, '2012-05-13'], ['Q2', Severity.YELLOW, '2013-03-14']]                                        | Severity.YELLOW |                      'Q2' |              '2013-03-14' |      true
    }

    @Unroll
    def 'concatenates list of blue alarm questionnaire names'() {
        setup:
        def patientQuestionnaires = [:]
        patientQuestionnaires['Q1'] = PatientQuestionnaire.build(templateQuestionnaire: Questionnaire.build(questionnaireHeader: new QuestionnaireHeaderBuilder().forName('Q1').build()))
        patientQuestionnaires['Q2'] = PatientQuestionnaire.build(templateQuestionnaire: Questionnaire.build(questionnaireHeader: new QuestionnaireHeaderBuilder().forName('Q2').build()))

        patient.blueAlarmQuestionnaireIDs = new HashSet(blueAlarmQuestionnaires.collect { patientQuestionnaires[it].id })

        CompletedQuestionnaire completedQuestionnaire = new CompletedQuestionnaireBuilder().forPatient(patient).build()
        service.questionnaireService.worstSeverityOfUnacknowledgedQuestionnaires(patient, [completedQuestionnaire]) >> worstSeverity

        when:
        service.createOverviewFor(patient)

        then:
        createdOverview.blueAlarmText == expectedBlueAlarmText
        createdOverview.important

        where:
        worstSeverity  | blueAlarmQuestionnaires | expectedBlueAlarmText
        Severity.GREEN |                      [] |                  null
        Severity.RED   |            ['Q1', 'Q2'] |              'Q1\nQ2'
        Severity.BLUE  |                  ['Q1'] |                  'Q1'
        Severity.BLUE  |            ['Q1', 'Q2'] |              'Q1\nQ2'
    }

    @Unroll
    def 'finds IDs of unacknowledged, green questionnaires'() {
        setup:
        def questionnaires = [:]
        acknowledgedQuestionnaires.each {
            String name = it
            questionnaires[name] = new CompletedQuestionnaireBuilder().forPatient(patient).acknowledged().build()
        }
        unacknowledgedQuestionnaires.each {
            String name = it[0]
            Severity severity = it[1]
            questionnaires[name] = new CompletedQuestionnaireBuilder().forPatient(patient).ofSeverity(severity).build()
        }

        def questionnaireIdsFromString = { idsAsString -> idsAsString == null ? null : idsAsString.split(',').toList().toSet() }
        def questionnaireIdsFromNames = { names -> names == null ? null : names.collect { questionnaires[it].id.toString() }.toSet() }

        service.questionnaireService.worstSeverityOfUnacknowledgedQuestionnaires(patient, _) >> Severity.NONE

        when:
        service.createOverviewFor(patient)

        then:
        questionnaireIdsFromString(createdOverview.greenQuestionnaireIds) == questionnaireIdsFromNames(expectedQuestionnaireNames)
        createdOverview.important == important

        where:
        acknowledgedQuestionnaires | unacknowledgedQuestionnaires                      | expectedQuestionnaireNames | important
        []                         | []                                                | null                       |     false
        ['Q1', 'Q2']               | []                                                | null                       |     false
        []                         | [['Q1', Severity.GREEN], ['Q2', Severity.GREEN]]  | ['Q1', 'Q2']               |      true
        ['Q1']                     | [['Q2', Severity.GREEN]]                          | ['Q2']                     |      true
        ['Q1']                     | [['Q2', Severity.GREEN], ['Q3', Severity.RED]]    | ['Q2']                     |      true
        ['Q1']                     | [['Q2', Severity.GREEN], ['Q3', Severity.YELLOW]] | ['Q2']                     |      true
        ['Q1']                     | [['Q2', Severity.RED], ['Q3', Severity.YELLOW]]   | null                       |      true
    }

    def 'calculates number of unread messages to patient, along with date of oldest unread'() {
        setup:
        Message.build(patient: patient, isRead: false, sentByPatient: false, sendDate: parseDate('2013-04-12'))
        Message.build(patient: patient, isRead: false, sentByPatient: false, sendDate: parseDate('2013-03-04'))
        Message.build(patient: patient, isRead: false, sentByPatient: false, sendDate: parseDate('2013-08-19'))

        when:
        service.createOverviewFor(patient)

        then:
        createdOverview.numberOfUnreadMessagesToPatient == 3
        createdOverview.dateOfOldestUnreadMessageToPatient == parseDate('2013-03-04')
        createdOverview.important
    }

    def 'does not trigger alarm if alarm is disabled for messages sent to patient'() {

        setup:
        Patient patientNo = Patient.build(firstName: 'No To', lastName: 'Alarms', cpr: '1234567891', noAlarmIfUnreadMessagesToPatient: true)

        Message.build(patient: patientNo, isRead: false, sentByPatient: false, sendDate: parseDate('2013-03-04'))

        when:
        service.createOverviewFor(patientNo)

        then:
        createdOverview.numberOfUnreadMessagesToPatient == 1
        !createdOverview.important
    }

    def 'calculates number of unread messages from patient, along with date of oldest unread'() {
        setup:
        Message.build(patient: patient, isRead: false, sentByPatient: true, sendDate: parseDate('2013-05-12'))
        Message.build(patient: patient, isRead: false, sentByPatient: true, sendDate: parseDate('2013-04-04'))
        Message.build(patient: patient, isRead: false, sentByPatient: true, sendDate: parseDate('2013-09-19'))

        when:
        service.createOverviewFor(patient)

        then:
        createdOverview.numberOfUnreadMessagesFromPatient == 3
        createdOverview.dateOfOldestUnreadMessageFromPatient == parseDate('2013-04-04')
        createdOverview.important
    }

    def 'knows when overview details are correct'() {
        when:
        service.createOverviewFor(patient)

        then:
        !service.overviewDetailsAreWrongFor(patient)
    }

    def 'knows when overview details are wrong'() {
        when:
        service.createOverviewFor(patient)
        patient.patientOverview.blueAlarmText = 'This is wrong'

        then:
        service.overviewDetailsAreWrongFor(patient)
    }

    PatientOverview getCreatedOverview() {
        assert PatientOverview.all.size() == 1

        PatientOverview.all[0]
    }

    Date parseDate(String dateAsString) {
        if (dateAsString == null) {
            return null
        }
        Date.parse('yyyy-MM-dd', dateAsString)
    }
}
