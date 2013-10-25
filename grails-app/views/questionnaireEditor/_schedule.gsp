<%@ page import="org.opentele.server.model.Schedule.ScheduleType" %>
<%@ page import="org.opentele.server.model.types.Weekday" %>

<tooltip:resources />

<script type="text/javascript">

function ScheduleViewModel() {
    var self = this;

    self.scheduleType = ko.observable('${questionnaire.standardSchedule.type}');

    self.weekdays = ko.observableArray();
    self.allDays = function () {
        self.weekdays.removeAll();
        self.weekdays.push("${Weekday.MONDAY}", "${Weekday.TUESDAY}", "${Weekday.WEDNESDAY}", "${Weekday.THURSDAY}", "${Weekday.FRIDAY}", "${Weekday.SATURDAY}", "${Weekday.SUNDAY}");
    };
    self.noDays = function () {
        self.weekdays.removeAll()
    };

    self.daysInMonthOptions = ['1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28'];
    self.daysInMonth = ko.observableArray();

    //self.timesOfDay = ko.observableArray(${timesOfDay.collect({"new TimeOfDay('${g.formatNumber(number: it.hour, minIntegerDigits: 2)}', '${g.formatNumber(number: it.minute, minIntegerDigits: 2)}')"})});
    self.timesOfDay = ko.observableArray();
    self.addTimeOfDay = function() {
        self.timesOfDay.push(new TimeOfDay("", ""));
    };
    self.removeTimeOfDay = function(timeOfDay) {
        self.timesOfDay.remove(timeOfDay);
    };

    self.intervalInDays = ko.observable(${questionnaire.standardSchedule.intervalInDays});
    self.startingDate = ko.observable('${questionnaire.standardSchedule.startingDate.toDate().format("dd-MM-yyyy")}');

    self.specificDate = ko.observable('${questionnaire.standardSchedule.specificDate?.toDate()?.format("dd-MM-yyyy")}');

    self.reminderStartMinutes = ko.observable(${questionnaire.standardSchedule.reminderStartMinutes});

    self.json = ko.computed(function() {
        return ko.toJS({
            type: self.scheduleType,
            weekdays: self.weekdays,
            daysInMonth: self.daysInMonth,
            intervalInDays: self.intervalInDays,
            startingDate: self.startingDate,
            specificDate: self.specificDate,
            timesOfDay: self.timesOfDay,
            reminderStartMinutes: self.reminderStartMinutes
        });
    }, self);


    self.fromJson = function (json) {
        self.scheduleType(json.type);
        self.weekdays(json.weekdays);
        self.daysInMonth(json.daysInMonth);
        self.intervalInDays(json.intervalInDays);
        self.startingDate(json.startingDate.replace(/\//g, '-'));
        self.specificDate(json.specificDate.replace(/\//g, '-'));
        self.timesOfDay(json.timesOfDay.map(function (it) {
            return new TimeOfDay(it.hour, it.minute)
        }));
        self.reminderStartMinutes(json.reminderStartMinutes)
    };
}

function TimeOfDay(hour, minute) {
    var self = this;
    while(hour.length < 2) { hour = "0" + hour; }
    while(minute.length < 2) { minute = "0" + minute; }
    self.hour = ko.observable(hour);
    self.minute = ko.observable(minute);
}


$(function() {
    standardScheduleViewModel = new ScheduleViewModel();
    ko.applyBindings(standardScheduleViewModel);
})
</script>
<span class="nav-header"><g:message code="questionnaireEditor.schedule"/> </span>

<g:each in="${ScheduleType.values()}" var="scheduleType">
<div class="fieldcontain noborder">
    <label class="radio" data-tooltip="${message(code: "schedule.scheduleType.${scheduleType}.tooltip")}">
        <g:message code="schedule.scheduleType.${scheduleType}.label" />
        <input type="radio" id="unscheduled" name="type" value="${scheduleType}" data-bind="checked: scheduleType" />
    </label>
</div>
</g:each>

<tmpl:/schedule/timesOfDay/>
<tmpl:/schedule/reminderStart/>
<tmpl:/schedule/weekdaySchedule/>
<tmpl:/schedule/monthlySchedule/>
<tmpl:/schedule/nthDaySchedule/>
<tmpl:/schedule/specificDate/>
