package org.opentele.server.model

import grails.test.mixin.*
import grails.buildtestdata.mixin.Build
import org.opentele.server.PatientService
import org.opentele.server.SessionService
import org.opentele.server.model.types.NoteType

@TestFor(PatientNoteController)
@Build([Patient, PatientNote])
class PatientNoteControllerTests {
    def populateValidParams(params) {
        assert params != null
        params['note'] = "foobar"
        params['type'] = NoteType.NORMAL
        return params
    }

    void testIndex() {
        controller.index()

        assert response.redirectedUrl == "/patientNote/list"
    }

    void testCanGiveEmptyListOfPatientNotes() {
        def (patient, patientId) = createPatient()

        def sessionServiceControl = mockFor(SessionService)
        sessionServiceControl.demand.setPatient {session, patientForSession ->
            assert patientForSession == patient
        }
        controller.sessionService = sessionServiceControl.createMock()

        controller.params.id = patientId

        def model = controller.list()

        assert model.patient == patient
        assert model.patientNoteInstanceList == []
        assert model.patientNoteInstanceTotal == 0
    }

    void testGives10PatientNotesOutOf20ForPagination() {
        def (Patient patient, patientId) = createPatient()
        20.times { patient.addToNotes(PatientNote.build()) }
        patient.save(failOnError: true)

        def sessionServiceControl = mockFor(SessionService)
        sessionServiceControl.demand.setPatient {session, patientForSession ->
            assert patientForSession == patient
        }
        controller.sessionService = sessionServiceControl.createMock()

        def patientServiceControl = mockFor(PatientService, true)
        patientServiceControl.demand.isNoteSeenByUser(10..10) { false }
        controller.patientService = patientServiceControl.createMock()

        controller.params.id = patientId

        def model = controller.list()

        assert model.patient == patient
        assert model.patientNoteInstanceList.size() == 10
        assert model.patientNoteInstanceTotal == 20
    }

    void testCreate() {
        def model = controller.create()

        assert model.patientNoteInstance != null
    }

    void testSave() {
        def ret = createPatient()
        controller.params.patientId = ret[1]
        controller.save()

        assert model.patientNoteInstance != null
        assert view == '/patientNote/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/patientNote/show/1'
        assert controller.flash.message != null
        assert PatientNote.count() == 1
    }

    void testUpdate() {
        populateValidParams(params)
        def patientNote = new PatientNote(params)
        def ret = createPatient()
        patientNote.patient = ret[0]
        assert patientNote.save() != null

        // test invalid parameters in update
        params.id = patientNote.id

        params.type = null

        controller.update()

        assert view == "/patientNote/edit"
        assert model.patientNoteInstance != null

        patientNote.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/patientNote/show/$patientNote.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        patientNote.clearErrors()

        populateValidParams(params)
        params.id = patientNote.id
        params.version = -1
        controller.update()

        assert view == "/patientNote/edit"
        assert model.patientNoteInstance != null
        assert model.patientNoteInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        def (Patient patient, patientId) = createPatient()
        def note1 = PatientNote.build()
        patient.addToNotes(note1)
        def note2 = PatientNote.build()
        patient.addToNotes(note2)
        def note3 = PatientNote.build()
        patient.addToNotes(note3)
        patient.save(failOnError: true)

        controller.params.id = note2.id
        controller.delete()

        def notes = PatientNote.findAll()

        assert response.redirectedUrl == "/patientNote/list/${patientId}"
        assert controller.flash.message != null
        assert notes.contains(note1)
        assert !notes.contains(note2)
        assert notes.contains(note3)
    }

    private createPatient() {
        def patient = Patient.build(cpr: '1234567890', thresholds: new ArrayList<Threshold>())
        patient.notes = []
        patient.save(failOnError: true)

        def patientId = patient.id
        [patient, patientId]
    }
}
