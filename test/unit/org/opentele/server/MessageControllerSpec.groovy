package org.opentele.server
import grails.buildtestdata.mixin.Build
import grails.converters.JSON
import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.opentele.server.model.*
import org.springframework.security.core.Authentication
import spock.lang.Specification

@TestFor(MessageController)
@Build([Patient, User, PatientGroup, Patient2PatientGroup, Department, Message])
class MessageControllerSpec extends Specification {
    Patient patient
    MessageService messageService
    SessionService sessionService

    def setup() {
        // Ugly hack to avoid a null pointer exception, since the springSecurityService can not be injected
        User.metaClass.encodePassword = {-> }

        patient = createPatient()

        Authentication authentication = Mock(Authentication)
        authentication.authenticated >> true

        messageService = Mock(MessageService)
        controller.messageService = messageService

        sessionService = Mock(SessionService)
        controller.sessionService = sessionService

        controller.springSecurityService = Mock(SpringSecurityService)
        controller.springSecurityService.currentUser >> patient.user
        controller.springSecurityService.authentication >> authentication
    }

    def 'can give message list as JSON'() {
        setup:
        Department department = Department.build(name: "Ward")
        PatientGroup patientGroup = PatientGroup.build(department:department)

        patient.patient2PatientGroups = [Patient2PatientGroup.build(patientGroup: patientGroup, patient: patient)]

        Message.build(department: department, patient: patient, title: 'The title from patient', text: 'The text from patient', sentByPatient: true)
        Message.build(department: department, patient: patient, title: 'The title from department', text: 'The text from department', sentByPatient: false)

        when:
        response.format = 'json'
        controller.list()
        def model = JSON.parse(response.contentAsString)

        then:
        model.unread == 1
        model.messages.size() == 2

        model.messages[0].title == 'The title from patient'
        model.messages[0].text == 'The text from patient'
        model.messages[0].from == [
            type: 'Patient',
            id: 1,
            name: patient.name
        ]
        model.messages[0].to == [
            type: 'Department',
            id:1,
            name: 'Ward'
        ]
        !model.messages[0].isRead

        model.messages[1].title == 'The title from department'
        model.messages[1].text == 'The text from department'
        model.messages[1].from == [
            type: 'Department',
            id:1,
            name: 'Ward'
        ]
        model.messages[1].to == [
            type: 'Patient',
            id: 1,
            name: patient.name
        ]
        !model.messages[1].isRead
    }

    def 'inserts Re: when creating a message reply'() {
        setup:
        Department department = Department.build(name: "Ward")
        def message = Message.build(department: department, patient: patient, title: 't', text: 'T', sentByPatient: true)

        when:
        params.id = message.id
        def model = controller.reply()

        then:
        model.messageInstance.title.equals("Re: t")
    }

    def 'does not insert a duplicate Re: if one is already at start of message'() {
        setup:
        Department department = Department.build(name: "Ward")
        def message = Message.build(department: department, patient: patient, title: 'Re: stuff', text: 'T', sentByPatient: true)

        when:
        params.id = message.id
        def model = controller.reply()

        then:
        model.messageInstance.title.equals("Re: stuff")
    }

    def 'can mark messages as read'() {
        setup:
        Department department = Department.build(name: "Ward")
        PatientGroup patientGroup = PatientGroup.build(department:department)

        patient.patient2PatientGroups = [Patient2PatientGroup.build(patientGroup: patientGroup, patient: patient)]

        def unreadMessageFromPatient = Message.build(department: department, patient: patient, title: 'The title from patient', text: 'The text from patient', sentByPatient: true)
        def firstUnreadMessageFromDepartment = Message.build(department: department, patient: patient, title: 'The title from department', text: 'The text from department', sentByPatient: false)
        def secondUnreadMessageFromDepartment = Message.build(department: department, patient: patient, title: 'Another title from department', text: 'Another text from department', sentByPatient: false)

        when:
        request.content = "[${firstUnreadMessageFromDepartment.id}]".getBytes()
        controller.markAsRead()

        then:
        0 * messageService.setRead(unreadMessageFromPatient)
        1 * messageService.setRead(firstUnreadMessageFromDepartment)
        0 * messageService.setRead(secondUnreadMessageFromDepartment)
        response.contentAsString == ''
    }

    def "When a patientgroup allows messages its department should appear in recipients list"() {
        setup:
        Department departmentWithPatientGroupsThatAllowMessages = Department.build(name: "ShouldAcceptMessages")

        PatientGroup allowsMessagesPatientGroup = PatientGroup.build(disableMessaging:false, department:departmentWithPatientGroupsThatAllowMessages)
        Patient2PatientGroup patient2PatientGroup = Patient2PatientGroup.build(patientGroup: allowsMessagesPatientGroup)

        patient.patient2PatientGroups = [patient2PatientGroup]

        patient2PatientGroup.patient = patient
        patient2PatientGroup.save(validate: false)

        when:
        controller.messageRecipients()

        then:
        def jsonReply = JSON.parse(response.contentAsString)
        jsonReply.first().id == departmentWithPatientGroupsThatAllowMessages.id
        jsonReply.first().name == departmentWithPatientGroupsThatAllowMessages.name
    }


    def "When a department only contains patientGroups that disallow messages it should not appear in recipients list"() {
        setup:
        Department departmentWithPatientGroupsThatDisallowMessages = Department.build(name: "ShouldNotAcceptMessages")

        PatientGroup disallowsMessagesPatientGroup = PatientGroup.build(disableMessaging:true, department:departmentWithPatientGroupsThatDisallowMessages)
        Patient2PatientGroup patient2PatientGroup = Patient2PatientGroup.build(patientGroup: disallowsMessagesPatientGroup)

        patient.patient2PatientGroups = [patient2PatientGroup]

        patient2PatientGroup.patient = patient
        patient2PatientGroup.save(validate: false)

        when:
        controller.messageRecipients()

        then:
        def jsonReply = JSON.parse(response.contentAsString)
        jsonReply.size() == 0
    }

    def "When a department only contains at least one patientGroup that allows messages it should appear in recipients list"() {
        setup:
        Department departmentWithMixedPatientGroups = Department.build(name: "ShouldAcceptMessages")

        PatientGroup disallowsMessagesPatientGroup = PatientGroup.build(disableMessaging:true, department:departmentWithMixedPatientGroups)
        Patient2PatientGroup patient2DisallowsMessagesPatientGroup = Patient2PatientGroup.build(patientGroup: disallowsMessagesPatientGroup)

        PatientGroup allowsMessagesPatientGroup = PatientGroup.build(disableMessaging:false, department:departmentWithMixedPatientGroups)
        Patient2PatientGroup patient2AllowsMessagesPatientGroup = Patient2PatientGroup.build(patientGroup: allowsMessagesPatientGroup)

        patient.patient2PatientGroups = [patient2DisallowsMessagesPatientGroup, patient2AllowsMessagesPatientGroup]

        patient2DisallowsMessagesPatientGroup.patient = patient
        patient2DisallowsMessagesPatientGroup.save(validate: false)

        patient2AllowsMessagesPatientGroup.patient = patient
        patient2AllowsMessagesPatientGroup.save(validate: false)

        when:
        controller.messageRecipients()

        then:
        def jsonReply = JSON.parse(response.contentAsString)
        jsonReply.size() == 1
        jsonReply.first().id == departmentWithMixedPatientGroups.id
        jsonReply.first().name == departmentWithMixedPatientGroups.name
    }

    private createPatient() {
        def user = User.build(password: "password1", cleartextPassword: null)
        user.save(validate: false)
        return Patient.build(user: user)
    }
}
