<div id="reminderStartContainer" data-bind='visible: scheduleType() !== undefined && scheduleType() !== "${org.opentele.server.model.Schedule.ScheduleType.UNSCHEDULED}"'>
    <div class="fieldcontain">
        <label for="reminderStartMinutes" data-tooltip="${message(code: "schedule.reminderStart.tooltip")}">
            <g:message code="schedule.reminderStart.label"/>
        </label>
        <input id="reminderStartMinutes"
               data-bind="value: reminderStartMinutes, valueUpdate: 'afterkeydown'"  maxLength="4" size="4"
               class='input-mini'
               data-tooltip="${message(code: 'tooltip.patient.questionnaireSchedule.create.reminderStart')}" />
        <span>min. før</span>
    </div>
</div>


