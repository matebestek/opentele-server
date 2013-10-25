package org.opentele.server.model.questionnaire

import org.opentele.server.model.AbstractObject

class QuestionnaireGroup2QuestionnaireHeader extends AbstractObject {

    QuestionnaireHeader questionnaireHeader
    StandardSchedule standardSchedule

    static belongsTo = [questionnaireGroup: QuestionnaireGroup]

    static constraints = {
        questionnaireHeader nullable: false, unique: 'questionnaireGroup'
        questionnaireGroup nullable: false
        standardSchedule nullable: true
    }

    StandardSchedule getSchedule() {
        standardSchedule ?: questionnaireHeader?.activeQuestionnaire?.standardSchedule
    }

    static transients = ['schedule']

    static embedded = ['standardSchedule']

}
