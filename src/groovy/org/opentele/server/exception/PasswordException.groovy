package org.opentele.server.exception

import org.springframework.validation.Errors

/**
 * Created with IntelliJ IDEA.
 * User: lch
 * Date: 6/27/12
 * Time: 8:31 AM
 * To change this template use File | Settings | File Templates.
 */
class PasswordException extends RuntimeException {
    Errors errors


    PasswordException() {
        super()
    }

    PasswordException(String s) {
        super(s)
    }


    PasswordException(String s, Errors errors) {
        super(s)
        this.errors = errors
    }
}
