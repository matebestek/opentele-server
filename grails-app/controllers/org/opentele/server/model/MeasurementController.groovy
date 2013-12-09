package org.opentele.server.model
import grails.plugins.springsecurity.Secured
import org.opentele.server.TimeFilter
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.model.types.PermissionName
import org.springframework.dao.DataIntegrityViolationException

@Secured(PermissionName.NONE)
class MeasurementController {
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def measurementService
    def patientService
    def sessionService

    @Secured(PermissionName.MEASUREMENT_READ_ALL)
    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.MEASUREMENT_READ_ALL)
    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [measurementInstanceList: Measurement.list(params), measurementInstanceTotal: Measurement.count()]
    }

    @Secured(PermissionName.MEASUREMENT_CREATE)
    def create() {
        [measurementInstance: new Measurement(params)]
    }

    @Secured(PermissionName.MEASUREMENT_CREATE)
    def save() {
        def measurementInstance = new Measurement(params)
        if (!measurementInstance.save(flush: true)) {
            render(view: "create", model: [measurementInstance: measurementInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'measurement.label')])
        redirect(action: "show", id: measurementInstance.id)
    }

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

    @Secured(PermissionName.MEASUREMENT_WRITE)
    def edit() {
        def measurementInstance = Measurement.get(params.id)
        if (!measurementInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'measurement.label')])
            redirect(action: "list")
            return
        }

        [measurementInstance: measurementInstance]
    }

    @Secured(PermissionName.MEASUREMENT_WRITE)
    def update() {
        def measurementInstance = Measurement.get(params.id)
        if (!measurementInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'measurement.label')])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (measurementInstance.version > version) {
                measurementInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'measurement.label', default: 'Measurement')] as Object[],
                        "Another user has updated this Measurement while you were editing")
                render(view: "edit", model: [measurementInstance: measurementInstance])
                return
            }
        }

        measurementInstance.properties = params

        if (!measurementInstance.save(flush: true)) {
            render(view: "edit", model: [measurementInstance: measurementInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'measurement.label')])
        redirect(action: "show", id: measurementInstance.id)
    }


    @Secured(PermissionName.MEASUREMENT_DELETE)
    def delete() {
        def measurementInstance = Measurement.get(params.id)
        if (!measurementInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'measurement.label')])
            redirect(action: "list")
            return
        }

        try {
            measurementInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'measurement.label')])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'measurement.label')])
            redirect(action: "show", id: params.id)
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
