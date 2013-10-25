<div id="timeOfDayContainer" data-bind='visible: scheduleType() !== undefined && scheduleType() !== "${org.opentele.server.model.Schedule.ScheduleType.UNSCHEDULED}"'>
    <div class="fieldcontain">
        <label data-tooltip="${message(code: "schedule.timesOfDay.tooltip")}"><g:message code="schedule.timesOfDay.label"/></label>
        <span data-bind="foreach: timesOfDay" style="display: inline-block;">
            <div style="white-space: nowrap;">
                <input data-bind="value: hour, valueUpdate: 'afterkeydown'" maxLength="2" size="2" class="twoCharacterInput" data-tooltip="${message(code: 'tooltip.patient.questionnaireSchedule.create.hours')}"/>
                :
                <input data-bind="value: minute, valueUpdate: 'afterkeydown'"  maxLength="2" size="2" class="twoCharacterInput" data-tooltip="${message(code: 'tooltip.patient.questionnaireSchedule.create.minutes')}"/>
                <g:img file="delete.png" data-bind="click: \$root.removeTimeOfDay" data-tooltip="${message(code: 'tooltip.questionnaireSchedule.removeTimeOfDay')}"/>
            </div>
        </span>
        <g:img file="add.png" data-bind="click: addTimeOfDay" data-tooltip="${message(code: 'tooltip.questionnaireSchedule.addTimeOfDay')}"/>
    </div>
</div>

