<script type="text/javascript">
    $(function() {
        $('#specificDate').datepicker({
            dateFormat: "dd-mm-yy",
            firstDay: 1,
            showOn: 'button',
            buttonImage: '${resource(dir: 'images', file: 'calendar.png')}'
        });
    });

</script>

<div id="nthScheduleSetupContainer" data-bind='visible: scheduleType() === "${org.opentele.server.model.Schedule.ScheduleType.SPECIFIC_DATE}"'>
    <div class="fieldcontain">
        <label for="specificDate">
            <g:message code="questionnaireSchedule.specificDate"/>
        </label>
        <span class="ui-datepicker-opentele-knockout">
            <input type="text" id="specificDate"
               placeholder="dd-MM-yyyy"
               class="input-small"
               maxLength="10" size="10"
               data-bind="value: specificDate"
               data-tooltip="${message(code: 'tooltip.patient.questionnaireSchedule.create.specificdate')}" />
        </span>
    </div>
</div>


