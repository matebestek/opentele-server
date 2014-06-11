package org.opentele.server.model

import grails.plugin.spock.IntegrationSpec


class UserIntegrationSpec extends IntegrationSpec {

    def "modified and created fields are set correctly"() {
        setup:
        def user = new User(username: "peter", password: "xyzs1234", cleartextPassword: "xyzs1234")
        user.save(failOnError: true, flush:true)
        def createdDateBefore = user.createdDate.clone()
        def modifiedDateBefore = user.modifiedDate.clone()

        when:
        user.username = "petrine"
        Thread.sleep(100)
        user.save(failOnError: true, flush:true)

        then:
        createdDateBefore.equals(modifiedDateBefore)
        !user.createdDate.equals(user.modifiedDate)
        user.createdDate.before(user.modifiedDate)
    }
}
