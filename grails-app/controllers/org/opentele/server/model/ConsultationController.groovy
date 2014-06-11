package org.opentele.server.model

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.model.types.MeasurementTypeName
import org.opentele.server.model.types.PermissionName
import org.opentele.server.model.types.Unit

@SecurityWhiteListController
@Secured(PermissionName.NONE)
class ConsultationController {

    def sessionService
    def springSecurityService
    def consultationService

    private def createShowModel(Patient patientInstance) {
        [patientInstance: patientInstance]
    }

    @Secured(PermissionName.PATIENT_CONSULTATION)
    def addmeasurements() {
        log.debug 'index....'

        def patientInstance = Patient.get(params.id)
        if (!patientInstance) {
            // Setting up session values
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient')])
            sessionService.setNoPatient(session)
            redirect(action: "overview")
            return
        }
        sessionService.setPatient(session, patientInstance)
        createShowModel(patientInstance)
    }

    @Secured(PermissionName.PATIENT_CONSULTATION)
    def save() {
        log.debug 'save.....'
        log.debug 'params..:' + params

        Patient patient = Patient.get(params.id)
        Clinician clinician = Clinician.findByUser(springSecurityService.currentUser)

        Consultation consultation = consultationService.addConsultation(patient, clinician, params)
        if (!consultation.hasErrors()) {
            render ([success: true, message: message(code: 'consultation.saved.ok')] as JSON)
            return
        }

        def errors = []
        errors.addAll(consultation.errors.allErrors.collect {message(error: it)})
        render ([success: false, errors: errors] as JSON)
    }
}
