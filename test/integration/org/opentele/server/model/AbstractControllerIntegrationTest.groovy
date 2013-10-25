package org.opentele.server.model

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder


abstract class AbstractControllerIntegrationTest extends GroovyTestCase {
    def caseInsensitivePasswordAuthenticationProvider
    protected authenticate = {String login, String password ->
        def authtoken, auth
        auth = new UsernamePasswordAuthenticationToken(login, password)
        authtoken = caseInsensitivePasswordAuthenticationProvider.authenticate(auth)
        SecurityContextHolder.getContext().setAuthentication(authtoken)
    }
}
