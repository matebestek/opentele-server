<%@ page import="org.opentele.server.model.types.PermissionName; org.opentele.server.model.PatientNote" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="title" value="${message(code: 'patientNote.list.title', args: [patient.name.encodeAsHTML()])}"/>
    <title>${title}</title>
</head>

<body>
<div id="list-patientNote" class="content scaffold-list" role="main">
    <h1>${title}</h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
            <tr>
                <g:sortableColumn property="note" title="${message(code: 'patientNote.note.label', default: 'Note')}"/>

                <g:sortableColumn property="type" title="${message(code: 'patientNote.type.label', default: 'Type')}"/>

                <g:sortableColumn property="reminderDate" title="${message(code: 'patientNote.reminder.label', default: 'Reminder Date')}"/>

                <g:sortableColumn property="isSeen" title="${message(code: 'patientNote.isSeen.label', default: 'Seen?')}"/>
            </tr>
        </thead>
        <tbody>
        <g:each in="${patientNoteInstanceList}" status="i" var="patientNoteInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <g:if test="${patientNoteInstance.remindToday}">
                    <td id="patientNoteReminder" onclick="window.location='${createLink(action: 'show', id: patientNoteInstance.id)}'" onmouseover="this.style.textDecoration='underline'" onmouseout="this.style.textDecoration='none'" style="cursor:pointer;">
                </g:if>
                <g:else>
                    <td onclick="window.location='${createLink(action: 'show', id: patientNoteInstance.id)}'" onmouseover="this.style.textDecoration='underline'" onmouseout="this.style.textDecoration='none'" style="cursor:pointer;">
                </g:else>
                ${fieldValue(bean: patientNoteInstance, field: "note")}
                </td>
                <td>${fieldValue(bean: patientNoteInstance, field: "type")}</td>

                <td><g:formatDate date="${patientNoteInstance.reminderDate}"/></td>

                <td><g:message code="default.yesno.${isSeen[patientNoteInstance]}"/></td>
            </tr>
        </g:each>
        </tbody>
    </table>
    <g:form>
        <fieldset class="buttons">
            <g:hiddenField name="id" value="${patientNoteInstance?.id}" />
            <sec:ifAnyGranted roles="${PermissionName.PATIENT_NOTE_CREATE}">
                <g:link class="create" action="create" id="${patientNoteInstance?.id}" params="[patientId: patient.id]">
                    <g:message code="default.button.create.label" default="Create" />
                </g:link>
            </sec:ifAnyGranted>
        </fieldset>
    </g:form>
    <div class="pagination">
        <g:paginate total="${patientNoteInstanceTotal}" id="${patient.id}"/>
    </div>
</div>
</body>
</html>
