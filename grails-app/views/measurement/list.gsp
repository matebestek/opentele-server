
<%@ page import="org.opentele.server.model.Measurement"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'measurement.label', default: 'Measurement')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
	<div id="list-measurement" class="content scaffold-list" role="main">
		<h1><g:message code="default.list.label" args="[entityName]" /></h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<table>
			<thead>
				<tr>
					<g:sortableColumn property="time" title="${message(code: 'measurement.time.label', default: 'Time')}" />
					<g:sortableColumn property="value" title="${message(code: 'measurement.value.label', default: 'Value')}" />
					<g:sortableColumn property="unit" title="${message(code: 'measurement.unit.label', default: 'Unit')}" />
					<th><g:message code="measurement.patient.label" default="Patient" /></th>
					<th><g:message code="measurement.meter.label" default="Meter" /></th>
					<th><g:message code="measurement.measurementType.label" default="Measurement Type" /></th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${measurementInstanceList}" status="i"
					var="measurementInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td>
                            <g:link action="show" id="${measurementInstance.id}">
								${fieldValue(bean: measurementInstance, field: "time")}
							</g:link>
                        </td>
						<td>
							${measurementInstance.toString()}
						</td>
						<td>
							${fieldValue(bean: measurementInstance, field: "unit")}
						</td>
						<td>
							${fieldValue(bean: measurementInstance, field: "patient")}
						</td>
						<td>
							${fieldValue(bean: measurementInstance, field: "meter.model")}
						</td>
						<td>
							${fieldValue(bean: measurementInstance, field: "measurementType")}
						</td>
					</tr>
				</g:each>
			</tbody>
		</table>
		<div class="pagination">
			<g:paginate total="${measurementInstanceTotal}" />
		</div>
	</div>
</body>
</html>
