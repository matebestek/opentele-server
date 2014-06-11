package org.opentele.server

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import org.apache.commons.lang.time.DateUtils
import org.opentele.server.model.*
import org.opentele.server.model.types.NoteType
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(PatientNoteService)
@Build([PatientNote])
class PatientNoteServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    @Unroll
    def "test service finds correct number of important notes with reminder"() {
        setup:

        def list = new ArrayList<PatientNote>()
        list.add(PatientNote.build(note: note, type: type, reminderDate: reminderDate))
        list.add(PatientNote.build(note: note, type: type, reminderDate: reminderDate))

        when:
        def count = service.countImportantWithReminder(list)

        then:
        count == expected

        where:
        type                || note         || reminderDate                         || expected
        NoteType.IMPORTANT  || "important"  || null                                 || 0
        NoteType.NORMAL     || "normal"     || null                                 || 0
        NoteType.IMPORTANT  || "important"  || DateUtils.addDays(new Date(), -1)    || 2
        NoteType.IMPORTANT  || "important"  || DateUtils.addDays(new Date(), 1)     || 0
        NoteType.NORMAL     || "normal"     || DateUtils.addDays(new Date(), -1)    || 0
        NoteType.NORMAL     || "normal"     || DateUtils.addDays(new Date(), 1)     || 0
    }

    @Unroll
    def "test service finds correct number of important notes without deadline"() {
        setup:

        def list = new ArrayList<PatientNote>()
        list.add(PatientNote.build(note: note, type: type, reminderDate: reminderDate))
        list.add(PatientNote.build(note: note, type: type, reminderDate: reminderDate))

        when:
        def count = service.countImportantWithoutDeadline(list)

        then:
        count == expected

        where:
        type                || note         || reminderDate                         || expected
        NoteType.IMPORTANT  || "important"  || null                                 || 2
        NoteType.NORMAL     || "normal"     || null                                 || 0
        NoteType.IMPORTANT  || "important"  || DateUtils.addDays(new Date(), -1)    || 0
        NoteType.IMPORTANT  || "important"  || DateUtils.addDays(new Date(), 1)     || 0
        NoteType.NORMAL     || "normal"     || DateUtils.addDays(new Date(), -1)    || 0
        NoteType.NORMAL     || "normal"     || DateUtils.addDays(new Date(), 1)     || 0
    }

    @Unroll
    def "test service finds correct number of normal notes with reminder"() {
        setup:

        def list = new ArrayList<PatientNote>()
        list.add(PatientNote.build(note: note, type: type, reminderDate: reminderDate))
        list.add(PatientNote.build(note: note, type: type, reminderDate: reminderDate))

        when:
        def count = service.countNormalWithReminder(list)

        then:
        count == expected

        where:
        type                || note         || reminderDate                         || expected
        NoteType.IMPORTANT  || "important"  || null                                 || 0
        NoteType.NORMAL     || "normal"     || null                                 || 0
        NoteType.IMPORTANT  || "important"  || DateUtils.addDays(new Date(), -1)    || 0
        NoteType.IMPORTANT  || "important"  || DateUtils.addDays(new Date(), 1)     || 0
        NoteType.NORMAL     || "normal"     || DateUtils.addDays(new Date(), -1)    || 2
        NoteType.NORMAL     || "normal"     || DateUtils.addDays(new Date(), 1)     || 0
    }
}
