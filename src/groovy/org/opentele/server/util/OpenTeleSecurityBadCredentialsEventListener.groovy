package org.opentele.server.util

import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent

class OpenTeleSecurityBadCredentialsEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent>, ApplicationContextAware {

    ApplicationContext ctx
    def passwordService

    @Override
    void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        def username = e.authentication.principal
        def wasLocked = passwordService.registerBadPasswordAttempt(username)
        WebUtils.retrieveGrailsWebRequest().flashScope.badloginAttempt = wasLocked
    }

    @Override
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext
        passwordService = ctx.getBean('passwordService')
    }
}
