<%@ page import="org.opentele.server.model.MonitoringPlan"%>
<%@ page import="org.opentele.server.model.Patient"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'monitoringPlan.label', default: 'MonitoringPlan')}" />
<title><g:message code="default.create.label"
		args="[entityName]" />for ${Patient.get(session.patientId)?.name()}</title>
</head>
<body>
	<div id="create-monitoringPlan" class="content scaffold-create"
		role="main">
		<h1>
			<g:message code="default.create.label" args="[entityName]" />
			for
			${Patient.get(session.patientId)?.name()}
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<g:hasErrors bean="${monitoringPlanInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${monitoringPlanInstance}" var="error">
					<li
						<g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
							error="${error}" /></li>
				</g:eachError>
			</ul>
		</g:hasErrors>
		<g:form action="save">
			<fieldset class="form">
				<g:render template="form" />
			</fieldset>
			<fieldset class="buttons">
				<div>
					<g:submitButton name="create" class="save"
						value="${message(code: 'default.button.create.label', default: 'Create')}"
						onmouseover="tooltip.show('${message(code: 'tooltip.patient.monitoringPlan.create')}');"
						onmouseout="tooltip.hide();" />
					<g:link class="cancel" action="show" id="${session.patientId}">
						<g:message code="default.button.goback.label" />
					</g:link>
				</div>
			</fieldset>
		</g:form>
	</div>
</body>
</html>
