package org.opentele.server.util

import org.springframework.security.authentication.LockedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class OpenteleSecurityBasicAuthenticationFilter extends BasicAuthenticationFilter  {

    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException {
        if(failed instanceof LockedException) {
            response.setHeader("AccountIsLocked", "true")
        }
    }
}
