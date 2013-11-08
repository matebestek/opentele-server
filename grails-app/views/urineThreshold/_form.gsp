<%@ page import="org.opentele.server.model.StandardThresholdSet" %>

<table>
	<thead>
		<tr>
			<th><g:message code="default.threshold.alertHigh.label" default="Alert High" /></th>
			<th><g:message code="default.threshold.warningHigh.label" default="Warning High" /></th>
			<th><g:message code="default.threshold.warningLow.label" default="Warning Low" /></th>
			<th><g:message code="default.threshold.alertLow.label" default="Alert Low" /></th>
		</tr>
	</thead>
	<tbody>
        <tmpl:/urineThreshold/threshold threshold="${standardThresholdInstance}" text=""/>
	</tbody>
</table>
