package org.opentele.server.model.questionnaire
import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.model.Clinician
import org.opentele.server.model.Patient
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.patientquestionnaire.NodeResult
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire
import org.opentele.server.model.types.PermissionName
import org.opentele.server.util.ISO8601DateParser
import org.springframework.dao.DataIntegrityViolationException

import java.text.ParseException

@Secured(PermissionName.NONE)
@SecurityWhiteListController
class QuestionnaireController {
	def questionnaireService
    def questionnaireDownloadService
    def springSecurityService
    def patientService
    def completedQuestionnaireService
    def clinicianService

    @Secured(PermissionName.QUESTIONNAIRE_READ_ALL)
	def index() {
        redirect(controller: "questionnaireHeader", action: "list", params: params)
	}
    @Secured(PermissionName.QUESTIONNAIRE_READ_ALL)
	def list() {
        redirect(controller: "questionnaireHeader", action: "list", params: params)
	}

    @Secured(PermissionName.QUESTIONNAIRE_READ_ALL)
	def listing() {
		def user = springSecurityService.currentUser
		def patient = Patient.findByUser(user)

		def results = questionnaireService.list(patient.cpr)

		render results.encodeAsJSON()
	}

    @Secured(PermissionName.QUESTIONNAIRE_DOWNLOAD)
	def download() {
		def user = springSecurityService.currentUser
		def patient = Patient.findByUser(user)

		String nameParam = params.name
		def questionnaireId = params.id

		if (!patient || !questionnaireId) {
			render [["failure"], ["A required parameter was empty. Required params: id"]] as JSON
			return
		}

		def q = PatientQuestionnaire.get(questionnaireId)

		if (!q) {
			render [["failure"], ["Patient Questionnaire not found for Patient id:${patient.id}, name:${nameParam}"]] as JSON
			return
		}

		render questionnaireDownloadService.asJson(q) as JSON
	}

    @Secured(PermissionName.QUESTIONNAIRE_UPLOAD)
    @SecurityWhiteListController
	def upload() {
		def user = springSecurityService.currentUser
		def patient = Patient.findByUser(user)

		def jsonRequest = request.JSON

		def patientQuestionnaireId = jsonRequest.QuestionnaireId as Long

		def errors = []
		def hasErrors = false

		if (!patient || !patientQuestionnaireId) {
			hasErrors = true
			errors <<  "A required parameter is missing. Received: QuestionnaireId:${patientQuestionnaireId}."
		}

		def date
		if (jsonRequest.date) {
			try {
				date = ISO8601DateParser.parse(jsonRequest.date)
			} catch (ParseException pax) {
				log.warn("ParseException: ${pax}", pax)
			}
		}
		if (!date) {
			hasErrors = true
			errors <<  "Required parameter 'date' is missing or not parseable. Received: ${jsonRequest.date}."
		}

		def results
		if (hasErrors) {
			results = []
			results << ["failure"]
			results << errors
		} else {
			results = completedQuestionnaireService.handleResults(patient.cpr, patientQuestionnaireId, date, jsonRequest.output)
		}

		render results as JSON
	}

	/**
	 * Acknowledges a questionnaire. The function checks, if the user has the sufficient rights. The result is redirected back to the overview.
     * If params.withAutoMessage is set the method sends an automatic message to the patient with a confirmation of acknowledgement of measurements.
	 */
    @Secured(PermissionName.QUESTIONNAIRE_ACKNOWLEDGE)
	def acknowledge() {
		def qId = params.id
		def note = params.note
        def withAutoMessage = (params.withAutoMessage != null && params.withAutoMessage.equals('true'))
		def questionnaire = CompletedQuestionnaire.get(qId)
		boolean errorOccured = false
		
		questionnaire = completedQuestionnaireService.acknowledge(questionnaire, note)

		if (questionnaire.hasErrors()) {
			def msg
			questionnaire.errors.each { error ->
				msg = msg + error
				errorOccured = true
			}
			flash.error = msg
		} else {
			flash.message  = g.message(code: "completedquestionnaire.acknowledged", args: [questionnaire.patientQuestionnaire?.name, g.formatDate(date: questionnaire.acknowledgedDate)])

            //Send message to patient with confirmation of measurments
            if (withAutoMessage) {
                completedQuestionnaireService.sendAcknowledgeAutoMessage(questionnaire)
            }
		}

		redirect(controller: session.lastController, action: session.lastAction, id: session.lastParams?.id, ignoreNavigation: true)
	}

    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
	def create() {
		[questionnaireInstance: new Questionnaire(params)]
	}

    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
	def save() {
		def questionnaireInstance = new Questionnaire(params)
		if (!questionnaireInstance.save(flush: true)) {
			render(view: "create", model: [questionnaireInstance: questionnaireInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'questionnaire.label', default: 'Questionnaire')])
		redirect(action: "show", id: questionnaireInstance.id)
	}

    @Secured(PermissionName.QUESTIONNAIRE_READ)
	def show() {
		def questionnaireInstance = Questionnaire.get(params.id)
		if (!questionnaireInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaire.label', default: 'Questionnaire')])
			redirect(action: "list")
			return
		}

		[questionnaireInstance: questionnaireInstance]
	}

    @Secured(PermissionName.QUESTIONNAIRE_WRITE)
	def edit() {
		def questionnaireInstance = Questionnaire.get(params.id)
		if (!questionnaireInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaire.label', default: 'Questionnaire')])
			redirect(action: "list")
			return
		}

		[questionnaireInstance: questionnaireInstance]
	}

    @Secured(PermissionName.QUESTIONNAIRE_WRITE)
	def update() {
		def questionnaireInstance = Questionnaire.get(params.id)
		if (!questionnaireInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaire.label', default: 'Questionnaire')])
			redirect(action: "list")
			return
		}

		if (params.version) {
			def version = params.version.toLong()
			if (questionnaireInstance.version > version) {
				questionnaireInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[message(code: 'questionnaire.label', default: 'Questionnaire')] as Object[],
						"Another user has updated this Questionnaire while you were editing")
				render(view: "edit", model: [questionnaireInstance: questionnaireInstance])
				return
			}
		}

		questionnaireInstance.properties = params

		if (!questionnaireInstance.save(flush: true)) {
			render(view: "edit", model: [questionnaireInstance: questionnaireInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'questionnaire.label', default: 'Questionnaire')])
		redirect(action: "show", id: questionnaireInstance.id)
	}

    @Secured(PermissionName.QUESTIONNAIRE_DELETE)
	def delete() {
		def questionnaireInstance = Questionnaire.get(params.id)
		if (!questionnaireInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaire.label', default: 'Questionnaire')])
			redirect(action: "list")
			return
		}

		try {
			questionnaireInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'questionnaire.label', default: 'Questionnaire')])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'questionnaire.label', default: 'Questionnaire')])
			redirect(action: "show", id: params.id)
		}
	}

    @Secured(PermissionName.QUESTIONNAIRE_IGNORE)
	def toggleIgnoreQuestionnaire() {
		def completedQuestionnaire = CompletedQuestionnaire.get(params.id)
		def user = springSecurityService.currentUser
		def clinician = Clinician.findByUser(user)
		def reason = params.ignoreReason
		
		if (!completedQuestionnaire._questionnaireIgnored) {
			completedQuestionnaire._questionnaireIgnored = true
			completedQuestionnaire.questionnaireIgnoredReason = reason
			completedQuestionnaire.questionnareIgnoredBy = clinician
			completedQuestionnaire.save()
			completedQuestionnaire.completedQuestions.each {nodeResult ->
				nodeResult.setNodeIgnored(true)
				nodeResult.setNodeIgnoredBy(clinician)
				nodeResult.setNodeIgnoredReason(reason)
				nodeResult.save()
			}
		} else {
			completedQuestionnaire._questionnaireIgnored = false
			completedQuestionnaire.questionnaireIgnoredReason = null
			completedQuestionnaire.questionnareIgnoredBy = null
			completedQuestionnaire.save()
			completedQuestionnaire.completedQuestions.each {nodeResult ->
				nodeResult.setNodeIgnored(false)
				nodeResult.setNodeIgnoredBy(null)
				nodeResult.setNodeIgnoredReason(null)
				nodeResult.save()
			}
		}
		
		completedQuestionnaire.save()
		redirect(controller: "patient", action: "questionnaire", params: [id: params.id, ignoreNavigation: 'true'])
	}

    @Secured(PermissionName.NODE_RESULT_IGNORE)
	def toggleIgnoreNode() {
		def node = NodeResult.find("from NodeResult as node where node.id=(:nID)", [nID:Long.parseLong(params.resultID)])

		if(node?.nodeIgnored) {
			//If we un-ignore atleast one node, the entire questionnaire cannot be ignored
			node?.completedQuestionnaire._questionnaireIgnored = false
			node?.completedQuestionnaire.questionnaireIgnoredReason = null
			node?.completedQuestionnaire.questionnareIgnoredBy = null

			node.setNodeIgnored(false)
			node.setNodeIgnoredBy(null)
			node.setNodeIgnoredReason(null)
		} else {
			def user = springSecurityService.currentUser
			def clinician = Clinician.findByUser(user)
			node?.setNodeIgnored(true)
			node?.setNodeIgnoredBy(clinician)
			node?.setNodeIgnoredReason(params.ignoreNodeReason)
		}
		node?.save()
	}
}
