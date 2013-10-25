package org.opentele.server.util


class PasswordUtil {

    static Closure getPasswordValidator() {

        return { password ->

            def isCleartextPassword = hasProperty('propertyName') && propertyName == "cleartextPassword"

            if(password) {
                if(password.size() < 8) {
                    return isCleartextPassword ? "validator.cleartext.too.short" : "validator.too.short"
                }
                def onlyDigitsAndAlpha = password =~ /^[\p{Graph}]{8,}$/
                if(!onlyDigitsAndAlpha) {
                    return isCleartextPassword ? "validator.cleartext.only.alphanumeric" : "validator.only.alphanumeric"
                }
                def atLeastOneDigit = password =~/\p{Digit}+/
                if(!atLeastOneDigit.size()) {
                    return isCleartextPassword ? "validator.cleartext.missing.digits" : "validator.missing.digits"
                }
                def atLeastOneAlpha = password =~/\p{Alpha}+/
                if(!atLeastOneAlpha.size()) {
                    return isCleartextPassword ? "validator.cleartext.missing.alphas" : "validator.missing.alphas"
                }
            }
            return true
        }
    }
}