package org.opentele.server.model
import grails.plugins.springsecurity.Secured
import org.opentele.server.TimeFilter
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.model.types.PermissionName
import org.springframework.dao.DataIntegrityViolationException

@Secured(PermissionName.NONE)
class MeasurementController {
    def measurementService
    def patientService
    def sessionService

    @Secured(PermissionName.MEASUREMENT_READ)
    @SecurityWhiteListController
    def show() {
        def measurement = Measurement.get(params.id)

        if (measurement.measurementNodeResult) {
            redirect(controller: 'patient', action: 'questionnaire', id: measurement.measurementNodeResult.completedQuestionnaire.id)
        } else if (measurement.conference) {
            redirect(controller: 'patient', action: 'conference', id: measurement.conference.id)
        } else {
            throw new IllegalStateException("Measurement ${params.id} (${measurement}) belongs to neither a questionnaire nor a conference")
        }
    }

    @Secured(PermissionName.MEASUREMENT_READ)
    def patientMeasurements(long patientId) {
        def patient = Patient.get(patientId)

        sessionService.setPatient(session, patient)
        def timeFilter = TimeFilter.fromParams(params)

        def (tables, bloodSugarData) = measurementService.dataForTablesAndBloodsugar(patient, timeFilter)

        [patientInstance: patient, tables: tables, bloodSugarData: bloodSugarData, filter: timeFilter]
    }

    @Secured(PermissionName.MEASUREMENT_READ)
    def patientGraphs(long patientId) {
        def patient = Patient.get(patientId)

        sessionService.setPatient(session, patient)
        def timeFilter = TimeFilter.fromParams(params)

        def measurements = measurementService.dataForGraphs(patient, timeFilter)

        [patientInstance: patient, measurements: measurements, filter: timeFilter]
    }

    @Secured(PermissionName.MEASUREMENT_READ)
    def graph(long patientId, String measurementType) {
        def patient = Patient.get(patientId)

        sessionService.setPatient(session, patient)
        def timeFilter = TimeFilter.fromParams(params)

        def measurement = measurementService.dataForGraph(patient, timeFilter, measurementType)

        [patient: patient, measurement: measurement];
    }

    @Secured(PermissionName.MEASUREMENT_READ)
    def bloodsugar(long patientId) {
        def patient = Patient.get(patientId)

        sessionService.setPatient(session, patient)
        def timeFilter = TimeFilter.fromParams(params)

        def bloodSugarData = measurementService.dataForBloodsugar(patient, timeFilter)

        [patientInstance: patient, bloodSugarData: bloodSugarData, filter: timeFilter]
    }
}
