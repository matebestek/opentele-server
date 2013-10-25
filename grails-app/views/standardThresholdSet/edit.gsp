<%@ page import="org.opentele.server.model.types.PermissionName; org.opentele.server.model.types.MeasurementTypeName; org.opentele.server.model.StandardThresholdSet" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="edit-standardThresholdSet" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit.label" args="[entityName]" /> for patientgruppe ${patientGroup.name}</h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

            <g:hasErrors bean="${standardThresholdSetInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${standardThresholdSetInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if> >
                    <g:message error="${error}"/>
                </li>
				</g:eachError>
			</ul>
			</g:hasErrors>

        %{--
                        <fieldset class="form">
                            <g:select name="thresholdtype" from="${org.opentele.server.model.MeasurementType.list()}" valueMessagePrefix="thresholdtype"/>
                        </fieldset>
        --}%
            <g:render template="/patient/thresholds"
                      model="[thresholds: standardThresholdSetInstance.thresholds, parent: standardThresholdSetInstance, writePermission: PermissionName.STANDARD_THRESHOLD_WRITE, deletePermission: PermissionName.STANDARD_THRESHOLD_DELETE]"/>
            <fieldset class="buttons">
                <sec:ifAnyGranted roles="${PermissionName.STANDARD_THRESHOLD_WRITE}">
                    <g:form method="post" action="addThreshold">
                        <g:hiddenField name="id" value="${standardThresholdSetInstance.id}"/>
                        <g:submitButton class="create" name="chooseThreshold"
                                        value="${message(code: 'default.standardThresholdSet.add.label', default: 'Add')}"/>

                    </g:form>
                </sec:ifAnyGranted>

            </fieldset>
        </div>
	</body>
</html>
