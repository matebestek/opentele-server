package org.opentele.util

import org.codehaus.groovy.grails.commons.GrailsClassUtils
import org.springframework.beans.BeanUtils
import spock.lang.Specification

import java.lang.reflect.Method

/**
 * This class is to temporarly fix Grails bug http://jira.grails.org/browse/GRAILS-9162 which is fixed in Grails 2.3
 * TODO: Remove this class and have those who extends it, extend Specification instead
 */
class CommandCanValidateSpecification extends Specification {

    static originalReference

    def setupSpec() {
        originalReference = GrailsClassUtils.&getStaticPropertyValue
        GrailsClassUtils.metaClass.'static'.getStaticPropertyValue = CommandCanValidateSpecification.&getStaticPropertyValue
    }

    def cleanupSpec() {
        GrailsClassUtils.metaClass.'static'.getStaticPropertyValue = originalReference
        originalReference = null
    }

    public static Object getStaticPropertyValue(Class<?> clazz, String name) {
        Method getter = BeanUtils.findDeclaredMethod(clazz, GrailsClassUtils.getGetterName(name), null);
        try {
            if (getter != null) {
                return getter.invoke(null);
            }
            return GrailsClassUtils.getStaticFieldValue(clazz, name);
        }
        catch (Exception ignored) {
            try {
                // If getting the static field fails, we have hit GRAILS-9162. Instead of failing silently,
                // we try to get the static field value before failing silently...
                return GrailsClassUtils.getStaticFieldValue(clazz, name);
            } catch (Exception ignored2) {
                return null
            }
        }
    }
}
