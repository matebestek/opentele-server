package org.opentele.server.model.patientquestionnaire

import org.opentele.server.model.Measurement
import org.opentele.server.model.MeterType


class PatientMeasurementNode extends PatientQuestionnaireNode {

    String text
    Boolean simulate = false
    
    PatientQuestionnaireNode monicaMeasuringTimeInputNode
    String monicaMeasuringTimeInputVar
    
    // When creating the client-questionnaire. If true: Map this node to a set of input-fields instead of a measurement-inputnode
    Boolean mapToInputFields = false

    MeterType meterType
    
    PatientQuestionnaireNode nextFail
    
    static constraints = {
        defaultNext(nullable:true)
        meterType(nullable: true)
        simulate(nullable: false)
        monicaMeasuringTimeInputNode(nullable: true)
        monicaMeasuringTimeInputVar(nullable: true)
        mapToInputFields(nullable: false)
        nextFail(nullable: true) // Because of the way PatientMeasurementNode is created from MeasurementNode
    }
    
    static mapping = {
        text type: 'text'
    }

    @Override
    void visit(PatientQuestionnaireNodeVisitor visitor) {
        visitor.visitMeasurementNode(this)
    }
}
