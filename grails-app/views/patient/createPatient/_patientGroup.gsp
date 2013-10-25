<%@ page import="org.opentele.server.model.PatientGroup" %>
<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'group', 'error')} required">

    <label for="group" onmouseover="tooltip.show('${message(code: 'tooltip.patient.create.group')}');"
           onmouseout="tooltip.hide();">
        <g:message code="patient.group.label" default="type" />
    </label>

    <g:select name="groupIds"
              from="${patientGroups.sort { it.name }}"
              optionKey="id" multiple="multiple"
              value="${patientInstance.groupIds?.collect{PatientGroup.findById(it)}}"
    />
</div>
