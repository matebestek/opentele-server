package org.opentele.server.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(Patient)
@Build([Patient,Measurement, MeasurementType, CompletedQuestionnaire])
class PatientSpec extends Specification {
    Patient patient
    def setup() {
        patient = Patient.build()
    }


    def "test that getLatestQuestionnaireUploadDate works as expected"() {
        setup:
        [2,4,1].each { day ->
            def completedQuestionnaire = CompletedQuestionnaire.build(uploadDate: new Date())
            completedQuestionnaire.uploadDate[Calendar.DATE] = day
            patient.addToCompletedQuestionnaires(completedQuestionnaire)
        }
        patient.save()

        expect:
        patient.latestQuestionnaireUploadDate[Calendar.DATE] == 4
    }

    def "test that getLatestQuestionnaireUploadDate works as expected when there is no completed questionnaires"() {
        expect:
        !patient.latestQuestionnaireUploadDate
    }

    @Unroll
    def "test gestation age is computed correctly"() {
        when:
        patient.dueDate = dueDate

        then:
        expected == patient.getGestationalAge(now)

        where:
        now                                                                 | dueDate                                                       | expected
        new GregorianCalendar(2014, Calendar.FEBRUARY, 4        ).getTime() | new GregorianCalendar(2014, Calendar.FEBRUARY ,  5).getTime() | "39+6"
        new GregorianCalendar(2014, Calendar.FEBRUARY, 4        ).getTime() | new GregorianCalendar(2014, Calendar.APRIL    ,  6).getTime() | "31+2"
        new GregorianCalendar(2014, Calendar.FEBRUARY, 4        ).getTime() | new GregorianCalendar(2014, Calendar.SEPTEMBER,  8).getTime() | "9+1"
        new GregorianCalendar(2014, Calendar.FEBRUARY, 4        ).getTime() | new GregorianCalendar(2014, Calendar.FEBRUARY ,  4).getTime() | "40+0"
        new GregorianCalendar(2014, Calendar.FEBRUARY, 4, 15, 15).getTime() | new GregorianCalendar(2014, Calendar.FEBRUARY ,  4).getTime() | "40+0"
        new GregorianCalendar(2014, Calendar.FEBRUARY, 4, 23,  1).getTime() | new GregorianCalendar(2014, Calendar.FEBRUARY ,  4).getTime() | "40+0"
        new GregorianCalendar(2014, Calendar.FEBRUARY, 4        ).getTime() | new GregorianCalendar(2014, Calendar.JANUARY  , 14).getTime() | "43+0"
        new GregorianCalendar(2014, Calendar.FEBRUARY, 4        ).getTime() | new GregorianCalendar(2014, Calendar.JANUARY  , 15).getTime() | "42+6"
        new GregorianCalendar(2014, Calendar.FEBRUARY, 4        ).getTime() | new GregorianCalendar(2014, Calendar.JANUARY  , 13).getTime() | ""
        new GregorianCalendar(2014, Calendar.FEBRUARY, 4        ).getTime() | new GregorianCalendar(2014, Calendar.NOVEMBER , 10).getTime() | ""
        new GregorianCalendar(2014, Calendar.FEBRUARY, 4        ).getTime() | new GregorianCalendar(2015, Calendar.NOVEMBER , 11).getTime() | ""
    }
}
