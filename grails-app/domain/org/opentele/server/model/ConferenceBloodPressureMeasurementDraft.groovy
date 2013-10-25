package org.opentele.server.model

import java.util.regex.Pattern

class ConferenceBloodPressureMeasurementDraft extends ConferenceMeasurementDraft {
    Integer systolic
    Integer diastolic
    Integer pulse

    static constraints = {
        systolic(nullable: true)
        diastolic(nullable: true)
        pulse(nullable: true)
    }

    static Map<String, Pattern> customValidators = [
        'systolic': INTEGER_PATTERN,
        'diastolic': INTEGER_PATTERN,
        'pulse': INTEGER_PATTERN
    ]
    static Map<String, Closure> customConverters = [
        'systolic': NUMBER_CONVERTER,
        'diastolic': NUMBER_CONVERTER,
        'pulse': NUMBER_CONVERTER
    ]

    @Override
    ConferenceMeasurementDraftType getType() {
        ConferenceMeasurementDraftType.BLOOD_PRESSURE
    }

    @Override
    List<String> getWarningFields() {
        if ((systolic != null && diastolic == null) || (systolic == null && diastolic != null)) {
            ['systolic', 'diastolic']
        } else {
            []
        }
    }
}
