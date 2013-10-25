package org.opentele.server.model

import grails.test.mixin.*

@TestFor(NextOfKinPersonController)
@Mock(NextOfKinPerson)
class NextOfKinPersonControllerTests {
    def populateValidParams(params) {
        assert params != null
        params.firstName = 'Valdemar'
    }

    void testDelete() {
        populateValidParams(params)
        def nextOfKinPerson = new NextOfKinPerson(params)

        assert nextOfKinPerson.save(failOnError: true) != null
        assert NextOfKinPerson.count() == 1

        params.id = nextOfKinPerson.id

        controller.delete()

        assert NextOfKinPerson.count() == 0
        assert NextOfKinPerson.get(nextOfKinPerson.id) == null
        assert response.redirectedUrl == '/patient/edit'
    }
}
