package org.opentele.server.model

class ScheduleWindow extends AbstractObject {

    Schedule.ScheduleType scheduleType
    int windowSizeMinutes

    static constraints = {
        scheduleType(unique: true)
    }
}
