package org.opentele.server.model

import grails.buildtestdata.mixin.Build
import grails.converters.JSON
import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.*
import org.opentele.server.PatientService
import org.opentele.server.SessionService
import org.opentele.server.model.types.MeasurementTypeName
import org.opentele.server.model.types.Unit
import spock.lang.Specification

@TestFor(ConferenceMeasurementsController)
@Build([Conference, ConferenceMeasurementDraft, ConferenceLungFunctionMeasurementDraft, ConferenceWeightMeasurementDraft,
    ConferenceBloodPressureMeasurementDraft, ConferenceSaturationMeasurementDraft, Measurement, MeasurementType])
class ConferenceMeasurementsControllerSpec extends Specification {
    Conference conference

    def setup() {
        conference = Conference.build(version: 5, measurements: [])
        def lungMeasurementType = MeasurementType.build(name: MeasurementTypeName.LUNG_FUNCTION)
        def weightMeasurementType = MeasurementType.build(name: MeasurementTypeName.WEIGHT)
        def bloodPressureMeasurementType = MeasurementType.build(name: MeasurementTypeName.BLOOD_PRESSURE)
        def saturationMeasurementType = MeasurementType.build(name: MeasurementTypeName.SATURATION)
        def pulseMeasurementType = MeasurementType.build(name: MeasurementTypeName.PULSE)
        [conference, lungMeasurementType, weightMeasurementType, bloodPressureMeasurementType, saturationMeasurementType, pulseMeasurementType]*.save(failOnError: true)

        controller.sessionService = Mock(SessionService)
        controller.patientService = Mock(PatientService)
        controller.patientService.allowedToView(_) >> true
        controller.springSecurityService = Mock(SpringSecurityService)
        controller.springSecurityService.getCurrentUser() >> conference.clinician.user
    }

    def 'blows up if other clinician is opening conference'() {
        setup:
        conference.clinician = Clinician.build()

        when:
        controller.show(conference.id)

        then:
        thrown(IllegalStateException)
    }

    def 'informs when conference is already completed'() {
        setup:
        def lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.build(conference: conference)
        def weightMeasurementDraft = ConferenceWeightMeasurementDraft.build(conference: conference)
        [lungFunctionMeasurementDraft, weightMeasurementDraft]*.save(failOnError: true)
        conference.completed = true
        conference.save(failOnError: true)

        when:
        controller.show(conference.id)

        then:
        response.redirectedUrl.endsWith('/alreadyCompleted')
    }

    def 'gives existing measurements on conference'() {
        setup:
        def lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.build(conference: conference)
        def weightMeasurementDraft = ConferenceWeightMeasurementDraft.build(conference: conference)
        [lungFunctionMeasurementDraft, weightMeasurementDraft]*.save(failOnError: true)

        when:
        def model = controller.show(conference.id)

        then:
        model.measurementDrafts.sort { it.id } == [lungFunctionMeasurementDraft, weightMeasurementDraft]
    }

    def 'can create new, manual blood pressure measurement draft'() {
        when:
        controller.loadForm(conference.id, 'BLOOD_PRESSURE', false)
        def measurement = ConferenceMeasurementDraft.findAll()[0] // I'd much rather like to get to the rendered model, but that's not possible

        then:
        measurement instanceof ConferenceBloodPressureMeasurementDraft
        measurement.conference == conference
        !measurement.automatic
        measurement.systolic == null
        measurement.diastolic == null
        measurement.pulse == null
        measurement.included
    }

    def 'can create new, manual saturation measurement draft'() {
        when:
        controller.loadForm(conference.id, 'SATURATION', false)
        def measurement = ConferenceMeasurementDraft.findAll()[0] // I'd much rather like to get to the rendered model, but that's not possible

        then:
        measurement instanceof ConferenceSaturationMeasurementDraft
        measurement.conference == conference
        !measurement.automatic
        measurement.saturation == null
        measurement.pulse == null
        measurement.included
    }

    def 'can create new, manual weight measurement draft'() {
        when:
        controller.loadForm(conference.id, 'WEIGHT', false)
        def measurement = ConferenceMeasurementDraft.findAll()[0] // I'd much rather like to get to the rendered model, but that's not possible

        then:
        measurement instanceof ConferenceWeightMeasurementDraft
        measurement.conference == conference
        !measurement.automatic
        measurement.weight == null
        measurement.included
    }

    def 'can create new, manual lung function measurement draft'() {
        when:
        controller.loadForm(conference.id, 'LUNG_FUNCTION', false)
        def measurement = ConferenceMeasurementDraft.findAll()[0] // I'd much rather like to get to the rendered model, but that's not possible

        then:
        measurement instanceof ConferenceLungFunctionMeasurementDraft
        measurement.conference == conference
        !measurement.automatic
        measurement.fev1 == null
        measurement.included
    }

    def 'can create new, automatic lung function measurement draft'() {
        when:
        controller.loadForm(conference.id, 'LUNG_FUNCTION', true)
        def measurement = ConferenceMeasurementDraft.findAll()[0]

        then:
        measurement instanceof ConferenceLungFunctionMeasurementDraft
        measurement.conference == conference
        measurement.automatic
        measurement.waiting
    }

    def 'can update manual measurement draft'() {
        setup:
        def lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.build(conference: conference)
        def oldConferenceVersion = conference.version

        when:
        params.conferenceVersion = oldConferenceVersion
        params.fev1 = '3,56'
        params.included = 'on'
        controller.updateMeasurement(lungFunctionMeasurementDraft.id)
        def model = JSON.parse(response.contentAsString)
        lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.get(lungFunctionMeasurementDraft.id)

        then:
        lungFunctionMeasurementDraft.fev1 == 3.56
        lungFunctionMeasurementDraft.included
        conference.version == oldConferenceVersion + 1
        model.conferenceVersion == conference.version
        model.errors == []
        model.warnings == []
    }

    def 'can only update included property on automatic measurement draft'() {
        setup:
        def lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.build(conference: conference, fev1: 3.0, included: false, automatic: true)
        def oldConferenceVersion = conference.version

        when:
        params.conferenceVersion = conference.version
        params.fev1 = '3,56'
        params.included = 'on'
        controller.updateMeasurement(lungFunctionMeasurementDraft.id)
        def model = JSON.parse(response.contentAsString)
        lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.get(lungFunctionMeasurementDraft.id)

        then:
        lungFunctionMeasurementDraft.fev1 == 3.0
        lungFunctionMeasurementDraft.included
        conference.version == oldConferenceVersion + 1
        model.conferenceVersion == conference.version
        model.errors == []
        model.warnings == []
    }

    def 'gives validation errors when setting fields to invalid values'() {
        setup:
        def lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.build(conference: conference)
        def oldConferenceVersion = conference.version

        when:
        params.conferenceVersion = oldConferenceVersion
        params.fev1 = 'invalid'
        controller.updateMeasurement(lungFunctionMeasurementDraft.id)
        def model = JSON.parse(response.contentAsString)

        then:
        conference.version == oldConferenceVersion
        model.errors == ['fev1']
        model.warnings == []
    }

    def 'gives validation warnings when missing certain fields'() {
        setup:
        def bloodPressureMeasurementDraft = ConferenceBloodPressureMeasurementDraft.build(conference: conference)
        def oldConferenceVersion = conference.version

        when:
        params.conferenceVersion = oldConferenceVersion
        params.systolic = '125'
        controller.updateMeasurement(bloodPressureMeasurementDraft.id)
        def model = JSON.parse(response.contentAsString)

        then:
        conference.version == oldConferenceVersion + 1
        model.errors == []
        model.warnings == ['systolic', 'diastolic']
    }

    def 'knows when automatic measurement has not been submitted by patient'() {
        setup:
        def lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.build(conference: conference, automatic: true, waiting: true)

        when:
        params.id = lungFunctionMeasurementDraft.id
        controller.loadAutomaticMeasurement(lungFunctionMeasurementDraft.id)

        then:
        response.status == 304
    }

    def 'knows when automatic measurement has been submitted by patient'() {
        setup:
        def lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.build(conference: conference, automatic: true, waiting: false, fev1: 3.4)

        when:
        params.id = lungFunctionMeasurementDraft.id
        controller.loadAutomaticMeasurement(lungFunctionMeasurementDraft.id)

        then:
        response.status == 200
        response.text.contains('3,4')
    }

    def 'complains if confirming drafts given the wrong conference version'() {
        when:
        params.id = conference.id
        params.conferenceVersion = conference.version - 1
        controller.confirm()

        then:
        thrown(IllegalStateException)
    }

    def 'creates measurement objects for confirmation, but does NOT save or attach them to patients'() {
        setup:
        ConferenceLungFunctionMeasurementDraft.build(conference: conference, fev1: 3.67, modifiedDate: new Date(), included: true)
        ConferenceWeightMeasurementDraft.build(conference: conference, weight: 76.5, modifiedDate: new Date(), included: true)
        conference.measurements = []

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        def model = controller.confirm()

        then:
        conference.measurements.empty
        model.measurementProposals.size() == 2

        model.measurementProposals[0].measurementType.name == MeasurementTypeName.LUNG_FUNCTION
        model.measurementProposals[0].value == 3.67
        !model.measurementProposals[0].patient

        model.measurementProposals[1].measurementType.name == MeasurementTypeName.WEIGHT
        model.measurementProposals[1].value == 76.5
        !model.measurementProposals[1].patient

        !conference.completed
    }

    def 'fills out all properties on automatic lung function measurements'() {
        setup:
        ConferenceLungFunctionMeasurementDraft.build(conference: conference, modifiedDate: new Date(), included: true,
                fev1: 3.67, fev6: 5.7, fev1Fev6Ratio: 0.543, fef2575: 2.34, goodTest: true, softwareVersion: 945)
        conference.measurements = []

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        Measurement lungFunctionMeasurement = controller.confirm().measurementProposals[0]

        then:
        lungFunctionMeasurement.measurementType.name == MeasurementTypeName.LUNG_FUNCTION
        lungFunctionMeasurement.value == 3.67
        lungFunctionMeasurement.fev6 == 5.7
        lungFunctionMeasurement.fev1Fev6Ratio == 0.543
        lungFunctionMeasurement.fef2575 == 2.34
        lungFunctionMeasurement.isGoodTest
        lungFunctionMeasurement.fevSoftwareVersion == 945
    }

    def 'ignores non-included measurement drafts for confirmation'() {
        setup:
        ConferenceLungFunctionMeasurementDraft.build(conference: conference, fev1: 3.67, modifiedDate: new Date(), included: false)

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        def model = controller.confirm()

        then:
        model.measurementProposals.empty
    }

    def 'complains if finishing drafts given the wrong conference version'() {
        when:
        params.id = conference.id
        params.conferenceVersion = conference.version - 1
        controller.finish()

        then:
        thrown(IllegalStateException)
    }

    def 'can complete conference by creating real measurements from drafts'() {
        setup:
        ConferenceLungFunctionMeasurementDraft.build(conference: conference, fev1: 3.67, modifiedDate: new Date(), included: true)
        ConferenceWeightMeasurementDraft.build(conference: conference, weight: 76.5, modifiedDate: new Date(), included: true)

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        controller.finish()

        then:
        def measurements = conference.measurements.sort { it.id }
        measurements.size() == 2

        measurements[0].measurementType.name == MeasurementTypeName.LUNG_FUNCTION
        measurements[0].value == 3.67
        measurements[0].unit == Unit.LITER

        measurements[1].measurementType.name == MeasurementTypeName.WEIGHT
        measurements[1].value == 76.5
        measurements[1].unit == Unit.KILO

        conference.completed
    }

    def 'ignores non-included measurement drafts for completion'() {
        setup:
        ConferenceLungFunctionMeasurementDraft.build(conference: conference, fev1: 3.67, modifiedDate: new Date(), included: false)

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        controller.finish()

        then:
        conference.measurements.empty
        conference.completed
    }

    def 'can complete conference with blood pressure drafts'() {
        setup:
        ConferenceBloodPressureMeasurementDraft.build(conference: conference, systolic: 120, diastolic: 65, pulse: 76, modifiedDate: new Date(), included: true)

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        controller.finish()

        then:
        def measurements = conference.measurements.sort { it.id }
        measurements.size() == 2

        measurements[0].measurementType.name == MeasurementTypeName.BLOOD_PRESSURE
        measurements[0].systolic == 120
        measurements[0].diastolic == 65
        measurements[0].unit == Unit.MMHG

        measurements[1].measurementType.name == MeasurementTypeName.PULSE
        measurements[1].value == 76
        measurements[1].unit == Unit.BPM

        conference.completed
    }

    def 'can complete conference with saturation drafts'() {
        setup:
        ConferenceSaturationMeasurementDraft.build(conference: conference, saturation: 97, pulse: 76, modifiedDate: new Date(), included: true)

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        controller.finish()

        then:
        def measurements = conference.measurements.sort { it.id }
        measurements.size() == 2

        measurements[0].measurementType.name == MeasurementTypeName.SATURATION
        measurements[0].value == 97
        measurements[0].unit == Unit.PERCENTAGE

        measurements[1].measurementType.name == MeasurementTypeName.PULSE
        measurements[1].value == 76
        measurements[1].unit == Unit.BPM

        conference.completed
    }

    def 'only creates a blood pressure measurement when pulse is not specified'() {
        setup:
        ConferenceBloodPressureMeasurementDraft.build(conference: conference, systolic: 120, diastolic: 65, modifiedDate: new Date(), included: true)

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        controller.finish()

        then:
        def measurements = conference.measurements.sort { it.id }
        measurements.size() == 1

        measurements[0].measurementType.name == MeasurementTypeName.BLOOD_PRESSURE
        measurements[0].systolic == 120
        measurements[0].diastolic == 65
        measurements[0].unit == Unit.MMHG
    }

    def 'only creates a pulse measurement when blood pressure is not specified'() {
        setup:
        ConferenceBloodPressureMeasurementDraft.build(conference: conference, pulse: 76, modifiedDate: new Date(), included: true)

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        controller.finish()

        then:
        def measurements = conference.measurements.sort { it.id }
        measurements.size() == 1

        measurements[0].measurementType.name == MeasurementTypeName.PULSE
        measurements[0].value == 76
        measurements[0].unit == Unit.BPM
    }

    def 'only creates a saturation measurement when pulse is not specified'() {
        setup:
        ConferenceSaturationMeasurementDraft.build(conference: conference, saturation: 97, modifiedDate: new Date(), included: true)

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        controller.finish()

        then:
        def measurements = conference.measurements.sort { it.id }
        measurements.size() == 1

        measurements[0].measurementType.name == MeasurementTypeName.SATURATION
        measurements[0].value == 97
        measurements[0].unit == Unit.PERCENTAGE
    }

    def 'only creates a pulse measurement when saturation is not specified'() {
        setup:
        ConferenceSaturationMeasurementDraft.build(conference: conference, pulse: 76, modifiedDate: new Date(), included: true)

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        controller.finish()

        then:
        def measurements = conference.measurements.sort { it.id }
        measurements.size() == 1

        measurements[0].measurementType.name == MeasurementTypeName.PULSE
        measurements[0].value == 76
        measurements[0].unit == Unit.BPM
    }
}
