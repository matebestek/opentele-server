package org.opentele.server.questionnaire
import org.opentele.server.model.MeterType
import org.opentele.server.model.questionnaire.*
import org.opentele.server.model.types.DataType
import org.opentele.server.model.types.MeterTypeName
import org.opentele.server.model.types.Severity

class EditorQuestionnaireBuilder {
    void buildQuestionnaire(QuestionnaireEditorCommand command, Questionnaire questionnaire) {

        questionnaire.creationDate = new Date()
        questionnaire.editorState  = command.asJSON()

        buildStandardSchedule(command, questionnaire)

        def nodes = createNodes(command.nodes)
        saveNodes(nodes)

        createConnections(command.connections, questionnaire, nodes)

        nodes.values().each {
            it.save(failOnError: true)
        }
        nodes.values().each {
            questionnaire.addToNodes(it)
        }

    }

    private void buildStandardSchedule(QuestionnaireEditorCommand command, Questionnaire questionnaire)
    {
        def standardSchedule = questionnaire.standardSchedule ?: new StandardSchedule()
        command.bindScheduleData(standardSchedule)
        questionnaire.standardSchedule = standardSchedule
    }

    private void createConnections(List connections, questionnaire, Map nodes) {
        connections.each {
            if (it.source.startsWith("start")) {
                questionnaire.startNode = nodes[it.target]
            } else {
                if (it.source.startsWith("input")) {
                    InputNode source = nodes[it.source]
                    QuestionnaireNode target = getTargetNode(it.target, nodes)
                    if (it.choiceValue == "true") {
                        source.defaultNext = target
                        source.defaultSeverity = getSeverity(it)
                    } else if(it.choiceValue == "false") {
                        source.alternativeNext = target
                        source.alternativeSeverity = getSeverity(it)
                    } else {
                        source.defaultNext = target
                        source.defaultSeverity = getSeverity(it)
                    }
                } else if (it.source.startsWith("measurement")) {
                    MeasurementNode source = nodes[it.source]
                    QuestionnaireNode target = getTargetNode(it.target, nodes)

                    if(it.measurementSkipped == "true") {
                        source.nextFail = target
                        source.nextFailSeverity = getSeverity(it)
                    } else {
                        source.defaultSeverity = getSeverity(it)
                        source.defaultNext = target
                    }

                } else {
                    QuestionnaireNode source = nodes[it.source]
                    QuestionnaireNode target = getTargetNode(it.target, nodes)

                    source.defaultNext = target

                }
            }
        }
    }

    private Severity getSeverity(connection) {
        if (connection.severity == null || connection.severity.isEmpty()) {
            return null
        } else {
            return connection.severity
        }
    }

    private QuestionnaireNode getTargetNode(targetId, nodes) {
        if (targetId.startsWith("end")) {
            return nodes["end"]
        } else {
            return nodes[targetId]
        }
    }

    private void saveNodes(LinkedHashMap nodes) {
        //GORM does not handle object graphs very well. So before starting to create relations between graphs they must be
        //save and thereby given a id. But defaultNext and nextFailed are required, so in order to save them they are temporarily set to
        //point to the endNode
        EndNode endNode = nodes["end"]
        endNode.save(failOnError: true)

        nodes.values().each {
            if (it instanceof MeasurementNode) {
                it.nextFail = endNode
            }
            it.defaultNext = endNode
            it.save(failOnError: true)
        }

        endNode.defaultNext = null
        endNode.save(failOnError: true)
    }

    private Map createNodes(Map jsonNodes) {
        def nodes = [:]
        jsonNodes.each {
            switch (it.value.type) {
                case 'measurement':
                    nodes[it.key] = createMeasurementNode(it)
                    break;
                case 'text':
                    nodes[it.key] = createTextNode(it)
                    break;
                case 'start':
                    //No nodetype: "Start"
                    break;
                case 'end':
                    nodes["end"] = createEndNode()
                    break;
                case 'input':
                    nodes[it.key] = createInputNode(it)
                    break;
                case 'delay':
                    nodes[it.key] = createDelayNode(it)
                    break;
                default:
                    throw new IllegalArgumentException("Nodetype ${it.value.type} not supported")
                    break;
            }
        }
        nodes
    }

    private def createEndNode() {
        def endNode = new EndNode()

        return endNode
    }

    private def createTextNode(def node) {
        def textNode = new TextNode()
        textNode.headline = node.value.headline
        textNode.text = node.value.text
        return textNode
    }

    private def createInputNode(def node) {
        def inputNode = new InputNode()
        inputNode.text = node.value.question
        inputNode.shortText = node.value.shortText
        inputNode.inputType = DataType.valueOf(node.value.dataType)
        return inputNode
    }

    private def createDelayNode(def node) {
        def delayNode = new DelayNode()
        delayNode.text = node.value.text
        delayNode.shortText = node.value.shortText
        delayNode.countTime = Integer.parseInt(node.value.countTime);
        if (node.value.countType == 'Op') {
            delayNode.countUp = true;
        } else {
            delayNode.countUp = false;
        }

        return delayNode
    }

    private def createMeasurementNode(def node) {
        def measurementNode = new MeasurementNode()

        switch (node.value.measurementForm) {
            case "simulated":
                measurementNode.simulate = true
                break;
            case "manual":
                measurementNode.mapToInputFields = true
                break;
            case "automatic":
                //Automatic is the default and has no effect on model attributes
                break;
        }

        measurementNode.meterType = MeterType.findByName(MeterTypeName.valueOf(node.value.measurementType))
        measurementNode.text = node.value.headline
        measurementNode.shortText = node.value.shortText

        return measurementNode
    }
}
