
<%@ page
	import="org.opentele.server.model.NextOfKinPerson; org.opentele.server.model.PatientGroup; org.opentele.server.model.types.MeasurementTypeName; org.opentele.server.constants.Constants; org.opentele.server.model.Patient; org.opentele.server.util.NumberFormatUtil"%>
<!doctype html>
<html xmlns="http://www.w3.org/1999/html">
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'patient.label', default: 'Patient')}" />
<title><g:message code="default.patient.show.label" /> ${patientInstance?.firstName}
	${patientInstance?.lastName}</title>
</head>
<body>
	<div id="show-patient" class="content scaffold-show" role="main">
		<h1>
			<g:message code="default.patient.confirm.create.label" args="${[patientInstance?.firstName, patientInstance?.lastName]}" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>

        <g:if test="${hasErrors}">
            <div class="errors" role="status">
                ${errorMessage}
            </div>
        </g:if>

        <g:hasErrors bean="${patientInstance}">
            <ul class="errors" role="alert">
                <g:eachError bean="${patientInstance}" var="error">
                    <li>
                        <g:message error="${error}" />
                    </li>
                </g:eachError>
            </ul>
        </g:hasErrors>

        <g:form>
            <fieldset class="buttons">
                <g:submitButton name="save" class="save" value="${message(code: 'patient.create.flow.summary.save.label', default: 'Next')}" onmouseover="tooltip.show('${message(code: 'tooltip.patient.create.permanent')}');" onmouseout="tooltip.hide();"/>
                <g:submitButton name="saveAndGotoMonplan" class="save" value="${message(code: 'patient.create.flow.button.saveAndExitToMonplan.label')}" onmouseover="tooltip.show('${message(code: 'patient.create.flow.finish.monplan.tooltip')}');" onmouseout="tooltip.hide();"/>
                <g:submitButton name="quitNoSaving" class="cancel" value="${message(code: 'patient.create.flow.summary.cancel.label', default: 'Previous')}" />
            </fieldset>
        </g:form>


    <ol class="property-list patient">
			<g:if test="${patientInstance?.username}">
				<li class="fieldcontain">
                    <span id="user-label" class="property-label">
                         <g:message code="patient.username.label"/>
				    </span>
                    <span class="property-value" aria-labelledby="user-label">
                        ${patientInstance?.username}
				    </span>
                </li>
			</g:if>
			<g:if test="${patientInstance?.cleartextPassword}">
				<li class="fieldcontain">
                    <span id="password-label" class="property-label">
                        <g:message code="patient.cleartextPassword.label"/>
                    </span>
                    <span class="property-value" aria-labelledby="user-label">
                        ${patientInstance?.cleartextPassword}
				    </span>
                </li>
			</g:if>

            <li class="fieldcontain buttons">
                <span class="property-label"></span>
                <span class="property-value">
                    <g:form>
                        <g:submitButton name="editAuth" class="edit" value="${message(code: 'patient.create.flow.button.edit.label', args: ['Brugeroplysninger'], default: 'Rediger')}" />
                    </g:form>
                </span>
            </li>

			<g:if test="${patientInstance?.cpr}">
				<li class="fieldcontain"><span id="cpr-label"
					class="property-label"><g:message code="patient.cpr.label"
							default="Cpr" /></span> <span class="property-value"
					aria-labelledby="cpr-label"> ${patientInstance?.cpr[0..5]+"-"+patientInstance?.cpr[6..9]}
				</span></li>
			</g:if>

			<g:if test="${patientInstance?.firstName}">
				<li class="fieldcontain"><span id="firstName-label"
					class="property-label"><g:message
							code="patient.firstName.label" default="First Name" /></span> <span
					class="property-value" aria-labelledby="firstName-label"><g:fieldValue
							bean="${patientInstance}" field="firstName" /></span></li>
			</g:if>

			<g:if test="${patientInstance?.lastName}">
				<li class="fieldcontain"><span id="lastName-label"
					class="property-label"><g:message
							code="patient.lastName.label" default="Last Name" /></span> <span
					class="property-value" aria-labelledby="lastName-label"><g:fieldValue
							bean="${patientInstance}" field="lastName" /></span></li>
			</g:if>

			<g:if test="${patientInstance?.sex}">
				<li class="fieldcontain"><span id="sex-label"
					class="property-label"><g:message code="patient.sex.label"
							default="Sex" /></span> <span class="property-value"
					aria-labelledby="sex-label"> ${message(code:'enum.sex.'+fieldValue(bean: patientInstance, field: "sex"))}
				</span></li>
			</g:if>

			<g:if test="${patientInstance?.address}">
				<li class="fieldcontain"><span id="address-label"
					class="property-label"><g:message
							code="patient.address.label" default="Address" /></span> <span
					class="property-value" aria-labelledby="address-label"><g:fieldValue
							bean="${patientInstance}" field="address" /></span></li>
			</g:if>

			<g:if test="${patientInstance?.postalCode}">
				<li class="fieldcontain"><span id="postalCode-label"
					class="property-label"><g:message
							code="patient.postalCode.label" default="Postal Code" /></span> <span
					class="property-value" aria-labelledby="postalCode-label"><g:fieldValue
							bean="${patientInstance}" field="postalCode" /></span></li>
			</g:if>

			<g:if test="${patientInstance?.city}">
				<li class="fieldcontain"><span id="city-label"
					class="property-label"><g:message code="patient.city.label"
							default="City" /></span> <span class="property-value"
					aria-labelledby="city-label"><g:fieldValue
							bean="${patientInstance}" field="city" /></span></li>
			</g:if>

			<g:if test="${patientInstance?.phone}">
				<li class="fieldcontain"><span id="phone-label"
					class="property-label"><g:message code="patient.phone.label"
							default="Phone" /></span> <span class="property-value"
					aria-labelledby="phone-label"><otformat:formatPhoneNumber
							message="${patientInstance.phone}" /></span></li>
			</g:if>

			<g:if test="${patientInstance?.mobilePhone}">
				<li class="fieldcontain"><span id="mobilePhone-label"
					class="property-label"><g:message
							code="patient.mobilePhone.label" default="Mobile Phone" /></span> <span
					class="property-value" aria-labelledby="mobilePhone-label"><otformat:formatPhoneNumber
							message="${patientInstance.mobilePhone}" /></span></li>
			</g:if>

			<g:if test="${patientInstance?.email}">
				<li class="fieldcontain"><span id="email-label"
					class="property-label"><g:message code="patient.email.label"
							default="Email" /></span> <span class="property-value"
					aria-labelledby="email-label"><g:fieldValue
							bean="${patientInstance}" field="email" /></span></li>
			</g:if>

            <li class="fieldcontain buttons">
                <span class="property-label"></span>
                <span class="property-value">
                    <g:form>
                        <g:submitButton name="editBasic" class="edit" value="${message(code: 'patient.create.flow.button.edit.label', args: ['Stamdata'], default: 'Rediger')}" />
                    </g:form>
                </span>
            </li>

    <li class="fieldcontain"><span id="comment-label"
                                   class="property-label"><g:message
                code="patient.comment.label" default="Comments" /></span> <span
            class="property-value fullhight" aria-labelledby="comment-label"><g:fieldValue
                bean="${patientInstance}" field="comment" /></span></li>

    <li class="fieldcontain buttons">
        <span class="property-label"></span>
        <span class="property-value">
            <g:form>
                <g:submitButton name="editComment" class="edit" value="${message(code: 'patient.create.flow.button.edit.label', args: ['Kommentar'], default: 'Rediger')}" />
            </g:form>
        </span>
    </li>
    <li>

            <g:if test="${patientInstance.groupIds}">
				<li class="fieldcontain"><span id="group-label"
					class="property-label">Patientgrupper:</span> <span
					class="property-value fullheight" aria-labelledby="comment-label"> <g:each
							in="${patientInstance.groupIds.collect{PatientGroup.findById(it)}}" var="group">
							${group}<br />
						</g:each>
				</span></li>
			</g:if>

            <li class="fieldcontain buttons">
                <span class="property-label"></span>
                <span class="property-value">
                    <g:form>
                        <g:submitButton name="editPG" class="edit" value="${message(code: 'patient.create.flow.button.edit.label', args: ['Patientgrupper'], default: 'Rediger')}" />
                    </g:form>
                </span>
            </li>

    <g:if test="${patientInstance.nextOfKins}">
        <li class="fieldcontain"><span id="nextOfKin-label"
                                       class="property-label">Pårørende:</span> <span
                class="property-value" aria-labelledby="comment-label"> <g:each
                    in="${patientInstance.nextOfKins}" var="nok">
                <g:link controller="nextOfKinPerson" action="show"
                        id="${nok.id}">${nok} Telefon: ${nok.phone}<br /></g:link>
            </g:each>
        </span></li>
    </g:if>

    <li class="fieldcontain buttons">
        <span class="property-label"></span>
        <span class="property-value">
            <g:form>
                <g:submitButton name="editNok" class="edit" value="${message(code: 'patient.create.flow.button.edit.label', args: ['Pårørende'], default: 'Rediger')}" />
            </g:form>
        </span>
    </li>


			<table>
				<thead>
					<tr>
						<g:sortableColumn property="type" title="${message(code: 'default.threshold.type.label', default: 'Type')}" />
						<g:sortableColumn property="alertHigh" title="${message(code: 'default.threshold.alertHigh.label', default: 'Alert High')}" />
						<g:sortableColumn property="warningHigh" title="${message(code: 'default.threshold.warningHigh.label', default: 'Warning High')}" />
						<g:sortableColumn property="warningLow" title="${message(code: 'default.threshold.warningLow.label', default: 'Warning Low')}" />
						<g:sortableColumn property="alertLow" title="${message(code: 'default.threshold.alertLow.label', default: 'Alert Low')}" />
					</tr>
				</thead>
				<tbody>
                    <g:each in="${patientInstance.thresholds.sort { it.prettyToString() } }" status="i" var="threshold">
                        <g:if test="${threshold.type.equals(MeasurementTypeName.BLOOD_PRESSURE)}">
                            <tr>
                            <td class="table-label-td buttons">${patientInstance.getThreshold(MeasurementTypeName.BLOOD_PRESSURE).prettyToString()} diastolisk</td>
                            <td>${fieldValue(bean: patientInstance.getThreshold(MeasurementTypeName.BLOOD_PRESSURE), field: "diastolicAlertHigh")}</td>
                            <td>${fieldValue(bean: patientInstance.getThreshold(MeasurementTypeName.BLOOD_PRESSURE), field: "diastolicWarningHigh")}</td>
                            <td>${fieldValue(bean: patientInstance.getThreshold(MeasurementTypeName.BLOOD_PRESSURE), field: "diastolicWarningLow")}</td>
                            <td>${fieldValue(bean: patientInstance.getThreshold(MeasurementTypeName.BLOOD_PRESSURE), field: "diastolicAlertLow")}</td>
                            </tr>
                            <tr>
                            <td class="table-label-td buttons">${patientInstance.getThreshold(MeasurementTypeName.BLOOD_PRESSURE).prettyToString()} systolisk</td>
                            <td>${fieldValue(bean: patientInstance.getThreshold(MeasurementTypeName.BLOOD_PRESSURE), field: "systolicAlertHigh")}</td>
                            <td>${fieldValue(bean: patientInstance.getThreshold(MeasurementTypeName.BLOOD_PRESSURE), field: "systolicWarningHigh")}</td>
                            <td>${fieldValue(bean: patientInstance.getThreshold(MeasurementTypeName.BLOOD_PRESSURE), field: "systolicWarningLow")}</td>
                            <td>${fieldValue(bean: patientInstance.getThreshold(MeasurementTypeName.BLOOD_PRESSURE), field: "systolicAlertLow")}</td>
                            </tr>
                        </g:if>
                        <g:elseif test="${threshold.type.equals(MeasurementTypeName.URINE)}">
                            <tr>
                            <td class="table-label-td buttons">${patientInstance.getThreshold(MeasurementTypeName.URINE).prettyToString()}</td>
                            <td>${fieldValue(bean: patientInstance.getThreshold(MeasurementTypeName.URINE), field: "alertHigh")}</td>
                            <td>${fieldValue(bean: patientInstance.getThreshold(MeasurementTypeName.URINE), field: "warningHigh")}</td>
                            <td>${fieldValue(bean: patientInstance.getThreshold(MeasurementTypeName.URINE), field: "warningLow")}</td>
                            <td>${fieldValue(bean: patientInstance.getThreshold(MeasurementTypeName.URINE), field: "alertLow")}</td>
                            </tr>
                        </g:elseif>
                        <g:elseif test="${threshold.type.equals(MeasurementTypeName.URINE_GLUCOSE)}">
                            <tr>
                                <td class="table-label-td buttons">${patientInstance.getThreshold(MeasurementTypeName.URINE_GLUCOSE).prettyToString()}</td>
                                <td>${fieldValue(bean: patientInstance.getThreshold(MeasurementTypeName.URINE_GLUCOSE), field: "alertHigh")}</td>
                                <td>${fieldValue(bean: patientInstance.getThreshold(MeasurementTypeName.URINE_GLUCOSE), field: "warningHigh")}</td>
                                <td>${fieldValue(bean: patientInstance.getThreshold(MeasurementTypeName.URINE_GLUCOSE), field: "warningLow")}</td>
                                <td>${fieldValue(bean: patientInstance.getThreshold(MeasurementTypeName.URINE_GLUCOSE), field: "alertLow")}</td>
                            </tr>
                        </g:elseif>
                        <g:else>
                            <tr>
                                <td class="table-label-td buttons">${threshold.prettyToString()}</td>
                                <td>${fieldValue(bean: threshold, field: "alertHigh")}</td>
                                <td>${fieldValue(bean: threshold, field: "warningHigh")}</td>
                                <td>${fieldValue(bean: threshold, field: "warningLow")}</td>
                                <td>${fieldValue(bean: threshold, field: "alertLow")}</td>
                            </tr>
                        </g:else>
                    </g:each>
				</tbody>
			</table>
            </li>
            <li class="fieldcontain buttons">
                <span class="property-label"></span>
                <span class="property-value">
                    <g:form>
                        <g:submitButton name="editThresholds" class="edit" value="${message(code: 'patient.create.flow.button.edit.label', args: ['Tærskelværdier'], default: 'Rediger')}" />
                    </g:form>
                </span>
            </li>
        </ol>
	</div>
</body>
</html>
