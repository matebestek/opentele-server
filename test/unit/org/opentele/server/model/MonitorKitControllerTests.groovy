package org.opentele.server.model

import org.junit.*
import org.opentele.server.model.Department;
import org.opentele.server.model.MonitorKit;
import org.opentele.server.model.MonitorKitController;
import org.opentele.server.model.Patient;

import grails.test.mixin.*

@TestFor(MonitorKitController)
@Mock([MonitorKit, Department, Patient])
class MonitorKitControllerTests {
    def populateValidParams(params) {
        assert params != null
        params['name'] = "Unit Test Monitor Kit"
        params['department'] = [id:1]
    }

    void testIndex() {
        controller.index()
        assert "/monitorKit/list" == response.redirectedUrl
    }

    void testList() {
        def model = controller.list()

        assert model.monitorKitInstanceList.size() == 0
        assert model.monitorKitInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.monitorKitInstance != null
    }

    void testSave() {
		mockFor(Department)
		mockFor(Patient)
		mockFor(MonitorKit)
        controller.save()
		
	   
        assert model.monitorKitInstance != null
        assert view == '/monitorKit/create'
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/monitorKit/list'
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/monitorKit/list'
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/monitorKit/list'
    }

    void testDelete() {
        controller.delete()

        assert flash.message != null
        assert response.redirectedUrl == '/monitorKit/list'
    }
}
