
<%@ page import="org.opentele.server.model.types.PermissionName; org.opentele.server.model.Measurement"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'measurement.label', default: 'Measurement')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
	<div id="show-measurement" class="content scaffold-show" role="main">
		<h1><g:message code="default.show.label" args="[entityName]" /></h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>

		<ol class="property-list measurement">
			<g:if test="${measurementInstance?.time}">
				<li class="fieldcontain">
                    <span id="time-label" class="property-label">
                        <g:message code="measurement.time.label" default="Time" />
                    </span>
                    <span class="property-value" aria-labelledby="time-label">
                        <g:formatDate date="${measurementInstance?.time}" />
                    </span>
                </li>
			</g:if>

			<g:if test="${measurementInstance?.value}">
				<li class="fieldcontain">
                    <span id="value-label" class="property-label">
                        <g:message code="measurement.value.label" default="Value" />
                    </span>
                    <span class="property-value" aria-labelledby="value-label">
                        <g:fieldValue bean="${measurementInstance}" field="value" />
                    </span>
                </li>
			</g:if>

			<g:if test="${measurementInstance?.systolic}">
				<li class="fieldcontain">
                    <span id="value-label" class="property-label">
                        <g:message code="measurement.systolic.label" default="Systolic" />
                    </span>
                    <span class="property-value" aria-labelledby="value-label">
                        <g:fieldValue bean="${measurementInstance}" field="systolic" />
                    </span>
                </li>
			</g:if>
			<g:if test="${measurementInstance?.diastolic}">
				<li class="fieldcontain">
                    <span id="value-label" class="property-label">
                        <g:message code="measurement.diastolic.label" default="Diastolic" />
                    </span>
                    <span class="property-value" aria-labelledby="value-label">
                        <g:fieldValue bean="${measurementInstance}" field="diastolic" />
                    </span>
                </li>
			</g:if>
			<g:if test="${measurementInstance?.unit}">
				<li class="fieldcontain">
                    <span id="unit-label" class="property-label">
                        <g:message code="measurement.unit.label" default="Unit" />
                    </span>
                    <span class="property-value" aria-labelledby="unit-label">
                        <g:fieldValue bean="${measurementInstance}" field="unit" />
                    </span>
                </li>
			</g:if>

			<g:if test="${measurementInstance?.patient}">
				<li class="fieldcontain">
                    <span id="patient-label" class="property-label">
                        <g:message code="measurement.patient.label" default="Patient" />
                    </span>
                    <span class="property-value" aria-labelledby="patient-label">
                        <sec:ifAnyGranted roles="${PermissionName.PATIENT_READ}">
                            <g:link controller="patient" action="show" id="${measurementInstance?.patient?.id}">
                                ${measurementInstance?.patient?.encodeAsHTML()}
                            </g:link>
                        </sec:ifAnyGranted>
                        <sec:ifNotGranted roles="${PermissionName.PATIENT_READ}">
                            ${measurementInstance?.patient?.encodeAsHTML()}
                        </sec:ifNotGranted>
                    </span>
                </li>
			</g:if>

			<g:if test="${measurementInstance?.meter}">
				<li class="fieldcontain">
                    <span id="meter-label" class="property-label">
                        <g:message code="measurement.meter.label" default="Meter" />
                    </span>
                    <span class="property-value" aria-labelledby="meter-label">
                        <sec:ifAnyGranted roles="${PermissionName.METER_READ}">
                            <g:link controller="meter" action="show" id="${measurementInstance?.meter?.id}">
                                ${measurementInstance?.meter?.encodeAsHTML()}
                            </g:link>
                        </sec:ifAnyGranted>
                        <sec:ifNotGranted roles="${PermissionName.METER_READ}">
                            ${measurementInstance?.meter?.encodeAsHTML()}
                        </sec:ifNotGranted>
                    </span>
                </li>
			</g:if>

			<g:if test="${measurementInstance?.createdBy}">
				<li class="fieldcontain">
                    <span id="createdBy-label" class="property-label">
                        <g:message code="measurement.createdBy.label" default="Created By" />
                    </span>
                    <span class="property-value" aria-labelledby="createdBy-label">
                        <g:fieldValue bean="${measurementInstance}" field="createdBy" />
                    </span>
                </li>
			</g:if>

			<g:if test="${measurementInstance?.createdDate}">
				<li class="fieldcontain">
                    <span id="createdDate-label" class="property-label">
                        <g:message code="measurement.createdDate.label" default="Created Date" />
                    </span>
                    <span class="property-value" aria-labelledby="createdDate-label">
                        <g:formatDate date="${measurementInstance?.createdDate}" />
                    </span>
                </li>
			</g:if>

			<g:if test="${measurementInstance?.modifiedBy}">
				<li class="fieldcontain">
                    <span id="modifiedBy-label" class="property-label">
                        <g:message code="measurement.modifiedBy.label" default="Modified By" />
                    </span>
                    <span class="property-value" aria-labelledby="modifiedBy-label">
                        <g:fieldValue bean="${measurementInstance}" field="modifiedBy" />
                    </span>
                </li>
			</g:if>

			<g:if test="${measurementInstance?.modifiedDate}">
				<li class="fieldcontain">
                    <span id="modifiedDate-label" class="property-label">
                        <g:message code="measurement.modifiedDate.label" default="Modified Date" />
                    </span>
					<span class="property-value" aria-labelledby="modifiedDate-label">
                        <g:formatDate date="${measurementInstance?.modifiedDate}" />
                    </span>
                </li>
			</g:if>
		</ol>

		<g:form>
			<fieldset class="buttons">
				<g:hiddenField name="id" value="${measurementInstance?.id}" />
                <sec:ifAnyGranted roles="${PermissionName.MEASUREMENT_WRITE}">
                    <g:link class="edit" action="edit" id="${measurementInstance?.id}">
                        <g:message code="default.button.edit.label" default="Edit" />
                    </g:link>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.MEASUREMENT_DELETE}">
                    <g:actionSubmit class="delete" action="delete"
                        value="${message(code: 'default.button.delete.label')}"
                        onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
                </sec:ifAnyGranted>
			</fieldset>
		</g:form>
	</div>
</body>
</html>
