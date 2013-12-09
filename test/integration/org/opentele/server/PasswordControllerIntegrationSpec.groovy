package org.opentele.server

import grails.converters.JSON
import grails.plugin.spock.IntegrationSpec
import org.opentele.server.model.User
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Unroll

class PasswordControllerIntegrationSpec extends IntegrationSpec {

    protected static final String PASSWORD = "abcd1234"
    protected static final String USERNAME = 'passwordtestuser'
    private User user
    PasswordController controller

    def setup() {
        controller = new PasswordController()
        user = User.findByUsername(USERNAME) ?: new User(username: 'passwordtestuser', enabled: true)
        user.password = PASSWORD
        user.cleartextPassword = PASSWORD
        user.save(flush: true, failOnError: true)
    }

    def "test change password on user in sunshine scenario"() {
        setup:
        authenticate(USERNAME, PASSWORD)
        populateParams(PASSWORD, "1234abcd")
        def version = user.version

        when:
        controller.update()

        then:
        controller.flash.message == "Password changed for user: passwordtestuser"
        !user.cleartextPassword
        user.version > version
        controller.response.redirectUrl == '/password/changed'
    }

    def "test change password on user with json in sunshine scenario"() {
        setup:
        authenticate(USERNAME, PASSWORD)
        populateParams(PASSWORD, "1234abcd")
        controller.response.format = 'json'
        def version = user.version

        when:
        controller.update()

        and:
        def json = JSON.parse(controller.response.text)

        then:
        json.status == 'ok'
        json.message == 'Password changed for user: passwordtestuser'
        !user.cleartextPassword
        user.version > version
    }

    def "test change password on user where old password is wrong"() {
        setup:
        authenticate(USERNAME, PASSWORD)
        populateParams("abcd12", "1234abcd")
        def version = user.version

        when:
        controller.update()

        then:
        controller.modelAndView.model.command.hasErrors()
        controller.modelAndView.model.command.errors['currentPassword'].code == 'passwordCommand.currentPassword.mismatch'
        controller.modelAndView.viewName == '/password/change'
        user.cleartextPassword
        user.version == version
    }

    @Unroll
    def "test change password on user with json with errors"() {
        setup:
        authenticate(USERNAME, PASSWORD)
        populateParams(currentPassword, password, passwordRepeat)
        controller.response.format = 'json'
        def version = user.version

        when:
        controller.update()

        and:
        def json = JSON.parse(controller.response.text)

        then:
        json.status == 'error'
        json.errors.size() == 1
        json.errors[0].field == errorField
        user.cleartextPassword
        user.version == version

        where:
        currentPassword | password   | passwordRepeat | errorField
        'abc123'        | '1234abcd' | '1234abcd'     | "currentPassword"
        PASSWORD        | '12345678' | '12345678'     | "password"
        PASSWORD        | 'abcd1234' | '12345678'     | "passwordRepeat"
    }


    def populateParams(currentPassword, password, passwordRepeat = null) {
        controller.params.currentPassword = currentPassword
        controller.params.password = password
        controller.params.passwordRepeat = passwordRepeat ?: password
    }



    def caseInsensitivePasswordAuthenticationProvider
    protected authenticate = { String login, String password ->
        def authtoken, auth
        auth = new UsernamePasswordAuthenticationToken(login, password)
        authtoken = caseInsensitivePasswordAuthenticationProvider.authenticate(auth)
        SecurityContextHolder.getContext().setAuthentication(authtoken)
    }
}
