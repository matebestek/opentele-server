<%@ page import="org.opentele.server.model.QuestionnaireSchedule"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')}" />
<title><g:message code="default.give.label"
		args="[entityName]" /></title>
</head>
<body>
	<div id="create-questionnaireSchedule" class="content scaffold-create"
		role="main">
		<h1>
			<g:message code="default.give.label" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<g:hasErrors bean="${questionnaireSchedule}">
			<ul class="errors" role="alert">
				<g:eachError bean="${questionnaireSchedule}" var="error">
					<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if> >
                        <g:message error="${error}" />
                    </li>
				</g:eachError>
			</ul>
		</g:hasErrors>

		<g:form action="save">
			<fieldset class="form">
				<g:render template="form" />
			</fieldset>
			<fieldset class="buttons">
				<g:submitButton name="create" class="save" value="${message(code: 'default.button.give.label')}" />
                <g:link class="cancel"  action="show"
					                    controller="monitoringPlan"
					                    id="${session.patientId}">
					<g:message code="default.button.goback.label" />
				</g:link>
			</fieldset>
		</g:form>
	</div>
</body>
</html>
