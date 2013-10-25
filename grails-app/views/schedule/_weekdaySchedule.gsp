<%@ page import="org.opentele.server.model.Schedule; org.opentele.server.model.types.Weekday"%>
<div id="weekdayScheduleSetupContainer" data-bind='visible: scheduleType() === "${org.opentele.server.model.Schedule.ScheduleType.WEEKDAYS}"'>
    <input type="button" name="check" id="_check" value="Vælg alle dage" data-bind="click: allDays" class="btn btn-mini">
    <input type="button" name="uncheck" id="_uncheck" value="Fravælg alle dage" data-bind="click: noDays" class="btn btn-mini">

    <g:each in="${Weekday.values()*.toString()}" var="day">
        <div class="fieldcontain noborder">
            <label for="${toString()}">
                <g:message code="enum.weekday.${day}" />
            </label>
            <input type="checkbox" value="${day}" data-bind="checked: weekdays" id="${day}"/>
        </div>
    </g:each>
</div>
