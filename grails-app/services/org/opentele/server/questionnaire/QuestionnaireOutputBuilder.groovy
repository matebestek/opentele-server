package org.opentele.server.questionnaire

import org.opentele.server.model.patientquestionnaire.*
import org.opentele.server.model.questionnaire.MeasurementNode
import org.opentele.server.model.types.DataType
import org.opentele.server.model.types.MeterTypeName
import org.opentele.server.model.types.Operation
import org.opentele.server.model.types.Severity

class QuestionnaireOutputBuilder implements PatientQuestionnaireNodeVisitor {
    final List nodes = []
    final Map<String,String> outputVariables = new HashMap<String,String>()

    def build(PatientQuestionnaire questionnaire) {
        questionnaire.getNodes().each {
            it.visit(this)
        }
    }

    @Override
    void visitEndNode(PatientQuestionnaireNode n) {
        nodes << [
            EndNode: [
                nodeName: n.id as String
            ]
        ]
    }

    @Override
    void visitBooleanNode(PatientQuestionnaireNode n) {
        addBooleanAssignmentNode(nodeName: "${n.id}", nextNodeId: n.defaultNext.id, variableName: n.variableName, variableValue: n.value)
    }

    @Override
    void visitChoiceNode(PatientQuestionnaireNode n) {
        def severityDefined = n.defaultSeverity || n.alternativeSeverity

        nodes << [
            DecisionNode: [
                nodeName: n.id as String,
                // drop assignment nodes if no severity defined
                next: (severityDefined ? "AN_${n.defaultNext.id}_T${n.id}" : n.defaultNext.id as String),
                nextFalse: (severityDefined ? "AN_${n.alternativeNext.id}_F${n.id}" : n.alternativeNext.id  as String),
                expression: patientChoiceNodeExpression(n)
            ]
        ]

        addBooleanAssignmentNode(nodeName: "AN_${n.defaultNext.id}_T${n.id}", nextNodeId: "ANSEV_${n.defaultNext.id}_T${n.id}", variableName: "${n.id}.DECISION", variableValue: true)
        addBooleanAssignmentNode(nodeName: "AN_${n.alternativeNext.id}_F${n.id}", nextNodeId: "ANSEV_${n.alternativeNext.id}_F${n.id}", variableName: "${n.id}.DECISION", variableValue: false)

        // Haandter ChoiceNode severity w/assignment nodes..
        if (severityDefined) {
            String defaultValue = n.defaultSeverity?.value() ?: Severity.GREEN.value()
            String alternativeValue = n.alternativeSeverity?.value() ?: Severity.GREEN.value()
            addStringAssignmentNode(nodeName: "ANSEV_${n.defaultNext.id}_T${n.id}", nextNodeId: n.defaultNext.id,
                    variableName: "${n.id}.DECISION#SEVERITY", variableValue: defaultValue)
            addStringAssignmentNode(nodeName: "ANSEV_${n.alternativeNext.id}_F${n.id}", nextNodeId: n.alternativeNext.id,
                    variableName: "${n.id}.DECISION#SEVERITY", variableValue: alternativeValue)
        }
    }

    @Override
    void visitInputNode(PatientQuestionnaireNode n) {
        def severityDefined = n.defaultSeverity || n.alternativeSeverity

        if (n.inputType == DataType.BOOLEAN) {
            addIoNode(nodeName: n.id, elements: [
                    textViewElement(text: n.text),
                    twoButtonElement(leftText: 'Nej', leftNextNodeId: "AN_${n.alternativeNext.id}_L${n.id}",
                        rightText: 'Ja', rightNextNodeId: "AN_${n.defaultNext.id}_R${n.id}")
                ]
            )
            // "Yes" choice
            def yesChoiceNext = severityDefined ? "ANSEV_${n.defaultNext.id}_R${n.id}" : "${n.defaultNext.id}"
            addBooleanAssignmentNode(nodeName: "AN_${n.defaultNext.id}_R${n.id}", nextNodeId: yesChoiceNext, variableName: "${n.id}.FIELD", variableValue: true)
            // "No" choice
            def noChoiceNext = severityDefined ? "ANSEV_${n.alternativeNext.id}_L${n.id}" : n.alternativeNext.id as String
            addBooleanAssignmentNode(nodeName: "AN_${n.alternativeNext.id}_L${n.id}", nextNodeId: noChoiceNext, variableName: "${n.id}.FIELD", variableValue: false)
            // Haandter severity w/assignment nodes..

            // Left severity
            if (severityDefined) {
                // "Yes"
                def yesValue = n.defaultSeverity?.value() ?: Severity.GREEN.value()
                addStringAssignmentNode(nodeName: "ANSEV_${n.defaultNext.id}_R${n.id}", nextNodeId: n.defaultNext.id,
                        variableName: "${n.id}.FIELD#SEVERITY", variableValue: yesValue)
                // "No"
                def noValue = n.alternativeSeverity?.value() ?: Severity.GREEN.value()
                addStringAssignmentNode(nodeName: "ANSEV_${n.alternativeNext.id}_L${n.id}", nextNodeId: n.alternativeNext.id,
                        variableName: "${n.id}.FIELD#SEVERITY", variableValue: noValue)
            }
        } else {
            // Patient input node.. but not boolean
            def elements

            if (n.inputType == DataType.STRING) {
                elements = [
                    textViewElement(text: n.text),
                    editStringElement(variableName: "${n.id}.FIELD"),
                    buttonElement(text: 'Næste', nextNodeId: n.defaultNext.id)
                ]
            } else if (n.inputType == DataType.INTEGER) {
                elements = [
                    textViewElement(text: n.text),
                    editIntegerElement(variableName: "${n.id}.FIELD"),
                    buttonElement(text: 'Næste', nextNodeId: n.defaultNext.id)
                ]
            } else if (n.inputType == DataType.FLOAT) {
                elements = [
                    textViewElement(text: n.text),
                    editFloatElement(variableName: "${n.id}.FIELD"),
                    buttonElement(text: 'Næste', nextNodeId: n.defaultNext.id)
                ]
            } else if (n.inputType == DataType.RADIOCHOICE) {
                def choices = n.choices.collect { choice(text: it.label, value: it.value) }

                elements = [
                    textViewElement(text: n.text, header: false),
                    radioButtonElement(choices: choices, variableName: "${n.id}.FIELD"),
                    buttonElement(text: 'Næste', nextNodeId: n.defaultNext.id, skipValidation: false)
                ]
            } else {
                throw new UnsupportedOperationException("Handling of inputtype: ${n.inputType} is not yet implemented.")
            }

            addIoNode(nodeName: n.id, nextNodeId: n.defaultNext.id, elements: elements)
        }
    }

    @Override
    void visitDelayNode(PatientQuestionnaireNode n) {
        addDelayNode(nodeName: n.id, nextNodeId: n.defaultNext.id,
                elements: [
                ],
                countTime: n.countTime,
                countUp: n.countUp,
                displayTextString: n.text
        )
    }

    @Override
    void visitNoteInputNode(PatientQuestionnaireNode n) {
        addIoNode(nodeName: n.id, nextNodeId: n.defaultNext.id, elements: [
                textViewElement(text: n.text),
                noteTextElement(parent: "${n.id}.FIELD"),
                buttonElement(text: 'Næste', nextNodeId: n.defaultNext.id)
            ]
        )
    }

    @Override
    void visitTextNode(PatientQuestionnaireNode n) {
        // TextNode maps to simple IONode, with a textfield and a "next" button..
        def elements = []
        if (n.headline != null && n.headline != '') {
            elements << textViewElement(text: n.headline)
        }
        elements << textViewElement(text: n.text)
        elements << buttonElement(text: 'Næste', nextNodeId: n.defaultNext.id)

        addIoNode(nodeName: n.id, elements: elements)
    }

    @Override
    void visitMeasurementNode(PatientQuestionnaireNode n) {
        switch (n.meterType.name) {
            case MeterTypeName.CTG.value():
                patientMeasurementNodeForCtg(n)
                break;
            case MeterTypeName.WEIGHT:
                patientMeasurementNodeForWeight(n)
                break;
            case MeterTypeName.TEMPERATURE:
                patientMeasurementNodeForTemperature(n)
                break;
            case MeterTypeName.BLOOD_PRESSURE_PULSE.value():
                patientMeasurementNodeForBloodPressureAndPulse(n)
                break;
            case MeterTypeName.SATURATION.value():
                patientMeasurementNodeForSaturation(n)
                break;
            case MeterTypeName.SATURATION_W_OUT_PULSE.value():
                patientMeasurementNodeForSaturationWithoutPulse(n)
                break;
            case MeterTypeName.URINE.value():
                patientMeasurementNodeForUrine(n)
                break;
            case MeterTypeName.URINE_GLUCOSE:
                patientMeasurementNodeForGlucoseInUrine(n)
                break;
            case MeterTypeName.CRP:
                patientMeasurementNodeForCrp(n)
                break;
            case MeterTypeName.HEMOGLOBIN:
                patientMeasurementNodeForHemoglobin(n)
                break;
            case MeterTypeName.BLOODSUGAR:
                patientMeasurementNodeForBloodSugar(n)
                break;
            case MeterTypeName.LUNG_FUNCTION:
                patientMeasurementNodeForLungFunction(n)
                break;
            default:
                throw new RuntimeException("Unsupported datatype ${n.meterType.name}")
        }
    }


    private def patientMeasurementNodeForCtg(n) {
        outputVariables["${n.id}.CTG#${MeasurementNode.FHR_VAR}"] = DataType.FLOAT_ARRAY.value()
        outputVariables["${n.id}.CTG#${MeasurementNode.MHR_VAR}"] = DataType.FLOAT_ARRAY.value()
        outputVariables["${n.id}.CTG#${MeasurementNode.QFHR_VAR}"] = DataType.INTEGER_ARRAY.value()
        outputVariables["${n.id}.CTG#${MeasurementNode.TOCO_VAR}"] = DataType.FLOAT_ARRAY.value()
        outputVariables["${n.id}.CTG#${MeasurementNode.SIGNAL_VAR}"] = DataType.STRING_ARRAY.value()
        outputVariables["${n.id}.CTG#${MeasurementNode.SIGNAL_TO_NOISE_VAR}"] = DataType.INTEGER_ARRAY.value()
        outputVariables["${n.id}.CTG#${MeasurementNode.FETAL_HEIGHT_VAR}"] = DataType.INTEGER_ARRAY.value()
        outputVariables["${n.id}.CTG#${MeasurementNode.VOLTAGESTART_VAR}"] = DataType.FLOAT.value()
        outputVariables["${n.id}.CTG#${MeasurementNode.VOLTAGEEND_VAR}"] = DataType.FLOAT.value()
        outputVariables["${n.id}.CTG#${MeasurementNode.STARTTIME_VAR}"] = DataType.STRING.value()
        outputVariables["${n.id}.CTG#${MeasurementNode.ENDTIME_VAR}"] = DataType.STRING.value()

        def deviceNodeContents = [
            nodeName: n.id as String,
            next: n.defaultNext.id as String,
            nextFail: "AN_${n.id}_CANCEL",

            fhr: [
                name: "${n.id}.CTG#${MeasurementNode.FHR_VAR}",
                type: DataType.FLOAT_ARRAY.value()
            ],
            mhr: [
                name: "${n.id}.CTG#${MeasurementNode.MHR_VAR}",
                type: DataType.FLOAT_ARRAY.value()
            ],
            qfhr: [
                name: "${n.id}.CTG#${MeasurementNode.QFHR_VAR}",
                type: DataType.INTEGER_ARRAY.value()
            ],
            toco: [
                name: "${n.id}.CTG#${MeasurementNode.TOCO_VAR}",
                type: DataType.FLOAT_ARRAY.value()
            ],
            signal: [
                name: "${n.id}.CTG#${MeasurementNode.SIGNAL_VAR}",
                type: DataType.STRING_ARRAY.value()
            ],
            signalToNoise: [
                name: "${n.id}.CTG#${MeasurementNode.SIGNAL_TO_NOISE_VAR}",
                type: DataType.INTEGER_ARRAY.value()
            ],
            fetalHeight: [
                name: "${n.id}.CTG#${MeasurementNode.FETAL_HEIGHT_VAR}",
                type: DataType.INTEGER_ARRAY.value()
            ],
            voltageStart: [
                name: "${n.id}.CTG#${MeasurementNode.VOLTAGESTART_VAR}",
                type: DataType.FLOAT.value()
            ],
            voltageEnd: [
                name: "${n.id}.CTG#${MeasurementNode.VOLTAGEEND_VAR}",
                type: DataType.FLOAT.value()
            ],
            startTime: [
                name: "${n.id}.CTG#${MeasurementNode.STARTTIME_VAR}",
                type: DataType.STRING.value()
            ],
            endTime: [
                name: "${n.id}.CTG#${MeasurementNode.ENDTIME_VAR}",
                type: DataType.STRING.value()
            ]
        ]

        def shouldIncludeMeasuringTime = inputVariableNameForMeasurementTime(n) != null
        if (shouldIncludeMeasuringTime) {
            deviceNodeContents['measuringTime'] = [
                type: 'name',
                value: inputVariableNameForMeasurementTime(n)
            ]
        }

        nodes << (n.simulate ? [MonicaTestDeviceNode: deviceNodeContents] : [MonicaDeviceNode: deviceNodeContents])

        addBooleanAssignmentNode(nodeName: "AN_${n.id}_CANCEL", nextNodeId: n.nextFail.id, variableName: "${n.id}.CTG##CANCEL", variableValue: true)
    }

    private def patientMeasurementNodeForWeight(n) {
        if (n.mapToInputFields) {
            addIoNode(nodeName: n.id, nextNodeId: n.defaultNext.id, elements: [
                textViewElement(text: n.text),
                editFloatElement(variableName: "${n.id}.${MeasurementNode.WEIGHT_VAR}"),
                buttonsToSkipInputForNode(n)
            ])
        } else {
            def outputVariableName = "${n.id}.${MeasurementNode.WEIGHT_VAR}"
            outputVariables[outputVariableName] = DataType.FLOAT.value()
            def nodeContents = [
                nodeName: n.id as String,
                next: n.defaultNext.id as String,
                nextFail: "AN_${n.id}_CANCEL",
                text: n.text,
                weight: [
                    name: outputVariableName,
                    type: DataType.FLOAT.value()
                ]
            ]
            nodes << (n.simulate ? [WeightTestDeviceNode: nodeContents] : [WeightDeviceNode: nodeContents])
        }

        addBooleanAssignmentNode(nodeName: "AN_${n.id}_CANCEL", nextNodeId: n.nextFail.id, variableName: "${n.id}.${MeasurementNode.WEIGHT_VAR}#CANCEL", variableValue: true)
    }

    private def patientMeasurementNodeForTemperature(n) {
        if (n.mapToInputFields) {
            addIoNode(nodeName: n.id, nextNodeId: n.defaultNext.id, elements: [
                textViewElement(text: n.text),
                editFloatElement(variableName: "${n.id}.${MeasurementNode.TEMPERATURE_VAR}"),
                buttonsToSkipInputForNode(n)
            ])
        } else { // No support for simulated nodes for now.. Since all temp measurements are manual
            def outputVariableName = "${n.id}.${MeasurementNode.TEMPERATURE_VAR}"
            outputVariables[outputVariableName] = DataType.FLOAT.value()
            nodes << [
                TemperatureDeviceNode: [
                    nodeName: n.id as String,
                    next: n.defaultNext.id as String,
                    nextFail: "AN_${n.id}_CANCEL",
                    text: n.text,
                    temperature: [
                        name: outputVariableName,
                        type: DataType.FLOAT.value()
                    ]
                ]
            ]
        }

        addBooleanAssignmentNode(nodeName: "AN_${n.id}_CANCEL", nextNodeId: n.nextFail.id, variableName: "${n.id}.${MeasurementNode.TEMPERATURE_VAR}#CANCEL", variableValue: true)
    }

    def patientMeasurementNodeForBloodPressureAndPulse(n) {
        if (n.mapToInputFields) {
            addIoNode(nodeName: n.id, nextNodeId: n.defaultNext.id, elements: [
                    textViewElement(text: n.text),
                    textViewElement(text: 'Systolisk blodtryk'),
                    editIntegerElement(variableName: "${n.id}.BP#${MeasurementNode.SYSTOLIC_VAR}"),
                    textViewElement(text: 'Diastolisk blodtryk'),
                    editIntegerElement(variableName: "${n.id}.BP#${MeasurementNode.DIASTOLIC_VAR}"),
                    textViewElement(text: 'Puls'),
                    editIntegerElement(variableName: "${n.id}.BP#${MeasurementNode.PULSE_VAR}"),
                    buttonsToSkipInputForNode(n)
                ]
            )
        } else {
            def systolicOutputVariableName = "${n.id}.BP#${MeasurementNode.SYSTOLIC_VAR}"
            def diastolicOutputVariableName = "${n.id}.BP#${MeasurementNode.DIASTOLIC_VAR}"
            def meanArterialPressureOutputVariableName = "${n.id}.BP#${MeasurementNode.MEAN_ARTERIAL_PRESSURE_VAR}"
            def pulseOutputVariableName = "${n.id}.BP#${MeasurementNode.PULSE_VAR}"
            def deviceIdOutputVariableName = "${n.id}.BP#${MeasurementNode.DEVICE_ID_VAR}"

            outputVariables[systolicOutputVariableName] = DataType.INTEGER.value()
            outputVariables[diastolicOutputVariableName] = DataType.INTEGER.value()
            outputVariables[meanArterialPressureOutputVariableName] = DataType.INTEGER.value()
            outputVariables[pulseOutputVariableName] = DataType.INTEGER.value()
            outputVariables[deviceIdOutputVariableName] = DataType.STRING.value()

            def nodeContents = [
                nodeName: n.id as String,
                next: n.defaultNext.id as String,
                nextFail: "AN_${n.id}_CANCEL",
                text: n.text,
                diastolic: [
                    name: diastolicOutputVariableName,
                    type: DataType.INTEGER.value()
                ],
                systolic: [
                    name: systolicOutputVariableName,
                    type: DataType.INTEGER.value()
                ],
                meanArterialPressure: [
                    name: meanArterialPressureOutputVariableName,
                    type: DataType.INTEGER.value()
                ],
                pulse: [
                    name: pulseOutputVariableName,
                    type: DataType.INTEGER.value()
                ],
                deviceId: [
                    name: deviceIdOutputVariableName,
                    type: DataType.STRING.value()
                ]
            ]
            nodes << (n.simulate ? [BloodPressureTestDeviceNode: nodeContents] : [BloodPressureDeviceNode: nodeContents])
        }

        addBooleanAssignmentNode(nodeName: "AN_${n.id}_CANCEL", nextNodeId: n.nextFail.id, variableName: "${n.id}.BP##CANCEL", variableValue: true)
    }

    private def patientMeasurementNodeForSaturation(n) {
        if (n.mapToInputFields) {
            addIoNode(nodeName: n.id, nextNodeId: n.defaultNext.id, elements: [
                    textViewElement(text: n.text),
                    textViewElement(text: 'Iltmætning'),
                    editIntegerElement(variableName: "${n.id}.SAT#${MeasurementNode.SATURATION_VAR}"),
                    textViewElement(text: 'Puls'),
                    editIntegerElement(variableName: "${n.id}.SAT#${MeasurementNode.PULSE_VAR}"),
                    buttonsToSkipInputForNode(n)
               ]
            )
        } else {
            def saturationOutputVariableName = "${n.id}.SAT#${MeasurementNode.SATURATION_VAR}"
            def pulseOutputVariableName = "${n.id}.SAT#${MeasurementNode.PULSE_VAR}"
            def deviceIdOutputVariableName = "${n.id}.SAT#${MeasurementNode.DEVICE_ID_VAR}"

            outputVariables[saturationOutputVariableName] = DataType.INTEGER.value()
            outputVariables[pulseOutputVariableName] = DataType.INTEGER.value()
            outputVariables[deviceIdOutputVariableName] = DataType.STRING.value()

            def nodeContents = [
                nodeName: n.id as String,
                next: n.defaultNext.id as String,
                nextFail: "AN_${n.id}_CANCEL",
                text: n.text,
                saturation: [
                    name: saturationOutputVariableName,
                    type: DataType.INTEGER.value()
                ],
                pulse: [
                    name: pulseOutputVariableName,
                    type: DataType.INTEGER.value()
                ],
                deviceId: [
                    name: deviceIdOutputVariableName,
                    type: DataType.STRING.value()
                ]
            ]
            nodes << (n.simulate ? [SaturationTestDeviceNode: nodeContents] : [SaturationDeviceNode: nodeContents])
        }

        addBooleanAssignmentNode(nodeName: "AN_${n.id}_CANCEL", nextNodeId: n.nextFail.id, variableName: "${n.id}.SAT##CANCEL", variableValue: true)
    }


    private def patientMeasurementNodeForSaturationWithoutPulse(n) {
        if (n.mapToInputFields) {
            addIoNode(nodeName: n.id, nextNodeId: n.defaultNext.id, elements: [
                    textViewElement(text: n.text),
                    textViewElement(text: 'Iltmætning'),
                    editIntegerElement(variableName: "${n.id}.SAT#${MeasurementNode.SATURATION_VAR}"),
                    buttonsToSkipInputForNode(n)
            ]
            )
        } else {
            def saturationOutputVariableName = "${n.id}.SAT#${MeasurementNode.SATURATION_VAR}"
            def deviceIdOutputVariableName = "${n.id}.SAT#${MeasurementNode.DEVICE_ID_VAR}"

            outputVariables[saturationOutputVariableName] = DataType.INTEGER.value()
            outputVariables[deviceIdOutputVariableName] = DataType.STRING.value()

            def nodeContents = [
                    nodeName: n.id as String,
                    next: n.defaultNext.id as String,
                    nextFail: "AN_${n.id}_CANCEL",
                    text: n.text,
                    saturation: [
                            name: saturationOutputVariableName,
                            type: DataType.INTEGER.value()
                    ],
                    deviceId: [
                            name: deviceIdOutputVariableName,
                            type: DataType.STRING.value()
                    ]
            ]
            nodes << (n.simulate ? [SaturationWithoutPulseTestDeviceNode: nodeContents] : [SaturationWithoutPulseDeviceNode: nodeContents])
        }

        addBooleanAssignmentNode(nodeName: "AN_${n.id}_CANCEL", nextNodeId: n.nextFail.id, variableName: "${n.id}.SAT##CANCEL", variableValue: true)
    }

    def patientMeasurementNodeForBloodSugar(n) {
        def deviceIdOutputVariableName = "${n.id}.BS#${MeasurementNode.DEVICE_ID_VAR}"
        def bloodSugarMeasurementsOutputVariableName = "${n.id}.BS#${MeasurementNode.BLOODSUGAR_VAR}"

        outputVariables[deviceIdOutputVariableName] = DataType.STRING.value()
        outputVariables[bloodSugarMeasurementsOutputVariableName] = DataType.INTEGER.value()

        def nodeContents = [
                nodeName: n.id as String,
                next: n.defaultNext.id as String,
                nextFail: "AN_${n.id}_CANCEL",
                text: n.text,
                bloodSugarMeasurements: [
                        name: bloodSugarMeasurementsOutputVariableName,
                        type: DataType.INTEGER.value()
                ],
                deviceId: [
                        name: deviceIdOutputVariableName,
                        type: DataType.STRING.value()
                ]
        ]

        if (n.mapToInputFields) {
            nodes << [BloodSugarManualDeviceNode: nodeContents]
        } else if (n.simulate) {
            nodes << [BloodSugarTestDeviceNode: nodeContents]
        } else {
            nodes << [BloodSugarDeviceNode: nodeContents]
        }

        addBooleanAssignmentNode(nodeName: "AN_${n.id}_CANCEL", nextNodeId: n.nextFail.id, variableName: "${n.id}.BS##CANCEL", variableValue: true)
    }

    def patientMeasurementNodeForLungFunction(n) {
        def deviceIdOutputVariableName = "${n.id}.LF#${MeasurementNode.DEVICE_ID_VAR}"
        def fev1OutputVariableName = "${n.id}.LF#${MeasurementNode.FEV1_VAR}"
        def fev6OutputVariableName = "${n.id}.LF#${MeasurementNode.FEV6_VAR}"
        def fev1Fev6RatioOutputVariableName = "${n.id}.LF#${MeasurementNode.FEV1_FEV6_RATIO_VAR}"
        def fef2575OutputVariableName = "${n.id}.LF#${MeasurementNode.FEF2575_VAR}"
        def goodTestOutputVariableName = "${n.id}.LF#${MeasurementNode.FEV_GOOD_TEST_VAR}"
        def softwareVersionOutputVariableName = "${n.id}.LF#${MeasurementNode.FEV_SOFTWARE_VERSION}"

        outputVariables[deviceIdOutputVariableName] = DataType.STRING.value()
        outputVariables[fev1OutputVariableName] = DataType.FLOAT.value()
        outputVariables[fev6OutputVariableName] = DataType.FLOAT.value()
        outputVariables[fev1Fev6RatioOutputVariableName] = DataType.FLOAT.value()
        outputVariables[fef2575OutputVariableName] = DataType.FLOAT.value()
        outputVariables[goodTestOutputVariableName] = DataType.BOOLEAN.value()
        outputVariables[softwareVersionOutputVariableName] = DataType.INTEGER.value()

        def nodeContents = [
                nodeName: n.id as String,
                next: n.defaultNext.id as String,
                nextFail: "AN_${n.id}_CANCEL",
                text: n.text,
                fev1: [
                        name: fev1OutputVariableName,
                        type: DataType.FLOAT.value()
                ],
                fev6: [
                        name: fev6OutputVariableName,
                        type: DataType.FLOAT.value()
                ],
                fev1Fev6Ratio: [
                        name: fev1Fev6RatioOutputVariableName,
                        type: DataType.FLOAT.value()
                ],
                fef2575: [
                        name: fef2575OutputVariableName,
                        type: DataType.FLOAT.value()
                ],
                goodTest: [
                        name: goodTestOutputVariableName,
                        type: DataType.BOOLEAN.value()
                ],
                softwareVersion: [
                        name: softwareVersionOutputVariableName,
                        type: DataType.INTEGER.value()
                ],
                deviceId: [
                        name: deviceIdOutputVariableName,
                        type: DataType.STRING.value()
                ]
        ]

        // No support for "n.mapToInputFields"
        if (n.simulate) {
            nodes << [LungMonitorTestDeviceNode: nodeContents]
        } else {
            nodes << [LungMonitorDeviceNode: nodeContents]
        }

        addBooleanAssignmentNode(nodeName: "AN_${n.id}_CANCEL", nextNodeId: n.nextFail.id, variableName: "${n.id}.LF##CANCEL", variableValue: true)
    }

    private def patientMeasurementNodeForUrine(n) {
        def outputVariableName = "${n.id}.${MeasurementNode.URINE_VAR}"
        outputVariables[outputVariableName] = DataType.INTEGER.value()

        nodes << [
            UrineDeviceNode: [
                nodeName: n.id as String,
                next: n.defaultNext.id as String,
                nextFail: "AN_${n.id}_CANCEL",
                text: n.text,
                urine : [
                    name: outputVariableName,
                    type: DataType.INTEGER.value()
                ]
            ]
        ]

        addBooleanAssignmentNode(nodeName: "AN_${n.id}_CANCEL", nextNodeId: n.nextFail.id, variableName: "${n.id}.${MeasurementNode.URINE_VAR}#CANCEL", variableValue: true)
    }

    private def patientMeasurementNodeForGlucoseInUrine(n) {
        def outputVariableName = "${n.id}.${MeasurementNode.GLUCOSE_URINE_VAR}"
        outputVariables[outputVariableName] = DataType.INTEGER.value()

        nodes << [
                GlucoseUrineDeviceNode: [
                        nodeName: n.id as String,
                        next: n.defaultNext.id as String,
                        nextFail: "AN_${n.id}_CANCEL",
                        text: n.text,
                        glucoseUrine : [
                                name: outputVariableName,
                                type: DataType.INTEGER.value()
                        ]
                ]
        ]

        addBooleanAssignmentNode(nodeName: "AN_${n.id}_CANCEL", nextNodeId: n.nextFail.id, variableName: "${n.id}.${MeasurementNode.GLUCOSE_URINE_VAR}#CANCEL", variableValue: true)
    }



    private def patientMeasurementNodeForCrp(n) {
        def outputVariableName = "${n.id}.${MeasurementNode.CRP_VAR}"
        outputVariables[outputVariableName] =  DataType.INTEGER.value()

        nodes << [
            CRPNode: [
                nodeName: n.id as String,
                text: n.text,
                next: n.defaultNext.id as String,
                nextFail: "AN_${n.id}_CANCEL",
                CRP : [
                    name: outputVariableName,
                    type: DataType.INTEGER.value()
                ]
            ]
        ]

        addBooleanAssignmentNode(nodeName: "AN_${n.id}_CANCEL", nextNodeId: n.nextFail.id, variableName: "${n.id}.${MeasurementNode.CRP_VAR}#CANCEL", variableValue: true)
    }

    private def patientMeasurementNodeForHemoglobin(n) {
        if (n.mapToInputFields) {
            addIoNode(nodeName: n.id, nextNodeId: n.defaultNext.id, elements: [
                    textViewElement(text: n.text),
                    editFloatElement(variableName: "${n.id}.${MeasurementNode.HEMOGLOBIN_VAR}"),
                    buttonsToSkipInputForNode(n)
            ])
        } else { // No support for simulated nodes for now.. Since all temp measurements are manual
            def outputVariableName = "${n.id}.${MeasurementNode.HEMOGLOBIN_VAR}"
            outputVariables[outputVariableName] = DataType.FLOAT.value()
            nodes << [
                    HaemoglobinDeviceNode: [
                            nodeName: n.id as String,
                            next: n.defaultNext.id as String,
                            nextFail: "AN_${n.id}_CANCEL",
                            text: n.text,
                            haemoglobinValue: [
                                    name: outputVariableName,
                                    type: DataType.FLOAT.value()
                            ]
                    ]
            ]
        }


        addBooleanAssignmentNode(nodeName: "AN_${n.id}_CANCEL", nextNodeId: n.nextFail.id, variableName: "${n.id}.${MeasurementNode.HEMOGLOBIN_VAR}#CANCEL", variableValue: true)
    }

    private def patientChoiceNodeExpression(n) {
        def variableName = inputVariableNameForDecisionNode(n)
        def options = [
            left: [
                type:n.dataType.value(),
                value: n.nodeValue
            ],
            right: [
                type: 'name',
                value: variableName
            ]
        ]

        if (n.operation == Operation.GREATER_THAN) {
            [ gt: options ]
        } else if (n.operation == Operation.LESS_THAN) {
            [ lt: options ]
        } else if (n.operation == Operation.EQUALS) {
            [ eq: options ]
        } else {
            throw new RuntimeException("Unsupported operation: ${n.operation}")
        }
    }

    private String inputVariableNameForMeasurementTime(PatientMeasurementNode dn) {
        if (dn.monicaMeasuringTimeInputNode) {
            "${dn.monicaMeasuringTimeInputNode.id}.${dn.monicaMeasuringTimeInputVar}"
        } else {
            null
        }
    }

    private String inputVariableNameForDecisionNode(PatientChoiceNode dn) {
        if (dn.inputNode.instanceOf(PatientBooleanNode)) {
            dn.inputVar
        } else if (!dn.inputNode.instanceOf(PatientMeasurementNode)) {
            "${dn.inputNode.id}.${dn.inputVar}"
        } else if (dn.inputNode.meterType.name.equals(MeterTypeName.CTG.value())) {
            "${dn.inputNode.id}.CTG#${dn.inputVar}"
        } else if (dn.inputNode.meterType.name.equals(MeterTypeName.BLOOD_PRESSURE_PULSE)) {
            "${dn.inputNode.id}.BP#${dn.inputVar}"
        } else if (dn.inputNode.meterType.name.equals(MeterTypeName.SATURATION)) {
            "${dn.inputNode.id}.SAT#${dn.inputVar}"
        } else if (dn.inputNode.meterType.name.equals(MeterTypeName.WEIGHT)) {
            "${dn.inputNode.id}.${MeasurementNode.WEIGHT_VAR}"
        } else if (dn.inputNode.meterType.name.equals(MeterTypeName.URINE)) {
            "${dn.inputNode.id}.${MeasurementNode.URINE_VAR}"
        } else if (dn.inputNode.meterType.name.equals(MeterTypeName.URINE_GLUCOSE)) {
            "${dn.inputNode.id}.${MeasurementNode.GLUCOSE_URINE_VAR}"
        } else if (dn.inputNode.meterType.name.equals(MeterTypeName.TEMPERATURE)) {
            "${dn.inputNode.id}.${MeasurementNode.TEMPERATURE_VAR}"
        } else if (dn.inputNode.meterType.name.equals(MeterTypeName.HEMOGLOBIN)) {
            "${dn.inputNode.id}.${MeasurementNode.HEMOGLOBIN_VAR}"
        } else if (dn.inputNode.meterType.name.equals(MeterTypeName.CRP)) {
            "${dn.inputNode.id}.${MeasurementNode.CRP_VAR}"
        } else if (dn.inputNode.meterType.name.equals(MeterTypeName.BLOODSUGAR)) {
            "${dn.inputNode.id}.BS#${dn.inputVar}"
        } else {
            'ERROR_unsupported'
        }
    }

    private void addBooleanAssignmentNode(parameters) {
        outputVariables[parameters.variableName] = DataType.BOOLEAN.value()

        nodes << [
            AssignmentNode: [
                nodeName: parameters.nodeName,
                next: parameters.nextNodeId as String,
                variable: [
                    name: parameters.variableName,
                    type: DataType.BOOLEAN.value()
                ],
                expression: [
                    type: DataType.BOOLEAN.value(),
                    value: parameters.variableValue
                ]
            ]
        ]
    }

    private void addStringAssignmentNode(parameters) {
        outputVariables[parameters.variableName] = DataType.STRING.value()

        nodes << [
            AssignmentNode: [
                nodeName: parameters.nodeName,
                next: parameters.nextNodeId as String,
                variable: [
                    name: parameters.variableName,
                    type: DataType.STRING.value()
                ],
                expression: [
                    type: DataType.STRING.value(),
                    value: parameters.variableValue
                ]
            ]
        ]
    }

    private void addIoNode(parameters) {
        def contents = [
            nodeName: parameters.nodeName as String,
            elements: parameters.elements
        ]
        if (parameters.nextNodeId) {
            contents['next'] = parameters.nextNodeId as String
        }

        nodes << [
            IONode: contents
        ]
    }

    private void addDelayNode(parameters) {
        def contents = [
                nodeName: parameters.nodeName as String,
                elements: parameters.elements,
                countTime: parameters.countTime,
                countUp: parameters.countUp,
                displayTextString: parameters.displayTextString
        ]
        if (parameters.nextNodeId) {
            contents['next'] = parameters.nextNodeId as String
        }

        nodes << [
                DelayNode: contents
        ]
    }

    private def textViewElement(parameters) {
        def contents = [text: parameters.text]
        if (parameters.header != null) {
            contents['header'] = parameters.header
        }

        [
            TextViewElement: contents
        ]
    }

    private def buttonsToSkipInputForNode(node) {
        [
            TwoButtonElement: [
                leftText: "Undlad",
                leftNext: "AN_${node.id}_CANCEL" as String,
                leftSkipValidation: true,

                rightText: "Næste",
                rightNext: node.defaultNext.id as String,
                rightSkipValidation: false
            ]
        ]
    }

    private def buttonElement(parameters) {
        def contents = [
            text: parameters.text,
            gravity: 'center',
            next: parameters.nextNodeId as String
        ]

        if (parameters.skipValidation != null) {
            contents['skipValidation'] = parameters.skipValidation
        }

        [
            ButtonElement: contents
        ]
    }

    private def twoButtonElement(parameters) {
        [
            TwoButtonElement: [
                leftText: parameters.leftText,
                leftNext: parameters.leftNextNodeId,
                rightText: parameters.rightText,
                rightNext: parameters.rightNextNodeId
            ]
        ]
    }

    private def choice(parameters) {
        [
            value: [
                type: DataType.STRING.value(),
                value: parameters.value
            ],
            text: parameters.text
        ]
    }

    private def radioButtonElement(parameters) {
        outputVariables[parameters.variableName] = DataType.STRING.value()

        [
            RadioButtonElement: [
                choices: parameters.choices,
                outputVariable: [
                    name: parameters.variableName,
                    type: DataType.STRING.value()
                ]
            ]
        ]
    }

    private def editStringElement(parameters) {
        outputVariables[parameters.variableName] = DataType.STRING.value()

        [
            EditTextElement: [
                outputVariable: [
                    name: parameters.variableName,
                    type: DataType.STRING.value()
                ]
            ]
        ]
    }

    private def editIntegerElement(parameters) {
        outputVariables[parameters.variableName] = DataType.INTEGER.value()

        [
            EditTextElement: [
                outputVariable: [
                    name: parameters.variableName,
                    type: DataType.INTEGER.value()
                ]
            ]
        ]
    }

    private def editFloatElement(parameters) {
        outputVariables[parameters.variableName] = DataType.FLOAT.value()

        [
            EditTextElement: [
                outputVariable: [
                    name: parameters.variableName,
                    type: DataType.FLOAT.value()
                ]
            ]
        ]
    }

    private def noteTextElement(parameters) {
        outputVariables[parameters.parent] = DataType.STRING.value()

        [
            NoteTextElement: [
                note: [
                    parent: parameters.parent,
                    type: DataType.STRING.value()
                ]
            ]
        ]
    }
}
