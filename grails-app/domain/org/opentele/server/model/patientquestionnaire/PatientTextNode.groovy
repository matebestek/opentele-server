package org.opentele.server.model.patientquestionnaire

class PatientTextNode extends PatientQuestionnaireNode {
    String headline
    String text
    
    static constraints = {
        headline(nullable:true)
        text(nullable:false)
        defaultNext(nullable:true)
    }
    
    static mapping = {
        text type: 'text'
     }

    @Override
    void visit(PatientQuestionnaireNodeVisitor visitor) {
        visitor.visitTextNode(this)
    }
}
