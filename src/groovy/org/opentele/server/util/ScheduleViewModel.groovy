package org.opentele.server.util
import grails.converters.JSON
import org.opentele.server.model.Schedule
import org.opentele.server.model.types.Weekday

class ScheduleViewModel {
    @Delegate Map viewModel = [:]

    ScheduleViewModel(String json) {
        viewModel = JSON.parse(json)
    }

    List validateViewModel() {
        def validationErrors = []

        if (viewModel.type && viewModel.type != Schedule.ScheduleType.UNSCHEDULED.toString()) {
            validationErrors.add(validateTimesOfDay(viewModel.timesOfDay))
            validationErrors.add(validateDaysInMonth(viewModel.daysInMonth))
            validationErrors.add(validateIntervalInDays(viewModel.intervalInDays))
            validationErrors.add(validateStartingDate(viewModel.startingDate))
            validationErrors.add(validateWeekDays(viewModel.weekdays))
            validationErrors.add(validateSpecificDate(viewModel.specificDate))
        }

        validationErrors.removeAll([null])
        validationErrors

    }

    private validateSpecificDate(specificDate) {
        if (viewModel.type == Schedule.ScheduleType.SPECIFIC_DATE.name()) {
            try {
                parseDate(specificDate.toString())
                return null
            } catch(java.text.ParseException ignored) {
                getErrorMessage("specificDate", "questionnaireSchedule.error.invalidSpecificDate")
            }
        }
    }

    private validateWeekDays(weekdays) {
        if (viewModel.type == Schedule.ScheduleType.WEEKDAYS.name()) {
            if (weekdays && weekdays.size() > 0) {
                return null
            } else {
                getErrorMessage("weekdays", "questionnaireSchedule.error.noWeekdaysSelected")
            }
        }
    }

    private validateStartingDate(startingDate) {
        if (viewModel.type == Schedule.ScheduleType.EVERY_NTH_DAY.name()) {
           try {
               parseDate(startingDate)
               return null
           } catch(java.text.ParseException ignored) {
               getErrorMessage("startingDate", "questionnaireSchedule.error.invalidStartDate")
           }
        }
    }

    private validateIntervalInDays(intervalInDays) {                                                                //It's okay: isDigits have just been called and short-circuit AND is being used.
        if (viewModel.type == Schedule.ScheduleType.EVERY_NTH_DAY.name() && !(isDigits(intervalInDays as String) && Integer.parseInt(intervalInDays as String) > 0)) {
            getErrorMessage("intervalInDays", "questionnaireSchedule.error.invalidInterval")
        }
    }

    private validateDaysInMonth(daysInMonth) {
        if (viewModel.type == Schedule.ScheduleType.MONTHLY.name() && isEmpty(daysInMonth)) {
            getErrorMessage("daysInMonth", "questionnaireSchedule.error.invalidDaysInMonth")
        }
    }

    private validateTimesOfDay(timesOfDay) {
        if(isEmpty(timesOfDay)) {
            return getErrorMessage("timesOfDay", "questionnaireSchedule.error.atLeastOneTimeOfDay")
        }

        boolean duplicateTimes
        timesOfDay.eachWithIndex { timeA, idxA ->
            timesOfDay.eachWithIndex { timeB, idxB ->
                duplicateTimes = duplicateTimes || ((timeA.hour == timeB.hour) && (timeA.minute == timeB.minute) && (idxA != idxB))
            }
        }

        if (duplicateTimes) {
            return getErrorMessage('timesOfDay', "questionnaireSchedule.error.multipleEqualTimeOfDay")
        }

        def validates = timesOfDay.every({
            isDigits(it.hour.toString()) && isDigits(it.minute.toString())
        })

        validates = validates && timesOfDay.every({
            it.hour && ((it.hour as Integer) < 24) && it.minute && ((it.minute as Integer) < 60)
        })

        validates ? null : getErrorMessage("timesOfDay", "questionnaireSchedule.error.invalidTime")
    }

    private def getErrorMessage(field, messageCode) {
        [
            field: field,
            message: messageCode
        ]
    }

    def updateSchedule(Schedule schedule) {
        schedule.type = scheduleType()
        schedule.timesOfDay = timesOfDay()
        schedule.reminderStartMinutes = reminderStartMinutes()

        switch (schedule.type) {
            case Schedule.ScheduleType.UNSCHEDULED:
                break
            case Schedule.ScheduleType.WEEKDAYS:
                schedule.weekdays = weekdays()
                break
            case Schedule.ScheduleType.MONTHLY:
                schedule.daysInMonth = daysInMonth()
                break
            case Schedule.ScheduleType.EVERY_NTH_DAY:
                def dayInterval = dayInterval()
                if (!schedule.hasErrors()) {
                    schedule.dayInterval = dayInterval
                    schedule.startingDate = startingDate()
                }
                break
            case Schedule.ScheduleType.SPECIFIC_DATE:
                schedule.specificDate = specificDate()
                break
            default:
                throw new RuntimeException("Unsupported schedule type: '${schedule.type}'")
        }
    }

    private List<Integer> daysInMonth() {
        viewModel.daysInMonth.collect{
            it as Integer
        }
    }

    private Schedule.ScheduleType scheduleType() {
        Schedule.ScheduleType.valueOf(viewModel.type) as Schedule.ScheduleType
    }

    private Integer reminderStartMinutes()
    {
        viewModel.reminderStartMinutes as Integer;
    }

    private List<Schedule.TimeOfDay> timesOfDay() {
        if (scheduleType() == Schedule.ScheduleType.UNSCHEDULED) {
            []
        } else {
            viewModel.timesOfDay.collect { Schedule.TimeOfDay.toTimeOfDay(it.hour, it.minute)}
        }
    }

    private List<Weekday> weekdays() {
        if (scheduleType() != Schedule.ScheduleType.WEEKDAYS) {
            []
        } else {
            viewModel.weekdays.collect({Weekday.valueOf(it)})
        }
    }

    private Integer dayInterval() {
        viewModel.intervalInDays as Integer
    }

    private Schedule.StartingDate startingDate() {
        def startingDate = Calendar.getInstance()
        startingDate.time = parseDate(viewModel.startingDate)

        Schedule.StartingDate.fromCalendar(startingDate)
    }

    private Schedule.StartingDate specificDate() {
        def specificDate = Calendar.getInstance()
        if(isEmpty(viewModel.specificDate)){
            null
        } else {
            specificDate.time = parseDate(viewModel.specificDate)
            Schedule.StartingDate.fromCalendar(specificDate)
        }
    }

    def parseDate(String date) {
        try {
            Date.parse("dd-MM-yyyy", date)
        } catch (e) {
            Date.parse("dd/MM/yyyy", date)
        }
    }

    private boolean isEmpty(obj) {
        !obj || obj.empty
    }

    private String formatWith2Digits(int i) {
        String.format('%02d', i)
    }

    private boolean isDigits(String s) {
        s ==~ /\d+/
    }


}
