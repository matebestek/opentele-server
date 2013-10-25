package org.opentele.server.model.types

import org.springframework.context.MessageSourceResolvable

enum MeasurementTypeName implements MessageSourceResolvable {
    BLOOD_PRESSURE { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitBloodPressure() } },
    CTG { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitCtg() } },
    TEMPERATURE { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitTemperature() } },
    URINE { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitUrine() } },
    URINE_GLUCOSE { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitUrineGlucose() } },
    PULSE { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitPulse() } },
    WEIGHT { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitWeight() } },
    HEMOGLOBIN { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitHemoglobin() } },
    SATURATION { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitSaturation() } },
    CRP { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitCrp() } },
    BLOODSUGAR { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitBloodSugar() } },
    LUNG_FUNCTION { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitLungFunction() } }

    void visit(MeasurementTypeNameVisitor visitor) {
        throw new RuntimeException("Unknown measurement type: '${this}'")
    }

    String value() {
        name()
    }

    static MeasurementTypeName safeValueOf(String type) {
        try {
            Enum.valueOf(MeasurementTypeName, type)
        } catch (Exception e) {
            null
        }
    }
     String[] getCodes() {
         ["thresholdtype.${name()}", "thresholdtype.${name().toLowerCase()}"] as String[]
     }

     Object[] getArguments() {
         [] as Object[]
     }

     String getDefaultMessage() {
         return name()
     }

    static TABLE_CAPABLE_MEASUREMENT_TYPE_NAME =
        [BLOOD_PRESSURE, TEMPERATURE, PULSE, WEIGHT, HEMOGLOBIN, SATURATION, CRP, LUNG_FUNCTION]
}

interface MeasurementTypeNameVisitor {
    void visitBloodPressure();
    void visitCtg();
    void visitTemperature();
    void visitUrine();
    void visitUrineGlucose();
    void visitPulse();
    void visitWeight();
    void visitHemoglobin();
    void visitSaturation();
    void visitCrp();
    void visitBloodSugar();
    void visitLungFunction();
}
