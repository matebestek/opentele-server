package org.opentele.server.model

import grails.plugin.spock.IntegrationSpec
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class AbstractControllerIntegrationSpec extends IntegrationSpec {
    def caseInsensitivePasswordAuthenticationProvider

    protected authenticate = {String login, String password ->
        def auth = new UsernamePasswordAuthenticationToken(login, password)
        def authtoken = caseInsensitivePasswordAuthenticationProvider.authenticate(auth)

        SecurityContextHolder.getContext().setAuthentication(authtoken)
    }
}
