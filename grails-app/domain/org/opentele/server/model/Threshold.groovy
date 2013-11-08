package org.opentele.server.model

import org.opentele.server.model.types.MeasurementTypeName

abstract class Threshold extends AbstractObject {
    // Let each subclass have its own table, just as if we didn't have this superclass
    static mapping = { tablePerHierarchy false }

    MeasurementType type

    String prettyToString() {
        switch(type.name) {
            case MeasurementTypeName.TEMPERATURE: return 'Temperatur'
            case MeasurementTypeName.HEMOGLOBIN: return 'Hæmoglobin'
            case MeasurementTypeName.PULSE: return 'Puls'
            case MeasurementTypeName.SATURATION: return 'Saturation'
            case MeasurementTypeName.WEIGHT: return 'Vægt'
            case MeasurementTypeName.URINE: return 'Proteinindhold i urin'
            case MeasurementTypeName.URINE_GLUCOSE: return 'Glukose i urin'
            case MeasurementTypeName.BLOOD_PRESSURE: return 'Blodtryk'
            case MeasurementTypeName.CRP: return 'C-reaktivt protein'
            case MeasurementTypeName.CTG: return 'CTG'
            case MeasurementTypeName.BLOODSUGAR: return 'Blodsukker'
            case MeasurementTypeName.LUNG_FUNCTION: return 'Lungefunktion'
        }
        throw new RuntimeException("Unknown threshold type: '${type.name}'")
    }

    String toString() {
        type?.name
    }

    public abstract Threshold duplicate()
}
