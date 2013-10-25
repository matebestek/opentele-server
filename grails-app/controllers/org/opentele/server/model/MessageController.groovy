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

    static allowedMethods = [save: "POST", update: "POST", delete: ["POST": "DELETE"]]

    /**
     * The following methods are used by the client:
     * list()
     * - generateJson(..)
     * -- messageAsJson(..)
     * save()
     * show()
     * getClinicians()
     * - createJsonForDepartments(..)
     */

    //Action used when a patient is logged in
    @Secured([PermissionName.MESSAGE_READ, PermissionName.MESSAGE_READ_JSON])
    @SecurityWhiteListController
    def list() {
        if (springSecurityService?.authentication?.isAuthenticated()) {
            def user = springSecurityService?.currentUser

            def p = Patient.findByUser(user)

            def messages = Message.findAllByPatient(p)

            withFormat {
                json {
                    response.status = 200
                    ArrayList results = generateJson(messages)
                    render results as JSON
                }
                xml {
                }
                html {
                }
            }
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

    def messageAsJson(Message message) {
        def msg
        if (message.sentByPatient) {
            msg = ["id": message.id,
                    "title": message.title,
                    "text": message.text,
                    to: ["id": message.department.id, "name": message.department.name],
                    from: ["id": message.patient.id, "name": message.patient.name()],
                    "isRead": message.isRead,
                    "sendDate": message.sendDate,
                    "readDate": message.readDate]
        } else {
            msg =  ["id": message.id,
                    "title": message.title,
                    "text": message.text,
                    to: ["id": message.patient.id, "name": message.patient.name()],
                    from: ["id": message.department.id, "name": message.department.name],
                    "isRead": message.isRead,
                    "sendDate": message.sendDate,
                    "readDate": message.readDate]

        }
        msg
    }

    private ArrayList generateJson(Collection<Message> messages) {
        def results = []
        if (messages?.size() > 0) {
            results << ["result": "OK"]

            def unread = 0

            results += messages?.collect { message ->
                if (!message.isRead) {
                    unread++
                }
                messageAsJson(message)
            }
            results.add(1, ["unread" : unread])
        } else {
            results << ["result": "No messages"]
        }

        results
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
        def newMessage = new Message(patient:msg.patient, department: msg.department, title: ( msg.title ? msg.title[0..2].equals("Re:") ? msg?.title  : "Re: " + msg?.title : "Re: "), inReplyTo: msg)
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
                    render messageAsJson(messageInstance) as JSON
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
                    render messageAsJson(messageInstance) as JSON
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
                    render messageAsJson(messageInstance) as JSON
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
                xml { render messageAsJson(messageInstance) as XML }
                json { render messageAsJson(messageInstance) as JSON }
            }

        }

    }

    @Secured([PermissionName.MESSAGE_READ,PermissionName.MESSAGE_READ_JSON ])
    def show() {
        def user = springSecurityService.currentUser
        def messageInstance = Message.get(params.id)

        sessionService.setPatient(session,messageInstance.patient)

        //Set isRead automatically - except if the last action was to mark the
        //messsage as unread.
        if (!(flash.lastController == 'message' && flash.lastAction == 'unread') && !(messageInstance.isRead)) {
            if (user.isPatient() && !messageInstance.sentByPatient) {
                messageService.setRead(messageInstance)
            } else if (user.isClinician() && messageInstance.sentByPatient) {
                messageService.setRead(messageInstance)
            }
        }

        def currentMsg = messageInstance
        def chain = []
        while(currentMsg.inReplyTo != null) {
            chain <<  currentMsg.inReplyTo
            currentMsg = currentMsg.inReplyTo
        }

        if (!messageInstance) {
            withFormat renderNotFound
        } else {
            withFormat {
                html { [messageInstance: messageInstance, messageChain: chain] }
                xml { render messageAsJson(messageInstance) as XML }
                json { render messageAsJson(messageInstance) as JSON }
            }

        }
    }

    def renderNotFound = {
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


    def render409orEdit = { messageInstance ->
        form {
            render(view: "edit", model: [messageInstance: messageInstance])
        }
        xml {
            response.status = SC_CONFLICT
            render messageInstance.errors.allErrors as XML
        }
        json {
            response.status = SC_CONFLICT
            render messageInstance.errors.allErrors as JSON
        }
    }
}
