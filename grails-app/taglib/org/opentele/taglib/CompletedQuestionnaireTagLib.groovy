package org.opentele.taglib

import grails.plugins.springsecurity.SpringSecurityService
import groovy.xml.MarkupBuilder
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.opentele.server.ClinicianService
import org.opentele.server.MeasurementService
import org.opentele.server.MessageService
import org.opentele.server.PatientService
import org.opentele.server.TimeFilter
import org.opentele.server.model.*
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.types.MeasurementTypeName
import org.opentele.server.model.types.NoteType
import org.opentele.server.model.types.PermissionName
import org.opentele.server.model.types.Severity
import org.opentele.server.questionnaire.*
import org.opentele.server.util.NumberFormatUtil

class CompletedQuestionnaireTagLib {
	static namespace = "cq"

	QuestionnaireService questionnaireService
    PatientService patientService
    MeasurementService measurementService
    SpringSecurityService springSecurityService
    MessageService messageService
    ClinicianService clinicianService

	def renderResultTableForPatient = { attrs, body ->
        def withPrefs = attrs.withPrefs
		def patientID = attrs.patientID
        def resultModel = attrs.completedQuestionnaireResultModel
        def builder = new MarkupBuilder(out)

        if (resultModel.results.size() == 0) {
            // No measurements fetched
            builder.div(style: "margin: 10px 0 20px;", g.message(code: "patient.questionnaire.noMeasurementsForPeriod"))

        } else {
            // Build html
            builder.div(id: "resultsContainer", class: "scrollable") {
                table(cellspacing: 0) {
                    tbody {
                        tr {
                            // Upper-left
                            buildOuterCellHeader(builder, "staticHeader", {
                                builder.th(g.message(code: "default.questions", default: "Spørgsmål"))
                            })
                            // Upper-right
                            buildOuterCellHeader(builder, "headerContainer", {
                                resultModel.columnHeaders.each { questionnaireMetaData ->
                                    builder.th {
                                        renderHeaderForThisType(builder, questionnaireMetaData)
                                    }
                                }
                            })
                        }
                        tr {
                            // Lower-left
                            def args =  [class: "questionTable" + patientID, 'data-bind': "template:{name:\"prefRowTemplate\", foreach: prefRows}"]
                            buildOuterCellBody(builder, "leftHeaderContainer", withPrefs, args, {
                                resultModel.questions.each { question ->
                                    renderQuestionForType(builder, question, patientID)
                                }
                            })
                            // Lower-right
                            args =  [class: "resultsTable" + patientID, 'data-bind': "template:{name:\"prefRowResTemplate\", foreach: prefResRows}"]
                            buildOuterCellBody(builder, "resultTableContainer", withPrefs, args, {
                                for (int i = 0; i < resultModel.questions.size(); i++) {
                                    tr(name: resultModel.questions[i].templateQuestionnaireNodeId, class: "result") {
                                        for(int j = 0; j < resultModel.columnHeaders.size(); j++) {
                                            def key = getKeyForThisCell(resultModel.questions[i], resultModel.columnHeaders[j])

                                            if(resultModel.results[key]) {
                                                renderMeasurementCell(builder, resultModel.results[key], key.type)
                                            } else {
                                                builder.td {
                                                    builder.div()
                                                }
                                            }
                                        }
                                    }
                                }
                            })
                        }
                    }
                }
            }
        }
	}

    private void buildOuterCellHeader(MarkupBuilder builder, containerID, innerContent) {
        builder.td(class: "outerCell") {
            builder.div(class:"tableContainer", id: containerID) {
                builder.table {
                    builder.thead {
                        builder.tr {
                            innerContent()
                        }
                    }
                }
            }
        }
    }

    private void buildOuterCellBody(MarkupBuilder builder, containerID, withPrefs, args, innerContent) {
        builder.td(class: "outerCell") {
            builder.div(class:"tableContainer", id: containerID) {
                builder.table(cellspacing: 0) {
                    builder.tbody(withPrefs ? [class: args['class'], "data-bind": args['data-bind']] : [class: args['class']]){
                        innerContent()
                    }
                }
            }
        }
    }

    private void renderQuestionForType(MarkupBuilder builder, MeasurementDescription measurementDescription, patientID) {
        switch (measurementDescription.type) {
            case MeasurementParentType.QUESTIONNAIRE:
                renderQuestionForQuestionnaire(builder, measurementDescription, patientID)
                break;
            case MeasurementParentType.CONFERENCE:
                renderQuestionForConference(builder, measurementDescription, patientID)
                break;
            default:
                throw new IllegalArgumentException("Unknown measurementParentType: ${measurementDescription.type}")
        }
    }

    private void renderQuestionForConference(MarkupBuilder builder, MeasurementDescription measurementDescription, patientID) {
        def thresholdTooltipMessage = getThresholdValuesForQuestion(patientID as Long, measurementDescription.measurementTypeNames)
        def tooltip = message(code: "result.table.question.tooltip.conference", args: [measurementDescription.questionnaireName])
        tooltip = thresholdTooltipMessage ? tooltip + "<br/>" + thresholdTooltipMessage : tooltip
        def units = measurementDescription.units ? measurementDescription.orderedUnits() : null
        def cellText = message(code: "enum.measurementType." + measurementDescription.measurementTypeNames?.find{true})

        renderQuestionCell(builder, tooltip, cellText, null, units)
    }

    private void renderQuestionForQuestionnaire(MarkupBuilder builder, MeasurementDescription measurementDescription, patientID) {
        def thresholdTooltipMessage = getThresholdValuesForQuestion(patientID as Long, measurementDescription.measurementTypeNames)
        def tooltip = message(code: "result.table.question.tooltip.questionnaire", args: [measurementDescription.questionnaireName])
        tooltip = thresholdTooltipMessage ? tooltip + "<br/>" + thresholdTooltipMessage : tooltip
        def units = measurementDescription.units ? measurementDescription.orderedUnits() : null
        def cellText = measurementDescription.text
        def args = [questionnaireName: measurementDescription.questionnaireName, templateQuestionnaireNodeId: measurementDescription.templateQuestionnaireNodeId]

        renderQuestionCell(builder, tooltip, cellText, args, units)
    }

    private void renderQuestionCell(MarkupBuilder builder, tooltip, cellText, args, units) {
        builder.tr {
            builder.td('data-tooltip': tooltip) {
                builder.div(args ? [class: "question", questionnaireName: args['questionnaireName'], name: "question", id: args['templateQuestionnaireNodeId']] : [class: "question"]) {
                    builder.b(cellText)
                    if (units) {
                       builder.getMkp().yieldUnescaped(buildUnitString(units))
                    }
                }
            }
        }
    }

    private String buildUnitString(units) {
        def unitString = " ("
        units.eachWithIndex { unit, idx ->
            if (idx > 0) { unitString += "/" }
            unitString += message(code: "enum.unit." + unit)
        }
        unitString += ")"

        return unitString
    }

    ResultKey getKeyForThisCell(MeasurementDescription question, OverviewColumnHeader columnHeader) {
        switch (columnHeader.type) {
            case MeasurementParentType.QUESTIONNAIRE:
                return new ResultKey(rowId: "cq-${question.templateQuestionnaireNodeId}", colId: "cq-${columnHeader.id}", type: MeasurementParentType.QUESTIONNAIRE)
            case MeasurementParentType.CONFERENCE:
                if(question.type == MeasurementParentType.CONFERENCE) {
                    // TODO: Den holder nu, men er på ingen måde pæn!
                    return new ResultKey(rowId: "conf-${question.measurementTypeNames.find{true}}", colId: "conf-${columnHeader.id}", type: MeasurementParentType.CONFERENCE)
                } else {
                    return new ResultKey(rowId: "", colId: "")
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown measurementParentType: ${columnHeader.type}")
        }
    }

    private void renderMeasurementCell(MarkupBuilder builder, List<MeasurementResult> measurementResultList, MeasurementParentType parentType) {
        switch(parentType) {
            case MeasurementParentType.QUESTIONNAIRE:
                renderMeasurementCellForQuestionnaire(builder, measurementResultList)
                break;
            case MeasurementParentType.CONFERENCE:
                renderMeasurementCellForConference(builder, measurementResultList)
                break;
            default:
                throw new IllegalArgumentException("Unknown measurementParentType: ${parentType}")
        }
    }

    private void renderMeasurementCellForQuestionnaire(MarkupBuilder builder, List<MeasurementResult> measurementResultList) {
        def measurementResult = measurementResultList.get(0)
        if (measurementResultList.size() > 1) {
            measurementResult.value = measurementResultList*.value.join(",")
        }
        def prettyResultString = getPrettyResultString(measurementResult)

        renderCellContents(builder, prettyResultString as String, {
            def measurementContent = {
                renderSeverityIcon(builder, measurementResult)
                builder.getMkp().yieldUnescaped(prettyResultString)
            }

            //If any of the measurements are ignored they are all ignored
            if (measurementResult.ignored) {
                builder.del {
                    measurementContent()
                }
            } else {
                measurementContent()
            }
        })
    }

    private void renderMeasurementCellForConference(MarkupBuilder builder, List<MeasurementResult> measurementResultList) {
        String results = measurementResultList.collect {getPrettyResultString(it)}.join(" <br/> ")
        renderCellContents(builder, results, {
            builder.getMkp().yieldUnescaped(results)
        })
    }

    private void renderCellContents(MarkupBuilder builder, tooltipText, cellContent) {
        def toolTip = tooltipForMeasurementResult(tooltipText)
        builder.td {
            builder.div(toolTip ? ['data-tooltip': toolTip] : [:]) {
                cellContent()
            }
        }
    }

    private String tooltipForMeasurementResult(String resultString) {
      if(resultString.length() > 15) {
        return resultString
      }
    }

    private void renderHeaderForThisType(MarkupBuilder builder, OverviewColumnHeader columnHeader) {
        MeasurementParentType columnType = columnHeader.type
        switch (columnType) {
            case MeasurementParentType.CONFERENCE:
                renderHeaderForConference(builder, columnHeader)
                break;
            case MeasurementParentType.QUESTIONNAIRE:
                renderHeaderForQuestionnaire(builder, columnHeader)
                break;
            default:
                throw new IllegalArgumentException("Unknown measurementParentType: ${columnType}")
        }
    }

    private void renderHeaderForConference(MarkupBuilder builder, OverviewColumnHeader columnHeader) {
        builder.div(columnHeader.uploadDate.toCalendar().format(g.message(code:"default.date.format.short"))) {
            builder.br()
            def conference_image = """<img src=${g.resource(dir: 'images', file: 'conferenceshow.png')} data-tooltip="Målinger foretaget under video-konference"/>"""
            builder.getMkp().yieldUnescaped(g.link(controller:"patient", action:"conference", id:columnHeader.id, conference_image))
        }
    }

    private void renderHeaderForQuestionnaire(MarkupBuilder builder, columnHeader) {
        builder.div(columnHeader.uploadDate.toCalendar().format(g.message(code:"default.date.format.short"))) {
            builder.br()
            builder.img(src: g.resource(dir: "images", file: columnHeader.severity.icon()))
            def img_edit = """<img src=${g.resource(dir: "images", file: "edit.png")} data-tooltip="Se spørgeskema / ignorer besvarelser / tilføj kommentarer"/>"""
            builder.getMkp().yieldUnescaped(g.link(controller:"patient", action:"questionnaire", id:columnHeader.id, img_edit))

            if (columnHeader.acknowledgedBy) {
                def tooltip = g.message(code:"completedQuestionnaire.acknowledged.label", args: [columnHeader.acknowledgedBy, columnHeader.acknowledgedDate.format(message(code: "default.date.format")).toString()]) + (columnHeader.acknowledgedNote? "\\nNote: " + columnHeader.acknowledgedNote:"")
                builder.img(src: g.resource(dir: "images", file: "acknowledged.png"), 'data-tooltip': tooltip)

            } else {
                builder.a(href: "#", class: "acknowledge", 'data-automessage': "false", 'data-questionnaire-id': columnHeader.id) {
                    builder.img(src: g.resource(dir: "images", file: "unacknowledged.png"), 'data-tooltip': "Kvittér")
                }

                def autoMessageEnabledForPatient = messageService.autoMessageIsEnabledForCompletedQuestionnaire(columnHeader.id)
                if (autoMessageEnabledForPatient) {
                    builder.a(href: "#", class: "acknowledge", 'data-automessage': "true", 'data-questionnaire-id': columnHeader.id) {
                        renderAutoMessageSingleIcon(builder, autoMessageEnabledForPatient)
                    }
                } else {
                    //Just the image
                    renderAutoMessageSingleIcon(builder, autoMessageEnabledForPatient)
                }
            }
        }
    }

    private String getAutoMessageAllIcon(boolean autoMessageEnabledForPatient) {
        if (autoMessageEnabledForPatient) {
            "<img src=${g.resource(dir: 'images', file: 'unacknowledgedWithAutoMessage.png')} data-tooltip='${message(code: 'tooltip.acknowledge.all.with.message')}'>"
        } else {
            "<img src=${g.resource(dir: 'images', file: 'unacknowledgedWithAutoMessageDisabled.png')} data-tooltip='${message(code: 'tooltip.acknowledge.with.message.disabled')}'>"
        }
    }

    private void renderAutoMessageSingleIcon(MarkupBuilder builder, boolean autoMessageEnabledForPatient) {
        if (autoMessageEnabledForPatient) {
            builder.img(src: g.resource(dir: 'images', file: 'unacknowledgedWithAutoMessage.png'), 'data-tooltip': message(code: 'tooltip.acknowledge.with.message'))
        } else {
            builder.img(src: g.resource(dir: 'images', file: 'unacknowledgedWithAutoMessageDisabled.png'), 'data-tooltip': message(code: 'tooltip.acknowledge.with.message.disabled'))
        }
    }

    private String getThresholdValuesForQuestion(Long patientId, Set<MeasurementTypeName> types) {
        Patient p = Patient.get(patientId)
        def tooltipText = ""

        types.each { type ->
            switch (type) {
                case MeasurementTypeName.BLOOD_PRESSURE:
                    BloodPressureThreshold bpThresh = p.getThreshold(type)
                    if (bpThresh) {
                        def argsSystolic = [getSafeThresholdString(bpThresh.systolicAlertHigh), getSafeThresholdString(bpThresh.systolicWarningHigh),
                                            getSafeThresholdString(bpThresh.systolicWarningLow), getSafeThresholdString(bpThresh.systolicAlertLow)]
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.${type as String}.systolic")
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.values", args: argsSystolic)

                        def argsDiastolic = [getSafeThresholdString(bpThresh.diastolicAlertHigh), getSafeThresholdString(bpThresh.diastolicWarningHigh),
                                             getSafeThresholdString(bpThresh.diastolicWarningLow), getSafeThresholdString(bpThresh.diastolicAlertLow)]
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.${type as String}.diastolic")
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.values", args: argsDiastolic)
                    }
                    break;
                case MeasurementTypeName.URINE:
                    UrineThreshold t = p.getThreshold(type)
                    if (t) {
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.${type as String}")
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.values", args: getSafeThresholdMap(t))
                    }
                    break;
                case MeasurementTypeName.URINE_GLUCOSE:
                    UrineGlucoseThreshold t = p.getThreshold(type)
                    if (t) {
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.${type as String}")
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.values", args: getSafeThresholdMap(t))
                    }
                    break;
                default:
                    if (type) {
                        NumericThreshold t = p.getThreshold(type)
                        if (t) {
                            tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.${type as String}")
                            tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.values", args: getSafeThresholdMap(t))
                        }
                    }
            }
        }
        return tooltipText
	}

    private List getSafeThresholdMap(th) {
        def resList = []
        if(th) {
            resList = [getSafeThresholdString(th.alertHigh), getSafeThresholdString(th.warningHigh), getSafeThresholdString(th.warningLow), getSafeThresholdString(th.alertLow)]
        }
        return resList
    }

    private String getSafeThresholdString(def thresholdValue) {
        if (thresholdValue != null) {
            return thresholdValue
        } else {
            return message(code: "result.table.question.tooltip.threshold.null")
        }
    }

    private void renderSeverityIcon(MarkupBuilder builder, MeasurementResult result) {
        if (result) {
            switch (result.severity) {
                case Severity.RED:
                case Severity.YELLOW:
                    builder.img(class: "measurementResultSeverityIcon", src: g.resource(dir: "images", file: result.severity.icon()))
                    break;
            }
        }
    }

    private String getPrettyResultString(MeasurementResult result) {
        def prettyString = ""
        if (result) {
            if (!result.type) {
                //InputNodeResult
                if (result.value.equals("false") || result.value == false) {
                    prettyString = message(code: "default.yesno.false", default: "Nej")
                } else if (result.value.equals("true") || result.value == true) {
                    prettyString = message(code: "default.yesno.true", default: "Ja")
                } else {
                    prettyString = result.value.toString().encodeAsHTML()
                }
            } else {
                //MeasurementNodeResult
                MeasurementTypeName type = MeasurementTypeName.valueOf(result.type as String)
                prettyString = NumberFormatUtil.formatMeasurementResult(result, type)
            }
        }
        return prettyString
    }

    def renderAcknowledgeAllGreenButtons = { attributes ->
        Patient patient = (Patient) attributes['patient']
        def questionnaires = attributes['completedQuestionnaires'] ?: []
        writeAcknowledgeAllGreenButtons(questionnaires, patient,false)
    }

	def renderOverviewForPatient = { attributes, body ->
		Patient patient = (Patient) attributes['patient']

		def questionnaires = attributes['completedQuestionnaires'] ?: []
		def numberOfUnacknowledgedQuestionnaires = questionnaires.size()
        def (numberOfUnreadFromDepartment, oldestUnreadFromDepartment, numberOfUnreadFromPatient, oldestUnreadFromPatient) = patient.numberOfUnreadMessages
        def (icon, severityTooltip) = questionnaireService.iconAndTooltip(g, patient, questionnaires)

        MarkupBuilder builder = new MarkupBuilder(out)
        def mkp = builder.getMkp()
        builder.div(id: "questionnaireListHeader", class: "overviewStyleShadow", "") {

            // Put the (right) floating element first to prevent the IE7 float-newline bug
            writeAcknowledgeAllGreenButtons(questionnaires, patient)
            if (!patient.blueAlarmQuestionnaireIDs.empty) {
                writeRemoveBlueAlarmsButton(builder, patient.id)
            }

            // First entry item: Patient status
            def imageArguments = [src: g.resource(dir: "images", file: icon), id: "statusIcon"]
            if (severityTooltip) {
                imageArguments['data-tooltip'] = severityTooltip
            }
            builder.img(imageArguments)

            // Second entry item: Messages to patient icon
            if(messageService.clinicianCanSendMessagesToPatient(clinicianService.currentClinician, patient)) {
                def imageOUT
                def imageIN

                if (numberOfUnreadFromDepartment > 0) {
                    def tooltip = message(code: "completedquestionnaires.messages.unreadFromDepartment",args: [numberOfUnreadFromDepartment, formatDate(date: oldestUnreadFromDepartment?.sendDate)])
                    imageOUT = """<img src=${g.resource(dir: "/images", file: "outboxNew.png")} id="outboxIcon" data-tooltip="$tooltip"/>"""
                } else {
                    def tooltip =message(code: "completedequestionnaires.messages.noUnreadFromDepartment")
                    imageOUT = """<img src=${g.resource(dir: "/images", file: "outbox.png")} id="outboxIcon" data-tooltip="$tooltip"/>"""
                }
                if (numberOfUnreadFromPatient > 0) {
                    def tooltip=message(code: "completedquestionnaires.messages.unreadFromPatient", args: [numberOfUnreadFromPatient, formatDate(date: oldestUnreadFromPatient?.sendDate)])
                    imageIN = """<img src=${g.resource(dir: "/images", file: "inboxNew.png")} id="inboxIcon" data-tooltip="$tooltip"/>"""
                } else {
                    def tooltip=message(code: "completedequestionnaires.messages.noUnreadFromPatient")
                    imageIN = """<img src=${g.resource(dir: "/images", file: "inbox.png")} id="inboxIcon" data-tooltip="$tooltip"/>"""
                }
                mkp.yieldUnescaped(g.link(controller:"patient", action:"messages", id:patient.id, imageOUT))
                mkp.yieldUnescaped(g.link(controller:"patient", action:"messages", id:patient.id, imageIN))

            } else {
                def tooltip = message(code: "completedquestionnaires.messages.cannotSend")
                builder.img(src: g.resource(dir: "/images", file: "outbox-dimmed.png"), id: "outboxIcon", 'data-tooltip': tooltip)
                builder.img(src: g.resource(dir: "/images", file: "inbox-dimmed.png"), id: "inboxIcon", 'data-tooltip': tooltip)
            }

            // Third entry item: Patient name and CPR
            builder.div('data-tooltip': message(code: 'patient.overview.goto.patient.tooltip')) {
                builder.h2(class: "questionnaireListHeader", id: "patientName", "") {
                    mkp.yieldUnescaped(g.link(action: 'questionnaires', controller: 'patient', id: patient.id, patient.name))
                }
            }
            builder.div('data-tooltip': message(code: 'patient.overview.goto.patient.tooltip')) {
                builder.h2(class: "questionnaireListHeader", id: "patientCPR", "") {
                    mkp.yieldUnescaped(g.link(action: 'questionnaires', controller: 'patient', id: patient.id, patient.formattedCpr))
                }
            }

            // Fourth entry item: Expland/Collapse measurement table
            def tooltip = "<strong>${numberOfUnacknowledgedQuestionnaires}</strong> ukvitterede besvarelser. Tryk for at se ukvitterede besvarelser for denne patient."
            builder.div('data-tooltip': tooltip) {
                builder.img(src: g.resource(dir: "images", file: 'measurements_expand.png'), class: "measurementsIcon", style: "position:center")
            }

            // Patient notes
            builder.div(class: "patientNotes", "") {
                renderPatientNoteIcon(builder, attributes['patientNotes'], patient)
            }
        }
    }

    private void renderPatientNoteIcon(MarkupBuilder builder, patientNotes, Patient patient) {
        def clinician = Clinician.findByUser(springSecurityService.currentUser)
        def patientNoteImage = g.resource(dir:'/images', file: patientNoteImage(patientNotes, clinician))
        def patientNoteTooltip = patientNoteToolTip(patientNotes, clinician)
        def patientNoteIcon = "<img src='${patientNoteImage}' data-tooltip='${patientNoteTooltip}' id='noteIcon'/>"

        builder.getMkp().yieldUnescaped(g.link(controller:"patientNote", action:"list", id:patient.id, patientNoteIcon))
    }

    private String patientNoteImage(patientNotes, clinician) {
        PatientNote[] unreadNotes = patientNotes.grep { !it.seenBy.contains(clinician) }

        def hasUnreadImportantWithReminder = unreadNotes.any { it.type == NoteType.IMPORTANT && it.remindToday }
        def hasUnreadNormalWithReminder = unreadNotes.any { it.type == NoteType.NORMAL && it.remindToday }
        def hasUnreadImportantWithoutDeadline = unreadNotes.any { it.type == NoteType.IMPORTANT && !it.reminderDate }

        if (hasUnreadImportantWithReminder) {
            'note_reminder_red.png'
        } else if (hasUnreadNormalWithReminder) {
            'note_reminder_green.png'
        } else if (hasUnreadImportantWithoutDeadline) {
            'note_important.png'
        } else {
            'note.png'
        }
    }

    private String patientNoteToolTip(patientNotes, clinician) {
        String tooltip = "Du har ingen ulæste noter til denne patient. Tryk for at gå til patientens noter."

        def reminders = patientNotes.findAll{!it.seenBy.contains(clinician) && it.remindToday}
        def important = patientNotes.findAll{!it.seenBy.contains(clinician) && it.type == NoteType.IMPORTANT}
        def unread = patientNotes.findAll{!it.seenBy.contains(clinician)}
        def numReminders = reminders.size()
        def numImportant = important.size()
        def numUnread = unread.size()

        if (numUnread + numReminders + numImportant > 0) {
            tooltip = "Du har ${numUnread} ulæste noter til denne patient (<strong>${numReminders} påmindelser </strong> og <strong>${numImportant} vigtige</strong>). Tryk for at gå til patientens noter."
            if (numReminders > 0) {
                tooltip = "${tooltip}<br/><strong>Påmindelser:</strong>"
                reminders.each {
                    tooltip = "${tooltip} <br/>${it.note}"
                }
            }
            if (numImportant > 0) {
                tooltip = "${tooltip}<br/><strong>Vigtige:</strong>"
                important.each {
                    tooltip = "${tooltip} <br/>${it.note}"
                }
            }
        }

        tooltip
    }

    def patientNoteMarkSeenButton = { attrs, body ->
        if (SpringSecurityUtils.ifAnyGranted(PermissionName.PATIENT_NOTE_MARK_SEEN)) {
            if(!patientService.isNoteSeenByUser(attrs['note'])) {
                out << g.link(controller: "patientNote", action: "markSeen", id: attrs['id'], class: "acknowledge", g.message(code: "patientNote.markSeen.label"))
            }
        }
    }

    def overviewGraphs = { attributes, body ->
        def patient = attributes['patient']
        def (measurements) = measurementService.dataForGraphsAndTables(patient, TimeFilter.lastMonth())

        MarkupBuilder builder = new MarkupBuilder(out)
        def mkp = builder.getMkp()
        builder.div(class: "measurementsPlots") {
            mkp.yieldUnescaped("<![if gt IE 7]>")
            builder.h2(message(code: "patient.overview.graphs.header", args:["30"], default: "Grafer"))

            measurements.each { measurement ->
                builder.div(id: "${measurement.type}-${patient.id}", class: "overviewGraph", style: "width:750px")
            }

            mkp.yieldUnescaped(g.render(template: "/measurement/measurementGraph", collection: measurements, var: 'measurement', model:[patient: patient]))
            mkp.yieldUnescaped("<![endif]>")
        }
	}
	
	private void writeRemoveBlueAlarmsButton(MarkupBuilder builder, patientID) {
        builder.div(id: "removeBlueButton") {
            def tooltip = message(code:"completedQuestionnaire.overview.remove.blue.alarms")
            builder.getMkp().yieldUnescaped(
                g.form(controller: "patient", action: "removeAllBlue",
                    """<fieldset class="buttons">
                        <input type="hidden" name="patientID" value="${patientID}" />
                        <input 	type="submit" name="_action_removeAllBlue"
                        data-tooltip="${tooltip}"
                        value="" class="removeBlueAlarms"
                        onclick="return confirm('${message(	code: 'default.confirm.msg',
                                args: [message(code: 'confirm.context.msg.remove.blue.alarms')], default: 'Are you sure?')}');" />
				    </fieldset>"""
                )
            )
        }
	}
	
	def writeAcknowledgeAllGreenButtons(questionnaires, patient, createDivWrapper = true) {
        def idsOfGreenQuestionnaires = questionnaires.findAll{ it.severity == Severity.GREEN }.collect{ it.id }

        MarkupBuilder builder = new MarkupBuilder(out)
        if (idsOfGreenQuestionnaires) {
            def innerContent = {
                def acknowledgeAllIcon = """<img src="${g.resource(dir: 'images', file: 'acknowledged.png')}" data-tooltip="${message(code: 'tooltip.acknowledge.all')}"/>"""
                out << g.remoteLink(controller: 'patientOverview',
                        action: 'acknowledgeAll',
                        onComplete: 'location.reload(true);',
                        id: patient.id,
                        params:[ids:idsOfGreenQuestionnaires, withAutoMessage: 'false'],
                        before: "return confirm('${message(code: 'default.confirm.msg', args: [message(code: 'confirm.context.msg.questionnaire.acknowledgeAllForAll')])}')", acknowledgeAllIcon)

               def withAutoMessageIcon = getAutoMessageAllIcon(messageService.clinicianCanSendMessagesToPatient(Clinician.findByUser(springSecurityService.currentUser), patient))
               out << g.remoteLink(controller: 'patientOverview',
                        action: 'acknowledgeAll',
                        onComplete: 'location.reload(true);',
                        id: patient.id,
                        params:[ids:idsOfGreenQuestionnaires, withAutoMessage: 'true'],
                        before: "return confirm('${message(code: 'default.confirm.msg', args: [message(code: 'confirm.context.msg.questionnaire.acknowledgeAllForAll.and.send.messages')])}')", withAutoMessageIcon)
            }

            if (createDivWrapper) {
                builder.div(id: "acknowledgeButton") {
                    fieldset(class: "buttons", "") {
                        innerContent()
                    }
                }
            } else {
                innerContent()
            }
        }
	}

    def showQuestionnaire =  { attrs, body ->
        def completedQuestionnaireId = attrs.questionnaireId
        def cq = CompletedQuestionnaire.get(completedQuestionnaireId)
        def resultModel = questionnaireService.extractCompletedQuestionnaireWithAnswers(cq.patient.id, [cq.id])

        MarkupBuilder builder = new MarkupBuilder(out)
        def mkp = builder.getMkp()
        builder.table {
            thead {
                tr {
                    th(message(code: "default.questions", default: "Spørgsmål"))
                    th(message(code:"default.anwer"))
                    th(message(code:"default.severity"))
                    th(message(code:"default.ignored"))
                }
            }
            tbody {
                HashMap<ResultKey, List<MeasurementResult>> results = resultModel.results
                List<MeasurementDescription> questions = resultModel.questions
                OverviewColumnHeader header = resultModel.columnHeaders[0]

                questions.each {question ->
                    ResultKey key = getKeyForThisCell(question, header)
                    def answerList = results.get(key)
                    def answer
                    if(answerList) {
                        answer = answerList[0]
                        if (answerList.size() > 1) {
                            answer.value = answerList*.value.join(",")
                        }
                    }

                    tr {
                        td {
                            if (answer?.ignored) {
                                del(question.text)
                            } else {
                                mkp.yieldUnescaped(question.text)
                            }
                        }
                        td {
                            if (answer?.ignored) {
                                del(getPrettyResultString(answer))
                            } else {
                                mkp.yieldUnescaped(getPrettyResultString(answer))
                            }
                        }
                        td {
                            if (answer?.severity != null) {
                                if (answer?.ignored) {
                                    del(g.message(code: "enum.severity." + answer.severity))
                                } else {
                                    builder.img(src: g.resource(dir: 'images', file: answer.severity.icon))
                                }
                            }
                        }
                        td(class: "buttons") {
                            def btnLabel = "Ignorer"
                            def btnIcon = "cancel"
                            if (answer?.ignored) {
                                //I'll buy an ice-cream for whomever can tell me what the action of "un-ignoring" is called in danish.
                                //Has to be in imperative form.
                                btnLabel = "Ophæv ignorering"
                                btnIcon = "acknowledge"
                            }

                            if (SpringSecurityUtils.ifAnyGranted(PermissionName.NODE_RESULT_IGNORE)) {
                                if (answer != null) {
                                    builder.div('data-tooltip': g.message(code: 'tooltip.patient.questionnaire.ignoreMeasurement'), "") {
                                        mkp.yieldUnescaped(g.remoteLink(controller:"questionnaire", action:"toggleIgnoreNode", onComplete: "location.reload(true);", class: btnIcon, before:"", params:[resultID:answer.id, ignoreNavigation:'true'], btnLabel))
                                    }
                                } else {
                                    div('data-tooltip': g.message(code: 'questionnaire.show.question.answer.missing.ignorebutton.replacement.tooltip'), g.message(code: "questionnaire.show.question.answer.missing.ignorebutton.replacement"))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
