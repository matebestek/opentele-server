package org.opentele.server.util

import org.opentele.server.PasswordService
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent

class OpenTeleSecurityGoodAttemptEventListener implements ApplicationListener<AuthenticationSuccessEvent>, ApplicationContextAware {

    ApplicationContext ctx
    PasswordService passwordService

    @Override
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext
        passwordService = ctx.getBean('passwordService')
    }

    @Override
    void onApplicationEvent(AuthenticationSuccessEvent e) {
        def username = e.authentication.principal.username
        passwordService.registerGoodPasswordAttempt(username)
    }
}
