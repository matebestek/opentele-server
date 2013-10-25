<%@ page import="org.opentele.server.model.Schedule" %>
<div data-bind='visible: scheduleType() !== undefined && scheduleType() !== "" && scheduleType() !== "${Schedule.ScheduleType.UNSCHEDULED}"'>
    <hr/>
    <tmpl:/schedule/timesOfDay/>
    <tmpl:/schedule/reminderStart/>
    <tmpl:/schedule/weekdaySchedule/>
    <tmpl:/schedule/monthlySchedule/>
    <tmpl:/schedule/nthDaySchedule/>
    <tmpl:/schedule/specificDate/>
</div>
