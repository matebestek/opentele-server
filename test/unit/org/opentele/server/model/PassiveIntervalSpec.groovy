package org.opentele.server.model

import spock.lang.Shared
import spock.lang.Specification
import grails.test.mixin.*
import grails.test.mixin.support.*
import spock.lang.Unroll

@TestMixin(GrailsUnitTestMixin)
@Mock([PassiveInterval])
class PassiveIntervalSpec extends Specification {

    @Shared
    def now = new Date()

    @Unroll
    void 'test null and before/after'() {
        setup:
        def interval = new PassiveInterval(intervalStartDate: start, intervalEndDate: end, patient: new Patient())

        when:
        interval.validate()

        then:
        interval.errors.hasErrors()
        interval.errors[field].code == expectedError

        where:
        start   | end  | field               | expectedError
        null    | now  | 'intervalStartDate' | 'nullable'
        now     | null | 'intervalEndDate'   | 'nullable'
        now + 1 | now  | 'intervalStartDate' | 'dateStartAfterEnd'

    }

    void 'test sunshine case'() {
        setup:
        def interval = new PassiveInterval(intervalStartDate: now, intervalEndDate: now+1, patient: new Patient())
        expect:
        interval.validate()
    }

    @Unroll
    void 'test inInterval'() {
        when:
        def interval = new PassiveInterval(intervalStartDate: start, intervalEndDate: end, patient: new Patient())

        then:
        inInterval == interval.inInterval()

        where:
        start   | end     | inInterval
        now - 2 | now + 2 | true
        now - 2 | now - 1 | false
        now + 1 | now + 2 | false
    }
}
