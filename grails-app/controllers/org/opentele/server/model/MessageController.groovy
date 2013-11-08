package org.opentele.server.model

import static javax.servlet.http.HttpServletResponse.*

import grails.converters.JSON
import grails.converters.XML
import grails.plugins.springsecurity.Secured
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.model.types.PermissionName

@Secured(PermissionName.NONE)
class MessageController {
    // Inject spring security service
    def springSecurityService
    def messageService
    def sessionService
    def patientService

    static allowedMethods = [save: "POST", update: "POST", delete: ["POST": "DELETE"], markAsRead: "POST"]

    /**
     * The following methods are used by the client:
     * list()
     * - createMessagesListResult(..)
     * -- createMessageResult(..)
     * save()
     * show()
     * getClinicians()
     * - createJsonForDepartments(..)
     */

    //Action used when a patient is logged in
    @Secured([PermissionName.MESSAGE_READ, PermissionName.MESSAGE_READ_JSON])
    @SecurityWhiteListController
    def list() {
        if (springSecurityService.authentication.isAuthenticated()) {
            def user = springSecurityService.currentUser
            def patient = Patient.findByUser(user)
            def messages = Message.findAllByPatient(patient)

            render createMessagesListResult(messages) as JSON
        } else {
            withFormat {
                json {
                    response.status = 403 // Forbidden
                    def results = []
                    results << "error"
                    results << message(code: 'user.not.authenticated', default: "User not authenticated")
                    render  results as JSON
                }
                html {
                }
            }
        }
    }

    @Secured([PermissionName.MESSAGE_WRITE,PermissionName.MESSAGE_WRITE_JSON ])
    @SecurityWhiteListController
    def markAsRead() {
        def user = springSecurityService.currentUser
        def patient = Patient.findByUser(user)
        def ids = request.JSON.collect { it as long }

        if (!ids.empty) {
            def messages = Message.findAllByPatientAndIdInList(patient, ids)
            messages.each {
                messageService.setRead(it);
            }
        }

        render ""
    }

    /**
     * Retrieves a list of recipients that a patient can send messages to
     */
    @Secured([PermissionName.MESSAGE_READ_JSON])
    @SecurityWhiteListController
    def messageRecipients() {
        def user = springSecurityService.currentUser

        if (!user) {
            user = User.get(params.user)
        }

        def patient = Patient.findByUser(user)

        def patientGroups = Patient2PatientGroup.findAllByPatient(patient)*.patientGroup
        def departments = patientGroups.findAll {!it.disableMessaging}.collect {it.department}.unique()

        render departments.collect {
            createJsonForDepartments(it)
        } as JSON
    }

    private createJsonForDepartments(Department department) {
        [
            "id": department.id,
            "name": department.name
        ]
    }

    @Secured([PermissionName.MESSAGE_CREATE])
    def create() {
        def user = springSecurityService.currentUser
        def patient
        def clinician
        def msg = new Message()
        def departments = []

        if (params.receipientId) {
            patient = Patient.get(params.receipientId)
        }

        if (user) {
            clinician = Clinician.findByUser(user)
        }

        if (patient != null && clinician != null) {
            departments = messageService.legalMessageSendersForClinicianToPatient(clinician, patient)
        }

        if (patient) {
            sessionService.setPatient(session, patient)
        }
        [messageInstance: msg, patient: patient, departments: departments]
    }

    @Secured([PermissionName.MESSAGE_WRITE])
    def reply() {
        def msg = Message.get(params.id)

        def newMessage = new Message(patient:msg.patient, department: msg.department, title: replyTitle(msg.title), inReplyTo: msg)
        messageService.setRead(msg)
        sessionService.setPatient(session, msg.patient)
        [messageInstance:newMessage, oldMessage:msg]
    }

    @Secured([PermissionName.MESSAGE_WRITE,PermissionName.MESSAGE_WRITE_JSON ])
    @SecurityWhiteListController
    def save() {
        def user = springSecurityService.currentUser
        def patient
        def department
        if (user.isPatient()) {
            patient = Patient.findByUser(user)
            department = Department.get(params.department)
            params.sentByPatient = true
        }

        if (user.isClinician()) {
            patient = Patient.get(params.patient)
            department = Department.get(params.department)
        }

        params.patient = patient
        params.department = department
        params.inReplyTo = Message.get(params.inReplyTo)

        def messageInstance = messageService.saveMessage(params)

        if (!messageInstance.hasErrors()) {
            withFormat {
                html {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'message.label', default: 'Message'), messageInstance.id])
                    redirect(controller: "patient", action: "messages", id: messageInstance.patient.id)
                }
                form {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'message.label', default: 'Message'), messageInstance.id])
                    redirect(action: "show", id: messageInstance.id)
                }
                json {
                    response.status = SC_OK
                    render createMessageResult(messageInstance) as JSON
                }
                xml {
                    response.status = SC_OK
                    render messageInstance as JSON
                }
            }
        } else {
            withFormat {
                form {
                    render(view: "create", model: [messageInstance: messageInstance])
                }
                json {
                    response.status = SC_BAD_REQUEST
                    render messageInstance as JSON
                }
                xml {
                    response.status = SC_BAD_REQUEST
                    render messageInstance as JSON
                }
            }
        }
    }

    @Secured([PermissionName.MESSAGE_WRITE,PermissionName.MESSAGE_WRITE_JSON ])
    @SecurityWhiteListController
    def read() {
        def messageInstance = Message.get(params.id)
        messageService.setRead(messageInstance)
        redirect(controller: "patient", action: "messages", id: messageInstance.patient.id)
    }

    @Secured([PermissionName.MESSAGE_WRITE,PermissionName.MESSAGE_WRITE_JSON ])
    @SecurityWhiteListController
    def unread() {
        def user = springSecurityService.currentUser
        def messageInstance = Message.get(params.id)

        def res
        def errorOccured = false
        if ( user.isPatient()) {
            def p = Patient.findByUser(user)
            if (messageInstance.patient == p) {
                if (!messageInstance.sentByPatient) {
                    res = messageService.setUnRead(messageInstance)
                }
            } else {
                redirect (controller: "login", action: "denied")
            }
        }

        if (user.isClinician()) {
            if (messageInstance.sentByPatient) {
                res = messageService.setUnRead(messageInstance)
            }
        }

        if (!errorOccured) {
            withFormat {
                html {
//					flash.message = message(code: 'default.created.message', args: [message(code: 'message.label', default: 'Message'), messageInstance.id])
                    redirect(controller: "patient", action: "messages", id: messageInstance.patient.id)
                }
                form {
//					flash.message = message(code: 'default.created.message', args: [message(code: 'message.label', default: 'Message'), messageInstance.id])
//					redirect(action: "show", id: messageInstance.id)
                    redirect(controller: "patient", action: "messages", id: messageInstance.patient.id)
                }
                json {
                    response.status = SC_OK
                    render createMessageResult(messageInstance) as JSON
                }
                xml {
                    response.status = SC_OK
                    render messageInstance as JSON
                }
            }
        } else {
            withFormat {
                form {
                    render(view: "show", model: [messageInstance: messageInstance])
                }
                json {
                    response.status = SC_BAD_REQUEST
                    render createMessageResult(messageInstance) as JSON
                }
                xml {
                    response.status = SC_BAD_REQUEST
                    render messageInstance as JSON
                }
            }

        }


        if (!messageInstance) {
            withFormat renderNotFound
        } else {
            withFormat {
                html { [messageInstance: messageInstance] }
                xml { render createMessageResult(messageInstance) as XML }
                json { render createMessageResult(messageInstance) as JSON }
            }

        }

    }

    private renderNotFound = {
        html {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'message.label', default: 'Message')])}"
            list()
        }
        form {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'message.label', default: 'Message')])}"
            list()
        }

        xml {
            response.status = SC_NOT_FOUND
            render "Message not found."
        }
        json  {
            response.status = SC_NOT_FOUND
            render "Message not found."
        }
    }

    private String replyTitle(String title) {

        if (title) {
            title.startsWith("Re:") ? title  : "Re: " + title
        } else {
            "Re: "
        }
    }

    private createMessagesListResult(Collection<Message> messages) {
        def items = messages.collect { createMessageResult(it) }
        def unread = messages.count { !it.sentByPatient && !it.isRead }

        [
            unread: unread,
            messages: items
        ]
    }

    private createMessageResult(Message message) {
        def patient = [type: 'Patient', id: message.patient.id, name: message.patient.name]
        def department = [type: 'Department', id: message.department.id, name: message.department.name]

        def from = message.sentByPatient ? patient : department
        def to = message.sentByPatient ? department : patient

        [
            id: message.id,
            title: message.title,
            text: message.text,
            to: to,
            from: from,
            isRead: message.isRead,
            sendDate: message.sendDate,
            readDate: message.readDate
        ]
    }
}
