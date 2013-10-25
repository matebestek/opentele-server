package org.opentele.server.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.*
import org.junit.*
import spock.lang.Specification

@TestFor(BlueAlarmCheck)
@Build(BlueAlarmCheck)
class BlueAlarmCheckSpec extends Specification {
    Date t1
    Date t2
    Date t3

    def setup() {
        t1 = new Date(2014,1,1,0,10,20)
        t2 = new Date(2014,1,1,10,20,30)
        t3 = new Date(2014,1,1,20,30,40)
    }

    void "Can find the previous blue alarm check if present"() {
        setup:
        BlueAlarmCheck.build(checkDate: t1)
        BlueAlarmCheck.build(checkDate: t2)

        when:
        def previous = BlueAlarmCheck.findPreviousCheckTime(t3)

        then:
        previous == t2
    }

    void "yields parameter if no previous time is found"() {
        setup:
        BlueAlarmCheck.build(checkDate: t2)

        when:
        def previous = BlueAlarmCheck.findPreviousCheckTime(t1)

        then:
        previous == t1
    }

    void "can create new check"() {
        setup:
        BlueAlarmCheck.checkedAt(t1)

        when:
        def previous = BlueAlarmCheck.findPreviousCheckTime(t2)

        then:
        previous == t1
    }
}
