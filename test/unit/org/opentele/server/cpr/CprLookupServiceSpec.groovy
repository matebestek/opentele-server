package org.opentele.server.cpr



import grails.test.mixin.*
import org.apache.commons.logging.Log
import org.junit.*
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(CprLookupService)
@spock.lang.Ignore // Open-source version has no Seal keystore
class CprLookupServiceSpec extends Specification{
    private String TEST_CPR = "2512484916"

    def setup() {
        service.log = Mock(Log) // Just to avoid excessive output during test
    }

    def "Call CPR service with valid CPR"() {
        when:
        def person = service.getPersonDetails(TEST_CPR)

        then:
        person.civilRegistrationNumber != null
        person.firstName != null
        person.lastName != null
        person.address != null
        person.postalCode != null
        person.city != null
        person.sex != null
    }

    def "Call CPR service with invalid CPR"() {
        when:
        def person = service.getPersonDetails("1111")

        then:
        person.hasErrors == true
        person.errorMessage.isEmpty() == false
    }

    def "Call CPR service with valid CPR but not used cpr"() {
        when:
        def person = service.getPersonDetails("1212123434")

        then:
        person.hasErrors == false
        person.civilRegistrationNumber == null
        person.firstName == null
        person.lastName == null
        person.address == null
        person.postalCode == null
        person.city == null
        person.sex == null
    }
}
