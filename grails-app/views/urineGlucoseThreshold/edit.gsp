<%@ page import="org.opentele.server.model.StandardThresholdSet" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${standardThresholdInstance.prettyToString()}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="edit-standardThreshold" class="content scaffold-edit" role="main">
            <g:if test="${patientGroup != null}">
                <h1><g:message code="default.edit.label" args="[entityName]" /> for patientgruppe ${patientGroup.name}</h1>
            </g:if>
            <g:else>
                <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            </g:else>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${standardThresholdInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${standardThresholdInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                    <g:message error="${error}"/>
                </li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form method="post" >
				<g:hiddenField name="id" value="${standardThresholdInstance?.id}" />
				<g:hiddenField name="version" value="${standardThresholdInstance?.version}" />
                <g:hiddenField name="ignoreNavigation" value="true"/>
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
