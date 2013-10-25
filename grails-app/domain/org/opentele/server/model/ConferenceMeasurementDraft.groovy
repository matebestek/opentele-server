package org.opentele.server.model

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

import java.util.regex.Pattern

class ConferenceMeasurementDraft extends AbstractObject {
    static belongsTo = [
        conference: Conference
    ]
    static Pattern INTEGER_PATTERN = Pattern.compile('\\d+')
    static Pattern DOUBLE_PATTERN_ONE_DECIMAL = Pattern.compile('\\d+(,\\d{1})?')
    static Pattern DOUBLE_PATTERN_TWO_DECIMALS = Pattern.compile('\\d+(,\\d{1,2})?')
    static NUMBER_CONVERTER = {
        it == '' ? null : it.replace(',', '.') as double
    }
    static Map<String, Pattern> customValidators = [:]
    static Map<String, Closure> customConverters = [:]

    boolean included
    boolean automatic
    boolean waiting
    String deviceId

    ConferenceMeasurementDraftType getType() {
        throw new UnsupportedOperationException('Not implemented - override in subtype.')
    }

    List<String> getWarningFields() {
        throw new UnsupportedOperationException('Not implemented - override in subtype.')
    }

    List<String> getErrorFields() {
        getErrors().getFieldErrors().collect { it.field }
    }

    void updateFields(GrailsParameterMap params) {
        params.each {
            if (this.customValidators.containsKey(it.key)) {
                if (it.value == '' || this.customValidators[it.key].matcher(it.value).matches()) {
                    properties."${it.key}" = this.customConverters[it.key]it.value
                } else {
                    errors.rejectValue(it.key, it.value)
                }
            }
        }
        included = params.included
    }

    static transients = [ 'type', 'warningFields', 'errorFields' ]
    static constraints = {
        deviceId(nullable: true)
    }
}
