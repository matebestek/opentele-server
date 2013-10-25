package org.opentele.server
import grails.buildtestdata.mixin.Build
import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.opentele.server.model.User
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import spock.lang.Specification

import javax.servlet.http.HttpServletResponse

@TestMixin(GrailsUnitTestMixin)
@TestFor(PasswordService)
@Build([User])
class PasswordServiceSpec extends Specification {
    HttpServletResponse mockResponse

    def setup() {
        User.metaClass.encodePassword = {-> }
        mockResponse = Mock(HttpServletResponse)
        service.metaClass.getResponse = {-> mockResponse}
    }

    def "the service can generate predictable password from one number and one word in dictionary"() {
        setup:
        service.randomNumberRange = 1; // Always a zero
        service.words = ["word"]

        expect:
        service.generateTempPassword() == "word0word"
    }


    def "the service will generate a random password containing four chars followed by two numbers followed by four chars"() {
        setup:
        service.words = ["read", "some", "word", "okay"]

        when:
        def password = service.generateTempPassword()

        then:
        password ==~ /^\p{Alpha}{4}\p{Digit}{2}\p{Alpha}{4}$/
    }

    def "test registerBadPasswordAttempt returns false when password was not reset"() {
        setup:
            User user = User.build(username: 'svend', cleartextPassword: null, badLoginAttemps: 0, password: "foobar1234", modifiedDate: new Date())
        when:
        def wasLocked = service.registerBadPasswordAttempt(user.username)

        then:
        !wasLocked
        user.badLoginAttemps == 1
        !user.accountLocked
        !user.cleartextPassword
    }

    def "test user account is locked on third bad loginattempt"() {
        setup:
        User user = User.build(username: 'svend', cleartextPassword: null, badLoginAttemps: 2, password: "foobar1234", modifiedDate: new Date())
        service.words = ["foobar"]
        user.metaClass.isDirty = {s -> true}


        when:
        def wasLocked = service.registerBadPasswordAttempt(user.username)

        then:
        wasLocked
        user.accountLocked
    }

    def "test user account unlock"() {
        setup:
        User user = User.build(username: 'svend', cleartextPassword: null, badLoginAttemps: 2, password: "foobar1234", modifiedDate: new Date(), accountLocked: true)
        user.metaClass.isDirty = {s -> true}

        when:
        service.unlockAccount(user)

        then:
        !user.accountLocked
        user.badLoginAttemps == 0
    }


    def "test change password in sunshine scenario"() {
        setup:
        User user = mockForPasswordTest(User.build(username: 'username', cleartextPassword: "abcd1234", password: "abcd1234"))
        user.metaClass.isDirty = {s -> true}

        when:
        def command = new PasswordCommand(currentPassword: user.password, password: "1234abcd", passwordRepeat: "1234abcd")
        and:
        service.changePassword(command)

        then:
        1 * service.springSecurityService.currentUser >> user
        1 * service.authenticationManager.authenticate(_)
        !user.cleartextPassword
        user.password == "1234abcd"
    }

    def "test change password when password authentication fails"() {
        setup:
        User user = mockForPasswordTest(User.build(username: 'username', cleartextPassword: "abcd1234", password: "abcd1234"))
        service.authenticationManager.authenticate(_) >> { throw new BadCredentialsException('') }

        when:
        def command = new PasswordCommand(currentPassword: user.password, password: "1234abcd", passwordRepeat: "1234abcd")
        and:
        service.changePassword(command)

        then:
        command.hasErrors()
        command.errors['currentPassword'] == 'passwordCommand.currentPassword.mismatch'
    }



    private mockForPasswordTest(User user) {
        mockForConstraintsTests(PasswordCommand)
        service.springSecurityService = Mock(SpringSecurityService)
        service.authenticationManager = Mock(AuthenticationManager)
        service.grailsApplication = Mock(GrailsApplication)
        service.grailsApplication.metadata >> [:]
        service.springSecurityService.currentUser >> user
        return user
    }

}
