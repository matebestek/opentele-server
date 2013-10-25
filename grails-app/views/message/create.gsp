<%@ page import="org.opentele.server.model.Message"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'message.label', default: 'Message')}" />
<title><g:message code="message.list.button.create"
		args="[entityName]" /></title>
</head>
<body>
	<div id="create-message" class="content scaffold-create" role="main">
		<h1>
			<g:message code="message.list.button.create" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<g:hasErrors bean="${messageInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${messageInstance}" var="error">
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
				<g:submitButton name="create" class="save"
					value="${message(code: 'message.list.button.send', default: 'Create')}" />
				<g:link class="cancel" controller="patient" action="messages" params="['id': patient.id]">
					<g:message code="default.button.goback.label" />
				</g:link>
			</fieldset>
		</g:form>
	</div>
</body>
</html>
