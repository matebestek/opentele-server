package org.opentele.server.model.types

enum IupacCode {

    FEV1("MCS88015"),
    FVC("MCS88016"),
    FEV1_FVC("MCS88017"),
    //FEV_PRC("MCS88023"),
    //ÅNDENØD_MRC("MCS88021"),
    //ÅNDENØD_NYHA("MCS88032"),
    SATURATION("NPU03011"),
    //EXACERBATIONER("MCS88022"),
    SYSTOLIC_CLINIC("DNK05472"),
    DIASTOLIC_CLINIC("DNK05473"),
    SYSTOLIC_HOME("MCS88019"),
    DIASTOLIC_HOME("MCS88020"),
    PULSE("NPU21692"),
    //HBA1C("NPU27412"),
    BLOODSUGAR("NPU02187"),
    //SAMLET_KOLESTEROL("NPU01566"),
    //HDL("NPU01567"),
    //LDL("NPU1568"),
    //TRIGLYCERID("NPU04094"),
    WEIGHT("NPU03804"),
    //LIVVIDDE("MCS88018"),
    //HØJDE("NPU03794"),
    //BMI("NPU27281"),
    //SKRIDT-TÆLLER("unspecified_1"),
    //MOTION("MCS88001"),
    TEMPERATURE("NPU08676"),
    URINE("NPU03958"), // PROTEIN in urine // Simens multistix 5 (RM): NPU03958, Siemens uristix 2857 (RH): NPU04206
    URINE_GLUCOSE("NPU03936"),
    //ØDEM_GRAD("unspecified_2"),
    //FOSTER_AKTIVIET("unspecified_3")
    CRP ("NPU01423")

    private final String value

    IupacCode(String value) {
        this.value = value
    }

    String value() {
        value
    }

    // Missing types:
   // HEMOGLOBIN
}