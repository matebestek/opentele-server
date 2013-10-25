package org.opentele.server
import grails.buildtestdata.mixin.Build
import grails.converters.JSON
import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.opentele.server.model.*
import spock.lang.Specification

@TestFor(MessageController)
@Build([Patient, User, PatientGroup, Patient2PatientGroup, Department])
class MessageControllerSpec extends Specification {

    def setup() {
        // Ugly hack to avoid a null pointer exception, since the springSecurityService can not be injected
        User.metaClass.encodePassword = {-> }
    }

    def "When a patientgroup allows messages its department should appear in recipients list"() {
        setup:
        Department departmentWithPatientGroupsThatAllowMessages = Department.build(name: "ShouldAcceptMessages")

        PatientGroup allowsMessagesPatientGroup = PatientGroup.build(disableMessaging:false, department:departmentWithPatientGroupsThatAllowMessages);
        Patient2PatientGroup patient2PatientGroup = Patient2PatientGroup.build(patientGroup: allowsMessagesPatientGroup)

        Patient patient = createPatient()
        patient.patient2PatientGroups = [patient2PatientGroup]

        patient2PatientGroup.patient = patient
        patient2PatientGroup.save(validate: false)

        controller.springSecurityService = Mock(SpringSecurityService)
        controller.springSecurityService.currentUser >> patient.user


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

        Patient patient = createPatient()
        patient.patient2PatientGroups = [patient2PatientGroup]

        patient2PatientGroup.patient = patient
        patient2PatientGroup.save(validate: false)

        controller.springSecurityService = Mock(SpringSecurityService)
        controller.springSecurityService.currentUser >> patient.user



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

        PatientGroup allowsMessagesPatientGroup = PatientGroup.build(disableMessaging:false, department:departmentWithMixedPatientGroups);
        Patient2PatientGroup patient2AllowsMessagesPatientGroup = Patient2PatientGroup.build(patientGroup: allowsMessagesPatientGroup)

        Patient patient = createPatient()
        patient.patient2PatientGroups = [patient2DisallowsMessagesPatientGroup, patient2AllowsMessagesPatientGroup]

        patient2DisallowsMessagesPatientGroup.patient = patient
        patient2DisallowsMessagesPatientGroup.save(validate: false)

        patient2AllowsMessagesPatientGroup.patient = patient
        patient2AllowsMessagesPatientGroup.save(validate: false)

        controller.springSecurityService = Mock(SpringSecurityService)
        controller.springSecurityService.currentUser >> patient.user



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
