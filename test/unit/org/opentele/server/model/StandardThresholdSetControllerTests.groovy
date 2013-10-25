package org.opentele.server.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.*
import org.opentele.server.SessionService
import org.opentele.server.model.types.MeasurementTypeName

@TestFor(StandardThresholdSetController)
@Build([StandardThresholdSet, MeasurementType, BloodPressureThreshold, NumericThreshold, UrineThreshold, UrineGlucoseThreshold])
class StandardThresholdSetControllerTests {
    def populateValidParams(params) {
        assert params != null
    }

    void testIndex() {
        controller.index()
        assert response.redirectedUrl == "/standardThresholdSet/list"
    }

    void testList() {
        def model = controller.list()

        assert model.standardThresholdSetInstanceList.size() == 0
    }

    void testStandardThresholdSetsSaveWorksWithEmptyStringValuesBLOODPRESSURE() {
        defineBeans {
            sessionService(SessionService)
        }
        MeasurementType.build(name: MeasurementTypeName.BLOOD_PRESSURE)
        StandardThresholdSet sts = StandardThresholdSet.build()
        sts.save()
        sts = StandardThresholdSet.findAll().get(0)
        assert sts != null

        params.id = sts.id
        params.type = MeasurementTypeName.BLOOD_PRESSURE.toString()
        params.diastolicAlertHigh = ""
        params.diastolicAlertLow = ""
        params.diastolicWarningHigh = ""
        params.diastolicWarningLow = ""
        params.systolicAlertHigh = ""
        params.systolicAlertLow = ""
        params.systolicWarningHigh = ""
        params.systolicWarningLow = ""

        controller.saveThresholdToSet()
        assert response.redirectedUrl == "/standardThresholdSet/list#1"
    }

    void testStandardThresholdSetsSaveWorksWithEmptyStringValuesNUMERICTYPES() {
        defineBeans {
            sessionService(SessionService)
        }
        MeasurementType.build(name: MeasurementTypeName.PULSE)
        StandardThresholdSet sts = StandardThresholdSet.build()
        sts.save()
        sts = StandardThresholdSet.findAll().get(0)
        assert sts != null

        params.id = sts.id
        params.type = MeasurementTypeName.PULSE.toString()
        params.alertHigh = ""
        params.alertLow = ""
        params.warningHigh = ""
        params.warningLow = ""

        controller.saveThresholdToSet()
        assert response.redirectedUrl == "/standardThresholdSet/list#1"
    }

    void testStandardThresholdSetsSaveWorksWithEmptyStringValuesURINE() {
        defineBeans {
            sessionService(SessionService)
        }
        MeasurementType.build(name: MeasurementTypeName.URINE)
        StandardThresholdSet sts = StandardThresholdSet.build()
        sts.save()
        sts = StandardThresholdSet.findAll().get(0)
        assert sts != null

        params.id = sts.id
        params.type = MeasurementTypeName.URINE.toString()
        params.alertHigh = ""
        params.alertLow = ""
        params.warningHigh = ""
        params.warningLow = ""

        controller.saveThresholdToSet()
        assert response.redirectedUrl == "/standardThresholdSet/list#1"
    }

    void testStandardThresholdSetsSaveWorksWithEmptyStringValuesGLUCOSE() {
        defineBeans {
            sessionService(SessionService)
        }
        MeasurementType.build(name: MeasurementTypeName.URINE_GLUCOSE)
        StandardThresholdSet sts = StandardThresholdSet.build()
        sts.save()
        sts = StandardThresholdSet.findAll().get(0)
        assert sts != null

        params.id = sts.id
        params.type = MeasurementTypeName.URINE_GLUCOSE.toString()
        params.alertHigh = ""
        params.alertLow = ""
        params.warningHigh = ""
        params.warningLow = ""

        controller.saveThresholdToSet()
        assert response.redirectedUrl == "/standardThresholdSet/list#1"
    }

}
