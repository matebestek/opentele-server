package org.opentele.server.model

import java.util.regex.Pattern

class ConferenceWeightMeasurementDraft extends ConferenceMeasurementDraft {
    Double weight

    static constraints = {
        weight(nullable: true)
    }

    static Map<String, Pattern> customValidators = [
        'weight': DOUBLE_PATTERN_ONE_DECIMAL
    ]
    static Map<String, Closure> customConverters = [
        'weight': NUMBER_CONVERTER
    ]

    @Override
    ConferenceMeasurementDraftType getType() {
        ConferenceMeasurementDraftType.WEIGHT
    }

    @Override
    List<String> getWarningFields() {
        []
    }
}
