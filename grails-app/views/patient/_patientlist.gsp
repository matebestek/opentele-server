<%@ page import="org.opentele.server.questionnaire.QuestionnaireService; org.opentele.server.model.types.Severity; org.opentele.server.model.PatientGroup"%>
<%@ page import="org.opentele.server.model.types.PatientState"%>
<%@ page import="org.opentele.server.model.Patient"%>
<%@ page import="org.opentele.server.PatientService" %>
<%
    def questionnaireService = (org.opentele.server.questionnaire.QuestionnaireService) grailsApplication.classLoader.loadClass('org.opentele.server.questionnaire.QuestionnaireService').newInstance()
%>


<div id="list-patient" class="content scaffold-list" role="main">
	<h1>
		<g:message code="patient.list.label" />
	</h1>
	<g:if test="${flash.message}">
		<div class="message" role="status">
			${flash.message}
		</div>
	</g:if>
	<table>
		<thead>
			<tr>
				<!--  Rows
           		 Situation, CPR nummer, Fornavn, Efternavn, Seneste spørgeskema, Type, Kvitteret
         		 -->
				<g:sortableColumn property="severity" title="${message(code: 'patient.situation.label')}" params="${params}"/>
				<g:sortableColumn property="firstName" title="${message(code: 'patient.firstNames.label')}" params="${params}"/>
				<g:sortableColumn property="lastName" title="${message(code: 'patient.lastName.label')}" params="${params}"/>
				<g:sortableColumn property="cpr" title="${message(code: 'patient.cpr.label', default: 'Cpr')}" params="${params}"/>

				<th>${message(code: 'patient.latestQuestionnaire.label')}</th>
				<th>${message(code: 'patient.group.label')}</th>

				<g:sortableColumn property="state" title="${message(code: 'patient.status')}" params="${params}"/>
			</tr>
		</thead>
		<tbody>
			<g:each in="${patients}" status="i" var="patientInstance">
				<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					<td>
						<%
                            def (icon, tooltip) = questionnaireService.iconAndTooltip(g, patientInstance)
                            if (tooltip) {
                                out << """<div data-tooltip="${tooltip}">"""
                            } else {
                                out << "<div>"
                            }
                            out << g.img(dir: "images", file: icon)
                            out << "</div>"
                      %>
					</td>
                    <td>
                        <g:link action="questionnaires" id="${patientInstance.id}"
                                data-tooltip="${message(code: 'patient.overview.goto.patient.tooltip')}">
                            ${fieldValue(bean: patientInstance, field: "firstName")}
                        </g:link>
                    </td>
                    <td>
                        <g:link action="questionnaires" id="${patientInstance.id}"
                                data-tooltip="${message(code: 'patient.overview.goto.patient.tooltip')}">
                            ${fieldValue(bean: patientInstance, field: "lastName")}
                        </g:link>
                    </td>
                    <td>
                        <g:link action="questionnaires" id="${patientInstance.id}"
                                data-tooltip="${message(code: 'patient.overview.goto.patient.tooltip')}">
							${patientInstance.formattedCpr}
						</g:link>
                    </td>
					<td>${formatDate(date: patientInstance.latestQuestionnaireUploadDate)}</td>
					<td>
                        <ul class="table">
							<g:each in="${patientInstance.groups}" var="group">
								<li>${group.name.encodeAsHTML()}</li>
							</g:each>
						</ul>
                    </td>
					<td>
						<g:message code="enum.patientstate.${patientInstance.state}"/>
					</td>
				</tr>
			</g:each>
		</tbody>
	</table>
</div>
