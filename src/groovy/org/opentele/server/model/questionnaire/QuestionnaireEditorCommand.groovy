package org.opentele.server.model.questionnaire

import grails.validation.Validateable
import groovy.transform.ToString
import org.apache.commons.collections.ListUtils
import org.apache.commons.collections.MapUtils
import org.codehaus.groovy.grails.web.json.JSONObject
import org.opentele.server.model.ScheduleCommand

@ToString(includeFields = true)
@Validateable
class QuestionnaireEditorCommand extends ScheduleCommand {
    QuestionnaireHeader questionnaireHeader
    Long baseId
    String title
    Map nodes  = MapUtils.lazyMap([:], lazyMapFactory)
    List connections = ListUtils.lazyList([], { [:] } as org.apache.commons.collections.Factory)
    Questionnaire getQuestionnaire() {
        return questionnaireHeader?.draftQuestionnaire ?: questionnaireHeader?.questionnaires?.find { it.id == baseId } ?: new Questionnaire()
    }

    static constraints = {
        questionnaireHeader nullable: false
        title blank: false
        nodes nullable: true, validator: { value ->
            def startNodesCountIsOne = value?.findAll{it.value.type == "start"}?.size() == 1
            def endNodeCountIsOne = value?.findAll{it.value.type == "end"}?.size() == 1
            if(!startNodesCountIsOne && endNodeCountIsOne) {
                return "start.node.count.wrong"
            }
            if(startNodesCountIsOne && !endNodeCountIsOne) {
                return "end.node.count.wrong"
            }
            if(!startNodesCountIsOne && !endNodeCountIsOne) {
                return "start.and.end.node.count.wrong"
            }
            if(value.size() == 2) {
                return "missing.other.nodes"
            }
        }
        connections nullable: true, validator: { value ->
            // TODO: The validation here could actually validate the whole graph
            if(value?.size() < 2) {
                return "too.few.connections"
            }
        }
    }

    String asJSON() {
        def json = new JSONObject([
                title: title,
                questionnaireHeaderId: questionnaireHeader.id,
                nodes: nodes,
                connections: connections
        ])


        json.toString()
    }

    private getLazyMapFactory() {
        return { MapUtils.lazyMap([:], lazyMapFactory) } as org.apache.commons.collections.Factory
    }


}
