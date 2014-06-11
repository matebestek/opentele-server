package org.opentele.server.model.patientquestionnaire

import org.opentele.server.model.Measurement
import org.opentele.server.model.MeterType
import org.opentele.server.model.types.Severity


class PatientMeasurementNode extends PatientQuestionnaireNode {

    String text
    Boolean simulate = false
    
    PatientQuestionnaireNode monicaMeasuringTimeInputNode
    String monicaMeasuringTimeInputVar
    
    // When creating the client-questionnaire. If true: Map this node to a set of input-fields instead of a measurement-inputnode
    Boolean mapToInputFields = false

    MeterType meterType
    
    PatientQuestionnaireNode nextFail

    Severity defaultSeverity
    Severity nextFailSeverity

    static constraints = {
        defaultNext(nullable:true)
        defaultSeverity(nullable:true)
        meterType(nullable: true)
        simulate(nullable: false)
        monicaMeasuringTimeInputNode(nullable: true)
        monicaMeasuringTimeInputVar(nullable: true)
        mapToInputFields(nullable: false)
        nextFail(nullable: true) // Because of the way PatientMeasurementNode is created from MeasurementNode
        nextFailSeverity(nullable: true)
    }
    
    static mapping = {
        text type: 'text'
    }

    @Override
    void visit(PatientQuestionnaireNodeVisitor visitor) {
        visitor.visitMeasurementNode(this)
    }
}
