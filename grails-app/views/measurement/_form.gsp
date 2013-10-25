<%@ page import="org.opentele.server.model.Measurement"%>



<div
	class="fieldcontain ${hasErrors(bean: measurementInstance, field: 'time', 'error')} required">
	<label for="time"> <g:message code="measurement.time.label"
			default="Time" /> <span class="required-indicator">*</span>
	</label>
	<jq:datePicker name="time" precision="day" value="${measurementInstance?.time}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: measurementInstance, field: 'value', 'error')} required">
	<label for="value"> <g:message code="measurement.value.label"
			default="Value" /> <span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="value" required=""
		value="${fieldValue(bean: measurementInstance, field: 'value')}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: measurementInstance, field: 'systolic', 'error')} ">
	<label for="systolic"> <g:message
			code="measurement.systolic.label" default="Systolic" />
	</label>
	<g:field type="number" name="systolic"
		value="${fieldValue(bean: measurementInstance, field: 'systolic')}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: measurementInstance, field: 'diastolic', 'error')} ">
	<label for="diastolic"> <g:message
			code="measurement.diastolic.label" default="diastolic" />
	</label>
	<g:field type="number" name="diastolic"
		value="${fieldValue(bean: measurementInstance, field: 'diastolic')}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: measurementInstance, field: 'patient', 'error')} required ">
	<label for="patient"> <g:message
			code="measurement.patient.label" default="Patient" /> <span
		class="required-indicator">*</span>
	</label>
	<g:select id="patient" name="patient.id"
		from="${org.opentele.server.model.Patient.list()}" optionKey="id"
		required="" value="${measurementInstance?.patient?.id}"
		class="many-to-one" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: measurementInstance, field: 'meter', 'error')} required">
	<label for="meter"> <g:message code="measurement.meter.label"
			default="Meter" /> <span class="required-indicator">*</span>
	</label>
	<g:select id="meter" name="meter.id"
		from="${org.opentele.server.model.Meter.list()}" optionKey="id"
		required="" value="${measurementInstance?.meter?.id}"
		class="many-to-one" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: measurementInstance, field: 'measurementType', 'error')} required">
	<label for="measurementType"> <g:message
			code="measurement.measurementType.label" default="Measurement Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="measurementType" name="measurementType.id"
		from="${org.opentele.server.model.MeasurementType.list()}"
		optionKey="id" required=""
		value="${measurementInstance?.measurementType?.id}"
		class="many-to-one" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: measurementInstance, field: 'createdBy', 'error')} ">
	<label for="createdBy"> <g:message
			code="measurement.createdBy.label" default="Created By" />

	</label>
	<g:textField name="createdBy" value="${measurementInstance?.createdBy}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: measurementInstance, field: 'createdDate', 'error')} required">
	<label for="createdDate"> <g:message
			code="measurement.createdDate.label" default="Created Date" /> <span
		class="required-indicator">*</span>
	</label>
	<ja:datePicker name="createdDate" precision="day" value="${measurementInstance?.createdDate}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: measurementInstance, field: 'modifiedBy', 'error')} ">
	<label for="modifiedBy"> <g:message
			code="measurement.modifiedBy.label" default="Modified By" />

	</label>
	<g:textField name="modifiedBy"
		value="${measurementInstance?.modifiedBy}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: measurementInstance, field: 'modifiedDate', 'error')} required">
	<label for="modifiedDate"> <g:message
			code="measurement.modifiedDate.label" default="Modified Date" /> <span
		class="required-indicator">*</span>
	</label>
	<ja:datePicker name="modifiedDate" precision="day" value="${measurementInstance?.modifiedDate}" />
</div>

