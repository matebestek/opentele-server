package org.opentele.server.model.patientquestionnaire

class PatientNoteInputNode extends PatientQuestionnaireNode {
    String text

    static constraints = {
        defaultNext(nullable:true) // Hvorfor nullable?
    }
    
    static mapping = {
        text type: 'text'
    }

    @Override
    void visit(PatientQuestionnaireNodeVisitor visitor) {
        visitor.visitNoteInputNode(this)
    }
}
