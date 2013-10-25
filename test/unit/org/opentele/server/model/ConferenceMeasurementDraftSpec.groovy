package org.opentele.server.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.*
import spock.lang.Specification

@TestFor(ConferenceMeasurementDraft)
@Mock([ConferenceWeightMeasurementDraft, ConferenceLungFunctionMeasurementDraft, ConferenceBloodPressureMeasurementDraft, ConferenceSaturationMeasurementDraft])
@Build(Conference)
class ConferenceMeasurementDraftSpec extends Specification {
    Conference conference

    def setup() {
        conference = Conference.build()
        conference.save(failOnError: true)
    }

    def 'contains all required fields for weight'() {
        when:
        def measurement = new ConferenceWeightMeasurementDraft(conference: conference)
        measurement.weight = 79.8
        measurement.save(failOnError: true)

        then:
        measurement.weight == 79.8
        measurement.warningFields == []
    }

    def 'contains all required fields for lung function'() {
        when:
        def measurement = new ConferenceLungFunctionMeasurementDraft(conference: conference)
        measurement.fev1 = 3.85
        measurement.save(failOnError: true)

        then:
        measurement.fev1 == 3.85
        measurement.warningFields == []
    }

    def 'contains all required fields for blood pressure and pulse'() {
        when:
        def measurement = new ConferenceBloodPressureMeasurementDraft(conference: conference)
        measurement.systolic = 120
        measurement.diastolic = 65
        measurement.pulse = 75
        measurement.save(failOnError: true)

        then:
        measurement.systolic == 120
        measurement.diastolic == 65
        measurement.pulse == 75
        measurement.warningFields == []
    }

    def 'complains when systolic, but not diastolic, is filled out in blood pressure measurement'() {
        when:
        def measurement = new ConferenceBloodPressureMeasurementDraft(conference: conference)
        measurement.systolic = 120
        measurement.save(failOnError: true)

        then:
        measurement.warningFields == ['systolic', 'diastolic']
    }

    def 'complains when diastolic, but not systolic, is filled out in blood pressure measurement'() {
        when:
        def measurement = new ConferenceBloodPressureMeasurementDraft(conference: conference)
        measurement.diastolic = 65
        measurement.save(failOnError: true)

        then:
        measurement.warningFields == ['systolic', 'diastolic']
    }

    def 'does not complain when pulse is missing from blood pressure'() {
        when:
        def measurement = new ConferenceBloodPressureMeasurementDraft(conference: conference)
        measurement.systolic = 120
        measurement.diastolic = 65
        measurement.save(failOnError: true)

        then:
        measurement.warningFields == []
    }

    def 'contains all required fields for saturation and pulse'() {
        when:
        def measurement = new ConferenceSaturationMeasurementDraft(conference: conference)
        measurement.saturation = 97
        measurement.pulse = 76
        measurement.save(failOnError: true)

        then:
        measurement.saturation == 97
        measurement.pulse == 76
        measurement.warningFields == []
    }

    def 'does not complain when pulse is missing from saturation measurement'() {
        when:
        def measurement = new ConferenceSaturationMeasurementDraft(conference: conference)
        measurement.saturation = 97
        measurement.save(failOnError: true)

        then:
        measurement.warningFields == []
    }
}
