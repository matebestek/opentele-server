package org.opentele.server.model.patientquestionnaire

class PatientEndNode extends PatientQuestionnaireNode {
    static constraints = {
        defaultNext(nullable: true) // Actually cannot have a value, but can we model that?
    }

    @Override
    void visit(PatientQuestionnaireNodeVisitor visitor) {
        visitor.visitEndNode(this)
    }
}
