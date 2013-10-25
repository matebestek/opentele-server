package org.opentele.server.model.questionnaire


import org.opentele.server.model.AbstractObject
import org.opentele.server.model.MeterType

/**
 * Required metertypes for a questionnaire
 * @author henrik
 *
 */
class Questionnaire2MeterType extends AbstractObject {

    Questionnaire questionnaire
    MeterType meterType
    
    static Questionnaire2MeterType link(questionnaire, meterType) {
        def ref = Questionnaire2MeterType.findByQuestionnaireAndMeterType(questionnaire, meterType)
        if (!ref) {
            ref = new Questionnaire2MeterType()
            questionnaire?.addToQuestionnaire2MeterTypes(ref)
            meterType?.addToQuestionnaire2MeterTypes(ref)
            ref.save(failOnError:true)
        }
        ref
    }

    static void unlink(questionnaire, meterType) {
        def ref = Questionnaire2MeterType.findByQuestionnaireAndMeterType(questionnaire, meterType)
        if (ref) {
            questionnaire?.removeFromQuestionnaire2MeterTypes(ref)
            meterType?.removeFromQuestionnaire2MeterTypes(ref)
            ref.delete(failOnError:true)
        }
    }

    
    
    static constraints = {
    }
}
