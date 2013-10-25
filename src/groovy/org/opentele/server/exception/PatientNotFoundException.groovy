package org.opentele.server.exception

import org.springframework.validation.Errors

/**
 * Created with IntelliJ IDEA.
 * User: lch
 * Date: 7/3/12
 * Time: 11:15 AM
 * To change this template use File | Settings | File Templates.
 */
class PatientNotFoundException extends RuntimeException {
    Errors errors


    PatientNotFoundException() {
        super()
    }

    PatientNotFoundException(String s) {
        super(s)
    }


    PatientNotFoundException(String s, Errors errors) {
        super(s)
        this.errors = errors
    }
}
