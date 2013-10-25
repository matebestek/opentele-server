<%@ page import="org.opentele.server.model.Patient"%>
<%@ page import="org.opentele.server.model.PatientGroup"%>
<%@ page import="org.opentele.server.model.types.PatientState"%>
<%@ page import="org.opentele.server.util.NumberFormatUtil"%>


<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'username', 'error')} required">
    <label for="username" onmouseover="tooltip.show('${message(code: 'tooltip.patient.create.username')}');" onmouseout="tooltip.hide();">
        <g:message code="patient.username.label" default="Username" /> <span class="required-indicator">*</span>
    </label>
    <g:textField name="username" value="${patientInstance?.username}" />
</div>


<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'cpr', 'error')} required">

	<label for="cleartextPassword" onmouseover="tooltip.show('${message(code: 'tooltip.patient.create.cleartextPassword')}');" onmouseout="tooltip.hide();">
        <g:message code="patient.cleartextPassword.label" default="Midlertidig adgangskode" /> <span class="required-indicator">*</span>
	</label>
	<g:textField name="cleartextPassword" value="${patientInstance?.cleartextPassword}" autocomplete="off" />
	<br />
</div>
