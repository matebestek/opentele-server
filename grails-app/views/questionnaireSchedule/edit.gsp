<%@ page import="org.opentele.server.model.QuestionnaireSchedule"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')}" />
<title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>

	<div id="edit-questionnaireSchedule" class="content scaffold-edit"
		role="main">
		<h1>
			<g:message code="default.edit.label" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		
		<g:form method="post">
			<fieldset class="form">
				<tmpl:form/>
			</fieldset>
			<fieldset class="buttons">
				<g:actionSubmit class="save" action="update"
					value="${message(code: 'default.button.update.label')}" />
			</fieldset>
		</g:form>
	</div>
</body>
</html>
