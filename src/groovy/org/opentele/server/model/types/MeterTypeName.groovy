package org.opentele.server.model.types


enum MeterTypeName {
	BLOOD_PRESSURE_PULSE,
    CTG,
    TEMPERATURE,
    URINE, //protein in urine
    WEIGHT,
    HEMOGLOBIN,
    SATURATION,
    SATURATION_W_OUT_PULSE,
    CRP,
    BLOODSUGAR,
    URINE_GLUCOSE,
    LUNG_FUNCTION

    String getKey() {
        name()
    }
    
	String value() {
		name()
	}
}
