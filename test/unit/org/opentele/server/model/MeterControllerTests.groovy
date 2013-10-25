package org.opentele.server.model

import org.junit.*
import org.opentele.server.model.Meter;
import org.opentele.server.model.MeterController;

import grails.test.mixin.*

@TestFor(MeterController)
@Mock(Meter)
class MeterControllerTests {
    def populateValidParams(params) {
        assert params != null
    }

    void testIndex() {
        controller.index()

        assert "/meter/list" == response.redirectedUrl
    }

    void testList() {
        def model = controller.list()

        assert model.meterInstanceList.size() == 0
        assert model.meterInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.meterInstance != null
    }

    void testSave() {
        controller.save()

        assert model.meterInstance != null
        assert view == '/meter/create'
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/meter/list'
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/meter/list'
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/meter/list'
    }

    void testDelete() {
        controller.delete()

        assert flash.message != null
        assert response.redirectedUrl == '/meter/list'
    }
}
