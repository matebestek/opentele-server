package org.opentele.server.model

import grails.plugins.springsecurity.Secured
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.constants.Constants
import org.opentele.server.model.types.PatientState
import org.opentele.server.model.types.PermissionName
import org.springframework.dao.DataIntegrityViolationException

@Secured(PermissionName.NONE)
class PatientNoteController {
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def springSecurityService
    def sessionService
    def patientService

    @Secured(PermissionName.PATIENT_NOTE_READ_ALL)
    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.PATIENT_NOTE_READ_ALL)
    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        Patient patient = Patient.get(params.id)
        sessionService.setPatient(session, patient)

        def notes = PatientNote.findAllByPatient(patient, [sort: 'createdDate', order: 'desc', max: params.max, offset: params.offset])
        def isSeen = notes.collectEntries { [it, patientService.isNoteSeenByUser(it)] }
        sortNotes(notes, isSeen)

        [
                patientNoteInstanceList: notes,
                isSeen: isSeen,
                patientNoteInstanceTotal: patient.notes.size(),
                patient: patient
        ]
    }

    @Secured(PermissionName.PATIENT_NOTE_READ_ALL_TEAM)
    @SecurityWhiteListController
    def listTeam() {
        User u =springSecurityService.currentUser
        def notes = []
        if (u.isClinician()) {
            Clinician c = Clinician.findByUser(u)
            def patients = patientService.getPatientsForClinician(c)

            // Only show notes for the active patients.
            patients = patients.findAll { it.state == PatientState.ACTIVE }

            notes = patients.findAll{it.notes.size() > 0}.collectMany {
                it.notes
            }
        } //Non clinicians should not be able to see this

        def isSeen = notes.collectEntries { [it, patientService.isNoteSeenByUser(it)] }
        sortNotes(notes, isSeen)

        [
                patientNoteInstanceList: notes,
                isSeen: isSeen,
                patientNoteInstanceTotal: notes.size()
        ]
    }

    private void sortNotes(List<PatientNote> notes, Map<PatientNote, Boolean> isSeen) {
        switch (params.sort) {
            case 'note':
                notes.sort { it.note }
                break;
            case 'type':
                notes.sort { it.type.toString() }
                break;
            case 'reminderDate':
                notes.sort { it.reminderDate }
                break;
            case 'isSeen':
                notes.sort { isSeen[it] }
                break;
            case 'patient':
            default:
                notes.sort { it.patient.name() }
        }
        if (params.order == 'desc') {
            notes.reverse(true)
        }
    }

    @Secured(PermissionName.PATIENT_NOTE_CREATE)
    def create() {
        Patient p = Patient.get(params.patientId)
        sessionService?.setPatient(session, p)
        [patientNoteInstance: new PatientNote(params), patient:  p]
    }

    @Secured(PermissionName.PATIENT_NOTE_CREATE)
    @SecurityWhiteListController
    def save() {
        params.reminderDate = params.datePicker
        def patientNoteInstance = new PatientNote(params)
        patientNoteInstance.patient = Patient.get(params.patientId)
        if (!patientNoteInstance.save(flush: true)) {
            render(view: "create", model: [patientNoteInstance: patientNoteInstance, patient: patientNoteInstance.patient])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'patientNote.label', default: 'PatientNote'), patientNoteInstance.id])
        redirect(action: "show", id: patientNoteInstance.id)
    }

    @Secured(PermissionName.PATIENT_NOTE_READ)
    def show() {
        def patientNoteInstance = PatientNote.get(params.id)
        if (!patientNoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patientNote.label', default: 'PatientNote'), params.id])
            def patient = Patient.get(session[Constants.SESSION_PATIENT_ID])
            sessionService.setPatient(session, patient)
            redirect(action: "list", id: patient.id)
            return
        }

        sessionService.setPatient(session, patientNoteInstance.patient)
        // Maintain which view show is called from
        if (params.comingFrom) {
            [patientNoteInstance: patientNoteInstance, comingFrom: params.comingFrom]
        } else {
            [patientNoteInstance: patientNoteInstance]
        }
    }

    @Secured(PermissionName.PATIENT_NOTE_MARK_SEEN)
    @SecurityWhiteListController
    def markSeen() {
        def patientNoteInstance = PatientNote.get(params.id)
        def user = springSecurityService.currentUser
        if (user && user.isClinician() && patientNoteInstance) {
            def clinician = Clinician.findByUser(user)
            patientNoteInstance.seenBy.add(clinician)
        }

        render(view: "show", model: [patientNoteInstance: patientNoteInstance])
    }

    @Secured(PermissionName.PATIENT_NOTE_WRITE)
    def edit() {
        def patientNoteInstance = PatientNote.get(params.id)
        if (!patientNoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patientNote.label', default: 'PatientNote'), params.id])
            def patient = Patient.get(session[Constants.SESSION_PATIENT_ID])
            sessionService.setPatient(session, patient)
            redirect(action: "list", id: patient.id)
            return
        }

        sessionService.setPatient(session, patientNoteInstance.patient)
        [patientNoteInstance: patientNoteInstance]
    }

    @Secured(PermissionName.PATIENT_NOTE_WRITE)
    @SecurityWhiteListController
    def update() {
        def patientNoteInstance = PatientNote.get(params.id)

        if (!patientNoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patientNote.label', default: 'PatientNote'), params.id])
            def patient = Patient.get(session[Constants.SESSION_PATIENT_ID])
            sessionService.setPatient(session, patient)
            redirect(action: "list", id: patient.id)
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (patientNoteInstance.version > version) {
                patientNoteInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'patientNote.label', default: 'PatientNote')] as Object[],
                        "Another user has updated this PatientNote while you were editing")
                render(view: "edit", model: [patientNoteInstance: patientNoteInstance])
                return
            }
        }

        patientNoteInstance.properties = params

        def params = params
        //datePicker_day, datePicker_month, datePicker_year
        /**
         * We want to check if either all dataPicker fields have values or none of them do,
         * which the following expression will do:
         a  	b   c   ¬(a ? b) ? ¬(b ? c)
         T      T  	T           T
         T	    T	F           F
         T	    F	T           F
         T	    F	F           F
         F	    T	T           F
         F	    T	F           F
         F	    F	T           F
         F	    F	F           T
         **/
        def A = params.datePicker_day == ''
        def B = params.datePicker_month == ''
        def C = params.datePicker_year == ''
        def expr = !(A ^ B) & !(B ^ C)
        if(!expr) { //Only when our desired condition is _not_ true should we reject the error
            patientNoteInstance.errors.reject(
                    'default.date.datePicker.error',
                    [] as Object[],
                    "Påmindelsesdato feltet skal enten være helt udfyldt eller slet ikke udfyldt"
            )

        }

        if (patientNoteInstance.hasErrors() || !patientNoteInstance.save(flush: true)) {
            render(view: "edit", model: [patientNoteInstance: patientNoteInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'patientNote.label', default: 'PatientNote'), patientNoteInstance.id])
        redirect(action: "show", id: patientNoteInstance.id)
    }

    @Secured(PermissionName.PATIENT_NOTE_DELETE)
    @SecurityWhiteListController
    def delete() {
        def patientNoteInstance = PatientNote.get(params.id)

        if (!patientNoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patientNote.label'), params.id])
            def patient = Patient.get(session[Constants.SESSION_PATIENT_ID])
            sessionService.setPatient(session, patient)
            redirect(action: "list", id: patient.id)
            return
        }

        try {
            patientNoteInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'patientNote.label'), params.id])

            // If coming from team note list
            if (params.comingFrom && params.comingFrom == 'listTeam') {
                redirect(action: "listTeam")
            } else {
                redirect(action: "list", id: patientNoteInstance.patient.id)
            }
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'patientNote.label'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
