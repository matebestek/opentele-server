package org.opentele.server.cpr

import org.opentele.server.model.types.Sex

class CPRPerson {
    def civilRegistrationNumber
    def firstName
    def middleName
    def lastName
    Sex sex
    def address
    def postalCode
    def city

    boolean hasErrors
    String errorMessage
}
