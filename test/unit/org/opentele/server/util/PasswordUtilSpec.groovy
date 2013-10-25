package org.opentele.server.util

import spock.lang.Specification
import spock.lang.Unroll

public class PasswordUtilSpec extends Specification {
    @Unroll("validation of '#password' should return '#valid'")
    def "test password validation rules"() {
        expect:
        valid == PasswordUtil.passwordValidator(password)

        where:
        password     | valid
        null         | true
        ""           | true
        "x"          | "validator.too.short"
        "x1"         | "validator.too.short"
        "1"          | "validator.too.short"
        "12345678"   | "validator.missing.alphas"
        "123456#%"   | "validator.missing.alphas"
        "abcdefgh"   | "validator.missing.digits"
        "ABCDEFGH"   | "validator.missing.digits"
        "abcdefg!"   | "validator.missing.digits"
        "ABCDEF12ÆØÅ"| "validator.only.alphanumeric"
        "12abcdefg!" | true
        "1234abcd"   | true
        "1234ABCD"   | true
        "1234AB#!"   | true
        "abcd1234"   | true
    }
}
