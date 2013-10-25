
<%@ page import="org.opentele.server.model.types.PermissionName; org.opentele.server.model.BloodPressureThreshold" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'bloodPressureThreshold.label', default: 'BloodPressureThreshold')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
	
		<div id="show-bloodPressureThreshold" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
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
                <tr>
                    <td><g:fieldValue bean="${standardThresholdInstance}" field="diastolicAlertHigh"/></td>
                    <td><g:fieldValue bean="${standardThresholdInstance}" field="diastolicWarningHigh"/></td>
                    <td><g:fieldValue bean="${standardThresholdInstance}" field="diastolicWarningLow"/></td>
                    <td><g:fieldValue bean="${standardThresholdInstance}" field="diastolicAlertLow"/></td>
                </tr>
                <tr>
                    <td><g:fieldValue bean="${standardThresholdInstance}" field="systolicAlertHigh"/></td>
                    <td><g:fieldValue bean="${standardThresholdInstance}" field="systolicWarningHigh"/></td>
                    <td><g:fieldValue bean="${standardThresholdInstance}" field="systolicWarningLow"/></td>
                    <td><g:fieldValue bean="${standardThresholdInstance}" field="systolicAlertLow"/></td>
                </tr>
                </tbody>
            </table>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${standardThresholdInstance?.id}" />
                    <sec:ifAnyGranted roles="${PermissionName.THRESHOLD_WRITE}">
    					<g:link class="edit" action="edit" id="${standardThresholdInstance?.id}" params="[ignoreNavigation:'true']"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                    </sec:ifAnyGranted>
                    %{--<sec:ifAnyGranted roles="${PermissionName.THRESHOLD_DELETE}">--}%
    					%{--<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />--}%
                    %{--</sec:ifAnyGranted>--}%
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
