package org.opentele.server.model

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.model.types.PermissionName
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import dk.silverbullet.kih.api.auditlog.SkipAuditLog

import javax.servlet.AsyncContext

@Secured(PermissionName.NONE)
@SecurityWhiteListController
class ConferenceController {
    def springSecurityService
    def videoConferenceService
    def conferenceStateService

    static allowedMethods = [show: 'GET', initializeCall: 'POST', startVideoClient: 'GET']

    @Secured(PermissionName.VIDEO_CALL)
    def show() {
        if (videoConferenceService == null) {
            redirect(action:'noVideo')
            return
        }

        def clinician = currentClinician()
        def patient = patientInContext()

        def pendingConferences = PendingConference.findAllByClinician(clinician)
        def hasConferenceWithThisPatient = pendingConferences.any { it.patient == patient }
        def hasConferenceWithAnotherPatient = pendingConferences.any { it.patient != patient }
        def userIsPresentInOwnRoom
        try {
            userIsPresentInOwnRoom = videoConferenceService.userIsAlreadyPresentInOwnRoom(clinician.videoUser, clinician.videoPassword)
        } catch (BadCredentialsException e) {
            log.error('User not authorized')
            redirect(action:'unauthorized')
            return
        } catch (AuthenticationException e) {
            log.error('Unknown video problem', e)
            redirect(action:'unknownVideoProblem')
            return
        }

        if (hasConferenceWithAnotherPatient || (userIsPresentInOwnRoom && !hasConferenceWithThisPatient)) {
            redirect(action:'conferenceActiveWithOtherPatient')
        } else if (userIsPresentInOwnRoom && hasConferenceWithThisPatient) {
            redirect(action:'activeConference')
        } else {
            [unfinishedConferences: findUnfinishedConferences(), clinician: clinician]
        }
    }

    @Secured(PermissionName.VIDEO_CALL)
    def initializeCall() {
        def clinician = currentClinician()

        deletePendingConferences(clinician)
        completeConferences(patientInContext())

        def clientParameters = videoConferenceService.initializeCallAndCreateClientParameters(clinician.videoUser, clinician.videoPassword)

        flash.clientParameters = clientParameters
        redirect(action:'startVideoClient')
    }

    @Secured(PermissionName.VIDEO_CALL)
    def noVideo() {
    }

    @Secured(PermissionName.VIDEO_CALL)
    def unauthorized() {
    }

    @Secured(PermissionName.VIDEO_CALL)
    def unknownVideoProblem() {
    }

    @Secured(PermissionName.VIDEO_CALL)
    def activeConference() {
        [unfinishedConferences: findUnfinishedConferences(), clinician: currentClinician()]
    }

    @Secured(PermissionName.VIDEO_CALL)
    def conferenceActiveWithOtherPatient() {
    }

    @Secured(PermissionName.VIDEO_CALL)
    def startVideoClient() {
    }

    @Secured(PermissionName.VIDEO_CALL)
    def linkEndpoint(String endpointId) {
        def clinician = currentClinician()

        session.roomKey = videoConferenceService.linkEndpoint(clinician.videoUser, clinician.videoPassword, endpointId)

        render ''
    }

    @Secured(PermissionName.VIDEO_CALL)
    def joinConference() {
        def clinician = currentClinician()

        videoConferenceService.joinConference(clinician.videoUser, clinician.videoPassword)

        render ''
    }

    @Secured(PermissionName.VIDEO_CALL)
    @SkipAuditLog
    def conferenceJoined() {
        def clinician = currentClinician()

        render ([joined: videoConferenceService.userIsAlreadyPresentInOwnRoom(clinician.videoUser, clinician.videoPassword)] as JSON)
    }

    @Secured(PermissionName.VIDEO_CALL)
    def finishSettingUpConference() {
        def clinician = currentClinician()

        def videoConference = new Conference(patient: patientInContext(), clinician: clinician)
        videoConference.save(failOnError: true)
        flash.conferenceToEdit = videoConference.id

        notifyPatientOfConference(clinician, session.roomKey)

        redirect(action: 'activeConference')
    }

    @Secured(PermissionName.VIDEO_CALL)
    def endConference() {
        def clinician = currentClinician()

        // Fetch properties from clinician, otherwise we sometimes get an org.hibernate.StaleObjectStateException
        // at the videoConferenceService.endConference method call, for some reason.
        def username = clinician.videoUser.toString()
        def password = clinician.videoPassword.toString()

        deletePendingConferences(clinician)
        videoConferenceService.endConference(username, password)

        redirect(action:'conferenceEnded')
    }

    @Secured(PermissionName.VIDEO_CALL)
    def conferenceEnded() {
    }

    @Secured(PermissionName.JOIN_VIDEO_CALL)
    @SkipAuditLog
    def patientHasPendingConference() {
        def patient = currentPatient()

        request.setAttribute('org.apache.catalina.ASYNC_SUPPORTED', true)
        AsyncContext context = request.startAsync()
        context.timeout = getAsyncTimeoutMillis();
        conferenceStateService.add(new Date(), patient.id, context)
    }

    @Secured(PermissionName.JOIN_VIDEO_CALL)
    @SkipAuditLog
    def patientHasPendingMeasurement() {
        def patient = currentPatient()

        def waitingConferenceMeasurementDraft = waitingConferenceMeasurementDraft(patient, ConferenceMeasurementDraftType.LUNG_FUNCTION)
        if (waitingConferenceMeasurementDraft != null) {
            def reply = [type: waitingConferenceMeasurementDraft.type.name()]
            render reply as JSON
        } else {
            render ''
        }
    }

    @Secured(PermissionName.JOIN_VIDEO_CALL)
    @SecurityWhiteListController
    def measurementFromPatient() {
        def patient = currentPatient()
        def measurementDetails = request.JSON
        def measurementType = ConferenceMeasurementDraftType.find { it.name() == measurementDetails.type }

        if (measurementType != ConferenceMeasurementDraftType.LUNG_FUNCTION) {
            throw new IllegalArgumentException('Only support for automatic lung function measurement for now')
        }

        def waitingConferenceMeasurementDraft = waitingConferenceMeasurementDraft(patient, measurementType)
        if (waitingConferenceMeasurementDraft == null) {
            throw new IllegalArgumentException('No matching, pending measurement')
        }

        waitingConferenceMeasurementDraft.fev1 = measurementDetails.measurement.fev1
        waitingConferenceMeasurementDraft.fev6 = measurementDetails.measurement.fev6
        waitingConferenceMeasurementDraft.fev1Fev6Ratio = measurementDetails.measurement.fev1Fev6Ratio
        waitingConferenceMeasurementDraft.fef2575 = measurementDetails.measurement.fef2575
        waitingConferenceMeasurementDraft.goodTest = measurementDetails.measurement.goodTest
        waitingConferenceMeasurementDraft.softwareVersion = measurementDetails.measurement.softwareVersion
        waitingConferenceMeasurementDraft.waiting = false
        render ''
    }

    private ConferenceMeasurementDraft waitingConferenceMeasurementDraft(Patient patient, ConferenceMeasurementDraftType type) {
        def allUnfinishedConferences = Conference.findAllByPatientAndCompleted(patient, false)
        def conferenceWithWaitingMeasurementDraft = allUnfinishedConferences.find {
            it.measurementDrafts.any { it.automatic && it.waiting && it.type == type }
        }
        conferenceWithWaitingMeasurementDraft?.measurementDrafts?.find { it.automatic && it.waiting && it.type == type }
    }

    private findUnfinishedConferences() {
        def result = Conference.findAllByPatientAndCompleted(patientInContext(), false)
        flash.conferenceToEdit ? result.findAll { it.id != flash.conferenceToEdit } : result
    }

    private notifyPatientOfConference(clinician, roomKey) {
        def patient = patientInContext()
        def pendingConference = new PendingConference(clinician: clinician, patient: patient, roomKey: roomKey)
        pendingConference.save(failOnError: true)
    }

    private deletePendingConferences(Clinician clinician) {
        PendingConference.findAllByClinician(clinician).each {
            it.delete()
        }
    }

    private completeConferences(Patient patient) {
        Conference.findAllByPatientAndCompleted(patient, false).each {
            it.completed = true
            it.save()
        }
    }

    private currentClinician() {
        Clinician.findByUser(springSecurityService.currentUser)
    }

    private currentPatient() {
        Patient.findByUser(springSecurityService.currentUser)
    }

    private patientInContext() {
        Patient.get(session.patientId)
    }

    private long getAsyncTimeoutMillis() {
        grailsApplication.config.video.connection.asyncTimeoutMillis
    }
}
