package org.opentele.server.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.*
import org.apache.commons.logging.Log
import org.opentele.server.ConferenceStateService
import org.opentele.server.ConsultationService
import org.springframework.security.authentication.BadCredentialsException
import spock.lang.Specification

@TestFor(ConsultationController)
@Build([Patient, Clinician, Consultation])
class ConsultationControllerSpec extends Specification {

    def consultationService = Mock(ConsultationService)
    Clinician clinician
    Patient patient
    Patient patientInContext

    void setup() {

        clinician = Clinician.build(videoUser: 'user', videoPassword: 'pass')
        controller.log = Mock(Log)

        patientInContext = Patient.build()
        session.patientId = patientInContext.id
    }
}
