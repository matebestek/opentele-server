package org.opentele.server.model

import org.junit.*
import grails.test.mixin.*

@TestFor(UrineThresholdController)
@Mock(UrineThreshold)
class UrineThresholdControllerTests {
    def populateValidParams(params) {
        assert params != null
    }

    void testIndex() {
        controller.index()
        assert response.redirectedUrl == "/urineThreshold/list"
    }
}
