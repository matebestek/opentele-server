package org.opentele.server

import org.opentele.server.constants.Constants
import org.opentele.server.exception.MessageException
import org.opentele.server.model.Clinician
import org.opentele.server.model.Department
import org.opentele.server.model.Message
import org.opentele.server.model.Patient
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.request.RequestContextHolder

class MessageService {

    def springSecurityService
	def clinicianService

	@Transactional
	def saveMessage (Message msg) {
		Date now = new Date()
		if (!msg.sendDate) {
			msg.sendDate = now
		}

		if (!msg.validate()) {
			log.warn "Not able to validate message"
			throw new MessageException ("Could not validate message" + msg.errors)
		}

		msg.save()

		if (msg.hasErrors()) {
			log.error "Could not save message: " + msg.errors
			throw new MessageException ("Could not save message" + msg.errors)
		}

		return msg
	}

	@Transactional
	def saveMessage (def params) {
		def msg = new Message(params)
		return saveMessage(msg)
	}

	@Transactional
	def saveMessage(Department department, Patient patient, String title, String message) {
		def msg = new Message(department: department, patient: patient, title: title, text: message, sendDate: new Date(), isRead:false)
		return saveMessage(msg)
	}

	@Transactional
	boolean setRead(Message message) {
		message.isRead = true
		message.readDate = new Date()
		saveMessage(message)
	}

	@Transactional
	boolean setUnRead(Message message) {
		message.isRead = false
		message.readDate = null
		saveMessage(message)
	}

	//NOT transactional
	def getUnreadMessageCount() {
        def user = springSecurityService.currentUser
		def patient
		def clinician
		def departments
		def unreadCount = 0
		def session = RequestContextHolder.currentRequestAttributes().getSession()
		if (user && user.isPatient()) {
			patient = Patient.findByUser(user)
			def countQuery = "from Message as m where m.patient=(:patient) and m.isRead=(:isread) and sentByPatient='false'"
			unreadCount = Message.findAll(countQuery, [patient: patient, isread: false]).size()
		} else if (user && user.isClinician() && session.name) {
			Long patientId = session[Constants.SESSION_PATIENT_ID]
			if (patientId) {
				//Logged in as clinician and there is a specific user selected
				def countQuery = "from Message as m where m.patient.id=(:patientId) and m.isRead=(:isread)"
				unreadCount = Message.findAll(countQuery, [patientId: patientId, isread: false]).size()
			}
		} else if (user && user.isClinician()) {
			clinician = Clinician.findByUser(user)
			departments = clinician.departments()
			def countQuery = "from Message as m where m.isRead=(:isread) and m.department in (:list) and m.sentByPatient='true'"
			unreadCount = (departments.size() > 0) ? Message.findAll(countQuery, [isread: false, list: departments]).size() : 0
		}

		unreadCount
	}

    boolean autoMessageIsEnabledForCompletedQuestionnaire(Long completedQuestionnaireID) {
        def completeQuestionnaire = CompletedQuestionnaire.findById(completedQuestionnaireID)
        def patient = completeQuestionnaire?.patient
        def clinician = clinicianService.currentClinician
        if(patient && clinician) {
            return clinicianCanSendMessagesToPatient(clinician, patient)
        } else  {
            return false;
        }
    }

    boolean clinicianCanSendMessagesToPatient(Clinician clinician, Patient patient) {
        !legalMessageSendersForClinicianToPatient(clinician, patient).empty
    }

    def legalMessageSendersForClinicianToPatient(Clinician clinician, Patient patient) {
        def clinicianPatientGroups = clinician.clinician2PatientGroups.collect {it.patientGroup}
        def patientPatientGroups = patient.patient2PatientGroups.collect {it.patientGroup}

        def sharedPatientGroups = clinicianPatientGroups.intersect(patientPatientGroups)

        return sharedPatientGroups.findAll {!it.disableMessaging}.collect {it.department}.unique()
    }
}
