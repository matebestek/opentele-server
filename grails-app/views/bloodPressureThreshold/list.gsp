
<%@ page import="org.opentele.server.model.BloodPressureThreshold" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'bloodPressureThreshold.label', default: 'BloodPressureThreshold')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="list-bloodPressureThreshold" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
    			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
						<g:sortableColumn property="diastolicAlertHigh" title="${message(code: 'bloodPressureThreshold.diastolicAlertHigh.label', default: 'Diastolic Alert High')}" />
						<g:sortableColumn property="diastolicWarningHigh" title="${message(code: 'bloodPressureThreshold.diastolicWarningHigh.label', default: 'Diastolic Warning High')}" />
						<g:sortableColumn property="diastolicWarningLow" title="${message(code: 'bloodPressureThreshold.diastolicWarningLow.label', default: 'Diastolic Warning Low')}" />
						<g:sortableColumn property="diastolicAlertLow" title="${message(code: 'bloodPressureThreshold.diastolicAlertLow.label', default: 'Diastolic Alert Low')}" />
						<g:sortableColumn property="systolicAlertHigh" title="${message(code: 'bloodPressureThreshold.systolicAlertHigh.label', default: 'Systolic Alert High')}" />
						<g:sortableColumn property="systolicWarningHigh" title="${message(code: 'bloodPressureThreshold.systolicWarningHigh.label', default: 'Systolic Warning High')}" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${standardThresholdInstanceList}" status="i" var="standardThresholdInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td><g:link action="show" id="${standardThresholdInstance.id}">${fieldValue(bean: standardThresholdInstance, field: "diastolicAlertHigh")}</g:link></td>
						<td>${fieldValue(bean: standardThresholdInstance, field: "diastolicWarningHigh")}</td>
						<td>${fieldValue(bean: standardThresholdInstance, field: "diastolicWarningLow")}</td>
						<td>${fieldValue(bean: standardThresholdInstance, field: "diastolicAlertLow")}</td>
						<td>${fieldValue(bean: standardThresholdInstance, field: "systolicAlertHigh")}</td>
						<td>${fieldValue(bean: standardThresholdInstance, field: "systolicWarningHigh")}</td>
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${standardThresholdInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
