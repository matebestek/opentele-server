<%@ page import="grails.converters.JSON; org.opentele.server.model.Schedule; org.opentele.server.model.QuestionnaireSchedule" %>
<%@ page import="org.opentele.server.model.types.Weekday" %>
<%@ page import="org.opentele.server.model.Schedule.ScheduleType" %>
<g:javascript src="knockout-2.2.0.js"/>
<g:javascript src="json2.js"/>
<g:javascript src="scheduleViewModel.js"/>

<r:script>
$(function() {
    $('form').scheduleViewModel({
            scheduleType: '${scheduleType}',
            weekdays: ${weekdays as JSON},
            allWeekdays: ["${Weekday.MONDAY}", "${Weekday.TUESDAY}", "${Weekday.WEDNESDAY}", "${Weekday.THURSDAY}", "${Weekday.FRIDAY}", "${Weekday.SATURDAY}", "${Weekday.SUNDAY}"],
            daysInMonth: [${daysInMonth as JSON}],
            timesOfDay: [${timesOfDay.collect { "'${it.hour}:${it.minute}'" }.join(', ')}],
            intervalInDays: ${intervalInDays},
            startingDate: '${startingDate}',
            specificDate: '${specificDate}',
            reminderStartMinutes: ${reminderStartMinutes},
            additionalBindings: function(self)  {
               //For Questionnaire select
                self.id = ${id};
                self.version = ${version};

                self.questionnaireGroupId = ${questionnaireGroupId};
                self.selectableQuestionnaires = ${selectableQuestionnaires as JSON};

                //self.selectedQuestionnaire = ko.observable();
                self._selectedQuestionnaire = ko.observable();
                self.selectedQuestionnaire = ko.computed({
                    read: function() { return self._selectedQuestionnaire() },
                    write: function(value) {
                        self._selectedQuestionnaire(value);
                        if(self.scheduleId !== "") {
                            return;
                        }
                        console.log("self:");
                        console.log(self);
                        console.log(value);
                    }
                });

                for (var i=0; i < self.selectableQuestionnaires.length; i++) {
                    var questionnaire = self.selectableQuestionnaires[i];
                    if (questionnaire.id == '${selectedQuestionnaireId}') {
                        self.selectedQuestionnaire(questionnaire)
                    }
                }

                self.validationErrors = ${validationErrors as JSON};

                self.mappedToJson.id = self.id;
                self.mappedToJson.version = self.version;
                self.mappedToJson.questionnaireGroupId = self.questionnaireGroupId;
                self.mappedToJson.selectedQuestionnaire = self.selectedQuestionnaire;
            }
    });
});
</r:script>


<ul class="errors" role="alert" data-bind="visible: validationErrors.length > 0, foreach: validationErrors">
    <li data-bind="text: message"></li>
</ul>

<div class="fieldcontain ${hasErrors(bean: questionnaireSchedule, field: 'patientQuestionnaire', 'error')} required">
    <label for="questionnaire">
        <g:message code="questionnaireSchedule.patientQuestionnaire.label"/> <span class="required-indicator">*</span>
    </label>
    <select id="questionnaire"
            data-bind="options: selectableQuestionnaires, optionsText: 'name', value: 'id', value: selectedQuestionnaire"></select>
</div>

<div class="fieldcontain">
    <label>
        <g:message code="questionnaireGroup2QuestionnaireHeader.schedule.label"/>:
    </label>
</div>

<div class="fieldcontain noborder">
    <label for="inherited"
           data-tooltip="${message(code: 'questionnaireGroup2QuestionnaireHeader.questionnaireSchedule.inherit.tooltip')}">
        <g:message code="questionnaireGroup2QuestionnaireHeader.questionnaireSchedule.inherit.label"/>
    </label>
    <g:radio id="inherited" name="type" value="" data-bind="checked: scheduleType"/>
</div>
<g:each in="${ScheduleType.values()}" var="scheduleType">
<div class="fieldcontain noborder">
    <label for="unscheduled_${scheduleType}"
           data-tooltip="${message(code: "questionnaireGroup2QuestionnaireHeader.questionnaireSchedule.${scheduleType}.tooltip")}">
        <g:message code="schedule.scheduleType.${scheduleType.toString()}.label"/>
    </label>
    <g:radio id="unscheduled_${scheduleType}" name="type" value="${scheduleType}"
             data-bind="checked: scheduleType"/>
</div>
</g:each>


<tmpl:/schedule/scheduleTypes/>

<g:hiddenField name="viewModel" data-bind="value: json()"/>
