package org.opentele.server.model.questionnaire

import grails.test.MockUtils
import grails.test.mixin.support.GrailsUnitTestMixin
import org.codehaus.groovy.grails.web.json.JSONObject
import org.opentele.util.CommandCanValidateSpecification
import spock.lang.Unroll

@Mixin(GrailsUnitTestMixin)
class QuestionnaireEditorCommandSpec extends CommandCanValidateSpecification {
    def setup() {
        MockUtils.prepareForConstraintsTests(QuestionnaireEditorCommand, [:])
    }

    def "when creating a graph, there needs to be at least a start and an end node and two or more connections"() {
        given:
        def command = createCommandObject("""{"nodes":
                        {"start1":{"type":"start","position":{"top":87,"left":275}},
                        "measurement2":{"headline":"overskrift", "type":"measurement","measurementType":"BLOOD_PRESSURE_PULSE","isManual":false,"isSimulated":false,"position":{"top":87,"left":275}},
                        "text3":{"type":"text","text":"Find din måler frem","position":{"top":87,"left":275}},
                        "end4":{"type":"end","position":{"top":87,"left":275}}},
                    "connections":[{"source":"start1","target":"text3"},{"source":"text3","target":"measurement2"},{"source":"measurement2","target":"end4"}]}
                """)

        expect:
        command.nodes
        command.connections

        and:
        command.validate()
    }

    @Unroll
    def "when creating a graph, there needs to be at least a start and an end node otherwise we should get an error"() {
        given:
        def command = createCommandObject("""{"nodes":
                        {"start1":{"type":"$startType","position":{"top":87,"left":275}},
                        "text3":{"type":"$extraType","text":"Find din måler frem","position":{"top":87,"left":275}},
                        "end4":{"type":"$endType","position":{"top":87,"left":275}}},
                    "connections":[{"source":"start1","target":"text3"},{"source":"text3","target":"measurement2"},{"source":"measurement2","target":"end4"}]}
                """)

        expect:
        command.nodes
        command.connections

        when:
        command.validate()

        then:
        command.errors['nodes'] == expectedError

        where:
        startType | endType | extraType | expectedError
        'foo'     | 'end'   | 'text'    | 'start.node.count.wrong'
        'start'   | 'end'   | 'start'   | 'start.node.count.wrong'
        'start'   | 'bar'   | 'text'    | 'end.node.count.wrong'
        'start'   | 'end'   | 'end'     | 'end.node.count.wrong'
        'foo'     | 'bar'   | 'text'    | 'start.and.end.node.count.wrong'
    }

    def "when creating a graph, there needs to be at least a start and an end node and one other node otherwise we should get an error"() {
        given:
        def command = createCommandObject("""{"nodes":
                        {"start1":{"type":"start","position":{"top":87,"left":275}},
                        "end4":{"type":"end","position":{"top":87,"left":275}}},
                    "connections":[{"source":"start1","target":"text3"},{"source":"text3","target":"measurement2"},{"source":"measurement2","target":"end4"}]}
                """)

        expect:
        command.nodes
        command.connections

        when:
        command.validate()

        then:
        command.errors['nodes'] == "missing.other.nodes"
    }

    def "when creating a graph, there needs to be at least two connections"() {
        given:
        def command = createCommandObject("""{"nodes":
                        {
                            "start1":{"type":"start","position":{"top":87,"left":275}},
                            "text2":{"type":"text","position":{"top":87,"left":275}},
                            "end4":{"type":"end","position":{"top":87,"left":275}}
                        },
                    "connections":[{"source":"start1","target":"text3"}]}
                """)

        expect:
        command.nodes
        command.connections

        when:
        command.validate()

        then:
        command.errors['connections'] == "too.few.connections"
    }

    private createCommandObject(String jsonString) {
        def json = new JSONObject(jsonString)

        def command = new QuestionnaireEditorCommand(json)
        command.questionnaireHeader = new QuestionnaireHeader()
        command.title = "Title"
        command.nodes = json.nodes
        command
    }
}
