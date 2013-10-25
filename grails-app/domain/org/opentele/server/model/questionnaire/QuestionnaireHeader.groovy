package org.opentele.server.model.questionnaire

import org.opentele.server.model.AbstractObject
import org.opentele.server.model.types.Severity

class QuestionnaireHeader extends AbstractObject {

    Questionnaire activeQuestionnaire
    Questionnaire draftQuestionnaire
    static hasMany = [questionnaires: Questionnaire]

    String name

    // If a questionnaire requires manual inspection then it has default severity orange.
    boolean requiresManualInspection

    String toString() {
        name
    }


    static constraints = {
        name(nullable: false, blank: false, unique: true)
        requiresManualInspection(nullable: false)
        activeQuestionnaire(nullable: true, validator: { value, object ->
            if (!value) return
            if (!(value.id in object.questionnaires*.id)) {
                return "activeQuestionnaire.not.in.questionnaires"
            }
        })
        draftQuestionnaire(nullable: true, validator: { value, object ->
            if (!value) return
            if (value.id == object.activeQuestionnaire?.id) {
                return "draftQuestionnaire.cannot.be.equal.to.activeQuestionnaire"
            } else if (!(value.id in object.questionnaires*.id)) {
                return "draftQuestionnaire.not.in.questionnaires"
            }
        })
    }

    static mapping = {
        questionnaires cascade: 'all-delete-orphan'
    }
}
