package org.opentele.server.questionnaire

import org.opentele.server.model.Clinician
import org.opentele.server.model.MeterType
import org.opentele.server.model.QuestionnaireUtil
import org.opentele.server.model.questionnaire.*
import org.opentele.server.model.types.DataType
import org.opentele.server.model.types.MeterTypeName
import org.opentele.server.model.types.Operation
import org.opentele.server.model.types.Severity

/**
 * User: henrik
 * Date: 26.01.13
 * Time: 13.18
 *
 * Helper class used for creating questionnaires for test+devel environments
 */
class TestQuestionnaireBuilder {

    static String BP_STRING = "Gør dig klar til at tage blodtryk ved at montere blodtryksmanchetten. Tryk næste når du er klar"
    static String WEIGHT_STRING = "Find vægten frem, og tryk næste når du er klar til at blive vejet"
    static String URINE_STRING = "Mål proteinindholdet i din urin og indtast resultatet nedenfor"
    static String HEMO_STRING = "Mål hæmoglobinindholdet i dit blod og indtast resultatet nedenfor"
    static String CRP_STRING = "Indtast værdi eller vælg <5"

    static String MANUAL_BP_STRING = "Indtast dit blodtryk og din puls i felterne nedenfor."
    static String MANUAL_WEIGHT_STRING = "Mål din vægt og indtast resultatet i feltet nedenfor"
    static String MANUAL_TEMPERATURE_STRING = "Mål din temperatur og indtast resultatet i feltet nedenfor"

    QuestionnaireUtil questionnaireUtil = new QuestionnaireUtil()

    String createdByName

    def Questionnaire createYesNoQuestionnaire(Clinician creator, String title, String revision) {
        Questionnaire questionnaire = Questionnaire.findByNameAndRevision(title, revision)

        if (!questionnaire) {
            EndNode endNode = questionnaireUtil.createEndNode(createdBy: createdByName, modifiedBy: createdByName);

            InputNode ynInputNode = questionnaireUtil.createInputNode(text:"Svar Ja eller Nej..",
                    inputType: DataType.BOOLEAN,
                    defaultNext: endNode,
                    alternativeNext: endNode,
                    defaultSeverity: Severity.GREEN,
                    alternativeSeverity: Severity.RED,
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire = questionnaireUtil.createQuestionnaire(name: title,
                    revision:revision,
                    startNode: ynInputNode,
                    creator: creator,
                    creationDate: new Date(),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire.addToNodes(ynInputNode )
            questionnaire.addToNodes(endNode)

            questionnaire.save(failOnError:true)
        }
        questionnaire
    }

    def Questionnaire createWeightQuestionnaire(Clinician creator, boolean mapToInputFields, String title, String revision) {
        Questionnaire questionnaire = Questionnaire.findByNameAndRevision(title, revision)

        if (!questionnaire) {
            EndNode wEndNode = questionnaireUtil.createEndNode(createdBy: createdByName, modifiedBy: createdByName);

            MeasurementNode wNode = questionnaireUtil.createMeasurementNode(text: MANUAL_WEIGHT_STRING,
                    defaultNext:wEndNode,
                    nextFail:wEndNode,
                    mapToInputFields: mapToInputFields,
                    shortText: "Vægt",
                    meterType: MeterType.findByName(MeterTypeName.WEIGHT),
                    createdBy: createdByName, modifiedBy: createdByName)

            // Not used if mapToInputFields is true
            TextNode maalDinVaegt
            if (!mapToInputFields) {
                maalDinVaegt = questionnaireUtil.createTextNode(defaultNext:wNode, text:WEIGHT_STRING, createdBy: createdByName, modifiedBy: createdByName)
            }

            questionnaire = questionnaireUtil.createQuestionnaire(name: title,
                    revision:revision,
                    startNode: (mapToInputFields ? wNode : maalDinVaegt),
                    creator: creator,
                    creationDate: new Date(),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire.addToNodes(wNode)
            if (!mapToInputFields) {
                questionnaire.addToNodes(maalDinVaegt)
            }

            questionnaire.addToNodes(wEndNode)
            questionnaire.save(failOnError:true)
        }
        questionnaire
    }

    def createBPQuestionnaire(Clinician creator, boolean mapToInputFields, String title, String revision) {
        Questionnaire questionnaire = Questionnaire.findByNameAndRevision(title, revision)

        if (!questionnaire) {
            EndNode blodtrykEndNode = questionnaireUtil.createEndNode(createdBy: createdByName, modifiedBy: createdByName);

            MeasurementNode maalBlodtrykNode = questionnaireUtil.createMeasurementNode(text: MANUAL_BP_STRING,
                    defaultNext:blodtrykEndNode,
                    nextFail:blodtrykEndNode,
                    mapToInputFields: mapToInputFields,
                    shortText: "Blodtryk/puls",
                    meterType: MeterType.findByName(MeterTypeName.BLOOD_PRESSURE_PULSE),
                    createdBy: createdByName, modifiedBy: createdByName)

            TextNode maalDitBlodtryk

            if (!mapToInputFields) {
                maalDitBlodtryk = questionnaireUtil.createTextNode(defaultNext:maalBlodtrykNode,
                        text:BP_STRING,
                        createdBy: createdByName, modifiedBy: createdByName)
            }

            questionnaire = questionnaireUtil.createQuestionnaire(name: title,
                    revision:revision,
                    startNode: (mapToInputFields ? maalBlodtrykNode : maalDitBlodtryk),
                    creator: creator,
                    creationDate: new Date(),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire.addToNodes(maalBlodtrykNode)

            if (!mapToInputFields) {
                questionnaire.addToNodes(maalDitBlodtryk)
            }
            questionnaire.addToNodes(blodtrykEndNode)

            questionnaire.save(failOnError:true)
        }
        questionnaire
    }

    def createLungFunctionQuestionnaire(Clinician creator, String title, String revision) {
        Questionnaire questionnaire = Questionnaire.findByNameAndRevision(title, revision)

        if (!questionnaire) {
            EndNode lungefunktionEndNode = questionnaireUtil.createEndNode(createdBy: createdByName, modifiedBy: createdByName);

            MeasurementNode maalLungefunktionNode = questionnaireUtil.createMeasurementNode(
                    text: 'Måling af lungefunktion',
                    defaultNext:lungefunktionEndNode,
                    nextFail:lungefunktionEndNode,
                    shortText: "Lungefunktion",
                    meterType: MeterType.findByName(MeterTypeName.LUNG_FUNCTION),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire = questionnaireUtil.createQuestionnaire(name: title,
                    revision:revision,
                    startNode: maalLungefunktionNode,
                    creator: creator,
                    creationDate: new Date(),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire.addToNodes(maalLungefunktionNode)
            questionnaire.addToNodes(lungefunktionEndNode)

            questionnaire.save(failOnError:true)
        }
        questionnaire
    }

    def createUrineQuestionnaire(Clinician clJens, String title, String revision) {
        Questionnaire questionnaire = Questionnaire.findByNameAndRevision(title, revision)

        if (!questionnaire) {
            EndNode endNode = questionnaireUtil.createEndNode(createdBy: createdByName, modifiedBy: createdByName);

            MeasurementNode tNode = questionnaireUtil.createMeasurementNode(text: URINE_STRING,
                    inputType: DataType.FLOAT,
                    defaultSeverity: Severity.GREEN,
                    defaultNext:endNode,
                    nextFail:endNode,
                    shortText: "Proteinindhold i urin",
                    meterType: MeterType.findByName(MeterTypeName.URINE),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire = questionnaireUtil.createQuestionnaire(name: title,
                    revision:revision,
                    startNode: tNode,
                    creator: clJens,
                    creationDate: new Date(),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire.addToNodes(tNode)
            questionnaire.addToNodes(endNode)

            questionnaire.save(failOnError:true)
        }
        questionnaire
    }

    def createHemoQuestionnaire(Clinician creator, String title, String revision) {
        Questionnaire questionnaire = Questionnaire.findByNameAndRevision(title, revision)

        if (!questionnaire) {
            EndNode endNode = questionnaireUtil.createEndNode(createdBy: createdByName, modifiedBy: createdByName);

            MeasurementNode tNode = questionnaireUtil.createMeasurementNode(text: HEMO_STRING,
                    inputType: DataType.FLOAT,
                    defaultSeverity: Severity.GREEN,
                    defaultNext:endNode,
                    nextFail:endNode,
                    shortText: "Hæmoglobin indhold i blod",
                    meterType: MeterType.findByName(MeterTypeName.HEMOGLOBIN),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire = questionnaireUtil.createQuestionnaire(name: title,
                    revision:revision,
                    startNode: tNode,
                    creator: creator,
                    creationDate: new Date(),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire.addToNodes(tNode)
            questionnaire.addToNodes(endNode)

            questionnaire.save(failOnError:true)
        }
        questionnaire
    }

    def createCRPQuestionnaire(Clinician creator, String title, String revision) {
        Questionnaire questionnaire = Questionnaire.findByNameAndRevision(title, revision)

        if (!questionnaire) {
            EndNode endNode = questionnaireUtil.createEndNode(createdBy: createdByName, modifiedBy: createdByName);

            MeasurementNode tNode = questionnaireUtil.createMeasurementNode(text: CRP_STRING,
                    defaultSeverity: Severity.GREEN,
                    defaultNext:endNode,
                    nextFail:endNode,
                    shortText: "CRP",
                    meterType: MeterType.findByName(MeterTypeName.CRP),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire = questionnaireUtil.createQuestionnaire(name: title,
                    revision:revision,
                    startNode: tNode,
                    creator: creator,
                    creationDate: new Date(),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire.addToNodes(tNode)
            questionnaire.addToNodes(endNode)

            questionnaire.save(failOnError:true)
        }
        questionnaire
    }

    def createTemperatureQuestionnaire(Clinician creator, String title, String revision) {
        Questionnaire questionnaire = Questionnaire.findByNameAndRevision(title, revision)

        if (!questionnaire) {
            EndNode endNode = questionnaireUtil.createEndNode(createdBy: createdByName, modifiedBy: createdByName);

            MeasurementNode tNode = questionnaireUtil.createMeasurementNode(text: MANUAL_TEMPERATURE_STRING,
                    inputType: DataType.FLOAT,
                    defaultSeverity: Severity.GREEN,
                    defaultNext:endNode,
                    nextFail:endNode,
                    shortText: "Temperatur",
                    meterType: MeterType.findByName(MeterTypeName.TEMPERATURE),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire = questionnaireUtil.createQuestionnaire(name: title,
                    revision:revision,
                    startNode: tNode,
                    creator: creator,
                    creationDate: new Date(),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire.addToNodes(tNode)
            questionnaire.addToNodes(endNode)

            questionnaire.save(failOnError:true)
        }
        questionnaire
    }

    def createSaturationQuestionnaire(Clinician creator, boolean mapToInputFields, String title, String revision, MeterTypeName meterTypeName) {
        Questionnaire questionnaire = Questionnaire.findByNameAndRevision(title, revision)

        if (!questionnaire) {
            EndNode satEndNode = questionnaireUtil.createEndNode(createdBy: createdByName, modifiedBy: createdByName);

            MeasurementNode satNode = questionnaireUtil.createMeasurementNode(text: "Saturation",
                    defaultNext:satEndNode,
                    nextFail: satEndNode,
                    meterType: MeterType.findByName(meterTypeName),
                    shortText: MeterTypeName.SATURATION.equals(meterTypeName) ? "Saturation/puls" : "Saturation",
                    mapToInputFields: mapToInputFields,
                    createdBy: createdByName, modifiedBy: createdByName)

            TextNode maalSaturation = questionnaireUtil.createTextNode(defaultNext:satNode,
                    text:"Så skal der måles Saturation!", createdBy: createdByName, modifiedBy: createdByName)

            questionnaire = questionnaireUtil.createQuestionnaire(name: title,
                    revision:revision,
                    startNode: maalSaturation,
                    creator: creator,
                    creationDate: new Date(),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire.addToNodes(satNode)
            questionnaire.addToNodes(maalSaturation)
            questionnaire.addToNodes(satEndNode)

            questionnaire.save(failOnError:true)
        }
        questionnaire
    }

    def createBloodSugarQuestionnaire(Clinician creator, boolean mapToInputFields, String title, String revision) {
        Questionnaire questionnaire = Questionnaire.findByNameAndRevision(title, revision)

        if (!questionnaire) {
            EndNode endNode = questionnaireUtil.createEndNode(createdBy: createdByName, modifiedBy: createdByName);

            MeasurementNode measurementNode = questionnaireUtil.createMeasurementNode(text: "Blodsukker",
                    defaultNext:endNode,
                    nextFail: endNode,
                    meterType: MeterType.findByName(MeterTypeName.BLOODSUGAR),
                    shortText: "Blodsukker",
                    mapToInputFields: mapToInputFields,
                    createdBy: createdByName, modifiedBy: createdByName)

            TextNode maalNode = questionnaireUtil.createTextNode(defaultNext:measurementNode,
                    text:"Så skal der måles blodsukker!", createdBy: createdByName, modifiedBy: createdByName)

            questionnaire = questionnaireUtil.createQuestionnaire(name: title,
                    revision:revision,
                    startNode: maalNode,
                    creator: creator,
                    creationDate: new Date(),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire.addToNodes(measurementNode)
            questionnaire.addToNodes(maalNode)
            questionnaire.addToNodes(endNode)

            questionnaire.save(failOnError:true)
        }
        questionnaire
    }

    def createRadioButtonQuestionnaire(Clinician creator, String title, String revision) {
        Questionnaire questionnaire = Questionnaire.findByNameAndRevision(title, revision)

        if (!questionnaire) {
            EndNode endNode = questionnaireUtil.createEndNode(createdBy: createdByName, modifiedBy: createdByName);

            InputNode inputNode = questionnaireUtil.createInputNode(text:"Test radioknapper",
                    defaultNext: endNode,
                    inputType: DataType.RADIOCHOICE,
                    createdBy: createdByName,
                    modifiedBy: createdByName)

            ChoiceValue cv = questionnaireUtil.createChoiceValue(inputNode: inputNode,
                    createdBy: createdByName, modifiedBy: createdByName,
                    label: "Red", value: "Rød")
            inputNode.addToChoices(cv)

            cv = questionnaireUtil.createChoiceValue(inputNode: inputNode,
                    createdBy: createdByName, modifiedBy: createdByName,
                    label: "Purple",
                    value: "Lilla")
            inputNode.addToChoices(cv)

            cv = questionnaireUtil.createChoiceValue(inputNode: inputNode,
                    createdBy: createdByName, modifiedBy: createdByName,
                    label: "Blue",
                    value: "Blå")
            inputNode.addToChoices(cv)

            TextNode introNode = questionnaireUtil.createTextNode(defaultNext:inputNode,
                    text:"Radioknap test!", createdBy: createdByName, modifiedBy: createdByName)

            questionnaire = questionnaireUtil.createQuestionnaire(name: title,
                    revision:revision,
                    startNode: introNode,
                    creator: creator,
                    creationDate: new Date(),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire.addToNodes(inputNode)
            questionnaire.addToNodes(introNode)
            questionnaire.addToNodes(endNode)

            questionnaire.save(failOnError:true)
        }
        questionnaire
    }

    def createCTGQuestionnaire(Clinician creator, String title, String revision) {
        Questionnaire questionnaire = Questionnaire.findByNameAndRevision(title, revision)

        if (!questionnaire) {
            EndNode ctgEndNode = questionnaireUtil.createEndNode(createdBy: createdByName, modifiedBy: createdByName);

            MeasurementNode ctgNode = questionnaireUtil.createMeasurementNode(text: "CTG",
                    defaultNext:ctgEndNode,
                    nextFail: ctgEndNode,
                    meterType: MeterType.findByName(MeterTypeName.CTG),
                    shortText: "CTG",
                    createdBy: createdByName, modifiedBy: createdByName)

            TextNode maalDitCtg = questionnaireUtil.createTextNode(defaultNext:ctgNode,
                    text:"Så skal der måles CTG!", createdBy: createdByName, modifiedBy: createdByName)

            questionnaire = questionnaireUtil.createQuestionnaire(name: title,
                    revision:revision,
                    requiresManualInspection: true,
                    startNode: maalDitCtg,
                    creator: creator,
                    creationDate: new Date(),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire.addToNodes(ctgNode)
            questionnaire.addToNodes(maalDitCtg)
            questionnaire.addToNodes(ctgEndNode)

            questionnaire.save(failOnError:true)
        }
        questionnaire
    }

    /**
     * Create test CTG questionnaire, with an added node for determining how many minutes CTG measurement should continue
     */
    def createTimedCTGQuestionnaire(Clinician creator, String title, String revision) {
        Questionnaire questionnaire = Questionnaire.findByNameAndRevision(title, revision)

        if (!questionnaire) {
            EndNode ctgEndNode = questionnaireUtil.createEndNode(createdBy: createdByName, modifiedBy: createdByName);

            MeterType ctgMeterType = MeterType.findByName(MeterTypeName.CTG)

            MeasurementNode ctgNode = questionnaireUtil.createMeasurementNode(text: "CTG (m/tid)",
                    defaultNext:ctgEndNode,
                    nextFail: ctgEndNode,
                    meterType: ctgMeterType,
                    shortText: "CTG (m/tid)",
                    createdBy: createdByName, modifiedBy: createdByName)

            InputNode maalDitCtg = questionnaireUtil.createInputNode(text:"Indtast antal minutter CTG målingen skal vare",
                    defaultNext: ctgNode,
                    inputType: DataType.INTEGER, createdBy: createdByName, modifiedBy: createdByName)

            ctgNode.setMonicaMeasuringTimeInputNode(maalDitCtg)
            ctgNode.setMonicaMeasuringTimeInputVar("FIELD")
            ctgNode.save(failOnError:true)

            questionnaire = questionnaireUtil.createQuestionnaire(name: title,
                    revision:revision,
                    requiresManualInspection: true,
                    startNode: maalDitCtg,
                    creator: creator,
                    creationDate: new Date(),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire.addToNodes(ctgNode)
            questionnaire.addToNodes(maalDitCtg)
            questionnaire.addToNodes(ctgEndNode)

            questionnaire.save(failOnError:true)
        }
        questionnaire
    }

    // Test schema for use when testing/developing JSON
    def createJsonTestQuestionnaire(Clinician creator, String title, String revision) {
        Questionnaire questionnaire = Questionnaire.findByNameAndRevision(title, revision)

        if (!questionnaire) {
            EndNode theEndNode = questionnaireUtil.createEndNode(createdBy: createdByName, modifiedBy: createdByName);
            InputNode reasonInputNode = questionnaireUtil.createInputNode(text:"Indtast årsag", defaultNext: theEndNode, inputType: DataType.STRING, createdBy: createdByName, modifiedBy: createdByName)

            TextNode oopsNode = questionnaireUtil.createTextNode(defaultNext:theEndNode,
                    text:"Oops- Vejningen fejlede eller blev sprunget over..!",
                    createdBy: createdByName, modifiedBy: createdByName)

            ChoiceNode theChoiceNode = questionnaireUtil.createChoiceNode(operation: Operation.GREATER_THAN,
                    dataType: DataType.INTEGER,
                    defaultNext: theEndNode,
                    defaultSeverity: Severity.GREEN,
                    alternativeNext: reasonInputNode,
                    alternativeSeverity: Severity.RED,
                    nodeValue: new Integer(50),
                    createdBy: createdByName, modifiedBy: createdByName)

            MeterType weightMeterType = MeterType.findByName(MeterTypeName.WEIGHT)

            MeasurementNode theMeasurementNode = questionnaireUtil.createMeasurementNode(text: "Vægt",
                    shortText: "Vægt",
                    defaultNext:theChoiceNode,
                    nextFail:oopsNode,
                    meterType: weightMeterType,
                    createdBy: createdByName, modifiedBy: createdByName)

            theChoiceNode.setInputNode(theMeasurementNode);
            theChoiceNode.setInputVar(MeasurementNode.WEIGHT_VAR)
            theChoiceNode.save(failOnError:true)


            TextNode firstTextNode = questionnaireUtil.createTextNode(defaultNext:theMeasurementNode,
                    text:"Det var ærgerligt!",
                    createdBy: createdByName, modifiedBy: createdByName)

            InputNode areYouOKInputNode = questionnaireUtil.createInputNode(text:"Har du det godt?",
                    inputType: DataType.BOOLEAN,
                    defaultNext: theMeasurementNode,
                    alternativeNext: firstTextNode,
                    defaultSeverity: Severity.GREEN,
                    alternativeSeverity: Severity.RED,
                    createdBy: createdByName, modifiedBy: createdByName)


            TextNode introTextNode = questionnaireUtil.createTextNode(defaultNext:areYouOKInputNode,
                    text:"Velkommen til skemaet!",
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire = questionnaireUtil.createQuestionnaire(name: title,
                    revision: revision,
                    startNode: introTextNode,
                    creator: creator,
                    creationDate: new Date(),
                    createdBy: createdByName, modifiedBy: createdByName)

            questionnaire.addToNodes(introTextNode)
            questionnaire.addToNodes(areYouOKInputNode)
            questionnaire.addToNodes(firstTextNode)
            questionnaire.addToNodes(theMeasurementNode)
            questionnaire.addToNodes(theChoiceNode)
            questionnaire.addToNodes(theMeasurementNode)
            questionnaire.addToNodes(reasonInputNode)
            questionnaire.addToNodes(oopsNode)
            questionnaire.addToNodes(theEndNode)

            questionnaire.save(failOnError:true)
        }
        questionnaire
    }

    def createRNKOLQuestionnaire(String title, String revision) {
        Questionnaire questionnaire = Questionnaire.findByNameAndRevision(title, revision)

        if (!questionnaire) {
            def nodes = []

            EndNode endNode = questionnaireUtil.createEndNode(createdBy: createdByName, modifiedBy: createdByName);
            nodes << endNode

            TextNode thankYouNode = questionnaireUtil.createTextNode(defaultNext:endNode,
                    text: "Dine målinger og besvarelser vil bliver sendt til videre evaluering. Tak!", createdBy: createdByName, modifiedBy: createdByName)
            nodes << thankYouNode

            // Inhaler - right side

            TextNode gettingWorse = questionnaireUtil.createTextNode(defaultNext:thankYouNode,
                    text:"Hvis din tilstand forværres, så du bliver bekymret over dine symptomer, kontakt da din læge eller sygeplejerske.",
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << gettingWorse

            TextNode triedInhalerNo = questionnaireUtil.createTextNode(defaultNext:gettingWorse,
                    text:"Husk at anvende din medicin. Brug dine vejrtrækningsteknikker, pep fløjte og skift stilling hyppigt.",
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << triedInhalerNo

            TextNode inhalerHelps = questionnaireUtil.createTextNode(defaultNext:gettingWorse,
                    text:"Godt. Brug dine vejrtrækningsteknikker, pep fløjte og skift stilling hyppigt. Husk fortsat din medicin.",
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << inhalerHelps

            TextNode inhalerHelpsNot = questionnaireUtil.createTextNode(defaultNext:gettingWorse,
                    text:"Brug dine vejrtrækningsteknikker, pep fløjte og skift stilling hyppigt. Husk fortsat din medicin.",
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << inhalerHelpsNot

            InputNode doesInhalerHelp = questionnaireUtil.createInputNode(
                    text:"Godt. Hjælper det at bruge din inhalator?",
                    shortText:"Hjælper inhalator",
                    inputType: DataType.BOOLEAN,
                    defaultNext: inhalerHelps,
                    defaultSeverity: Severity.GREEN,
                    alternativeNext: inhalerHelpsNot,
                    alternativeSeverity: Severity.GREEN,
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << doesInhalerHelp


            InputNode triedInhaler = questionnaireUtil.createInputNode(
                    text:"Godt. Har du brugt din inhalator?",
                    shortText:"Brugt inhalator?",
                    inputType: DataType.BOOLEAN,
                    defaultNext: doesInhalerHelp,
                    defaultSeverity: Severity.GREEN,
                    alternativeNext: triedInhalerNo,
                    alternativeSeverity: Severity.GREEN,
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << triedInhaler


            // End of inhaler part

            TextNode rememberBreathingEtcLeft = questionnaireUtil.createTextNode(defaultNext:thankYouNode,
                    text:"Brug vejrtrækningsteknikker, pep fløjte og skift stilling hyppigt. Kontakt din læge eller sygeplejerske hvis der sker forværring i dine symptomer eller der ikke er tegn på bedring efter 2 døgns behandling.",
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << rememberBreathingEtcLeft

            TextNode rememberBreathingLeft = questionnaireUtil.createTextNode(defaultNext:thankYouNode,
                    text:"Brug dine vejrtrækningsteknikker, pep fløjte og skift stilling hyppigt.",
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << rememberBreathingLeft

            InputNode antibioticsYes = questionnaireUtil.createInputNode(
                    text:"Har du fået det bedre efter opstart med antibiotika?",
                    shortText:"Antibiotika hjælper?",
                    inputType: DataType.BOOLEAN,
                    defaultNext: rememberBreathingLeft,
                    defaultSeverity: Severity.GREEN,
                    alternativeNext: rememberBreathingEtcLeft,
                    alternativeSeverity: Severity.GREEN,
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << antibioticsYes

            TextNode rememberBreathing = questionnaireUtil.createTextNode(defaultNext:thankYouNode,
                    text:"Husk fortsat dine vejrtrækningsteknikker, pep fløjte og skift stilling hyppigt.",
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << rememberBreathing

            TextNode antibioticsNo = questionnaireUtil.createTextNode(defaultNext:rememberBreathing,
                    text:"Hvis du har en selvbehandlingsplan start da op på denne, eller kontakt din læge. Er du i tvivl om din behandling kontakt da din læge.",
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << antibioticsNo

            InputNode antibiotics = questionnaireUtil.createInputNode(
                    text:"Er du i øjeblikket i behandling med antibiotika?",
                    shortText:"Antibiotika?",
                    inputType: DataType.BOOLEAN,
                    defaultNext: antibioticsYes,
                    defaultSeverity: Severity.GREEN,
                    alternativeNext: antibioticsNo,
                    alternativeSeverity: Severity.GREEN,
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << antibiotics



            TextNode followSelfTreatmentPlan = questionnaireUtil.createTextNode(defaultNext:thankYouNode,
                    text:"Hvis farven på slim ændres skal du følge anvisning på din selvbehandlingsplan.",
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << followSelfTreatmentPlan

            TextNode ifMoreSlimeLater = questionnaireUtil.createTextNode(defaultNext:thankYouNode,
                    text:"Godt. Hvis du får en tiltagende mængde slim, skal du følge anvisningerne fra din selvbehandlingsplan, hvis du har en - ellers kontakt din læge.",
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << ifMoreSlimeLater

            InputNode colorChangeNoLeft = questionnaireUtil.createInputNode(
                    text:"Plejer farven på slim at ændre sig når du har forværring?",
                    shortText:"Farveændring ved forværring?",
                    inputType: DataType.BOOLEAN,
                    defaultNext: followSelfTreatmentPlan,
                    defaultSeverity: Severity.GREEN,
                    alternativeNext: ifMoreSlimeLater,
                    alternativeSeverity: Severity.GREEN,
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << colorChangeNoLeft

            InputNode colorChangeLeft = questionnaireUtil.createInputNode(
                    text:"Er farven på slim ændret?",
                    shortText:"Slim farve ændret",
                    inputType: DataType.BOOLEAN,
                    defaultNext: antibiotics,
                    defaultSeverity: Severity.GREEN,
                    alternativeNext: colorChangeNoLeft,
                    alternativeSeverity: Severity.GREEN,
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << colorChangeLeft

            InputNode colorChangeNoRight = questionnaireUtil.createInputNode(
                    text:"Plejer farven på slim at ændre sig når du har forværring?",
                    shortText:"Farveændring ved forværring?",
                    inputType: DataType.BOOLEAN,
                    defaultNext: followSelfTreatmentPlan,
                    defaultSeverity: Severity.GREEN,
                    alternativeNext: antibiotics,
                    alternativeSeverity: Severity.GREEN,
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << colorChangeNoRight

            InputNode colorChangeRight = questionnaireUtil.createInputNode(
                    text:"Er farven på slim ændret?",
                    shortText:"Slim farve ændret",
                    inputType: DataType.BOOLEAN,
                    defaultNext: antibiotics,
                    defaultSeverity: Severity.GREEN,
                    alternativeNext: colorChangeNoRight,
                    alternativeSeverity: Severity.GREEN,
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << colorChangeRight

            InputNode moreSlime = questionnaireUtil.createInputNode(
                    text:"Er mængden af slim forøget?",
                    shortText:"Slim forøget",
                    inputType: DataType.BOOLEAN,
                    defaultNext: colorChangeRight,
                    defaultSeverity: Severity.GREEN,
                    alternativeNext: colorChangeLeft,
                    alternativeSeverity: Severity.GREEN,
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << moreSlime

            InputNode coughSlime = questionnaireUtil.createInputNode(
                    text:"Hoster du slim op?",
                    shortText:"Hoster slim?",
                    inputType: DataType.BOOLEAN,
                    defaultNext: moreSlime,
                    defaultSeverity: Severity.GREEN,
                    alternativeNext: triedInhaler,
                    alternativeSeverity: Severity.GREEN,
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << coughSlime

            InputNode coughMoreLeft = questionnaireUtil.createInputNode(
                    text:"Hoster du mere end vanligt?",
                    shortText:"Mere host",
                    inputType: DataType.BOOLEAN,
                    defaultNext: coughSlime,
                    defaultSeverity: Severity.RED,
                    alternativeNext: coughSlime,
                    alternativeSeverity: Severity.GREEN,
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << coughMoreLeft

            // BP - Left side of questionnaire

            MeasurementNode bpResultLeft = questionnaireUtil.createMeasurementNode(text: "Indtast blodtryk og puls",
                    defaultNext:coughMoreLeft,
                    nextFail:coughMoreLeft,
                    simulate: false,
                    mapToInputFields: true,
                    shortText: "Blodtryk/puls",
                    meterType: MeterType.findByName(MeterTypeName.BLOOD_PRESSURE_PULSE),
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << bpResultLeft

            TextNode prepareBPLeft = questionnaireUtil.createTextNode(defaultNext:bpResultLeft,
                    text:"Placer manchetten omkring armen. Før måling af blodtryk er det vigtigt at sidde stille i ca. 5 min. Tryk \"Start\" på blodtryksmåleren når du er klar. Tryk \"Næste\" når målingen er færdig.",
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << prepareBPLeft


            // BP - right side of questionnaire
            MeasurementNode bpResultRight = questionnaireUtil.createMeasurementNode(text: "Indtast blodtryk og puls",
                    defaultNext:coughSlime,
                    nextFail:coughSlime,
                    simulate: false,
                    mapToInputFields: true,
                    shortText: "Blodtryk/puls",
                    meterType: MeterType.findByName(MeterTypeName.BLOOD_PRESSURE_PULSE),
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << bpResultRight

            TextNode prepareBPRight = questionnaireUtil.createTextNode(defaultNext:bpResultRight,
                    text:"Placer manchetten omkring armen. Før måling af blodtryk er det vigtigt at sidde stille i ca. 5 min. Tryk \"Start\" på blodtryksmåleren når du er klar. Tryk \"Næste\" når målingen er færdig.",
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << prepareBPRight

            TextNode keepTraining = questionnaireUtil.createTextNode(defaultNext:thankYouNode,
                    text:"Fortsæt med din fysiske træning og spis rigtigt. Husk at tage din lungemedicin.", createdBy: createdByName, modifiedBy: createdByName)
            nodes << keepTraining

            TextNode keepBreathing = questionnaireUtil.createTextNode(defaultNext:keepTraining,
                    text:"Godt. Fortsæt med dine vejrtrækningsteknikker og brug pep fløjten.", createdBy: createdByName, modifiedBy: createdByName)
            nodes << keepBreathing

            InputNode coughMore = questionnaireUtil.createInputNode(
                    text:"Hoster du mere end vanligt?",
                    shortText:"Hoster mere",
                    inputType: DataType.BOOLEAN,
                    defaultNext: prepareBPRight,
                    defaultSeverity: Severity.YELLOW,
                    alternativeNext: keepBreathing,
                    alternativeSeverity: Severity.GREEN,
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << coughMore

            InputNode aandeNoed = questionnaireUtil.createInputNode(
                    text:"Har du mere åndenød end vanligt?",
                    shortText:"Mere åndenød",
                    inputType: DataType.BOOLEAN,
                    defaultNext: prepareBPLeft,
                    defaultSeverity: Severity.GREEN,
                    alternativeNext: coughMore,
                    alternativeSeverity: Severity.GREEN,
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << aandeNoed

            MeasurementNode manualSatNode = questionnaireUtil.createMeasurementNode(text: "Indtast resultatet fra din saturationsmåling.",
                    defaultNext:aandeNoed,
                    nextFail: aandeNoed,
                    meterType: MeterType.findByName(MeterTypeName.SATURATION),
                    shortText: "Saturation/puls (manuel)",
                    mapToInputFields: true,
                    simulate: false,
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << manualSatNode

            InputNode manuelMaaling = questionnaireUtil.createInputNode(
                    text:"Hvis måling ikke overføres, ønsker du så at indtaste måling?",
                    shortText:"Mere åndenød",
                    inputType: DataType.BOOLEAN,
                    defaultNext: manualSatNode,
                    defaultSeverity: Severity.GREEN,
                    alternativeNext: aandeNoed,
                    alternativeSeverity: Severity.GREEN,
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << manuelMaaling

            MeasurementNode satNode = questionnaireUtil.createMeasurementNode(text: "Indtast resultatet fra din saturationsmåling.",
                    defaultNext:aandeNoed,
                    nextFail: manuelMaaling,
                    meterType: MeterType.findByName(MeterTypeName.SATURATION),
                    shortText: "Saturation/puls",
                    mapToInputFields: true,
                    simulate: false,
                    createdBy: createdByName, modifiedBy: createdByName)
            nodes << satNode

            TextNode warmUpNode = questionnaireUtil.createTextNode(defaultNext:satNode,
                    text: "Varm din finger godt op. Når fingeren er varm sættes iltmåleren på. Tryk herefter 'Næste' for at starte en måling.", createdBy: createdByName, modifiedBy: createdByName)
            nodes << warmUpNode

            questionnaire = questionnaireUtil.createQuestionnaire(name: title,
                    revision:revision,
                    startNode: warmUpNode,
                    creationDate: new Date(),
                    createdBy: createdByName, modifiedBy: createdByName)

            nodes.reverseEach() { node ->
                questionnaire.addToNodes(node)
            }
            questionnaire.save(failOnError:true)
        }
        questionnaire
    }
}
