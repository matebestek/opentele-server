package org.opentele.server

import grails.plugin.spock.IntegrationSpec
import grails.plugins.springsecurity.SpringSecurityService
import org.opentele.server.model.User
import org.springframework.security.authentication.AuthenticationManager
import spock.lang.Unroll

class PasswordServiceIntegrationSpec extends IntegrationSpec {
    def passwordService

    def setup() {
        //passwordService.springSecurityService = Mock(SpringSecurityService)
        //passwordService.authenticationManager = Mock(AuthenticationManager)
    }

    @Unroll
    def "test change password when new password fails on different criterias"() {
        setup:
        def user = User.findByUsername('testuser') ?: new User(username: 'testuser', password: 'abcd1234', enabled: true).save(failOnError: true, flush: true)
        passwordService.springSecurityService = Mock(SpringSecurityService)
        passwordService.springSecurityService.getCurrentUser() >> user
        passwordService.authenticationManager = Mock(AuthenticationManager)

        when:
        def command = new PasswordCommand(currentPassword: user.password, password: password, passwordRepeat: passwordRepeat)
        and:
        passwordService.changePassword(command)

        then:
        0 * passwordService.authenticationManager.authenticate(_)
        command.hasErrors()
        command.errors[field].code == error

        where:
        password    | passwordRepeat | field            | error
        "abc"       | "abc"          | 'password'       | 'validator.too.short'
        "abcdefgh"  | "abcdefgh"     | 'password'       | 'validator.missing.digits'
        "12345678"  | "12345678"     | 'password'       | 'validator.missing.alphas'
        "abcd1234æ" | "abcd1234æ"    | 'password'       | 'validator.only.alphanumeric'
        "abcd1234"  | "abcd12"       | 'passwordRepeat' | 'validator.invalid'
    }

}
