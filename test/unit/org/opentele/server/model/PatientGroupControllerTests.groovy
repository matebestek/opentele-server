package org.opentele.server.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.*
import org.opentele.server.PatientGroupService

@TestFor(PatientGroupController)
@Build([PatientGroup, StandardThresholdSet, Department, Patient2PatientGroup, Clinician2PatientGroup])
class PatientGroupControllerTests {
    def populateValidParams(params) {
        assert params != null
        params.name = "foobar"
        params.department = Department.build()
        params.standardThresholdSet = StandardThresholdSet.build()
    }

    void testIndex() {
        controller.index()

        assert response.redirectedUrl == "/patientGroup/list"
    }

    void testList() {
        def model = controller.list()

        assert model.patientGroupInstanceList.size() == 0
        assert model.patientGroupInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.patientGroupInstance != null
    }

    void testSave() {
        controller.save()

        assert model.patientGroupInstance != null
        assert view == '/patientGroup/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/patientGroup/show/1'
        assert controller.flash.message != null
        assert PatientGroup.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/patientGroup/list'

        populateValidParams(params)
        def patientGroup = new PatientGroup(params)

        assert patientGroup.save() != null

        params.id = patientGroup.id

        def model = controller.show()

        assert model.patientGroupInstance == patientGroup
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/patientGroup/list'

        populateValidParams(params)
        def patientGroup = new PatientGroup(params)

        assert patientGroup.save() != null

        params.id = patientGroup.id

        def model = controller.edit()

        assert model.patientGroupInstance == patientGroup
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/patientGroup/list'

        response.reset()

        populateValidParams(params)
        def patientGroup = new PatientGroup(params)

        assert patientGroup.save() != null

        // test invalid parameters in update
        params.id = patientGroup.id
        params.name = null

        controller.update()

        assert view == "/patientGroup/edit"
        assert model.patientGroupInstance != null

        patientGroup.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/patientGroup/show/$patientGroup.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        patientGroup.clearErrors()

        populateValidParams(params)
        params.id = patientGroup.id
        params.version = -1
        controller.update()

        assert view == "/patientGroup/edit"
        assert model.patientGroupInstance != null
        assert model.patientGroupInstance.errors.getFieldError('version')
        assert flash.message != null
    }
}
