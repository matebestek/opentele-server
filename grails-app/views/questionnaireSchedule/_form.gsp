<%@ page import="grails.converters.JSON; org.opentele.server.model.QuestionnaireSchedule" %>
<%@ page import="org.opentele.server.model.types.Weekday" %>
<%@ page import="org.opentele.server.model.Schedule.ScheduleType" %>
<g:javascript src="knockout-2.2.0.js"/>
<g:javascript src="json2.js"/>
<g:javascript src="scheduleViewModel.js"/>
<link href="${resource(dir: 'css', file: 'jquery-ui.custom.css')}" rel="stylesheet">
<g:javascript>
$(function() {
    $('form').scheduleViewModel({
        scheduleType: '${scheduleType}',
        weekdays: ${weekdays},
        allWeekdays: ["${Weekday.MONDAY}", "${Weekday.TUESDAY}", "${Weekday.WEDNESDAY}", "${Weekday.THURSDAY}", "${Weekday.FRIDAY}", "${Weekday.SATURDAY}", "${Weekday.SUNDAY}"],
        daysInMonth: ${daysInMonth},
        timesOfDay: [${timesOfDay?.collect { "'${it.hour}:${it.minute}'" }?.join(', ')}],
        intervalInDays: ${intervalInDays},
        startingDate: '${startingDate}',
        specificDate: '${specificDate}',
        reminderStartMinutes: ${reminderStartMinutes},
        additionalBindings: function(self)  {
            self.scheduleId = ${id};
            self.scheduleVersion = ${version};
            self.monitoringPlanId = ${monitoringPlanId};
            self.selectedQuestionnaire = ko.observable();
            self.selectableQuestionnaires = ${selectableQuestionnaires};
            /* CAN THIS BE REMOVED??   /jhs
            self._selectedQuestionnaire = ko.observable();
            self.selectedQuestionnaire = ko.computed({
                read: function() { return self._selectedQuestionnaire() },
                write: function(value) {
                    self._selectedQuestionnaire(value);
                    if(self.scheduleId !== "") {
                        return;
                    }
                    self.scheduleType(value.type);
                    self.timesOfDay($.map(value.timesOfDay, function(it) { return new TimeOfDay(it.hour.toString(), it.minute.toString()) }));
                    self.daysInMonth(value.daysInMonth);
                    self.weekdays(value.weekdays);
                    self.intervalInDays(value.intervalInDays);
                    self.startingDate(value.startingDate);
                    self.specificDate(value.specificDate);
                    self.reminderStartMinutes(value.reminderStartMinutes);
                }
            });
            */

            for (var i=0; i < self.selectableQuestionnaires.length; i++) {
                var questionnaire = self.selectableQuestionnaires[i];
                if (questionnaire.id == ${selectedQuestionnaireId ?: -1}) {
                    self.selectedQuestionnaire(questionnaire)
                }
            }

            self.validationErrors =  self.validationErrors = ${validationErrors};
            self.mappedToJson.id = self.scheduleId;
            self.mappedToJson.version = self.scheduleVersion;
            self.mappedToJson.selectedQuestionnaire = self.selectedQuestionnaire;
            self.mappedToJson.monitoringPlanId = self.monitoringPlanId;
        }
        });
    })
</g:javascript>


<ul class="errors" role="alert" data-bind="visible: validationErrors.length > 0, foreach: validationErrors">
    <li data-bind="text: message"></li>
</ul>

<div class="fieldcontain ${hasErrors(bean: questionnaireSchedule, field: 'patientQuestionnaire', 'error')} required">
    <label for="questionnaire">
        <g:message code="questionnaireSchedule.patientQuestionnaire.label"/> <span class="required-indicator">*</span>
    </label>

    <select id="questionnaire" data-bind="
        options: selectableQuestionnaires,
        optionsText: function(item) {
            if (item.hasActiveQuestionnaire) {
                return item.name;
            } else {
                return '-' + item.name + '- ${message(code: 'questionnaireSchedule.noActiveVersion')}';
            }
        },

        value: selectedQuestionnaire">
    </select>
</div>

<g:each in="${ScheduleType.values()}" var="scheduleType">
    <div class="fieldcontain noborder">
        <label for="unscheduled_${scheduleType}"
               data-tooltip="${message(code: "schedule.scheduleType.${scheduleType.toString()}.tooltip")}">
            <g:message code="schedule.scheduleType.${scheduleType.toString()}.label"/>
        </label>
        <g:radio id="unscheduled_${scheduleType}" name="type" value="${scheduleType}" data-bind="checked: scheduleType"/>
    </div>
</g:each>

<tmpl:/schedule/scheduleTypes/>

<g:hiddenField name="viewModel" data-bind="value: json()"/>
