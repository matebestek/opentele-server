package opentele.server

import org.opentele.server.model.Patient

class PatientOverviewCheckJob {
    def patientOverviewService
    def concurrent = false

    static triggers = {
        cron(name: 'NightlyPatientOverviewCheckJob', cronExpression: "0 0 23 * * ?") // Every night at 23:00
    }

    def execute() {
        log.info 'Starting nightly PatientOverview check job'
        Patient.findAll().each { patient ->
            if (patientOverviewService.overviewDetailsAreWrongFor(patient)) {
                log.error "Patient overview details are wrong for patient with id: ${patient.id}"
            }
        }
        log.info 'Finished nightly PatientOverview check job'
    }
}
