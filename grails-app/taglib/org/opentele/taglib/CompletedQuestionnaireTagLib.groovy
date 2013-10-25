package org.opentele.taglib

import grails.plugins.springsecurity.SpringSecurityService
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

        if (resultModel.results.size() == 0) {
            out << "<div style=\"margin: 10px 0 20px;\">${g.message(code: 'patient.questionnaire.noMeasurementsForPeriod')}</div>"
            return
        }

        out << "<div id=\"resultsContainer\" class=\"scrollable\">"
        out << "<table cellspacing=\"0\"><tbody>"
        out << "<tr>"
        out << "<td class=\"outerCell\">"

        // Upper left corner
        out << "<div class=\"tableContainer\" id=\"staticHeader\">"
        out << "<table><thead><tr><th>"
        out << g.message(code: "default.questions", default: "Spørgsmål")
        out << "</th></tr></thead></table>"
        out << "</div>"

        out << "</td><td class=\"outerCell\">"

        // Top table with headers
        out << "<div class=\"tableContainer\" id=\"headerContainer\">"
        out << "<table cellspacing=\"0\"><thead><tr>"
        resultModel.columnHeaders.each { questionnaireMetaData ->
            out << "<th><div>"
            renderHeaderForThisType(questionnaireMetaData)
            out << "</div></th>"
        }
        out << "</tr></thead></table>"
        out << "</div>"

        out << "</td>"
        out << "</tr><tr>"
        out << "<td class=\"outerCell\">"

        // Left table with headers
        out << "<div class=\"tableContainer\" id=\"leftHeaderContainer\">"
        out << "<table cellspacing=\"0\">"
        if (withPrefs) {
            out << "<tbody class=\"questionTable"+patientID+"\" data-bind=\"template:{name:'prefRowTemplate', foreach: prefRows}\">"
        } else {
            out << "<tbody class='questionTable"+patientID+"'>"
        }

        resultModel.questions.each { question ->
            renderQuestionForType(question, patientID)
        }
        out << "</tbody></table>"
        out << "</div>"

        out << "</td><td class=\"outerCell\">"

        // Result table
        out << "<div class=\"tableContainer\" id=\"resultTableContainer\" >"
        out << "<table cellspacing=\"0\">"
        if(withPrefs) {
            out << "<tbody class='resultsTable${patientID}' data-bind=\"template:{name:'prefRowResTemplate', foreach: prefResRows}\">"
        } else {
            out << "<tbody class='resultsTable${patientID}'>"
        }

        for (int i = 0; i < resultModel.questions.size(); i++) {
            out << "<tr name='${resultModel.questions[i].templateQuestionnaireNodeId}' class='result'> "
            for(int j = 0; j < resultModel.columnHeaders.size(); j++) {
                ResultKey key = getKeyForThisCell(resultModel.questions[i], resultModel.columnHeaders[j])

                if(resultModel.results[key]) {
                    renderMeasurementCell(resultModel.results[key], key.type)
                } else {
                    out << "<td><div></div></td>"
                }
            }
            out << "</tr>"
        }
        out << "</tbody></table>"
        out << "</div>"

        out << "</td></tr>"
        out << "</tbody></table>"
        out << "</div>"
	}

    def renderQuestionForType(MeasurementDescription measurementDescription, patientID) {
        switch (measurementDescription.type) {
            case MeasurementParentType.QUESTIONNAIRE:
                renderQuestionForQuestionnaire(measurementDescription, patientID)
                break;
            case MeasurementParentType.CONFERENCE:
                renderQuestionForConference(measurementDescription, patientID)
                break;
            default:
                throw new IllegalArgumentException("Unknown measurementParentType: ${measurementDescription.type}")
        }
    }

    def renderQuestionForConference(MeasurementDescription measurementDescription, patientID) {
        def _thresholdTooltipMessage = getThresholdValuesForQuestion(patientID as Long, measurementDescription.measurementTypeNames)
        def _tooltip = message(code: "result.table.question.tooltip.conference", args: [measurementDescription.questionnaireName])
        _tooltip = _thresholdTooltipMessage ? _tooltip + "<br/>" + _thresholdTooltipMessage : _tooltip
        def _units = measurementDescription.units ? measurementDescription.orderedUnits() : null

        def cellContent = message(code: "enum.measurementType." + measurementDescription.measurementTypeNames?.find{true})

        renderQuestionCell(_tooltip, cellContent, "<div class='question'>", _units)
    }

    private void renderQuestionForQuestionnaire(MeasurementDescription measurementDescription, patientID) {
        def _thresholdTooltipMessage = getThresholdValuesForQuestion(patientID as Long, measurementDescription.measurementTypeNames)
        def _tooltip = message(code: "result.table.question.tooltip.questionnaire", args: [measurementDescription.questionnaireName])
        _tooltip = _thresholdTooltipMessage ? _tooltip + "<br/>" + _thresholdTooltipMessage : _tooltip

        def nestedDivStartTag = "<div questionnaireName=\"" + measurementDescription.questionnaireName + "\" name=\"question\" id=\"" + measurementDescription.templateQuestionnaireNodeId + "\" class=\"question\">"
        def cellContent = measurementDescription.text

        def _units = measurementDescription.units ? measurementDescription.orderedUnits() : null

        renderQuestionCell(_tooltip, cellContent, nestedDivStartTag, _units)
    }

    private void renderQuestionCell(_tooltip, cellContent, nestedDivStartTag, _units) {
        out << """<tr><td data-tooltip="$_tooltip">"""
        out << nestedDivStartTag

        out << "<B>"
        out << cellContent
        out << "</B>"

        if (_units) {

            out << " ("

            _units.eachWithIndex { unit, idx ->
                if (idx > 0) {
                    out << "/"
                }
                out << message(code: "enum.unit." + unit)
            }

            out << ")"
        }
        out << "</div></td></tr>"
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

    def renderMeasurementCell(List<MeasurementResult> measurementResultList, MeasurementParentType parentType) {
        switch(parentType) {
            case MeasurementParentType.QUESTIONNAIRE:
                renderMeasurementCellForQuestionnaire(measurementResultList)
                break;
            case MeasurementParentType.CONFERENCE:
                renderMeasurementCellForConference(measurementResultList)
                break;
            default:
                throw new IllegalArgumentException("Unknown measurementParentType: ${parentType}")
        }

    }

    private void renderMeasurementCellForQuestionnaire(List<MeasurementResult> measurementResultList) {
        def measurementResult = measurementResultList.get(0)
        if (measurementResultList.size() > 1) {
            measurementResult.value = measurementResultList*.value.join(",")
        }
        def contents = ""
        if (measurementResult.ignored) {
            contents += "<del>"
        } //If any of the measurements are ignored they are all ignored

        contents += getSeverityIcon(measurementResult)

        def prettyResultString = getPrettyResultString(measurementResult)
        contents += prettyResultString

        if (measurementResult.ignored) {
            contents += "</del>"
        }

        renderCellContents(contents, prettyResultString as String)
    }


    def renderMeasurementCellForConference(List<MeasurementResult> measurementResultList) {
        def contents = measurementResultList.collect {getPrettyResultString(it)}.join(" <br/> ")
        renderCellContents(contents, contents)
    }

    private void renderCellContents(String cellContent, tooltipText) {
        def toolTip = tooltipForMeasurementResult(tooltipText)
        if (toolTip) {
            out << """<td><div data-tooltip="${toolTip.encodeAsHTML()}">"""
        } else {
            out << "<td><div>"
        }

        out << cellContent

        out << "</div></td>"
    }


    def tooltipForMeasurementResult(String resultString) {
      if(resultString.length() > 15) {
        return resultString
      }
    }

    def renderHeaderForThisType(OverviewColumnHeader columnHeader) {
        MeasurementParentType columnType = columnHeader.type
        switch (columnType) {
            case MeasurementParentType.CONFERENCE:
                renderHeaderForConference(columnHeader)
                break;
            case MeasurementParentType.QUESTIONNAIRE:
                renderHeaderForQuestionnaire(columnHeader)
                break;
            default:
                throw new IllegalArgumentException("Unknown measurementParentType: ${columnType}")
        }
    }

    def renderHeaderForConference(OverviewColumnHeader columnHeader) {
        out << columnHeader.uploadDate.toCalendar().format(g.message(code:"default.date.format.short")) + "<br/>"

        def conference_image = """<img src=${g.resource(dir: 'images', file: 'conferenceshow.png')} data-tooltip="Målinger foretaget under video-konference"/>"""
        out << g.link(controller:"patient", action:"conference",id:columnHeader.id, conference_image)

    }

    def renderHeaderForQuestionnaire(columnHeader) {
        def img_severity = "<img src=" + g.resource(dir: "images", file: columnHeader.severity.icon()) + " />"
        def img_edit = """<img src=${g.resource(dir: "images", file: "edit.png")} data-tooltip="Se spørgeskema / ignorer besvarelser / tilføj kommentarer"/>"""
        def img_acknowledge = """<img src=${g.resource(dir: "images", file: "unacknowledged.png")} data-tooltip="Kvittér"/>"""

        def autoMessageEnabledForPatient = messageService.autoMessageIsEnabledForCompletedQuestionnaire(columnHeader.id)
        def img_acknowledgeWithAutoMessage = getAutoMessageIcon(autoMessageEnabledForPatient)


        out << columnHeader.uploadDate.toCalendar().format(g.message(code:"default.date.format.short")) + "<br/>"
        out << img_severity
        out << g.link(controller:"patient", action:"questionnaire", id:columnHeader.id, img_edit)
        if (columnHeader.acknowledgedBy) {
            def tooltip = g.message(code:"completedQuestionnaire.acknowledged.label", args: [columnHeader.acknowledgedBy, columnHeader.acknowledgedDate.format(message(code: "default.date.format")).toString()]) + (columnHeader.acknowledgedNote? "\\nNote: " + columnHeader.acknowledgedNote:"")
            img_acknowledge = """<img src=${g.resource(dir: "images", file: "acknowledged.png")} data-tooltip="$tooltip"/>"""
            out << img_acknowledge
        } else {
            out << "<a href='#' class='acknowledge' data-automessage='false' data-questionnaire-id='${columnHeader.id}')'>${img_acknowledge}</a>"

            if (autoMessageEnabledForPatient) {
                out << "<a href='#' class='acknowledge' data-automessage='true' data-questionnaire-id='${columnHeader.id}')'>${img_acknowledgeWithAutoMessage}</a>"
            } else {
                //Just the image
                out << img_acknowledgeWithAutoMessage
            }
        }
    }

    private String getAutoMessageIcon(boolean autoMessageEnabledForPatient) {
        getAutoMessageIcon(autoMessageEnabledForPatient, 'tooltip.acknowledge.with.message')
    }

    private String getAutoMessageAllIcon(boolean autoMessageEnabledForPatient) {
        getAutoMessageIcon(autoMessageEnabledForPatient, 'tooltip.acknowledge.all.with.message')
    }

    private String getAutoMessageIcon(autoMessageEnabledForPatient, tooltipMessageCode) {
        if (autoMessageEnabledForPatient) {
            "<img src=${g.resource(dir: 'images', file: 'unacknowledgedWithAutoMessage.png')} data-tooltip='${message(code: tooltipMessageCode)}'>"
        } else {
            "<img src=${g.resource(dir: 'images', file: 'unacknowledgedWithAutoMessageDisabled.png')} data-tooltip='${message(code: 'tooltip.acknowledge.with.message.disabled')}'>"
        }
    }

    def getThresholdValuesForQuestion(Long patientId, Set<MeasurementTypeName> types) {

        Patient p = Patient.get(patientId)

        def tooltipText = ""

        types.each { type ->
            switch (type) {

                case MeasurementTypeName.BLOOD_PRESSURE:
                    BloodPressureThreshold bloodPressureThreshold = p.getThreshold(type)

                    if (!(bloodPressureThreshold)) {
                        break
                    }

                    if (bloodPressureThreshold) {
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.${type as String}.systolic")
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.values", args: [getSafeThresholdString(bloodPressureThreshold.systolicAlertHigh),
                                                                                                                            getSafeThresholdString(bloodPressureThreshold.systolicWarningHigh),
                                                                                                                            getSafeThresholdString(bloodPressureThreshold.systolicWarningLow),
                                                                                                                            getSafeThresholdString(bloodPressureThreshold.systolicAlertLow)])
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.${type as String}.diastolic")
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.values", args: [getSafeThresholdString(bloodPressureThreshold.diastolicAlertHigh),
                                                                                                                            getSafeThresholdString(bloodPressureThreshold.diastolicWarningHigh),
                                                                                                                            getSafeThresholdString(bloodPressureThreshold.diastolicWarningLow),
                                                                                                                            getSafeThresholdString(bloodPressureThreshold.diastolicAlertLow)])
                    }
                    break;
                case MeasurementTypeName.URINE:
                    UrineThreshold t = p.getThreshold(type)
                    if (!t) {
                        break //No threshold of this type for this patient
                    }
                    tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.${type as String}")
                    tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.values", args: [getSafeThresholdString(t.alertHigh),
                                                                                                                        getSafeThresholdString(t.warningHigh),
                                                                                                                        getSafeThresholdString(t.warningLow),
                                                                                                                        getSafeThresholdString(t.alertLow)])
                    break;
                case MeasurementTypeName.URINE_GLUCOSE:
                    UrineGlucoseThreshold t = p.getThreshold(type)
                    if (!t) {
                        break //No threshold of this type for this patient
                    }
                    tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.${type as String}")
                    tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.values", args: [getSafeThresholdString(t.alertHigh),
                                                                                                                        getSafeThresholdString(t.warningHigh),
                                                                                                                        getSafeThresholdString(t.warningLow),
                                                                                                                        getSafeThresholdString(t.alertLow)])
                    break;
                default:
                    if (!type) {
                        break //No type for this measurment
                    }
                    NumericThreshold t = p.getThreshold(type)
                    if (!t) {
                        break //No threshold of this type for this patient
                    }
                    tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.${type as String}")
                    tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.values", args: [getSafeThresholdString(t.alertHigh),
                                                                                                                        getSafeThresholdString(t.warningHigh),
                                                                                                                        getSafeThresholdString(t.warningLow),
                                                                                                                        getSafeThresholdString(t.alertLow)])
            }
        }

        return tooltipText
	}

    def getSafeThresholdString(def thresholdValue) {
        if (thresholdValue != null) {
            return thresholdValue
        } else {
            return message(code: "result.table.question.tooltip.threshold.null")
        }
    }

    def getSeverityIcon(MeasurementResult result) {
        if (!result) {
            return "";
        }

        if (result.severity.equals(Severity.RED)) {
            "<img class=\"measurementResultSeverityIcon\" src=" + g.resource(dir: "images", file: result.severity.icon()) + ">"
        } else if (result.severity.equals(Severity.YELLOW)) {
            "<img class=\"measurementResultSeverityIcon\" src=" + g.resource(dir: "images", file: result.severity.icon()) + ">"
        } else {
            ""
        }
    }
    def getPrettyResultString(MeasurementResult result) {
        if (!result) {
            return ""
        }

        def prettyString

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

		//Stuff to show in the overview line
		out << """<div id="questionnaireListHeader" class="overviewStyleShadow">"""

		// Put the (right) floating element first to prevent the IE7 float-newline bug
		writeAcknowledgeAllGreenButtons(questionnaires, patient)
		if (!patient.blueAlarmQuestionnaireIDs.empty) {
			writeRemoveBlueAlarmsButton(patient.id)
		}

        //First entry item: Patient status
        def imageArguments = [dir: "images", file: icon, id: 'statusIcon']
        if (severityTooltip) {
            imageArguments['data-tooltip'] = "${severityTooltip}"
        }
        out << g.img(imageArguments)


        //Second entry item: Messages to patient icon
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
            out << g.link(controller:"patient", action:"messages", id:patient.id, imageOUT)
           out << g.link(controller:"patient", action:"messages", id:patient.id, imageIN)       } else {
             out << """<img src=${g.resource(dir: "/images", file: "outbox-dimmed.png")} id="outboxIcon" data-tooltip="${message(code: "completedquestionnaires.messages.cannotSend")}"/>"""
             out << """<img src=${g.resource(dir: "/images", file: "inbox-dimmed.png")} id="inboxIcon" data-tooltip="${message(code: "completedquestionnaires.messages.cannotSend")}"/>"""
        }


        //Third entry item: Patient name and CPR
        out << """<div data-tooltip="${message(code: 'patient.overview.goto.patient.tooltip')}">"""
		out << """<h2 class="questionnaireListHeader" id="patientName">${g.link(action: 'questionnaires', controller: 'patient', id: patient.id, patient.name)}</h2>"""
		out << "</div>"

        out << """<div data-tooltip="${message(code: 'patient.overview.goto.patient.tooltip')}">"""
        out << """<h2 class="questionnaireListHeader" id="patientCPR">${g.link(action: 'questionnaires', controller: 'patient', id: patient.id, patient.formattedCpr)}</h2>"""
        out << "</div>"

        //Fourth entry item: Expland/Collapse measurement table
        out << """<div data-tooltip="<strong>${numberOfUnacknowledgedQuestionnaires}</strong> ukvitterede besvarelser. Tryk for at se ukvitterede besvarelser for denne patient.">"""
        out << "<img src='${g.resource(dir: "images", file: 'measurements_expand.png')}' class='measurementsIcon' style='position:center'/>"
        out << "</div>"

        out << "<div class='patientNotes'>${patientNoteIcon(attributes['patientNotes'], patient)}</div>"
        out << "</div>"
    }

    private String patientNoteIcon(patientNotes, Patient patient) {
        def clinician = Clinician.findByUser(springSecurityService.currentUser)

        def patientNoteImage = g.resource(dir:'/images', file: patientNoteImage(patientNotes, clinician))
        def patientNoteTooltip = patientNoteToolTip(patientNotes, clinician)
        def patientNoteIcon = "<img src='${patientNoteImage}' data-tooltip='${patientNoteTooltip}' id='noteIcon'/>"

        g.link(controller:"patientNote", action:"list", id:patient.id, patientNoteIcon)
    }

    def patientNoteImage(patientNotes, clinician) {


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

    def patientNoteToolTip(patientNotes, clinician) {
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
                //out << """<g:link class="acknowledge" action="markSeen" id=\"${patientNoteInstance?.id}\"><g:message code="patientNote.markSeen.label" default="Seen"/></g:link>"""
                out << g.link(controller: "patientNote", action: "markSeen", id: attrs['id'], class: "acknowledge", g.message(code: "patientNote.markSeen.label"))
            }
        }
    }

    def overviewGraphs = { attributes, body ->
        def patient = attributes['patient']
        def (measurements) = measurementService.dataForGraphsAndTables(patient, TimeFilter.lastMonth())

        out << "<div class=\"measurementsPlots\">"
		//Graphs are not supported in IE7 (yet?)
		out << "<![if gt IE 7]>"
		out << "<h2>"+g.message(code: "patient.overview.graphs.header", args:["30"], default: "Grafer")+"</h2>"
		measurements.each { measurement ->
            out << "<div id='${measurement.type}-${patient.id}' class='overviewGraph' style='width:750px'></div>"
        }
        out << g.render(template: "/measurement/measurementGraph", collection: measurements, var: 'measurement', model:[patient: patient])
        out << "<![endif]>"
        out << "</div>"
	}
	
	def writeRemoveBlueAlarmsButton(patientID) {
		out << "<div id=\"removeBlueButton\">"
		def tooltip = message(code:"completedQuestionnaire.overview.remove.blue.alarms")
		out << g.form(controller: "patient", action: "removeAllBlue",
			"""
				<fieldset class="buttons">
					<input type="hidden" name="patientID" value="${patientID}" />
					<input 	type="submit" name="_action_removeAllBlue"
					data-tooltip="${tooltip}"
					value="" class="removeBlueAlarms"
					onclick="return confirm('${message(	code: 'default.confirm.msg',
														args: [message(code: 'confirm.context.msg.remove.blue.alarms')], default: 'Are you sure?')}');" />
				</fieldset>
			"""
		)
		
		out << "</div>"
	}
	
	def writeAcknowledgeAllGreenButtons(questionnaires, patient, createDivWrapper = true) {
        def idsOfGreenQuestionnaires = questionnaires.findAll{ it.severity == Severity.GREEN }.collect{ it.id }

        if (idsOfGreenQuestionnaires) {
            if(createDivWrapper) {
                out << '<div id="acknowledgeButton">'
                out << '<fieldset class="buttons">'
            }

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
            if(createDivWrapper) {
                out << '</fieldset>'
                out << '</div>'
            }
		}
	}



    def showQuestionnaire =  { attrs, body ->
        def completedQuestionnaireId = attrs.questionnaireId
        def cq = CompletedQuestionnaire.get(completedQuestionnaireId)
        def resultModel = questionnaireService.extractCompletedQuestionnaireWithAnswers(cq.patient.id, [cq.id])

        out << "<table>"
        out << "\n"
        out << "<thead><TR>"
        out << "\n"
        out << "<TH>"
        out << g.message(code: "default.questions", default: "Spørgsmål")

        out << "</TH>"
        out << "<TH>"
        out << message(code:"default.anwer")
        out << "</TH>"

        out << "<TH>"
        out << message(code:"default.severity")
        out << "</TH>"

        out << "<TH>"
        out << message(code:"default.ignored")
        out << "</TH>"
        out << "</THEAD><TBODY>"


        //We assume that:
        // |model.questions| = 1
        // |model.results| = 1
        // |model.questions[0]| = |model.results[0]|
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


            out << "<TR>"

            out << "<TD>"
            if (answer?.ignored) {out << "<del>"}
            out << question.text
            if (answer?.ignored) {out << "</del>"}
            out << "</TD>"

            out << "<TD>"
            if (answer?.ignored) {out << "<del>"}
            out << getPrettyResultString(answer)
            if (answer?.ignored) {out << "</del>"}
            out << "</TD>"

            out << "<TD>"
            if (answer?.severity != null) {
                if (answer?.ignored) {
                    out << "<del>"
                    out << g.message(code: "enum.severity." + answer.severity)
                    out << "<del>"
                } else {
                    out << g.img(dir: "images", file: answer.severity.icon)
                }
            }
            out << "</TD>"

            out << "<TD class=\"buttons\">"
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
                    out << """<div data-tooltip="${g.message(code: 'tooltip.patient.questionnaire.ignoreMeasurement')}">"""
                    out << g.remoteLink(controller:"questionnaire", action:"toggleIgnoreNode", onComplete: "location.reload(true);", class: btnIcon, before:"", params:[resultID:answer.id, ignoreNavigation:'true'], btnLabel)
                    out << "</div>"
                } else {
                    out << """<div data-tooltip="${g.message(code: 'questionnaire.show.question.answer.missing.ignorebutton.replacement.tooltip')}">"""
                    out << g.message(code: "questionnaire.show.question.answer.missing.ignorebutton.replacement")
                    out << "</div>"
                }
            }
            out << "</TD>"

            out << "</TR>"
        }
        out << "</TBODY></TABLE>"
    }
}
