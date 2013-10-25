package org.opentele.server.model

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@SuppressWarnings("UnnecessaryQualifiedReference")
@Mock(User)
class UserSpec extends Specification {

    def "when all parameters are good, validate will not fail"() {
        expect:
        createUser(username: "kurt", password: "abcd1234", cleartextPassword: "abcd1234").validate()
    }

    @Unroll("when parameter #errorOnField does not pass validation and error of '#validationError' is returned")
    def "when some parameters are invalid, validate will fail"() {
        setup:
        // Used to check unique constraint
        createUser(username: "kryf", password: "plyf1234", cleartextPassword: "plyf1234").save(failOnError: true)

        when:
        def user = createUser(username: username, password: password, cleartextPassword: password)
        user.validate()

        then:
        user.errors[errorOnField].code == validationError

        where:
        username  | password     | errorOnField | validationError
        null      | "password"   | "username"   | "nullable"
        ""        | "password"   | "username"   | "blank"
        "kryf"    | "password"   | "username"   | "unique"
        "x" * 130 | "password"   | "username"   | "maxSize.exceeded"
        "svend"   | null         | "password"   | "nullable"
        "svend"   | ""           | "password"   | "blank"
        "svend"   | "123"        | "password"   | "validator.too.short"
        "svend"   | "abcdefgh"   | "password"   | "validator.missing.digits"
        "svend"   | "12345678"   | "password"   | "validator.missing.alphas"
        "svend"   | "abcd1234æø" | "password"   | "validator.only.alphanumeric"
    }

    User createUser(Map params) {
        def user = new User(params)
        user.springSecurityService = Mock(SpringSecurityService)
        user.springSecurityService.encodePassword(_) >> { v -> v.first() }
        return user
    }

}
