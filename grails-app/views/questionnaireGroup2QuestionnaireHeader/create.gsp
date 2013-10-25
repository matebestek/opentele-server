<%@ page import="org.opentele.server.model.questionnaire.QuestionnaireGroup2QuestionnaireHeader" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<title><g:message code="questionnaireGroup2QuestionnaireHeader.give.label" /></title>
	</head>
	<body>
		<div id="create-questionnaireGroup2QuestionnaireHeader" class="content scaffold-create" role="main">
			<h1><g:message code="questionnaireGroup2QuestionnaireHeader.give.label"/></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${questionnaireGroup2QuestionnaireHeaderInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${questionnaireGroup2QuestionnaireHeaderInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form action="save" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'questionnaireGroup2QuestionnaireHeader.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
