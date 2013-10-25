
<%@ page import="org.opentele.server.model.types.PermissionName; org.opentele.server.model.StandardThresholdSet" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${standardThresholdInstance.prettyToString()}" />
		<title>
			<g:message code="default.standardThreshold.show.label" args="[entityName]" />
		</title>
	</head>

	<body>
        <div class="content">
			<h1><g:message code="default.standardThreshold.show.label" args="[entityName]" /></h1>
			
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
						<td><g:fieldValue bean="${standardThresholdInstance}" field="alertHigh"/></td>
						<td><g:fieldValue bean="${standardThresholdInstance}" field="warningHigh"/></td>
						<td><g:fieldValue bean="${standardThresholdInstance}" field="warningLow"/></td>
						<td><g:fieldValue bean="${standardThresholdInstance}" field="alertLow"/></td>
					</tr>
				</tbody>
			</table>

			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${standardThresholdInstance?.id}" />
                    <sec:ifAnyGranted roles="${PermissionName.THRESHOLD_WRITE}">
    					<g:link class="edit" action="edit" id="${standardThresholdInstance?.id}" params="[ignoreNavigation:'true']">
                            <g:message code="default.button.edit.label" />
                        </g:link>
                    </sec:ifAnyGranted>
                </fieldset>
			</g:form>
        </div>
	</body>
</html>
