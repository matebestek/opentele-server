<%@ page import="org.opentele.server.model.Patient"%>
<%@ page import="org.opentele.server.model.PatientGroup"%>
<%@ page import="org.opentele.server.util.NumberFormatUtil"%>

<script>
$(document).ready(function() {

	$('input#cpr').change(function() {
		
		var input_cpr = $('input#cpr').val();

		if (input_cpr.indexOf("-") > 0 && input_cpr.length == 11) {
			try {
				var temp;
				temp = input_cpr.substring(0, input_cpr.indexOf('-'));
				input_cpr = temp + input_cpr.substring(input_cpr.indexOf('-')+1, input_cpr.length)
			} catch (err) {
			}
		}
		if (input_cpr.length == 10 && Number(input_cpr) != 'NaN') {
			if (input_cpr%2==0) {
				$('select#sex').attr('value', 'FEMALE')
			} else {
				$('select#sex').attr('value', 'MALE')
			}			
		}
	});

});
</script>

<div
	class="fieldcontain ${hasErrors(bean: patientInstance, field: 'cpr', 'error')} required">
	<label for="cpr" data-tooltip="${message(code: 'tooltip.patient.create.SSN')}"> <g:message code="patient.cpr.label"
			default="Cpr" /> <span class="required-indicator">*</span>
	</label>
	<g:textField name="cpr" value="${patientInstance?.cpr}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: patientInstance, field: 'firstName', 'error')} required">
	<label for="firstName"> <g:message
			code="patient.firstName.label" default="First Name" /> <span
		class="required-indicator">*</span>

	</label>
	<g:textField name="firstName" value="${patientInstance?.firstName}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: patientInstance, field: 'lastName', 'error')} required">
	<label for="lastName"> <g:message code="patient.lastName.label"
			default="Last Name" /> <span class="required-indicator">*</span>

	</label>
	<g:textField name="lastName" value="${patientInstance?.lastName}" />
</div>
<div
	class="fieldcontain ${hasErrors(bean: patientInstance, field: 'sex', 'error')} required">
	<label for="sex"> <g:message code="patient.sex.label"
			default="Sex" /> <span class="required-indicator">*</span>
	</label>
	<g:select name="sex"
		from="${org.opentele.server.model.types.Sex?.values()}"
		keys="${org.opentele.server.model.types.Sex.values()}"
		valueMessagePrefix="enum.sex" required=""
		value="${patientInstance?.sex ? patientInstance.sex : org.opentele.server.model.types.Sex.UNKNOWN}" id="sex"
    />
</div>

<div
	class="fieldcontain ${hasErrors(bean: patientInstance, field: 'address', 'error')} required">
	<label for="address"> <g:message code="patient.address.label"
			default="Address" /> <span class="required-indicator">*</span>
	</label>
	<g:textField name="address" value="${patientInstance?.address}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: patientInstance, field: 'postalCode', 'error')} required">
	<label for="postalCode"> <g:message
			code="patient.postalCode.label" default="Postal Code" /> <span
		class="required-indicator">*</span>

	</label>
	<g:textField name="postalCode" value="${patientInstance?.postalCode}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: patientInstance, field: 'city', 'error')} required">
	<label for="city"> <g:message code="patient.city.label"
			default="City" /> <span class="required-indicator">*</span>

	</label>
	<g:textField name="city" value="${patientInstance?.city}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: patientInstance, field: 'phone', 'error')} required">
	<label for="phone"> <g:message code="patient.phone.label"
			default="Phone" />

	</label>
	<g:textField name="phone" value="${patientInstance?.phone}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: patientInstance, field: 'mobilePhone', 'error')} required">
	<label for="mobilePhone"> <g:message
			code="patient.mobilePhone.label" default="Mobile Phone" />

	</label>
	<g:textField name="mobilePhone" value="${patientInstance?.mobilePhone}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: patientInstance, field: 'email', 'error')} required">
	<label for="email"> <g:message code="patient.email.label"
			default="Email" />

	</label>
	<g:textField name="email" value="${patientInstance?.email}" />
</div>
