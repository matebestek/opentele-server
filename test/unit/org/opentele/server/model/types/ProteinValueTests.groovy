package org.opentele.server.model.types

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin

@TestMixin(GrailsUnitTestMixin)
class ProteinValueTests {
    void testKnowsWhichOrdinalsExist() {
        assert !ProteinValue.hasOrdinal('not really a number')
        assert !ProteinValue.hasOrdinal(null)

        assert !ProteinValue.hasOrdinal(-1)
        assert ProteinValue.hasOrdinal(0)
        // ...
        assert ProteinValue.hasOrdinal(5)
        assert !ProteinValue.hasOrdinal(6)
    }

    void testCanConvertFromOrdinalToProteinValue() {
        assert ProteinValue.fromOrdinal(0) == ProteinValue.NEGATIVE
        // ...
        assert ProteinValue.fromOrdinal(5) == ProteinValue.PLUS_FOUR
    }

    void testCanConvertFromStringToProteinValue() {
        assert ProteinValue.fromString('Neg.') == ProteinValue.NEGATIVE
        // ...
        assert ProteinValue.fromString('+4') == ProteinValue.PLUS_FOUR
    }
}
