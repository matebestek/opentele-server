<%@ page import="org.opentele.server.model.Patient"%>
<%@ page import="org.opentele.server.model.PatientGroup"%>

<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'dataResponsible', 'error')} required"
	    onmouseover="tooltip.show('${message(code: 'tooltip.patient.responsible.group')}');"
	    onmouseout="tooltip.hide();">

    <label for="dataResponsible">
        <g:message code="responsible.patient.group.label" />
	</label>

	<g:select name="dataResponsible" from="${patientInstance.patient2PatientGroups*.patientGroup?.flatten()}"
		optionKey="id"
        noSelection="${['':'Vælg...']}"
		value="${patientInstance?.dataResponsible?.id}" />
</div>

