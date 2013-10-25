
<%@ page import="org.opentele.server.model.PatientGroup" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<title><g:message code="default.show.label" args="['patientgruppe']" /></title>
	</head>
	<body>
	
		<div id="show-patientGroup" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="['patientgruppe']" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list patientGroup">
			
				<g:if test="${patientGroupInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="patientGroup.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${patientGroupInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${patientGroupInstance?.department}">
				<li class="fieldcontain">
					<span id="department-label" class="property-label"><g:message code="patientGroup.department.label" default="Department" /></span>
					
						<span class="property-value" aria-labelledby="department-label"><g:link controller="department" action="show" id="${patientGroupInstance?.department?.id}">${patientGroupInstance?.department?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>


                <li class="fieldcontain">
                    <span id="patientGroup.disable_messaging.label" class="property-label">
                        <g:message code="patientGroup.disable_messaging.label"/>
                    </span>

                    <span class="property-value" aria-labelledby="patientGroup.disable_messaging.label">
                        <g:checkBox name="disableMessaging" value="${patientGroupInstance?.disableMessaging}" disabled="true" />
                    </span>
                </li>


			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${patientGroupInstance?.id}" />
					<g:link class="edit" action="edit" id="${patientGroupInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'patientGroup.delete.message.confirm', args: [patientGroupInstance?.name, patientGroupInstance?.department], default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
