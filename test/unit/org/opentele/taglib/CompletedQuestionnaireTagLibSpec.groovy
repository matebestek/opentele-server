package org.opentele.taglib

import grails.buildtestdata.mixin.Build
import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.opentele.server.ClinicianService
import org.opentele.server.MessageService
import org.opentele.server.model.*
import org.opentele.server.model.types.NoteType
import org.opentele.server.questionnaire.QuestionnaireService
import spock.lang.Specification
import spock.lang.Unroll

@Build([Patient, Message, NextOfKinPerson, Clinician, User])
@TestFor(CompletedQuestionnaireTagLib)
class CompletedQuestionnaireTagLibSpec extends Specification {
    def patient, clinician

    def setup() {
        patient = Patient.build(blueAlarmQuestionnaireIDs: [], cpr:"0000000000")
        clinician = Clinician.build()

        tagLib.clinicianService = Mock(ClinicianService)
        tagLib.clinicianService.currentClinician >> clinician

        QuestionnaireService questionnaireService = Mock(QuestionnaireService)
        questionnaireService.iconAndTooltip(_, _, _) >> ["", ""]
        tagLib.questionnaireService = questionnaireService

        SpringSecurityService springSecurityService = Mock(SpringSecurityService)
        springSecurityService.currentUser >> clinician.user

        tagLib.springSecurityService = springSecurityService
        tagLib.messageService = Mock(MessageService)
    }

    @Unroll
    def "Patient notes are shown with the correct icon"() {

        when:
        def patientNote = new PatientNote(type: type, remindToday: remindToday, reminderDate: reminderDate, seenBy: [])
        def output = tagLib.renderOverviewForPatient(patient: patient, patientNotes: [patientNote], null)

        then:
        output.contains icon

        where:
        type                | reminderDate  |remindToday    | icon
        NoteType.NORMAL     | new Date()    | true          | "note_reminder_green.png"
        NoteType.NORMAL     | new Date()    | false         | "note.png"
        NoteType.NORMAL     | null          | false         | "note.png"
        NoteType.IMPORTANT  | new Date()    | false         | "note.png"
        NoteType.IMPORTANT  | new Date()    | true          | "note_reminder_red.png"
        NoteType.IMPORTANT  | null          | false         | "note_important.png"
    }

    @Unroll
    def "Patient notes are only visible until seen"() {
        when:

        def patientNote = new PatientNote(type: type, remindToday: remindToday, reminderDate: reminderDate, seenBy: (seenByClinician ? [clinician] : []))
        def output = tagLib.renderOverviewForPatient(patient: patient, patientNotes: [patientNote], null)

        then:
        output.contains icon

        where:
        type                | reminderDate  |remindToday   | seenByClinician    | icon
        NoteType.NORMAL     | new Date()    | true         | false              | "note_reminder_green.png"
        NoteType.IMPORTANT  | new Date()    | true         | false              | "note_reminder_red.png"
        NoteType.IMPORTANT  | null          | false        | false              | "note_important.png"
        NoteType.NORMAL     | new Date()    | true         | true               | "note.png"
        NoteType.IMPORTANT  | new Date()    | true         | true               | "note.png"
        NoteType.IMPORTANT  | null          | false        | true               | "note.png"

    }

    @Unroll
    def "Patient can be messaged if enabled on the patientgroup otherwise not"() {
        given:
        tagLib.messageService.clinicianCanSendMessagesToPatient(_, _) >> canMessage

        when:
        def output = tagLib.renderOverviewForPatient(patient: patient, null)

        then:
        output.contains iconInbox
        output.contains iconOutbox

        where:
        canMessage | iconInbox          | iconOutbox
        false      | 'inbox-dimmed.png' | 'outbox-dimmed.png'
        true       | 'inbox.png'        | 'outbox.png'
    }


}
