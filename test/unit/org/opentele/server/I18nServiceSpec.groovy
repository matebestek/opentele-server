package org.opentele.server

import grails.test.mixin.TestFor
import org.springframework.context.MessageSource
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@SuppressWarnings(["GroovyAccessibility", "GroovyAssignabilityCheck"])
@TestFor(I18nService)
class I18nServiceSpec extends Specification {
    def setup() {
        service.messageSource = Mock(MessageSource)
        service.localeResolver = Mock(SessionLocaleResolver)
    }

    @Unroll
    def "when translating a message messageSource is called once with the correct arguments"() {
        when:
        def message = service.msg(msgKey, defaultMessage, objs)

        then:
        1 * service.localeResolver.defaultLocale
        1 * service.messageSource.getMessage(msgKey, wantedObjs, wantedDefault, _) >> theMessage
        message == expectedMessage

        where:
        msgKey    | defaultMessage | objs  | theMessage   | wantedDefault | wantedObjs        | expectedMessage
        "abc.def" | null           | null  | "Translated" | null          | null              | "Translated"
        "abc.def" | "Default"      | null  | "Translated" | "Default"     | null              | "Translated"
        "abc.def" | "Default"      | null  | null         | "Default"     | null              | "Default"
        "abc.def" | null           | ["a"] | "Translated" | null          | ["a"] as Object[] | "Translated"
        "abc.def" | "Default"      | ["a"] | "Translated" | "Default"     | ["a"] as Object[] | "Translated"
        "abc.def" | "Default"      | ["a"] | null         | "Default"     | ["a"] as Object[] | "Default"

    }
}
