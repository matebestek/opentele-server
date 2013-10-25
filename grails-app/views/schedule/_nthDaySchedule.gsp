<r:script>
    $(function() {
        $('#startingDate').datepicker({
            dateFormat: "dd-mm-yy",
            firstDay: 1,
            showOn: 'button',
            buttonImage: '${resource(dir: 'images', file: 'calendar.png')}'
        });
    });
</r:script>
<div id="nthScheduleSetupContainer" data-bind='visible: scheduleType() === "${org.opentele.server.model.Schedule.ScheduleType.EVERY_NTH_DAY}"'>
    <div class="fieldcontain">
        <label for="intervalInDays">
            <g:message code="questionnaireSchedule.intervalDays"/>
        </label>
        <input type="text" data-bind="value: intervalInDays" class="twoCharacterInput" name="intervalInDays" id="intervalInDays"
                     maxLength="3" size="3"
                     data-tooltip="${message(code: 'tooltip.patient.questionnaireSchedule.create.dayInterval')}" />
    </div>
    <div class="fieldcontain">
        <label for="startingDate">
            <g:message code="questionnaireSchedule.nthDayScheduleStartingDate"/>
        </label>
        <span class="ui-datepicker-opentele-knockout">
            <input type="text" id="startingDate"
               placeholder="dd-MM-yyyy"
               class="input-small"
               maxLength="10" size="10"
               data-bind="value: startingDate"
               data-tooltip="${message(code: 'tooltip.patient.questionnaireSchedule.create.startdato')}"/>
        </span>
    </div>
</div>


