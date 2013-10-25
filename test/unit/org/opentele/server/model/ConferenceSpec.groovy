package org.opentele.server.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.*
import grails.validation.ValidationException
import spock.lang.Specification

@TestFor(Conference)
@Build([Patient, Clinician])
class ConferenceSpec extends Specification {
    def 'requires patient'() {
        when:
        new Conference(clinician: Clinician.build()).save(failOnError: true)

        then:
        thrown(ValidationException)
    }

    def 'requires clinician'() {
        when:
        new Conference(patient: Patient.build()).save(failOnError: true)

        then:
        thrown(ValidationException)
    }

    def 'can be created for patient and clinician'() {
        when:
        new Conference(patient: Patient.build(), clinician: Clinician.build()).save(failOnError: true)

        then:
        true // a.k.a. "it does not crash"
    }
}
