package org.opentele.server.exception

import org.springframework.validation.Errors

/**
 * Created with IntelliJ IDEA.
 * User: lch
 * Date: 6/27/12
 * Time: 8:31 AM
 * To change this template use File | Settings | File Templates.
 */
class PatientException extends RuntimeException {
    Errors errors


    PatientException() {
        super()
    }

    PatientException(String s) {
        super(s)
    }


    PatientException(String s, Errors errors) {
        super(s)
        this.errors = errors
    }
}
