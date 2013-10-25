package org.opentele.server
import org.codehaus.groovy.grails.validation.Validateable
import org.opentele.server.model.User
import org.opentele.server.util.PasswordUtil

@Validateable
class PasswordCommand {

    User user
    String currentPassword
    String password
    String passwordRepeat

    static constraints = {
        currentPassword(blank: false)
        password(blank: false, validator: PasswordUtil.passwordValidator)
        passwordRepeat(validator: { value, obj -> value == obj.password })
    }


}
