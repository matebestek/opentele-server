<%@ page import="org.opentele.server.model.Patient"%>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'patient.label', default: 'Patient')}" />
    <title>
        <g:message code="patient.create.flow.addNextOfKin.label"/>
    </title>
</head>
<body>

<div id="create-patient" class="content scaffold-create" role="main">
    <h1>
        <g:message code="patient.create.flow.addNextOfKin.label"/>
    </h1>

    <g:if test="${error}">
        <div class="errors" role="status">
            ${error}
        </div>
    </g:if>


    <g:form>
        <fieldset class="form">
                <ul class="one-to-many">
                    <g:each in="${patientInstance?.nextOfKins?}" var="c">
                        <li>
                            ${c} Telefon: ${c.phone}
                        </li>
                    </g:each>
                </ul>
        </fieldset>

        <fieldset class="buttons">
            <g:submitButton name="previous" class="goback" value="${message(code: 'patient.create.flow.button.previous.label', default: 'Previous')}" />
            <g:submitButton name="create" class="create" value="${message(code: 'patient.create.flow.button.createNextOfKin.label', default: 'Create')}" />
            <g:submitButton name="next" class="gonext" value="${message(code: 'patient.create.flow.button.next.label', default: 'Next')}" />
            <g:submitButton name="saveAndShow" class="save" value="${message(code: 'patient.create.flow.button.saveAndExit.label', default: 'Next')}" onmouseover="tooltip.show('${message(code: 'patient.create.flow.finish.tooltip')}');" onmouseout="tooltip.hide();"/>
            <g:submitButton name="saveAndGotoMonplan" class="save" value="${message(code: 'patient.create.flow.button.saveAndExitToMonplan.label', default: 'Next')}" onmouseover="tooltip.show('${message(code: 'patient.create.flow.finish.monplan.tooltip')}');" onmouseout="tooltip.hide();"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
