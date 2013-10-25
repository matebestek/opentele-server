<%@ page import="org.opentele.server.model.StandardThresholdSet" %>

<table>
	<thead>
		<tr>
			<th><g:message code="default.threshold.alertHigh.label" default="Alert High" /></th>
			<th><g:message code="default.threshold.warningHigh.label" default="Warning High" /></th>
			<th><g:message code="default.threshold.warningLow.label" default="Warning Low" /></th>
			<th><g:message code="default.threshold.alertLow.label" default="Alert Low" /></th>
		</tr>
	</thead>
	<tbody>

			<td><g:select 	name="alertHigh"
							from="${org.opentele.server.model.types.GlucoseInUrineValue.values()}"
							keys="${org.opentele.server.model.types.GlucoseInUrineValue.values()}"
							valueMessagePrefix="enum.glucoseiInUrineValue" noSelection="${['':"..."]}"
							value="${standardThresholdInstance.alertHigh}" />
			</td>
			<td><g:select 	name="warningHigh"
							from="${org.opentele.server.model.types.GlucoseInUrineValue.values()}"
							keys="${org.opentele.server.model.types.GlucoseInUrineValue.values()}"
							valueMessagePrefix="enum.glucoseiInUrineValue" noSelection="${['':"..."]}"
							value="${standardThresholdInstance.warningHigh}" />
			</td>
			<td><g:select 	name="warningLow"
							from="${org.opentele.server.model.types.GlucoseInUrineValue.values()}"
							keys="${org.opentele.server.model.types.GlucoseInUrineValue.values()}"
							valueMessagePrefix="enum.glucoseiInUrineValue" noSelection="${['':"..."]}"
							value="${standardThresholdInstance.warningLow}" />
			</td>
			<td><g:select 	name="alertLow"
							from="${org.opentele.server.model.types.GlucoseInUrineValue.values()}"
							keys="${org.opentele.server.model.types.GlucoseInUrineValue.values()}"
							valueMessagePrefix="enum.glucoseiInUrineValue" noSelection="${['':"..."]}"
							value="${standardThresholdInstance.alertLow}" />
			</td>
		</tr>
	</tbody>
</table>
