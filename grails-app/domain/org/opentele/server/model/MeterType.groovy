package org.opentele.server.model

import org.opentele.server.model.questionnaire.Questionnaire2MeterType
import org.opentele.server.model.types.MeterTypeName

class MeterType extends AbstractObject {

    static hasMany = [meters: Meter, questionnaire2MeterTypes : Questionnaire2MeterType]
    
    MeterTypeName name
    
    static constraints = {
        name(nullable:false)
    }
	
	String toString() {
		name
	}
}
