<div id="monthlyScheduleSetupContainer" data-bind='visible: scheduleType() === "${org.opentele.server.model.Schedule.ScheduleType.MONTHLY}"'>
    <div class="fieldcontain">
        <label for="daysInMonth">
            <g:message code="questionnaireSchedule.daysInMonth"/>
        </label>
        <select class="input-small" multiple="yes" id="daysInMonth" data-bind="options: daysInMonthOptions, selectedOptions: daysInMonth" data-tooltip="${message(code: 'tooltip.patient.questionnaireSchedule.create.dayInMonth')}"></select>
    </div>
</div>


