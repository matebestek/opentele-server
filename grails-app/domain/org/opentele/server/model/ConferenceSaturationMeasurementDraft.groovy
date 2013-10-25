package org.opentele.server.model

import java.util.regex.Pattern

class ConferenceSaturationMeasurementDraft extends ConferenceMeasurementDraft {
    Double saturation
    Integer pulse

    static constraints = {
        saturation(nullable: true)
        pulse(nullable: true)
    }

    static Map<String, Pattern> customValidators = [
        'saturation': INTEGER_PATTERN,
        'pulse': INTEGER_PATTERN
    ]
    static Map<String, Closure> customConverters = [
        'saturation': NUMBER_CONVERTER,
        'pulse': NUMBER_CONVERTER
    ]

    @Override
    ConferenceMeasurementDraftType getType() {
        ConferenceMeasurementDraftType.SATURATION
    }

    @Override
    List<String> getWarningFields() {
        []
    }
}
