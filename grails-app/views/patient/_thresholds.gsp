<%@ page import="org.opentele.server.model.types.MeasurementTypeName" %>
<table class="thresholds">
    <thead>
        <tr>
            <th><g:message code="default.threshold.type.label"/></th>
            <th><g:message code="default.threshold.alertHigh.label"/></th>
            <th><g:message code="default.threshold.warningHigh.label"/></th>
            <th><g:message code="default.threshold.warningLow.label"/></th>
            <th><g:message code="default.threshold.alertLow.label"/></th>
            <g:unless test="${readonly}">
                <sec:ifAnyGranted roles="${writePermission},${deletePermission}">
                    <th class="actions"><g:message code="default.threshold.action.label"/></th>
                </sec:ifAnyGranted>
            </g:unless>
        </tr>
    </thead>
    <tbody>
    <g:each in="${thresholds.sort { it.refresh(); it.prettyToString() }}" status="i" var="threshold">
        <g:if test="${threshold.type.name == MeasurementTypeName.BLOOD_PRESSURE}">
            <tmpl:/patient/thresholdRow threshold="${threshold}" field="diastolic" controllerName="bloodPressureThreshold" rowspan="2"/>
            <tmpl:/patient/thresholdRow threshold="${threshold}" field="systolic" controllerName="bloodPressureThreshold" readonly="true"/>

            </g:if>
        <g:elseif test="${threshold.type.name == MeasurementTypeName.URINE}">
            <tmpl:/patient/thresholdRow threshold="${threshold}" controllerName="urineThreshold"/>
            </g:elseif>
        <g:elseif test="${threshold.type.name == MeasurementTypeName.URINE_GLUCOSE}">
            <tmpl:/patient/thresholdRow threshold="${threshold}" controllerName="urineGlucoseThreshold"/>
            </g:elseif>
            <g:else>
                <tmpl:/patient/thresholdRow threshold="${threshold}" controllerName="numericThreshold"/>
            </g:else>
        </g:each>
    </tbody>
    <% if(body) out << body() %>
</table>
