package org.opentele.server.model.questionnaire

import org.opentele.server.model.MeterType
import org.opentele.server.model.types.Severity

class MeasurementNode extends QuestionnaireNode {
    public static final String DEVICE_ID_VAR = "DEVICE_ID"

    public static final String WEIGHT_VAR = "WEIGHT"
	public static final String TEMPERATURE_VAR = "TEMPERATURE"
	public static final String HEMOGLOBIN_VAR = "HEMOGLOBIN"

    public static final String SATURATION_VAR = "SATURATION"
    
    public static final String PULSE_VAR = "PULSE"
    public static final String SYSTOLIC_VAR = "SYSTOLIC"
    public static final String DIASTOLIC_VAR = "DIASTOLIC"
    public static final String MEAN_ARTERIAL_PRESSURE_VAR = "MEAN_ARTERIAL_PRESSURE"

    public static final String URINE_VAR = "URINE"
    public static final String GLUCOSE_URINE_VAR = "URINE_GLUCOSE"

    public static final String CRP_VAR = "CRP"

    public static final String BLOODSUGAR_VAR = "BLOODSUGARMEASUREMENTS"
    public static final String CONTINUOUS_BLOOD_SUGAR_MEASUREMENTS_VAR = "CONTINUOUS_BLOOD_SUGAR_MEASUREMENTS"
    
    // Monica vars
    public static final String FHR_VAR = "FHRX"; // Varname must be unique
    public static final String MHR_VAR = "MHR"
    public static final String QFHR_VAR = "QFHR"
    public static final String TOCO_VAR = "TOCO"
    public static final String SIGNAL_VAR = "SIGNAL"
    public static final String SIGNAL_TO_NOISE_VAR = "SIGNAL_TO_NOISE"
    public static final String FETAL_HEIGHT_VAR = "FETAL_HEIGHT"
    public static final String SIMULATE_VAR = "SIMULATE"
    public static final String VOLTAGESTART_VAR = "VSTART"
    public static final String VOLTAGEEND_VAR = "VEND"
    public static final String STARTTIME_VAR = "START"
    public static final String ENDTIME_VAR = "END"

    // Lung function variables
    public static final String FEV1_VAR = "FEV1";
    public static final String FEV6_VAR = "FEV6";
    public static final String FEV1_FEV6_RATIO_VAR = "FEV1_FEV6_RATIO";
    public static final String FEF2575_VAR = "FEF2575";
    public static final String FEV_GOOD_TEST_VAR = "FEV_GOOD_TEST";
    public static final String FEV_SOFTWARE_VERSION = "FEV_SOFTWARE_VERSION";

    String text
    Boolean simulate = false
    
    QuestionnaireNode monicaMeasuringTimeInputNode
    String monicaMeasuringTimeInputVar
    
    // When creating the client-questionnaire. If true: Map this node to a set of input-fields instead of a measurement-inputnode 
    Boolean mapToInputFields = false 
    
    MeterType meterType
    
    QuestionnaireNode nextFail

    Severity defaultSeverity
    Severity nextFailSeverity
    
    void setMonicaMeasuringTimeInputNode(QuestionnaireNode inNode) {
        monicaMeasuringTimeInputNode = inNode
    }
    
    void setNextFail(QuestionnaireNode nxt) {
        nextFail = nxt
    }
    
    static constraints = {
        defaultNext(nullable:false)
        meterType(nullable: false) 
        simulate(nullable: false)
        monicaMeasuringTimeInputNode(nullable: true) 
        monicaMeasuringTimeInputVar(nullable: true)
        mapToInputFields(nullable: false)
        nextFail(nullable:false)

        defaultSeverity(nullable:true)
        nextFailSeverity(nullable:true)
    }
	
	String toString() {
		"MeasurementNode"
	}
    
    static mapping = {
        text type: 'text'
     }
}
