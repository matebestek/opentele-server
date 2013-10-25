;
(function ($, window, document, undefined) {

    // Create the defaults once
    var pluginName = "scheduleViewModel",
        defaults = {
            scheduleType: undefined,
            weekdays: [],
            allWeekdays: ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"],
            daysInMonth: [],
            timesOfDay: [],
            intervalInDays: 2,
            startingDate: '',
            specificDate: '',
            reminderStartMinutes: 0,
            additionalBindings: undefined
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;
        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        if(!ko) {
            throw "Depends on Knockout.js - please include before this script"
        }
        if(!_) {
            throw "Depends on Underscore.js - please include before this script"
        }
        this.init();
    }
    var TimeOfDay = function(hour, minute) {
        var self = this;
        while(hour.length < 2) { hour = "0" + hour; }
        while(minute.length < 2) { minute = "0" + minute; }
        //noinspection JSUnresolvedFunction
        self.hour = ko.observable(hour);
        //noinspection JSUnresolvedFunction
        self.minute = ko.observable(minute);
    };

    var ScheduleViewModel = function(params) {

        var self = this;

        //noinspection JSUnresolvedFunction
        self.scheduleType = ko.observable(params.scheduleType);

        //For weekday schedules
        //noinspection JSUnresolvedFunction
        self.weekdays = ko.observableArray(params.weekdays);
        self.allDays = function() {
            self.weekdays.push(params.allWeekdays);

        };
        self.noDays = function() {
            self.weekdays.removeAll()
        };

        //For monthly schedules
        self.daysInMonthOptions = ['1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28'];
        //noinspection JSUnresolvedFunction
        self.daysInMonth = ko.observableArray(params.daysInMonth);

        var timesOfDay = _.map(params.timesOfDay, function(timeOfDay) {
            var hourMinute = timeOfDay.split(":");
            return new TimeOfDay(hourMinute[0], hourMinute[1]);
        });
        //noinspection JSUnresolvedFunction
        self.timesOfDay = ko.observableArray(timesOfDay);
        self.addTimeOfDay = function() {
            self.timesOfDay.push(new TimeOfDay("", "00"));
        };
        self.removeTimeOfDay = function(timeOfDay) {
            self.timesOfDay.remove(timeOfDay);
        };

        //For NthDay schedules
        //noinspection JSUnresolvedFunction
        self.intervalInDays = ko.observable(params.intervalInDays);
        //noinspection JSUnresolvedFunction
        self.startingDate = ko.observable(params.startingDate);

        //noinspection JSUnresolvedFunction
        self.specificDate = ko.observable(params.specificDate);

        //noinspection JSUnresolvedFunction
        self.reminderStartMinutes = ko.observable(params.reminderStartMinutes);


        self.mappedToJson = {
                type: self.scheduleType,
                timesOfDay: self.timesOfDay,
                weekdays: self.weekdays,
                daysInMonth: self.daysInMonth,
                intervalInDays: self.intervalInDays,
                startingDate: self.startingDate,
                specificDate: self.specificDate,
                reminderStartMinutes: self.reminderStartMinutes
        };

        if(params.additionalBindings && typeof params.additionalBindings === "function") {
            params.additionalBindings(self)
        }

        self.json = ko.computed(function() {
            return  ko.toJSON(self.mappedToJson);
        }, self);
    };


    Plugin.prototype = {
        init: function () {
            // Place initialization logic here
            // You already have access to the DOM element and
            // the options via the instance, e.g. this.element
            // and this.settings
            // you can add more functions like the one below and
            // call them like so: this.yourOtherFunction(this.element, this.settings).
            var viewModel = new ScheduleViewModel(this.settings);

            //noinspection JSUnresolvedFunction
            ko.applyBindings(viewModel);
        }
    };

    $.fn[ pluginName ] = function (options) {
        return this.each(function () {
            if (!$.data(this, "plugin_" + pluginName)) {
                $.data(this, "plugin_" + pluginName, new Plugin(this, options));
            }
        });
    };

})(jQuery, window, document);
