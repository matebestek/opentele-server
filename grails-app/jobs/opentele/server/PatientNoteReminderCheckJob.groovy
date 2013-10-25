package opentele.server

import org.opentele.server.constants.Constants
import org.opentele.server.model.PatientNote

class PatientNoteReminderCheckJob {

//    cronExpression: "s m h D M W Y"
                //    | | | | | | `- Year [optional]
                //    | | | | | `- Day of Week, 1-7 or SUN-SAT, ?
                //    | | | | `- Month, 1-12 or JAN-DEC
                //    | | | `- Day of Month, 1-31, ?
                //    | | `- Hour, 0-23
                //    | `- Minute, 0-59
                //    `- Second, 0-59

    static triggers = {
        simple(name: 'StartupCheckPatientNotes', startDelay: 20000, repeatInterval: 0, repeatCount: 0)
        //cron(name: 'NewDayCheckPatientNotes', cronExpression: "1 0 0 * * ?")          //Every day at 00:00:01
        //As long as we run some kind of test/develop, it's better if we have this running more often

        simple(name: 'RepeatCheckPatientNotes', startDelay: 20000, repeatInterval: Constants.PATIENT_NOTE_REMINDER_CHECK*60000, repeatCount: -1)
    }

    def execute() {
        log.debug("Running PatientNote reminders check!!")
        PatientNote.findAll().each {
            if (it.reminderDate && it.reminderDate.getTime() < Calendar.getInstance().getTimeInMillis()) {
                it.remindToday = true
            } else {
                //Don't set remindToday false: Clinicians that didn't work yesterday should still
                //see yesterdays reminders, thus remindToday should stay true (from yesterdays check)
            }
        }
    }
}

