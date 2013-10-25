package opentele.server

import org.opentele.server.constants.Constants
import org.opentele.server.model.BlueAlarmCheck;
import org.opentele.server.model.Patient

class QuestionnaireScheduleCheckJob {
	def questionnaireService

    static triggers = {
        simple(name: 'CheckForBlueAlarms', startDelay: 20000, repeatInterval: Constants.BLUE_ALARM_CHECK_INTERVAL*60000, repeatCount: -1)
    }

    // Don't allow concurrent execution.
    // See http://grails-plugins.github.io/grails-quartz/guide/configuration.html
    // Plugin Configuration - Reference Documentation
    // Configuring concurrent execution
    def concurrent = false

    def execute() {
		log.debug "Checking for blue alarms.."

        Calendar checkTo = Calendar.getInstance()

        Date checkFromDate = BlueAlarmCheck.findPreviousCheckTime(checkTo.time)
        Calendar checkFrom = checkFromDate.toCalendar()

		Patient.findAllByMonitoringPlanIsNotNull().each { findBlueAlarmsForPatient(it, checkFrom, checkTo) }

        BlueAlarmCheck.checkedAt(checkTo.time)
    }

    private void findBlueAlarmsForPatient(Patient patient, Calendar checkFrom, Calendar checkTo) {
        def blueAlarms = questionnaireService.checkForBlueAlarms(patient, checkFrom, checkTo)

        if (blueAlarms) {
            patient.blueAlarmQuestionnaireIDs.addAll(blueAlarms)
            patient.save(failOnError: true)
            log.debug "New blue alarms for " + patient + ": " + blueAlarms
        }
    }
}
