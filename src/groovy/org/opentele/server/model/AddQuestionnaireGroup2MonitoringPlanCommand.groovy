package org.opentele.server.model

import grails.validation.Validateable
import groovy.transform.ToString
import groovy.util.logging.Log4j
import org.apache.commons.collections.Transformer
import org.apache.commons.collections.map.LazyMap
import org.opentele.server.model.questionnaire.QuestionnaireGroup
import org.opentele.server.model.questionnaire.QuestionnaireGroup2QuestionnaireHeader

@Log4j
@ToString(includeNames = true)
@Validateable
class AddQuestionnaireGroup2MonitoringPlanCommand {
    MonitoringPlan monitoringPlan
    QuestionnaireGroup questionnaireGroup
    List<Map> newQuestionnaires
    Map questionnaireGroup2Headers = LazyMap.decorate([:],new Transformer() {
        def transform(id) {
            new QuestionnaireGroup2HeaderCommand(questionnaireGroup2Header: QuestionnaireGroup2QuestionnaireHeader.get(id))
        }
    })

    List<QuestionnaireGroup> getQuestionnaireGroups() {
        QuestionnaireGroup.list()
    }
    List addedQuestionnaires = []
    List updatedQuestionnaires = []

    static constraints = {
        monitoringPlan nullable: false
        questionnaireGroup nullable: false
    }
}

@Validateable
@ToString(includeNames = true)
class QuestionnaireGroup2HeaderCommand {
    QuestionnaireGroup2QuestionnaireHeader questionnaireGroup2Header
    boolean useStandard
    boolean addQuestionnaire
}
