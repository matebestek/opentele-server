package org.opentele.server.model

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(NumericThresholdController)
@Mock(NumericThreshold)
class NumericThresholdControllerTests {
    def populateValidParams(params) {
      assert params != null
    }

    void testIndex() {
        controller.index()
        assert response.redirectedUrl == "/numericThreshold/list"
    }
}