package org.opentele.server.questionnaire

import org.hibernate.Criteria
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.hibernate.criterion.Order
import org.opentele.server.TimeFilter
import org.opentele.server.model.*
import org.opentele.server.model.patientquestionnaire.*
import org.opentele.server.model.questionnaire.*
import org.opentele.server.model.types.*
import org.springframework.transaction.annotation.Transactional

import static java.util.Calendar.HOUR_OF_DAY
import static java.util.Calendar.MINUTE
import static java.util.Calendar.SECOND

class QuestionnaireService {
    static transactional = false

	def springSecurityService
    def sessionFactory
    def questionnaireNodeService
    def grailsApplication

    /**
     *
     * Creates patient questionnaire from template questionnaire 
     */
	@Transactional
    PatientQuestionnaire createPatientQuestionnaire(Clinician creator, Questionnaire template) {

        PatientQuestionnaire pq = PatientQuestionnaire.findByTemplateQuestionnaire(template)

        if (!pq) {

            pq = new PatientQuestionnaire(templateQuestionnaire: template, creator: creator, creationDate: new Date())

            applyDBFields(pq)

            pq.save(failOnError:true)

            Map<String, PatientQuestionnaireNode> map = new HashMap<String, PatientQuestionnaireNode>()

            // 1st run: Attributes

            def nodes = QuestionnaireNode.findAllByQuestionnaire(template, [sort: "id", order: "desc"])

            for (QuestionnaireNode qn : nodes) {

                PatientQuestionnaireNode pqn = null;
                if (qn.instanceOf(BooleanNode)) {
                    pqn = new PatientBooleanNode()
                    pqn.setVariableName(qn.variableName)
                    pqn.setValue(qn.value)
                } else if (qn.instanceOf(TextNode)) {
                    pqn = new PatientTextNode()
                    pqn.setHeadline(qn.headline)
                    pqn.setText(qn.text)
                } else if (qn.instanceOf(ChoiceNode)) {
                    pqn = new PatientChoiceNode()
                    pqn.dataType = qn.dataType

                    pqn.operation = qn.operation
                    pqn.nodeValue= qn.nodeValue
                    pqn.inputVar = qn.inputVar

                    pqn.defaultSeverity = qn.defaultSeverity
                    pqn.alternativeSeverity = qn.alternativeSeverity

                } else if (qn.instanceOf(InputNode)) {
                    pqn = new PatientInputNode()

                    pqn.text = qn.text
                    pqn.inputType = qn.inputType

                    // Iff Boolean:
                    pqn.defaultSeverity = qn.defaultSeverity
                    pqn.alternativeSeverity = qn.alternativeSeverity
                } else if (qn.instanceOf(NoteInputNode)) {
                    pqn = new PatientNoteInputNode()
                    pqn.text = qn.text
                } else if (qn.instanceOf(DelayNode)) {
                    pqn = new PatientDelayNode()
                    pqn.text = qn.text
                    pqn.countUp = qn.countUp
                    pqn.countTime = qn.countTime
                } else if (qn.instanceOf(EndNode)) {
                    pqn = new PatientEndNode()
                } else if (qn.instanceOf(MeasurementNode)) {
                    pqn = new PatientMeasurementNode()
                    pqn.text = qn.text
                    pqn.simulate = qn.simulate
                    pqn.monicaMeasuringTimeInputVar = qn.monicaMeasuringTimeInputVar
                    pqn.mapToInputFields = qn.mapToInputFields
                    pqn.meterType = qn.meterType

                } else {
                    log.warn("Unknown node type encountered 1..${qn}")
                }

                pqn.shortText = qn.shortText
                pqn.setTemplateQuestionnaireNode(qn)
                pqn.setQuestionnaire(pq)
                applyDBFields(pqn)
                pqn.save(failOnError: true)

                map.put(qn.id, pqn)
            }

            // 2nd run: Refs..
            for (QuestionnaireNode qn : template.getNodes()) {

               if (qn.instanceOf(TextNode) || qn.instanceOf(MeasurementNode) || qn.instanceOf(BooleanNode)) {

                   PatientQuestionnaireNode ptn = map.get(qn.id);
                   ptn.defaultNext = map.get(qn.defaultNext.id);

                   if (qn.instanceOf(MeasurementNode)) {
                       ptn.nextFail = map.get(qn.nextFail.id);
                       ptn.monicaMeasuringTimeInputNode = map.get(qn?.monicaMeasuringTimeInputNode?.id)
                   }
                   ptn.save(failOnError:true)

               } else if (qn.instanceOf(InputNode)) {

                   PatientQuestionnaireNode ptn = (PatientInputNode)map.get(qn.id);
                   ptn.defaultNext = map.get(qn.defaultNext.id);

                   if (qn.alternativeNext) {
                       ptn.alternativeNext = map.get(qn.alternativeNext.id);
                   }
                   ptn.save(failOnError:true)

                   if (qn.inputType == DataType.RADIOCHOICE) {

                       def choices = qn.getChoices()
                       if (choices && choices.size() > 0) {

                           choices.each { c ->
                               PatientChoiceValue pc = new PatientChoiceValue()
                               pc.value = c.value
                               pc.label = c.label
                               pc.ordering = c.ordering
                               pc.patientInputNode = ptn
                               applyDBFields(pc)
                               pc.save(failOnError:true)
                           }
                       }
                   }
               } else if (qn.instanceOf(NoteInputNode)) {
                    PatientQuestionnaireNode ptn = (PatientNoteInputNode)map.get(qn.id);
                    ptn.defaultNext = map.get(qn.defaultNext.id);

                    ptn.save(failOnError:true)
               } else if (qn.instanceOf(DelayNode)) {
                   PatientQuestionnaireNode ptn = (PatientDelayNode)map.get(qn.id);
                   ptn.defaultNext = map.get(qn.defaultNext.id);
                   ptn.save(failOnError: true)
               } else if (qn.instanceOf(ChoiceNode)) {

                   PatientChoiceNode pcn = map.get(qn.id);
                   pcn.defaultNext = map.get(qn.defaultNext.id);
                   pcn.alternativeNext = map.get(qn.alternativeNext.id);

                   pcn.inputNode = map.get(qn.inputNode.id);

                   pcn.save(failOnError:true)
               } else if (qn.instanceOf(EndNode)) {
                   // do nothing
               } else {
                   log.warn("Unknown node type encountered 2..${qn}")
               }
            }

            QuestionnaireNode root = template.getStartNode()
            pq.setStartNode(map.get(root.id))
            pq.save(failOnError:true)
        }
        pq
    }

    def extractMeasurements(Long patientID, boolean unacknowledgedOnly = false, TimeFilter timeFilter = null) {
        def completedQuestionnaireResultModel = extractCompletedQuestionnaireWithAnswers(patientID, null, unacknowledgedOnly, timeFilter)

        if(!unacknowledgedOnly) {
            def conferenceResultModel = extractConferenceResults(patientID, timeFilter)
            completedQuestionnaireResultModel.columnHeaders.addAll(conferenceResultModel.columnHeaders)
            completedQuestionnaireResultModel.questions.addAll(0, conferenceResultModel.questions)
            completedQuestionnaireResultModel.results.putAll(conferenceResultModel.results)

        }

        completedQuestionnaireResultModel.columnHeaders.sort({a, b -> b.uploadDate <=> a.uploadDate} as Comparator)
        return completedQuestionnaireResultModel
    }

    def extractConferenceResults(Long patientID, TimeFilter timeFilter = null) {
        ResultTableViewModel result = new ResultTableViewModel()
        def patient = Patient.findById(patientID)

        def completedConferences
        if (timeFilter && timeFilter.isLimited) {
            // Filter created date
            completedConferences = Conference.findAllByPatientAndCompletedAndCreatedDateBetween(patient, true, timeFilter.start, timeFilter.end)
        } else {
            completedConferences = Conference.findAllByPatientAndCompleted(patient, true)
        }

        result.columnHeaders = completedConferences.collect {
            new OverviewColumnHeader(type: MeasurementParentType.CONFERENCE, uploadDate: it.createdDate, id: it.id)
        }

        List<Measurement> measurements = (completedConferences*.measurements).flatten()

        def types = [:]
        measurements.each {
            types[it.measurementType.name] = it.unit
        }

        result.questions = types.keySet().collect { typeName ->

            MeasurementDescription md = new MeasurementDescription(type: MeasurementParentType.CONFERENCE)
            md.measurementTypeNames = new HashSet<MeasurementTypeName>()
            md.measurementTypeNames.add(typeName)

            md.units = new HashSet<Unit>()
            md.units.add(types[typeName])
            md
        }

        result.results = [:]
        measurements.each { measurement ->
            //Severity, ignored, ignoredReason, ignoredBy left out
            //exported, cqId, tqnId, pqnId not relevant for this type
            def value
            switch (measurement.measurementType.name) {
                case MeasurementTypeName.BLOOD_PRESSURE:
                    value = "${measurement.systolic},${measurement.diastolic}"
                    break
                default:
                    value = measurement.value
                    break;
            }
            def key = new ResultKey(rowId: "conf-${measurement.measurementType.name}", colId: "conf-${measurement.conference.id}")
            if(!result.results.containsKey(key)) {
                result.results[key] = []
            }
            result.results[key] << new MeasurementResult(type: measurement.measurementType.name, unit: measurement.unit, value: value, id: measurement.id)
        }

        result
    }

    def extractCompletedQuestionnaireWithAnswers(Long patientID, List<Long> completedQuestionnairesFilter = null, boolean unacknowledgedOnly = false, TimeFilter timeFilter = null) {
        ResultTableViewModel result = new ResultTableViewModel()

        def session = sessionFactory.currentSession

        Criteria completedQuestionnairesCriteria = session.createCriteria(CompletedQuestionnaire.class, "CQ")
                .add(Restrictions.eq("patient.id", patientID))
                .setProjection(
                Projections.projectionList()
                    .add(Projections.property("id"))
                    .add(Projections.property("uploadDate"))
                    .add(Projections.property("severity"))
                    .add(Projections.property("acknowledgedBy"))
                    .add(Projections.property("acknowledgedDate"))
                    .add(Projections.property("acknowledgedNote"))
                    .add(Projections.property("_questionnaireIgnored"))
                    .add(Projections.property("questionnaireIgnoredReason"))
                    .add(Projections.property("questionnareIgnoredBy"))
                ).addOrder(Order.asc("CQ.id"));

        if (completedQuestionnairesFilter && completedQuestionnairesFilter.size() > 0) { //Limit to this set of patientQuestionnaires
            completedQuestionnairesCriteria.add(Restrictions.in("id", completedQuestionnairesFilter))
        }

        if (unacknowledgedOnly) {
            completedQuestionnairesCriteria.add(Restrictions.isNull("acknowledgedBy"))
        }

        // Filter upload date
        if (timeFilter && timeFilter.isLimited) {
            completedQuestionnairesCriteria.add(Restrictions.between("uploadDate", timeFilter.start, timeFilter.end))
        }

        def completedQuestionnaires = completedQuestionnairesCriteria.list()
        if (completedQuestionnaires.empty) {
            return result
        }

        def sortedCompletedQuestionnaires = completedQuestionnaires.sort {a, b -> b[1] <=> a[1]}
        def completedQuestionnairesIds = sortedCompletedQuestionnaires.collect {it[0]}

        result.columnHeaders = sortedCompletedQuestionnaires.collect {
            new OverviewColumnHeader(type: MeasurementParentType.QUESTIONNAIRE, id: it[0], uploadDate: it[1], severity: it[2], acknowledgedBy: it[3], acknowledgedDate: it[4], acknowledgedNote: it[5], _questionnaireIgnored: it[6], questionnaireIgnoredReason: it[7], questionnareIgnoredBy: it[8] )
        }

        Criteria questionnaireNodesCriteria = session.createCriteria(QuestionnaireNode.class, "QN")
                .createAlias("questionnaire", "Q")
                .createAlias("Q.patientQuestionnaires", "PQs")
                .createAlias("PQs.completedQuestionnaires", "CQs")
                .createAlias("Q.questionnaireHeader", "QH")
                .add(Restrictions.eq("CQs.patient.id", patientID))
                .add(Restrictions.in("CQs.id", completedQuestionnairesIds))

        questionnaireNodesCriteria.setProjection(
                Projections.projectionList()
                        .add(Projections.property("QN.id"))
                        .add(Projections.property("QN.text"))
                        .add(Projections.property("QH.name"))     // 2
                        .add(Projections.property("Q.startNode.id"))
                        .add(Projections.property("alternativeNext.id"))
                        .add(Projections.property("nextFail.id"))
                        .add(Projections.property("defaultNext.id"))
                        .add(Projections.property("class"))
                        .add(Projections.property("QN.id"))
                        .add(Projections.property("QN.shortText"))
                        .add(Projections.property("Q.revision"))
        );

        questionnaireNodesCriteria.addOrder(Order.asc("QN.id"))

        def questionnaireNodes = questionnaireNodesCriteria.list().unique()
        //We need to sort (order: Same as presented when answering questionnaire) and filter (only interested in MeasurementNode and InputNode)
        //(Query of all from DB is needed to sort)
        //Group by questionnaire

        Map unsortedNodesPerQuestionnaire = questionnaireNodes.groupBy {"${it[2]} v. ${it[10]}"} // Group by name, version
        Map sortedFilteredNodesPerQuestionnaire = [:]

        //Sort by presented order
        unsortedNodesPerQuestionnaire.each {questionnaire, listNodes ->

            def sorted = sortNodes(listNodes)

            //Filter
            def filtered = sorted.findAll {GraphNode n ->    n.clazz == MeasurementNode.class.canonicalName ||
                                                             n.clazz == InputNode.class.canonicalName}
            sortedFilteredNodesPerQuestionnaire[questionnaire] = filtered
        }

        sortedFilteredNodesPerQuestionnaire = sortedFilteredNodesPerQuestionnaire.sort({a, b -> b <=> a} as Comparator)

        sortedFilteredNodesPerQuestionnaire.values().each { listQuestions ->
            listQuestions.each {it ->
                result.questions.add(new MeasurementDescription(type: MeasurementParentType.QUESTIONNAIRE, text: it.text, questionnaireName: it.questionnaireName, templateQuestionnaireNodeId: it.templateQuestionnaireNodeId, units: [] as Set, measurementTypeNames: [] as Set))
            }
        }

        Criteria inputNodeResultsCriteria = session.createCriteria(InputNodeResult.class, "INR")
                    .createAlias("completedQuestionnaire", "CQ")
                    .createAlias("patientQuestionnaireNode", "PQN")
                    .createAlias("PQN.templateQuestionnaireNode", "TQN")
                    .add(Restrictions.in("CQ.id", completedQuestionnairesIds))
                    .setProjection(
                        Projections.projectionList()
                        .add(Projections.property("INR.id"))
                        .add(Projections.property("INR.result"))
                        .add(Projections.property("INR.nodeIgnored"))
                        .add(Projections.property("INR.nodeIgnoredReason"))
                        .add(Projections.property("INR.nodeIgnoredBy"))
                        .add(Projections.property("CQ.id"))
                        .add(Projections.property("TQN.id")) //6
                        .add(Projections.property("PQN.id"))
                        .add(Projections.property("severity"))
                    ).addOrder(Order.asc("INR.id"))

        Criteria measurementNodeResultsCriteria = session.createCriteria(Measurement.class, "MEAS")
                    .createAlias("measurementNodeResult", "MNR")
                    .createAlias("MNR.completedQuestionnaire", "CQ")
                    .createAlias("MNR.patientQuestionnaireNode", "PQN")
                    .createAlias("PQN.templateQuestionnaireNode", "TQN")
                    .createAlias("measurementType", "MTYPE")
                    .add(Restrictions.in("CQ.id", completedQuestionnairesIds))
                    .setProjection(
                        Projections.projectionList()
                        .add(Projections.property("MNR.id"))
                        .add(Projections.property("MNR.nodeIgnored"))
                        .add(Projections.property("MNR.nodeIgnoredReason"))
                        .add(Projections.property("MNR.nodeIgnoredBy"))
                        .add(Projections.property("CQ.id"))
                        .add(Projections.property("TQN.id"))    //5
                        .add(Projections.property("PQN.id"))
                        /* Now get each property and make nice afterwards */
                        .add(Projections.property("MTYPE.name"))
                        .add(Projections.property("unit"))         //8
                        .add(Projections.property("exported"))     //9
                        .add(Projections.property("value"))
                        .add(Projections.property("systolic"))        //11
                        .add(Projections.property("diastolic"))
                        .add(Projections.property("protein"))
                        .add(Projections.property("glucoseInUrine"))
                        .add(Projections.property("MNR.severity"))            //15
                        /* Don't get CTG - that is not for us to show, but shown in Milou */
                    ).addOrder(Order.asc("MEAS.id"))

        def inputNodeResults = inputNodeResultsCriteria.list()
        def measurementNodeResults = measurementNodeResultsCriteria.list()

        def normalizedResults = []
        inputNodeResults.each {
            normalizedResults.add(new MeasurementResult(id: it[0], value: it[1], type: null, unit: null, severity: it[8], ignored: it[2], ignoredReason: it[3], ignoredBy: it[4], cqId: it[5], tqnId: it[6], pqnId: it[7]))
        }

        measurementNodeResults.each {
            def _value
            if (it[10] != null) {
                _value = it[10]
            } else if (it[11] != null && it[12] != null) {
                // Blood pressure
                _value = "${it[11]},${it[12]}"
            } else if (it[13] != null) {
                // Protein
                _value = "${it[13]}"
            } else if (it[14] != null) {
                // glucoseInUrine
                _value = "${it[14]}"
            } else {
                _value = "NO_VALUE"
            }

            //TQN = it[5]
            MeasurementDescription descForThisRow = result.questions.find {question -> question.templateQuestionnaireNodeId == it[5]}
            if (descForThisRow == null) {

                log.error("No find measurement from result templateQNodeId:(it[5]${it[5]}) ..1:${it[1]},2:${it[2]},3:${it[3]},4:${it[4]},5:${it[5]},6:${it[6]},7:${it[7]},8:${it[8]}")

            } else {

                if (descForThisRow.units == null) {

                    log.error("Has desc but no units:(it[5]${it[5]}) ..1:${it[1]},2:${it[2]},3:${it[3]},4:${it[4]},5:${it[5]},6:${it[6]},7:${it[7]},8:${it[8]}")

                } else {

                    descForThisRow.units.add(it[8])

                    descForThisRow.measurementTypeNames.add(it[7])
                    normalizedResults.add(new MeasurementResult(id: it[0], value: _value, type: it[7], unit: it[8], severity: it[15],exported: it[9], ignored: it[1], ignoredReason: it[2], ignoredBy: it[3], cqId: it[4], tqnId: it[5], pqnId: it[6]))
                }
            }
        }

        def resultsMap = [:]

        normalizedResults.each {it ->
            def key = new ResultKey(rowId: "cq-${it.tqnId}", colId: "cq-${it.cqId}")
            //Need to handle the possibility of multiple Measurements for each (measurement) NodeResult
            if(!resultsMap.containsKey(key)) {
                resultsMap[key] = []
            }
            resultsMap[key] << it
        }
        result.results = resultsMap
        result
    }

    private sortNodes(listNodes) {
        if (listNodes.size() < 1) {return []}

        /**
         * Bellman-Ford SSSP with all weights = 1, however we look for longest path, to include all nodes.
        **/

        //Initialize graph
        List<GraphNode> G = new ArrayList<GraphNode>()
        List<GraphEdge> E = new ArrayList<GraphEdge>()
        listNodes.each {
            def _distance
            if (listNodes[0][3] == it[0]) //All nodes have questionnaire.startNode at struct[3]
                _distance = 99999
            else
                _distance = 1

            def _text
            if (it[9]) {
                _text = it[9]
            } else {
                _text = it[1]
            }

            G.add(new GraphNode(id: it[0], distance: _distance, predecessor: null, clazz: it[7], text:  _text, questionnaireName: it[2], templateQuestionnaireNodeId: it[8]))
        }
        listNodes.each {
            GraphNode u = G.find {n -> n.id == it[0]}
            if (it[4]) {
                GraphNode v = G.find { n -> n.id == it[4]}
                GraphEdge edge = new GraphEdge(u, v, 1)
                E.add(edge)
            }
            if (it[5]) {
                GraphNode v = G.find {n -> n.id == it[5]}
                GraphEdge edge = new GraphEdge(u, v, 1)
                E.add(edge)
            }
            if (it[6]) {
                GraphNode v = G.find {n -> n.id == it[6]}
                GraphEdge edge = new GraphEdge(u, v, 1)
                E.add(edge)
            }
        }

        //Relax edges repeatedly
        G.each { GraphNode vertex ->
            E.each { GraphEdge edge ->
                if (edge.u.distance + edge.w > edge.v.distance) {
                    edge.v.distance = edge.u.distance + edge.w
                    edge.v.predecessor = edge.u
                }
            }
        }

        //Check for negative-weight cycles omitted since we use weight = 1 for all edges

        //Build and return sorted list
        def endNodeId = listNodes.find {it[7] == "org.opentele.server.model.questionnaire.EndNode"}[0]
        def endNode = G.find {it.id == endNodeId}
        def sortedNodes = []
        def nextNode = endNode
        while (nextNode != null) {
            sortedNodes << nextNode
            nextNode = nextNode.predecessor
        }

        sortedNodes.reverse(true)

        if (!(sortedNodes.size() == G.size())) {

            //The questionnaire includes multiple paths - add the nodes we did not get with the sort
            G.each { GraphNode u ->
                if (!sortedNodes.contains(u)) {
                    sortedNodes << u
                }
            }
        }
        sortedNodes
    }

	@Transactional
    def applyDBFields(object) {
        object.setCreatedBy("QService")
        object.setModifiedBy("QService")
        object.setCreatedDate(new Date())
        object.setModifiedDate(new Date())
    }

    def getNextReminders(def patient, Calendar requestDate)  {
        if (patient.state != PatientState.ACTIVE || patient.monitoringPlan == null || patient.isPaused()) {
            return []
        }

        Collection reminders = getNextRemindersForAllQuestionnaireSchedules(patient, requestDate)

        reminders.sort { it.questionnaireId }
    }

    private Collection getNextRemindersForAllQuestionnaireSchedules(patient, Calendar requestDate) {
        def questionnaireSchedules = getActiveQuestionnaireHeadersForPatient(patient)
        def reminders = questionnaireSchedules.collect { getNextRemindersForQuestionnaireSchedule(it, requestDate) }

        // Remove nulls from the list.
        reminders - null
    }

    private def getNextRemindersForQuestionnaireSchedule(QuestionnaireSchedule schedule, Calendar requestDate) {
        Calendar deadline = schedule.getNextDeadlineAfter(requestDate)
        if (deadline) {
            def remindersForQuestionnaire = nextRemindersForDeadline(deadline, schedule, requestDate)
            if (remindersForQuestionnaire) {
                return remindersForQuestionnaire
            } else {
                // If no reminders were found then also check the next deadline.
                deadline = schedule.getNextDeadlineAfter(deadline)
                if (deadline) {
                    return nextRemindersForDeadline(deadline, schedule, requestDate)
                }
            }
        }
    }

    private nextRemindersForDeadline(Calendar deadline, QuestionnaireSchedule schedule, Calendar requestDate) {
        // Only generate reminders if there has not been an upload within the grace period.
        if (!hasUploadWithinGracePeriod(schedule, deadline)) {
            Calendar reminder = deadline.clone()
            if (schedule.type == Schedule.ScheduleType.WEEKDAYS_ONCE) {
                reminder[HOUR_OF_DAY] = schedule.reminderTime.hour
                reminder[MINUTE] = schedule.reminderTime.minute
                reminder[SECOND] = 0
            } else {
                reminder.add(MINUTE, -schedule.reminderStartMinutes)
            }

            def reminderEveryMinutes = (grailsApplication.config.reminderEveryMinutes ?: 15) as int
            if (reminderEveryMinutes <= 0) {
                // Invalid server configuration. Return early to avoid entering an infinite loop.
                return null
            }

            def alarms = []
            while (reminder.before(deadline)) {
                // Only consider reminders in the future.
                if (reminder.after(requestDate)) {
                    def secondsToNextReminder = (int)((reminder.getTimeInMillis() - requestDate.getTimeInMillis()) / 1000)
                    alarms << secondsToNextReminder
                }

                reminder.add(MINUTE, reminderEveryMinutes)
            }

            if (alarms.empty) {
                return null
            } else {
                def questionnaireHeader = schedule.questionnaireHeader
                def questionnaireId = questionnaireHeader.activeQuestionnaire.patientQuestionnaires?.find{true}?.id
                def questionnaireName = questionnaireHeader.name
                return [questionnaireId: questionnaireId,
                        questionnaireName: questionnaireName,
                        alarms: alarms]
            }
        }
    }

    def checkForBlueAlarms(Patient patient, def checkFrom, def checkTo) {
        if (patient.state != PatientState.ACTIVE || patient.isPaused()) {
            return []
        }

		def idsForPatientQuestionnairesWithBlueAlarms = []

        def questionnaireSchedules = getActiveQuestionnaireHeadersForPatient(patient)
        for (it in questionnaireSchedules) {
            if (hasBlueAlarm(it, checkFrom, checkTo)) {
                it.getQuestionnaireHeader().getActiveQuestionnaire().getPatientQuestionnaires().each { patientQuestionnaire ->
                    idsForPatientQuestionnairesWithBlueAlarms << patientQuestionnaire.id
                }
            }
        }

        idsForPatientQuestionnairesWithBlueAlarms
	}

    private ArrayList<QuestionnaireSchedule> getActiveQuestionnaireHeadersForPatient(patient) {
        def questionnaireSchedules = QuestionnaireSchedule.
                findAllByMonitoringPlan(patient.monitoringPlan).
                findAll { it.questionnaireHeader.activeQuestionnaire != null }
        questionnaireSchedules
    }

    private boolean hasBlueAlarm(QuestionnaireSchedule questionnaireSchedule, Calendar checkFrom, Calendar checkTo) {
        if (questionnaireSchedule.hasTimeSchedule()) {
            // Find the latest deadline.
            Calendar latestDeadline = questionnaireSchedule.getLatestDeadlineBefore(checkTo)

            // If there hasn't been a deadline yet then there is no blue alarm.
            if (latestDeadline == null) {
                return false
            }

            // If the latest deadline before the previous check time then it has already been checked.
            if (latestDeadline.before(checkFrom)) {
                return false
            }

            return !hasUploadWithinGracePeriod(questionnaireSchedule, latestDeadline)
        }
        return false
    }

    private boolean hasUploadWithinGracePeriod(QuestionnaireSchedule questionnaireSchedule, Calendar deadline) {
        Calendar windowStart = deadline.clone()

        def scheduleWindow = ScheduleWindow.findByScheduleType(questionnaireSchedule.type)
        if (scheduleWindow == null) {
            return false
        }

        windowStart.add(MINUTE, -scheduleWindow.windowSizeMinutes)

        // Find the previous deadline and adjust the window start if necessary.
        Calendar previousDeadline = questionnaireSchedule.getLatestDeadlineBefore(deadline)
        if (previousDeadline != null && previousDeadline.after(windowStart)) {
            windowStart = previousDeadline
        }

        def patient = questionnaireSchedule.monitoringPlan.patient
        def questionnaireHeader = questionnaireSchedule.questionnaireHeader
        def startTime = windowStart.getTime()
        def questionnairesUploadedSinceLastCheck = CompletedQuestionnaire.findAllByPatientAndQuestionnaireHeaderAndReceivedDateGreaterThanEquals(patient, questionnaireHeader, startTime)
        return questionnairesUploadedSinceLastCheck.any()
    }

    //NOT transactional
    def list(String cpr) {
        def results = []
        if (!cpr) {

            results << ["failure"]
            results << ["Required CPR parameter empty"]
            return results

        } else {

            Patient p = Patient.findByCpr(cpr)

            if (!p) {
                results << ["failure"]
                results << ["Patient with cpr: ${cpr} unknown."]

                return results
            } else {

                p.refresh()
                MonitoringPlan plan = p.monitoringPlan

                QuestionnaireListResult res = new QuestionnaireListResult()
                res.questionnaires = []

                if (plan) {

                    plan.refresh()

                    plan.questionnaireSchedules.each() { questionnaireSchedule ->
                        if (questionnaireSchedule.getQuestionnaireHeader().getActiveQuestionnaire()) {
                            QuestionnaireListElement elm = new QuestionnaireListElement()
                            elm.id = questionnaireSchedule.getQuestionnaireHeader().getActiveQuestionnaire().patientQuestionnaires?.find {true}?.id
                            elm.name = questionnaireSchedule.getQuestionnaireHeader().name
                            elm.version = questionnaireSchedule.getQuestionnaireHeader().getActiveQuestionnaire().revision

                            res.questionnaires << elm
                        }
                    }

                    res.questionnaires.sort { a, b ->
                        String aName = a.name + a.version
                        String bName = b.name + b.version
                        aName.compareToIgnoreCase(bName)
                    }
                }
                return res
            }
        }
    }

    def iconAndTooltip(g, Patient patient) {
        def unacknowledgedQuestionnaires = CompletedQuestionnaire.findAllByPatientAndAcknowledgedDate(patient, null, [sort: 'uploadDate', order: 'desc'])

        iconAndTooltip(g, patient, unacknowledgedQuestionnaires)
    }

    def iconAndTooltip(g, Patient patient, questionnaires) {
        def questionnaireOfWorstSeverity = questionnaireOfWorstSeverity(questionnaires)
        def severity = severity(patient, questionnaireOfWorstSeverity)

        if (severity == Severity.BLUE) {
            StringBuilder sb = new StringBuilder()
            sb.append('Følgende spørgeskemaer er ikke besvaret til tiden:<br/>')
            patient.blueAlarmQuestionnaireIDs.each {
                sb.append(PatientQuestionnaire.get(it).name).append("<br/>")
            }
            [severity.icon(), sb.toString()]
        } else if (severity != Severity.NONE) {
            [severity.icon(), "Fra skema: ${questionnaireOfWorstSeverity.patientQuestionnaire.name} (${g.formatDate(date: questionnaireOfWorstSeverity.uploadDate)})"]
        } else {
            [Severity.NONE.icon(), "Ingen nye besvarelser fra denne patient"]
        }
    }

    def severity(Patient patient, questionnaireOfWorstSeverity=null) {
        if (questionnaireOfWorstSeverity == null) {
            def unacknowledgedQuestionnaires = CompletedQuestionnaire.findAllByPatientAndAcknowledgedDate(patient, null, [sort: 'uploadDate', order: 'desc'])
            questionnaireOfWorstSeverity = this.questionnaireOfWorstSeverity(unacknowledgedQuestionnaires)
        }

        def severity = (questionnaireOfWorstSeverity == null) ? Severity.NONE : questionnaireOfWorstSeverity.severity
        if (severity < Severity.BLUE && !patient.blueAlarmQuestionnaireIDs.empty) {
            return Severity.BLUE
        }

        severity
    }

    //NOT Transactional
    def worstSeverityOfUnacknowledgedQuestionnaires(Patient patient, questionnaires) {
        Severity severity = Severity.NONE
        questionnaires.each { questionnaire ->
            if (questionnaire.severity && questionnaire.severity > severity) {
                severity = questionnaire.severity
            }
        }

        if (severity < Severity.BLUE && !patient.blueAlarmQuestionnaireIDs.empty) {
            return Severity.BLUE
        }

        severity
    }


     def getUnusedQuestionnaireHeadersForMonitoringPlan(MonitoringPlan mp) {
        // List skemaer fra monitoreringsplanen
        def usedQuestionnaireHeaders = [:]

        def schedules = mp.questionnaireSchedules
        schedules.each() {
            it.refresh()
            QuestionnaireHeader qh = it.questionnaireHeader
            qh.refresh()
            usedQuestionnaireHeaders.put(qh.id, qh)
        }

        // hent alle skemaer
        def activeQuestionnaireHeadersList = QuestionnaireHeader.findAll()

        def unused = []

        // Fjern de anvendte
        activeQuestionnaireHeadersList.each { qh ->
            if (!usedQuestionnaireHeaders.get(qh.id)) {
                unused.add(qh)
            }
        }
        unused.sort { it.toString().toLowerCase() }
    }


    private  CompletedQuestionnaire questionnaireOfWorstSeverity(List<CompletedQuestionnaire> questionnaires) {
        questionnaires.max { it.severity }
    }

    @Transactional()
    def deleteQuestionnaire(Questionnaire questionnaire) {
        if (questionnaire.questionnaireHeader.draftQuestionnaire != questionnaire) {
            throw new DeleteQuestionnaireException("cannot delete questionnaire that is not marked as draft")
        }
        if (questionnaire.patientQuestionnaires) {
            throw new DeleteQuestionnaireException("Cannot delete questionnaire which has a patient questionnaire")
        }
        questionnaireNodeService.deleteQuestionnaireNodes(questionnaire, true)

        deleteQuestionnaire2MeterReferences(questionnaire)

        def questionnaireHeader = questionnaire.questionnaireHeader

        questionnaireHeader.draftQuestionnaire = null
        questionnaireHeader.removeFromQuestionnaires(questionnaire)
        questionnaireHeader.save(flush: true, failOnError: true)
        // TODO: This should be questionnaire.delete() but that continously fails
        Questionnaire.executeUpdate("delete from Questionnaire q where q=?",[questionnaire])
    }


    private deleteQuestionnaire2MeterReferences(Questionnaire questionnaire) {
        def questionnaire2MeterTypes = Questionnaire2MeterType.findAllByQuestionnaire(questionnaire)
        questionnaire2MeterTypes.each {
            questionnaire.removeFromQuestionnaire2MeterTypes(it)
            questionnaire.save(failOnError: true)
            it.delete()
        }
    }

    def findQuestionnaireGroup2HeadersAndOverlapWithExistingQuestionnaireSchedules(QuestionnaireGroup questionnaireGroup, MonitoringPlan monitoringPlan) {
        questionnaireGroup.questionnaireGroup2Header.collect { questionnaireGroup2Header ->
            def candidate = [
                    questionnaireGroup2Header: questionnaireGroup2Header
            ]
            def questionnaireSchedule = findExistingQuestionnaireSchedule(monitoringPlan.questionnaireSchedules, questionnaireGroup2Header.questionnaireHeader)
            if (questionnaireSchedule) {
                candidate.questionnaireSchedule = questionnaireSchedule
                candidate.questionnaireScheduleOverlap = !isSchedulesEqual(questionnaireGroup2Header.schedule, questionnaireSchedule)
            }
            return candidate
        }
    }

    def addOrUpdateQuestionnairesOnMonitoringPlan(AddQuestionnaireGroup2MonitoringPlanCommand command) {
        def monitoringPlan = command.monitoringPlan
        def addToMonitoringPlan = findAddedQuestionnaireGroup2HeaderCommands(command)
        addToMonitoringPlan.each { questionnaireGroup2HeaderCmd ->
            def questionnaireInMonitoringPlan = findQuestionnaireInMonitoringPlan(questionnaireGroup2HeaderCmd, monitoringPlan)
            if(questionnaireInMonitoringPlan) {
                if(updateQuestionnaireSchedule(questionnaireGroup2HeaderCmd, questionnaireInMonitoringPlan)) {
                    command.updatedQuestionnaires << questionnaireInMonitoringPlan
                }
            } else {
                command.addedQuestionnaires << addQuestionnaireToMonitoringPlan(questionnaireGroup2HeaderCmd, monitoringPlan)
            }
        }
        monitoringPlan.save(flush: true)
    }


    private addQuestionnaireToMonitoringPlan(QuestionnaireGroup2HeaderCommand questionnaireGroup2HeaderCommand, MonitoringPlan monitoringPlan) {
        def questionnaireGroup2Header = questionnaireGroup2HeaderCommand.questionnaireGroup2Header
        def questionnaireSchedule = new QuestionnaireSchedule(monitoringPlan: monitoringPlan, questionnaireHeader: questionnaireGroup2HeaderCommand.questionnaireGroup2Header.questionnaireHeader)
        questionnaireSchedule.save(failOnError: true)
        assignStandardScheduleToQuestionnaireSchedule(questionnaireGroup2Header.schedule, questionnaireSchedule)
        monitoringPlan.addToQuestionnaireSchedules(questionnaireSchedule)
        monitoringPlan.save(failOnError: true)
        return questionnaireSchedule
    }


    private updateQuestionnaireSchedule(QuestionnaireGroup2HeaderCommand questionnaireGroup2HeaderCommand, QuestionnaireSchedule questionnaireSchedule) {
        def questionnaireHeaderSchedule = questionnaireGroup2HeaderCommand.questionnaireGroup2Header.schedule
        if(questionnaireGroup2HeaderCommand.useStandard && !isSchedulesEqual(questionnaireHeaderSchedule, questionnaireSchedule)) {
            assignStandardScheduleToQuestionnaireSchedule(questionnaireHeaderSchedule, questionnaireSchedule)
            questionnaireSchedule.save(failOnError: true)
            return true
        }
    }

    private assignStandardScheduleToQuestionnaireSchedule(StandardSchedule schedule, QuestionnaireSchedule questionnaireSchedule) {
        if(schedule) {
            questionnaireSchedule.type = schedule.type
            questionnaireSchedule.weekdays = schedule.weekdays
            questionnaireSchedule.daysInMonth = schedule.daysInMonth
            questionnaireSchedule.timesOfDay = schedule.timesOfDay
            questionnaireSchedule.startingDate = schedule.startingDate
            questionnaireSchedule.dayInterval = schedule.dayInterval
            questionnaireSchedule.specificDate = schedule.specificDate
            questionnaireSchedule.reminderStartMinutes = schedule.reminderStartMinutes
            questionnaireSchedule.introPeriodWeeks = schedule.introPeriodWeeks
            questionnaireSchedule.reminderTime = schedule.reminderTime
            questionnaireSchedule.blueAlarmTime = schedule.blueAlarmTime
            questionnaireSchedule.weekdaysIntroPeriod = schedule.weekdaysIntroPeriod
            questionnaireSchedule.weekdaysSecondPeriod = schedule.weekdaysSecondPeriod
        }
    }


    List<QuestionnaireGroup2HeaderCommand> findAddedQuestionnaireGroup2HeaderCommands(AddQuestionnaireGroup2MonitoringPlanCommand addQuestionnaireGroup2MonitoringPlan) {
        addQuestionnaireGroup2MonitoringPlan.questionnaireGroup2Headers*.value.findAll { QuestionnaireGroup2HeaderCommand cmd ->
            cmd.questionnaireGroup2Header && cmd.addQuestionnaire
        } as List<QuestionnaireGroup2HeaderCommand>
    }

    private findQuestionnaireInMonitoringPlan(QuestionnaireGroup2HeaderCommand questionnaireGroup2HeaderCommand, MonitoringPlan monitoringPlan) {
        monitoringPlan.questionnaireSchedules.find {
            it.questionnaireHeader.id == questionnaireGroup2HeaderCommand.questionnaireGroup2Header.questionnaireHeader.id
        }
    }

    private isSchedulesEqual(StandardSchedule standardSchedule, QuestionnaireSchedule questionnaireSchedule) {
        if (standardSchedule) {
            if (isTypeEquals(standardSchedule, questionnaireSchedule)) {
                switch (standardSchedule.type) {
                    case Schedule.ScheduleType.UNSCHEDULED:
                        return true
                    case Schedule.ScheduleType.WEEKDAYS:
                        return isWeekdaysEquals(standardSchedule, questionnaireSchedule) && isTimesOfDayEquals(standardSchedule, questionnaireSchedule)
                    case Schedule.ScheduleType.MONTHLY:
                        return isDaysInMonthEquals(standardSchedule, questionnaireSchedule) && isTimesOfDayEquals(standardSchedule, questionnaireSchedule)
                    case Schedule.ScheduleType.EVERY_NTH_DAY:
                        return isEveryNthDayEquals(standardSchedule, questionnaireSchedule) && isTimesOfDayEquals(standardSchedule, questionnaireSchedule)
                }

            } else {
                return false
            }
        } else {
            return true
        }
    }

    private isEveryNthDayEquals(StandardSchedule standardSchedule, QuestionnaireSchedule questionnaireSchedule) {
        standardSchedule.dayInterval == questionnaireSchedule.dayInterval
    }

    private isDaysInMonthEquals(StandardSchedule standardSchedule, QuestionnaireSchedule questionnaireSchedule) {
        standardSchedule.daysInMonth.sort() == questionnaireSchedule.daysInMonth.sort()
    }

    private isTimesOfDayEquals(StandardSchedule standardSchedule, QuestionnaireSchedule questionnaireSchedule) {
        standardSchedule.timesOfDay.sort() == questionnaireSchedule.timesOfDay.sort()
    }

    private isWeekdaysEquals(StandardSchedule standardSchedule, QuestionnaireSchedule questionnaireSchedule) {
        standardSchedule.weekdays.sort() ==  questionnaireSchedule.weekdays.sort()
    }

    private isTypeEquals(StandardSchedule standardSchedule, QuestionnaireSchedule questionnaireSchedule) {
        standardSchedule.type == questionnaireSchedule.type
    }

    private findExistingQuestionnaireSchedule(Collection<QuestionnaireSchedule> questionnaireSchedules, QuestionnaireHeader questionnaireHeader) {
        questionnaireSchedules.find { it.questionnaireHeader.id == questionnaireHeader.id }
    }
}

public class ResultTableViewModel {
    List<OverviewColumnHeader> columnHeaders = []
    List<MeasurementDescription> questions = []
    Map<ResultKey, MeasurementResult> results = [:]
}

public class MeasurementDescription {
    def type
    def text
    def questionnaireName
    def templateQuestionnaireNodeId

    Set<Unit> units
    Set<MeasurementTypeName> measurementTypeNames

    def orderedUnits() {

        if (!units) {
            return Collections.EMPTY_LIST
        }
        if (units.size() == 2 && units.containsAll([Unit.BPM, Unit.MMHG])) {
            return [Unit.MMHG, Unit.BPM]
        }
        if (units.size() == 2 && units.containsAll([Unit.BPM, Unit.PERCENTAGE])) {
            return [Unit.PERCENTAGE, Unit.BPM]
        }
        return units.toList()
    }
}

public class OverviewColumnHeader {
    def type, id, uploadDate, severity, acknowledgedBy, acknowledgedDate, acknowledgedNote, _questionnaireIgnored, questionnaireIgnoredReason, questionnareIgnoredBy
}

public enum MeasurementParentType {
    QUESTIONNAIRE,
    CONFERENCE;
}

public class MeasurementResult {
    def value
    def type
    def unit
    def severity

    def exported // CTG

    def ignored
    def ignoredReason
    def ignoredBy

    def id //NodeResult
    def cqId //CompletedQuestionnaire
    def tqnId //TemplateQuestionnaireNode
    def pqnId //PatientQuestionnaireNode
}

class GraphNode {
    def id
    def distance
    def predecessor

    //Needed by filter
    def clazz

    //Needed by view
    def text
    def questionnaireName
    def templateQuestionnaireNodeId
}

class GraphEdge {
    GraphNode u
    GraphNode v
    int w

    public GraphEdge(def u, def v, def w) {
        this.u = u
        this.v = v
        this.w = w
    }
}

class ResultKey {
    def type
    def rowId
    def colId

    @Override
    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        ResultKey resultKey = (ResultKey) o

        if (colId != resultKey.colId) return false
        if (rowId != resultKey.rowId) return false

        return true
    }
    @Override
    int hashCode() {
        int result
        result = rowId.hashCode()
        result = 31 * result + colId.hashCode()
        return result
    }
}
