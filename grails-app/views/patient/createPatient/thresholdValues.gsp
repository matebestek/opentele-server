<%@ page import="org.opentele.server.model.types.MeasurementTypeName; org.opentele.server.model.Patient"%>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'patient.label', default: 'Patient')}" />
    <title>
        <g:message code="patient.create.flow.thresholdValues.label"/></title>
</head>
<body>
<div id="create-patient" class="content scaffold-create" role="main">
    <h1>
        <g:message code="patient.create.flow.thresholdValues.label"/>
    </h1>
    <g:if test="${error}">
        <div class="errors" role="status">
            ${error}
        </div>
    </g:if>
    <g:if test="${patientInstance.thresholdSetWasReduced()}">
        <div class="errors" role="status">
            <g:message code="patient.create.flow.thresholdValues.duplicates.label"/>
        </div>
    </g:if>
    <g:each in="${patientInstance.thresholds}" var="threshold">
        <g:hasErrors bean="${threshold}">
            <ul class="errors" role="alert">
                <g:eachError bean="${threshold}" var="error">
                    <li>
                        <g:message error="${error}" />
                    </li>
                </g:eachError>
            </ul>
        </g:hasErrors>
    </g:each>
    <g:form>

        <table>
            <thead>
            <tr>
                <th>${message(code: 'default.threshold.type.label', default: 'Type')}</th>
                <th>${message(code: 'default.threshold.alertHigh.label', default: 'Alert High')}</th>
                <th>${message(code: 'default.threshold.warningHigh.label', default: 'Warning High')}</th>
                <th>${message(code: 'default.threshold.warningLow.label', default: 'Warning Low')}</th>
                <th>${message(code: 'default.threshold.alertLow.label', default: 'Alert Low')}</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${patientInstance.thresholds.sort { it.prettyToString() } }" var="threshold">
                <g:if test="${threshold.type.name == MeasurementTypeName.BLOOD_PRESSURE}">
                    <tr>
                        <td>${threshold.prettyToString()} diastolisk</td>
                        <td><g:field type="text" step="any" name="${threshold.type.name}" value="${threshold.diastolicAlertHigh}"/></td>
                        <td><g:field type="text" step="any" name="${threshold.type.name}" value="${threshold.diastolicWarningHigh}"/></td>
                        <td><g:field type="text" step="any" name="${threshold.type.name}" value="${threshold.diastolicWarningLow}"/></td>
                        <td><g:field type="text" step="any" name="${threshold.type.name}" value="${threshold.diastolicAlertLow}"/></td>
                    </tr>
                    <tr>
                        <td>${threshold.prettyToString()} systolisk</td>
                        <td><g:field type="text" step="any" name="${threshold.type.name}" value="${threshold.systolicAlertHigh}"/></td>
                        <td><g:field type="text" step="any" name="${threshold.type.name}" value="${threshold.systolicWarningHigh}"/></td>
                        <td><g:field type="text" step="any" name="${threshold.type.name}" value="${threshold.systolicWarningLow}"/></td>
                        <td><g:field type="text" step="any" name="${threshold.type.name}" value="${threshold.systolicAlertLow}"/></td>
                    </tr>
                </g:if>
                <g:elseif test="${threshold.type.name == MeasurementTypeName.URINE}">
                    <tr>
                        <td>${threshold.prettyToString()}</td>
                        <td><g:select 	  name="${threshold.type.name}"
                                            from="${org.opentele.server.model.types.ProteinValue.values()}"
                                            keys="${org.opentele.server.model.types.ProteinValue.values()}"
                                            valueMessagePrefix="enum.proteinValue"
                                            noSelection="${['':"..."]}"
                                            value="${threshold.alertHigh}" />
                        </td>
                        <td><g:select 	  name="${threshold.type.name}"
                                            from="${org.opentele.server.model.types.ProteinValue.values()}"
                                            keys="${org.opentele.server.model.types.ProteinValue.values()}"
                                            valueMessagePrefix="enum.proteinValue"
                                            noSelection="${['':"..."]}"
                                            value="${threshold.warningHigh}" />
                        </td>
                        <td><g:select 	  name="${threshold.type.name}"
                                            from="${org.opentele.server.model.types.ProteinValue.values()}"
                                            keys="${org.opentele.server.model.types.ProteinValue.values()}"
                                            valueMessagePrefix="enum.proteinValue"
                                            noSelection="${['':"..."]}"
                                            value="${threshold.warningLow}" />
                        </td>
                        <td><g:select 	name="${threshold.type.name}"
                                          from="${org.opentele.server.model.types.ProteinValue.values()}"
                                          keys="${org.opentele.server.model.types.ProteinValue.values()}"
                                          valueMessagePrefix="enum.proteinValue"
                                          noSelection="${['':"..."]}"
                                          value="${threshold.alertLow}" />
                        </td>
                    </tr>
                </g:elseif>
                <g:elseif test="${threshold.type.name == MeasurementTypeName.URINE_GLUCOSE}">
                    <tr>
                        <td>${threshold.prettyToString()}</td>
                        <td><g:select 	  name="${threshold.type.name}"
                                            from="${org.opentele.server.model.types.GlucoseInUrineValue.values()}"
                                            keys="${org.opentele.server.model.types.GlucoseInUrineValue.values()}"
                                            valueMessagePrefix="enum.proteinValue"
                                            noSelection="${['':"..."]}"
                                            value="${threshold.alertHigh}" />
                        </td>
                        <td><g:select 	  name="${threshold.type.name}"
                                            from="${org.opentele.server.model.types.GlucoseInUrineValue.values()}"
                                            keys="${org.opentele.server.model.types.GlucoseInUrineValue.values()}"
                                            valueMessagePrefix="enum.proteinValue"
                                            noSelection="${['':"..."]}"
                                            value="${threshold.warningHigh}" />
                        </td>
                        <td><g:select 	  name="${threshold.type.name}"
                                            from="${org.opentele.server.model.types.GlucoseInUrineValue.values()}"
                                            keys="${org.opentele.server.model.types.GlucoseInUrineValue.values()}"
                                            valueMessagePrefix="enum.proteinValue"
                                            noSelection="${['':"..."]}"
                                            value="${threshold.warningLow}" />
                        </td>
                        <td><g:select 	name="${threshold.type.name}"
                                          from="${org.opentele.server.model.types.GlucoseInUrineValue.values()}"
                                          keys="${org.opentele.server.model.types.GlucoseInUrineValue.values()}"
                                          valueMessagePrefix="enum.proteinValue"
                                          noSelection="${['':"..."]}"
                                          value="${threshold.alertLow}" />
                        </td>
                    </tr>
                </g:elseif>
                <g:else>
                    <tr>
                        <td>${threshold.prettyToString()}</td>
                        <td><g:field type="text" step="any" name="${threshold.type.name}" value="${threshold.alertHigh}"/></td>
                        <td><g:field type="text" step="any" name="${threshold.type.name}" value="${threshold.warningHigh}"/></td>
                        <td><g:field type="text" step="any" name="${threshold.type.name}" value="${threshold.warningLow}"/></td>
                        <td><g:field type="text" step="any" name="${threshold.type.name}" value="${threshold.alertLow}"/></td>
                    </tr>
                </g:else>
            </g:each>
            </tbody>
        </table>

        <fieldset class="buttons">
            <g:submitButton name="previous" class="goback" value="${message(code: 'patient.create.flow.button.previous.label', default: 'Previous')}" />
            <g:submitButton name="next" class="gonext" value="${message(code: 'patient.create.flow.button.next.label', default: 'Next')}" />
            <g:submitButton name="saveAndShow" class="save" value="${message(code: 'patient.create.flow.button.saveAndExit.label', default: 'Next')}" data-tooltip="${message(code: 'patient.create.flow.finish.tooltip')}"/>
            <g:submitButton name="saveAndGotoMonplan" class="save" value="${message(code: 'patient.create.flow.button.saveAndExitToMonplan.label', default: 'Next')}" data-tooltip="${message(code: 'patient.create.flow.finish.monplan.tooltip')}"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
