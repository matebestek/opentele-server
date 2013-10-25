package org.opentele.server.model

import grails.buildtestdata.mixin.Build
import org.junit.*
import grails.test.mixin.*
import org.opentele.server.model.types.MeasurementTypeName

@TestFor(BloodPressureThresholdController)
@Build([BloodPressureThreshold, MeasurementType, StandardThresholdSet])
class BloodPressureThresholdControllerTests {
    def populateValidParams(params) {
        assert params != null
        params.type = MeasurementType.build(name: MeasurementTypeName.BLOOD_PRESSURE)
        params.diastolicAlertHigh = 10
        params.diastolicWarningHigh = 8
        params.diastolicWarningLow = 7
        params.diastolicAlertLow = 6

        params.systolicAlertHigh = 10
        params.systolicWarningHigh = 8
        params.systolicWarningLow = 7
        params.systolicAlertLow = 6
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/bloodPressureThreshold/list'


        populateValidParams(params)
        def bloodPressureThreshold = new BloodPressureThreshold(params)

        assert bloodPressureThreshold.save() != null

        params.id = bloodPressureThreshold.id

        def model = controller.show()

        assert model.standardThresholdInstance == bloodPressureThreshold
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/bloodPressureThreshold/index'


        populateValidParams(params)
        def bloodPressureThreshold = new BloodPressureThreshold(params)

        assert bloodPressureThreshold.save() != null

        params.id = bloodPressureThreshold.id

        def model = controller.edit()

        assert model.standardThresholdInstance == bloodPressureThreshold
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/bloodPressureThreshold/list'

        response.reset()


        populateValidParams(params)
        def bloodPressureThreshold = new BloodPressureThreshold(params)

        assert bloodPressureThreshold.save() != null

        // test invalid parameters in update
        params.id = bloodPressureThreshold.id

        controller.update()

        assert view == "/bloodPressureThreshold/edit"
        assert model.standardThresholdInstance != null
    }
}
