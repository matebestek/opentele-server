package org.opentele.server

import grails.buildtestdata.mixin.Build
import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.opentele.server.constants.Constants
import org.opentele.server.exception.PatientException
import org.opentele.server.model.*
import org.opentele.server.model.types.PatientState
import org.opentele.server.model.types.PermissionName
import org.opentele.server.model.types.Sex
import org.opentele.server.service.MailSenderService
import org.springframework.validation.FieldError
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(PatientService)
@Build([Patient, User, Role, UserRole, PatientGroup, Patient2PatientGroup, MonitoringPlan, NextOfKinPerson, Clinician, Clinician2PatientGroup])
class PatientServiceSpec extends Specification {

    def setup() {
        // Ugly hack to avoid a null pointer exception, since the springSecurityService can not be injected
        User.metaClass.encodePassword = { -> }
        service.springSecurityService = Mock(SpringSecurityService)
        service.clinicianService = Mock(ClinicianService)
        service.i18nService = Mock(I18nService)
        service.mailSenderService = Mock(MailSenderService)
        service.patientOverviewService = Mock(PatientOverviewService)

        SpringSecurityUtils.metaClass.'static'.ifAnyGranted = { String roles ->
            return false
        }
    }

    def cleanup() {
        SpringSecurityUtils.metaClass = null;
    }

    def "when I create a user with a valid CreatePatientCommand it works"() {
        setup:
        Role.build(authority: Constants.DEFAULT_PATIENT_ROLE)
        def patientGroup = PatientGroup.build()
        def command = new CreatePatientCommand(
                username: "Kryf",
                cleartextPassword: "abcd1234",
                cpr: "12345678",
                firstName: "Kryf",
                lastName: "Plyf",
                sex: Sex.MALE,
                address: "Testvej 1",
                postalCode: "8000",
                city: "Aarhus",
                groupIds: [patientGroup.id.toString()],
                thresholds: [],
                nextOfKins: []
        )

        when:
        def patient = service.buildAndSavePatient(command)

        then:
        !patient.hasErrors()
        patient.state == PatientState.ACTIVE
        1 * service.patientOverviewService.createOverviewFor({ it.cpr == '12345678' })
    }

    @Unroll
    def "when I create a user with invalid CreatePatientCommand it fails"() {
        setup:
        Role.build(authority: Constants.DEFAULT_PATIENT_ROLE)
        def patientGroup = PatientGroup.build()
        def command = new CreatePatientCommand(
                username: username,
                cleartextPassword: "abcd1234",
                cpr: cpr,
                firstName: firstName,
                lastName: "Plyf",
                sex: Sex.MALE,
                address: "Testvej 1",
                postalCode: "8000",
                city: "Aarhus",
                groupIds: [patientGroup.id.toString()],
                thresholds: [],
                nextOfKins: []
        )

        when:
        service.buildAndSavePatient(command)

        then:
        def e = thrown(PatientException)
        e.message == errorMessage
        0 * service.patientOverviewService.createOverviewFor(_)

        where:
        username   | cpr        | firstName | errorMessage
        null       | "12345678" | "Kryf"    | 'patient.could.not.create.user'
        "Username" | null       | "Kryf"    | 'patient.not.created'
    }

    def "when I update a patient with valid params it works"() {
        setup:
        def patient = createPatientForUpdate()
        def params = [cleartextPassword: 'newpassword1']
        // TODO: Until http://jira.grails.org/browse/GRAILS-7506 is fixed, we have to supply our own isDirty for User
        patient.user.metaClass.isDirty = { true }

        expect:

        patient.user.password == 'password1'
        !patient.user.cleartextPassword

        when:
        service.updatePatient(params, patient)

        then:
        patient.user.password == 'newpassword1'
        patient.user.cleartextPassword == 'newpassword1'
        1 * service.patientOverviewService.updateOverviewFor(patient)
    }

    def "when I remove all patientgroups they get removed"() {
        setup:
        Patient patient = buildPatientForUpdatePatientGroup()

        def id = patient.patient2PatientGroups.find { true }.id
        println "Id: ${id}"
        def params = [groupId: []]

        SpringSecurityUtils.metaClass.'static'.ifAnyGranted = { String roles ->
            return false
        }

        when:
        service.updatePatient(params, patient)

        then:
        1 * service.patientOverviewService.updateOverviewFor(patient)
    }

    def "when I update a patient with invalid clearTextPassword it fails"() {
        setup:
        def patient = createPatientForUpdate()
        def params = [cleartextPassword: 'new1']

        when:
        service.updatePatient(params, patient)

        then:
        thrown(PatientException)
        patient.hasErrors()
        0 * service.patientOverviewService.updateOverviewFor(patient)
    }

    @Unroll
    def "when I update a patient with a dataresponsible that is not included in the patients patientgroups it fails"() {
        setup:
        def patient = buildPatientForUpdatePatientGroup()
        def pg = PatientGroup.build()
        pg.save()
        patient.dataResponsible = pg

        def params = []

        when:
        service.updatePatient(params, patient)

        then:
        thrown(grails.validation.ValidationException)
        patient.getErrors().getFieldError('patient2PatientGroups').code == 'validate.patient.dataResponsible'
        0 * service.patientOverviewService.updateOverviewFor(patient)
    }

    @Unroll
    def "when I search for patients with ssn starting with 1 only patients for the current clinician is returned"() {
        setup:
        def user = buildDataForSearch()
        1 * service.springSecurityService.currentUser >> user

        when:
        def patientList = service.searchPatient(new PatientSearchCommand(ssn: ssn))

        then:
        patientList.size() == size

        where:
        ssn  || size
        "1"  || 1 // cpr matching /.*1.*/
        "2"  || 1 // cpr matching /.*2.*/
        "77" || 2 // cpr matching /.*77.*/
        "88" || 0 // cpr matching /.*88.*/
    }

    @Unroll
    def "when I search for patients with phonenumber for next of kin, the patient is returned for the current clinician"() {
        setup:
        def user = buildDataForSearch()
        1 * service.springSecurityService.currentUser >> user


        when:
        def patientList = service.searchPatient(new PatientSearchCommand(phone: phonenumber))

        then:
        patientList.size() == 1
        patientList.first().cpr == wantedCpr

        where:
        phonenumber || wantedCpr
        "10101010"  || "2777777777"  // Matches patient 2 next of kin
        "20202020"  || "1777777777"
    }

    def "test that sendPassword calls to correct method on mailSenderService"() {
        setup:
        def user = User.build(password: "password2", cleartextPassword: "password1")
        def patient = Patient.build(firstName: "Svend", lastName: "Bent", email: 'some@email.com', user: user)
        service.clinicianService.currentClinician >> Clinician.build(firstName: "Doktor", lastName: "Hansen")
        service.i18nService.message(_) >> "subject"

        when:
        service.sendPassword(patient)

        then:
        1 * service.mailSenderService.sendMail("subject", "some@email.com", '/email/passwordRecovery', [
                patient: "Svend Bent",
                clinician: "Doktor Hansen",
                password: "password1"
        ])
    }


    private createPatientForUpdate() {
        def user = User.build(password: "password1", cleartextPassword: null)
        user.save(validate: false)
        return Patient.build(user: user)
    }

    private buildPatientForUpdatePatientGroup() {
        def user = User.build(username: "c1", password: "password1", cleartextPassword: null)
        Clinician clinician = Clinician.build(user: user)
        def patientGroup = PatientGroup.build()

        Clinician2PatientGroup.link(clinician, patientGroup)

        def patientUser1 = User.build(username: "p1", password: "password1", cleartextPassword: null)
        def patientUser2 = User.build(username: "p2", password: "password1", cleartextPassword: null)
        def patient1 = Patient.build(user:patientUser1, firstName: "Patient1", cpr: "1777777777", phone: "20202020")
        def patient2 = Patient.build(user:patientUser2, firstName: "Patient2", cpr: "2777777777")
        patient1.save()
        patient2.save()

        Patient2PatientGroup.link(patient1, patientGroup)
        Patient2PatientGroup.link(patient2, patientGroup)

        patient1.refresh()
        return patient1
    }

    private buildDataForSearch() {
        def user = User.build(password: "password1", cleartextPassword: null)
        Clinician clinician = Clinician.build(user: user)
        def patientGroup = PatientGroup.build()
        def otherPatientGroup = PatientGroup.build()
        Clinician2PatientGroup.link(clinician, patientGroup)
        def patient1 = Patient.build(firstName: "Patient1", cpr: "1777777777", phone: "20202020")
        def patient2 = Patient.build(firstName: "Patient2", cpr: "2777777777")
        patient2.addToNextOfKinPersons(NextOfKinPerson.build(phone: "10101010", patient: patient2))
        patient2.save()
        def patient3 = Patient.build(firstName: "Patient3", cpr: "1888888888")
        def patient4 = Patient.build(firstName: "Patient4", cpr: "1888888889")
        patient4.addToNextOfKinPersons(NextOfKinPerson.build(phone: "20202020", patient: patient4))
        patient4.save()

        Patient2PatientGroup.link(patient1, patientGroup)
        Patient2PatientGroup.link(patient2, patientGroup)
        Patient2PatientGroup.link(patient3, otherPatientGroup)
        Patient2PatientGroup.link(patient4, otherPatientGroup)
        return user
    }
}
