package org.opentele.server.model

class BlueAlarmCheck extends AbstractObject {
    Date checkDate

    static constraints = {
        checkDate(nullable: false)
    }

    static Date findPreviousCheckTime(Date priorTo) {
        def previous = BlueAlarmCheck.withCriteria {
            lt("checkDate", priorTo)
            order("checkDate", "desc")
            maxResults(1)
        }
        previous.empty ? priorTo : previous.first().checkDate
    }

    static void checkedAt(Date checkTime) {
        new BlueAlarmCheck(checkDate: checkTime).save(failOnError: true)
    }
}
