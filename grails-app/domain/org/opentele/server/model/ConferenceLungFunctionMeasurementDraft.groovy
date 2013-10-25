package org.opentele.server.model

import java.util.regex.Pattern

class ConferenceLungFunctionMeasurementDraft extends ConferenceMeasurementDraft {
    Double fev1

    // Only filled out for automatic measurements
    Double fev6
    Double fev1Fev6Ratio
    Double fef2575
    Boolean goodTest
    Integer softwareVersion

    static constraints = {
        fev1(nullable: true)
        fev6(nullable: true)
        fev1Fev6Ratio(nullable: true)
        fef2575(nullable: true)
        goodTest(nullable: true)
        softwareVersion(nullable: true)
    }

    static mapping = {
        fev1Fev6Ratio column:'fev1_fev6_ratio'
    }

    static Map<String, Pattern> customValidators = [
        'fev1': DOUBLE_PATTERN_TWO_DECIMALS
    ]
    static Map<String, Closure> customConverters = [
        'fev1': NUMBER_CONVERTER
    ]

    @Override
    ConferenceMeasurementDraftType getType() {
        ConferenceMeasurementDraftType.LUNG_FUNCTION
    }

    @Override
    List<String> getWarningFields() {
        []
    }
}
