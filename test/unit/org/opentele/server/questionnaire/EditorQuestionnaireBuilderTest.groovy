package org.opentele.server.questionnaire

import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import org.codehaus.groovy.grails.web.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.opentele.server.model.MeterType
import org.opentele.server.model.Schedule
import org.opentele.server.model.questionnaire.*
import org.opentele.server.model.types.Severity

@TestMixin(DomainClassUnitTestMixin)
@Mock([Questionnaire, QuestionnaireNode, EndNode, MeasurementNode, TextNode, InputNode, MeterType])
@Build(QuestionnaireHeader)
class EditorQuestionnaireBuilderTest {
    EditorQuestionnaireBuilder builder

    @Before
    void setup() {
       new MeterType([name: "BLOOD_PRESSURE_PULSE"]).save(validate: false)
        builder = new EditorQuestionnaireBuilder()
    }

    @Test
    void canCreateSimpleQuestionnaire() {
        def command = createQuestionnaireEditorCommandWithStandardSchedule("""
                    {"title":"Test skema",
                    "nodes":
                        {"start1":{"type":"start","position":{"top":87,"left":275}},
                        "measurement2":{"headline":"overskrift", "type":"measurement","measurementType":"BLOOD_PRESSURE_PULSE","isManual":false,"isSimulated":false,"position":{"top":87,"left":275}},
                        "text3":{"type":"text","text":"Find din måler frem","position":{"top":87,"left":275}},
                        "end4":{"type":"end","position":{"top":87,"left":275}}},
                    "connections":[{"source":"start1","target":"text3"},{"source":"text3","target":"measurement2"},{"source":"measurement2","target":"end4"}]}
                    """)

        Questionnaire questionnaire = new Questionnaire();
        builder.buildQuestionnaire(command, questionnaire)

        TextNode textNode = questionnaire.nodes.find {it instanceof TextNode }
        MeasurementNode measurementNode = questionnaire.nodes.find {it instanceof MeasurementNode }
        EndNode endNode = questionnaire.nodes.find {it instanceof EndNode }
        def standardSchedule = questionnaire.standardSchedule

        assert questionnaire.startNode == textNode
        assert textNode.defaultNext == measurementNode
        assert measurementNode.nextFail == endNode
        assert measurementNode.text == command.nodes.measurement2.headline

        assertNotNull standardSchedule
        assertEquals Schedule.ScheduleType.UNSCHEDULED, standardSchedule.type
        assertEquals 2, standardSchedule.dayInterval
        assertEquals 30, standardSchedule.reminderStartMinutes
        assert [new Schedule.TimeOfDay(hour: 12, minute: 34)] == standardSchedule.timesOfDay
        assert [1] == standardSchedule.daysInMonth
        assertEquals new Date("2014/1/1"), standardSchedule.startingDate
    }

    private createQuestionnaireEditorCommandWithStandardSchedule(jsonString) {
        def questionnaireHeader = QuestionnaireHeader.build()
        def command = new QuestionnaireEditorCommand(type: Schedule.ScheduleType.UNSCHEDULED, timesOfDay: [new Schedule.TimeOfDay(hour: 12, minute: 34)], daysInMonth: [1], startingDate: Date.parse("dd/MM/yyyy", "01/01/2014"), questionnaireHeader: questionnaireHeader, title: "Title")
        def json =  new JSONObject(jsonString)
        command.nodes = json.nodes
        command.connections = json.connections

        command

    }

    @Test
    void willSetSeverties() {
        def command = createQuestionnaireEditorCommandWithStandardSchedule("""
                    {
                      "nodes": {
                        "input2": {
                          "id": "input2",
                          "position": {
                            "top": 174,
                            "left": 189
                          },
                          "dataType": "BOOLEAN",
                          "shortText": "dfg",
                          "question": "dfg",
                          "type": "input"
                        },
                        "end3": {
                          "id": "end3",
                          "position": {
                            "top": 371,
                            "left": 177
                          },
                          "type": "end"
                        },
                        "start1": {
                          "id": "start1",
                          "position": {
                            "top": 19,
                            "left": 19
                          },
                          "type": "start"
                        }
                      },
                      "connections": [
                        {
                          "source": "start1",
                          "target": "input2",
                          "severity": ""
                        },
                        {
                          "source": "input2",
                          "target": "end3",
                          "choiceValue": "true",
                          "severity": "YELLOW"
                        },
                        {
                          "source": "input2",
                          "target": "end3",
                          "choiceValue": "false",
                          "severity": "RED"
                        }
                      ]
                    }
        """)


        Questionnaire questionnaire = new Questionnaire()
        builder.buildQuestionnaire(command, questionnaire)

        InputNode inputNode = questionnaire.nodes.find {it instanceof InputNode }
        assert inputNode.defaultSeverity == Severity.YELLOW
        assert inputNode.alternativeSeverity == Severity.RED
    }

    @Test
    void canHandleInputNodesWithBooleanInputType() {
        def command = createQuestionnaireEditorCommandWithStandardSchedule("""
        {
          "nodes": {
            "end7": {
              "position": {
                "left": 249,
                "top": 477
              },
              "id": "end7",
              "type": "end"
            },
            "input6": {
              "position": {
                "left": 269,
                "top": 161
              },
              "id": "input6",
              "dataType": "BOOLEAN",
              "shortText": "sdf",
              "question": "sdf",
              "type": "input"
            },
            "text4": {
              "position": {
                "left": 91,
                "top": 276
              },
              "id": "text4",
              "text": "TRUE",
              "type": "text"
            },
            "start1": {
              "position": {
                "left": 299,
                "top": 19
              },
              "id": "start1",
              "type": "start"
            },
            "text5": {
              "position": {
                "left": 426,
                "top": 271
              },
              "id": "text5",
              "text": "FALSE",
              "type": "text"
            }
          },
          "connections": [
            {
              "source": "start1",
              "target": "input6",
              "severity": ""


            },
            {
              "source": "input6",
              "target": "text4",
              "choiceValue": "true",
                    "severity": ""

            },
            {
              "source": "input6",
              "target": "text5",
              "choiceValue": "false",
                    "severity": ""
            },
            {
              "source": "text4",
              "target": "end7",
                    "severity": ""
            },
            {
              "source": "text5",
              "target": "end7",
                    "severity": ""
            }
          ]
        }
        """)
        Questionnaire questionnaire = new Questionnaire();
        builder.buildQuestionnaire(command, questionnaire)

        InputNode inputNode = questionnaire.nodes.find {it instanceof InputNode }

        TextNode defaultNext = inputNode.defaultNext as TextNode
        TextNode alternativeNext = inputNode.alternativeNext as TextNode

        assert defaultNext.text == "TRUE";
        assert alternativeNext.text == "FALSE";
    }
}
