
<%@ page import="org.opentele.server.model.types.PermissionName; org.opentele.server.model.StandardThresholdSet" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${standardThresholdInstance.prettyToString()}" />
		<title>
			<g:message code="default.urineThreshold.show.label"/>
		</title>
	</head>
	<body>
        <div class="content">
			<h1>
				<g:message code="default.urineThreshold.show.label"/>
			</h1>
			
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
                        <g:if test="${standardThresholdInstance.alertHigh != null}">
                            <td>${standardThresholdInstance.alertHigh?.value()}</td>
                        </g:if>
                        <g:else>
                            <td><g:message code="result.table.question.tooltip.threshold.null" /></td>
                        </g:else>
                        <g:if test="${standardThresholdInstance.warningHigh != null}">
                            <td>${standardThresholdInstance.warningHigh?.value()}</td>
                        </g:if>
                        <g:else>
                            <td><g:message code="result.table.question.tooltip.threshold.null" /></td>
                        </g:else>
                        <g:if test="${standardThresholdInstance.warningLow != null}">
                            <td>${standardThresholdInstance.warningLow?.value()}</td>
                        </g:if>
                        <g:else>
                            <td><g:message code="result.table.question.tooltip.threshold.null" /></td>
                        </g:else>
                        <g:if test="${standardThresholdInstance.alertLow != null}">
                            <td>${standardThresholdInstance.alertLow?.value()}</td>
                        </g:if>
                        <g:else>
                            <td><g:message code="result.table.question.tooltip.threshold.null" /></td>
                        </g:else>
					</tr>
				</tbody>
			</table>

			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${standardThresholdInstance?.id}" />
                    <sec:ifAnyGranted roles="${PermissionName.THRESHOLD_WRITE}">
                        <g:link class="edit" action="edit" id="${standardThresholdInstance?.id}" params="[ignoreNavigation:'true']">
                            <g:message code="default.button.edit.label" default="Edit" />
                        </g:link>
                    </sec:ifAnyGranted>
				</fieldset>
			</g:form>
        </div>
	</body>
</html>
