package org.opentele.server.exception

import org.springframework.validation.Errors


class ClinicianException extends RuntimeException {
    Errors errors


    ClinicianException() {
        super()
    }

    ClinicianException(String s) {
        super(s)
    }


    ClinicianException(String s, Errors errors) {
        super(s)
        this.errors = errors
    }
}
