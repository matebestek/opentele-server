package org.opentele.server.model.types

import org.springframework.context.MessageSourceResolvable

enum MeasurementTypeName implements MessageSourceResolvable {
    BLOOD_PRESSURE { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitBloodPressure() } },
    CTG { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitCtg() } },
    TEMPERATURE { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitTemperature() } },
    URINE { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitUrine() } },
    URINE_COMBI { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitUrineCombi() } },
    URINE_GLUCOSE { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitUrineGlucose() } },
    PULSE { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitPulse() } },
    WEIGHT { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitWeight() } },
    HEMOGLOBIN { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitHemoglobin() } },
    SATURATION { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitSaturation() } },
    CRP { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitCrp() } },
    BLOODSUGAR { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitBloodSugar() } },
    LUNG_FUNCTION { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitLungFunction() } },
    CONTINUOUS_BLOOD_SUGAR_MEASUREMENT { @Override void visit(MeasurementTypeNameVisitor visitor) { visitor.visitContinuousBloodSugarMeasurement() } }

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
        ["threshold.${name()}"] as String[]
    }

    Object[] getArguments() {
        [] as Object[]
    }

    String getDefaultMessage() {
        return name()
    }

    static MEASUREMENT_TYPE_NAMES_WITHOUT_THRESHOLD_SUPPORT = [CTG, CONTINUOUS_BLOOD_SUGAR_MEASUREMENT, URINE_COMBI]

    static TABLE_CAPABLE_MEASUREMENT_TYPE_NAME =
            [BLOOD_PRESSURE, TEMPERATURE, PULSE, WEIGHT, HEMOGLOBIN, SATURATION, CRP, LUNG_FUNCTION, BLOODSUGAR, CONTINUOUS_BLOOD_SUGAR_MEASUREMENT]

    static TABLE_CAPABLE_MEASUREMENT_TYPE_NAME_CONSULTATION = [
            BLOOD_PRESSURE,
            SATURATION,
            LUNG_FUNCTION,
            URINE_COMBI,
            WEIGHT
            ]
}

interface MeasurementTypeNameVisitor {
    void visitBloodPressure()
    void visitCtg()
    void visitTemperature()
    void visitUrine()
    void visitUrineCombi()
    void visitUrineGlucose()
    void visitPulse()
    void visitWeight()
    void visitHemoglobin()
    void visitSaturation()
    void visitCrp()
    void visitBloodSugar()
    void visitLungFunction()
    void visitContinuousBloodSugarMeasurement()
}
