<%@ page import="org.opentele.server.model.PassiveInterval" %>


<div class="fieldcontain ${hasErrors(bean: passiveIntervalInstance, field: 'intervalStartDate', 'error')} required">
    <label for="intervalStartDate">
        <g:message code="passiveInterval.intervalStartDate.label" default="Interval Start Date" />
        <span class="required-indicator">*</span>
    </label>
        <jq:datePicker default="none" name="intervalStartDate" precision="day" years="${2013..2050}" value="${passiveIntervalInstance?.intervalStartDate}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: passiveIntervalInstance, field: 'intervalEndDate', 'error')} required">
	<label for="intervalEndDate">
		<g:message code="passiveInterval.intervalEndDate.label" default="Interval End Date" />
		<span class="required-indicator">*</span>
	</label>
        <jq:datePicker default="none" name="intervalEndDate" precision="day" years="${2013..2050}" value="${passiveIntervalInstance?.intervalEndDate}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: passiveIntervalInstance, field: 'comment', 'error')} ">
    <label for="comment">
        <g:message code="passiveInterval.comment.label" default="i18n Missing"/>
    </label>
    <g:textArea name="comment" value="${passiveIntervalInstance?.comment}"/>
</div>


