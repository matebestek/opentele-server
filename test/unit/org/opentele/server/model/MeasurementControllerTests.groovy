package org.opentele.server.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import org.junit.Before
import org.opentele.server.GraphData
import org.opentele.server.MeasurementService
import org.opentele.server.PatientService
import org.opentele.server.SessionService
import org.opentele.server.model.patientquestionnaire.MeasurementNodeResult
import org.opentele.server.model.types.*

import javax.servlet.http.HttpSession

@TestFor(MeasurementController)
@TestMixin(DomainClassUnitTestMixin)
@Mock([User, UserRole, Role, Department, MeterType, Meter, MonitorKit, StandardThresholdSet])
@Build([Patient, Measurement, MeasurementType, MeasurementNodeResult, Conference])
class MeasurementControllerTests {
    def sessionServiceControl, measurementServiceControl, patientServiceControl
    def weightMeasurementType

    @Before
    public void setUp() {
        sessionServiceControl = mockFor(SessionService)
        controller.sessionService = sessionServiceControl.createMock()

        measurementServiceControl = mockFor(MeasurementService)
        controller.measurementService = measurementServiceControl.createMock()

        patientServiceControl = mockFor(PatientService, true)
        patientServiceControl.demand.allowedToView { true }
        controller.patientService = patientServiceControl.createMock()

        weightMeasurementType = MeasurementType.build(name: MeasurementTypeName.WEIGHT)
        weightMeasurementType.save(failOnError: true)
    }

    void testCanGivePatientGraphs() {
        def (patient, patientId) = createPatient()
        def measurements = [new GraphData(type: MeasurementTypeName.BLOOD_PRESSURE)]

        sessionServiceControl.demand.setPatient { HttpSession session, Patient p -> assert p == patient }
        measurementServiceControl.demand.dataForGraphs { Patient p, timeFilter -> measurements }

        def result = controller.patientGraphs(patientId)

        assert result.patientInstance == patient
        assert result.measurements == measurements
    }

    void testCanGivePatientGrphsForLastWeek() {
        def (patient, patientId) = createPatient()
        def graphData = [new GraphData(type: MeasurementTypeName.BLOOD_PRESSURE)]

        sessionServiceControl.demand.setPatient { HttpSession session, Patient p -> assert p == patient }
        measurementServiceControl.demand.dataForGraphs { Patient p, timeFilter ->
            assert timeFilter.type == MeasurementFilterType.WEEK
            graphData
        }

        params.filter = 'WEEK'
        controller.patientGraphs(patientId)
    }

    void testCanGiveDataForFullScreenGraph() {
        def (patient, patientId) = createPatient()
        def graphData = new GraphData(type: MeasurementTypeName.BLOOD_PRESSURE)

        sessionServiceControl.demand.setPatient { HttpSession session, Patient p -> assert p == patient }
        measurementServiceControl.demand.dataForGraph { Patient p, timeFilter, measurementType -> graphData }

        def result = controller.graph(patientId, 'BLOOD_PRESSURE')

        assert result.patient == patient
        assert result.measurement.type == 'BLOOD_PRESSURE'
    }

    void testRedirectsToQuestionnaireWhenShowingMeasurementForQuestionnaire() {
        def measurement = Measurement.build(value: 78.5, measurementNodeResult: MeasurementNodeResult.build(), measurementType: weightMeasurementType)
        measurement.save(failOnError: true)

        params.id = measurement.id
        controller.show()

        assert response.redirectUrl.endsWith("questionnaire/${measurement.measurementNodeResult.completedQuestionnaire.id}")
    }

    void testRedirectsToConferenceWhenShowingMeasurementForConference() {
        def measurement = Measurement.build(value: 78.5, conference: Conference.build(), measurementType: weightMeasurementType)
        measurement.save(failOnError: true)

        params.id = measurement.id
        controller.show()

        assert response.redirectUrl.endsWith("conference/${measurement.conference.id}")
    }

    private createPatient() {
        def patient = Patient.build(cpr: '1234567890')
        patient.save(failOnError: true)
        def patientId = patient.id
        [patient, patientId]
    }
}
