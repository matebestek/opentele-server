
<%@ page import="org.opentele.server.model.PassiveInterval" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<title><g:message code="passiveInterval.list.label" args="[session['name'].encodeAsHTML()]" /></title>
	</head>
	<body>
		<div id="list-passiveInterval" class="content scaffold-list" role="main">
			<h1><g:message code="passiveInterval.list.label" args="[session['name'].encodeAsHTML()]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
                        <g:sortableColumn property="intervalStartDate" title="${message(code: 'passiveInterval.intervalStartDate.label', default: 'Interval Start Date')}" />
						<g:sortableColumn property="intervalEndDate" title="${message(code: 'passiveInterval.intervalEndDate.label', default: 'Interval End Date')}" />
                        <g:sortableColumn property="comment" title="${message(code: 'passiveInterval.comment.label', default: 'i18n missing')}" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${passiveIntervalInstanceList}" status="i" var="passiveIntervalInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">


						<td><g:link action="show" id="${passiveIntervalInstance.id}"><g:formatDate formatName="default.date.format.notime" date="${passiveIntervalInstance.intervalStartDate}" /></g:link></td>

						<td><g:formatDate formatName="default.date.format.notime" date="${passiveIntervalInstance.intervalEndDate}" /></td>

                        <td><g:link action="show" id="${passiveIntervalInstance.id}">${fieldValue(bean: passiveIntervalInstance, field: "comment")}</g:link></td>

					</tr>
				</g:each>
				</tbody>
			</table>
            <g:form>
                <fieldset class="buttons">
                    <sec:ifAnyGranted roles="${org.opentele.server.model.types.PermissionName.PASSIVE_INTERVAL_CREATE}">
                        <g:link class="create" action="create" params="[patientId: patient.id]">
                            <g:message code="default.button.create.label" default="Create" />
                        </g:link>
                    </sec:ifAnyGranted>
                </fieldset>
            </g:form>
			<div class="pagination">
				<g:paginate total="${passiveIntervalInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
