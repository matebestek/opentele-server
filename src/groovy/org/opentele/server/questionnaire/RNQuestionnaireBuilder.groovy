package org.opentele.server.questionnaire

import org.opentele.server.model.QuestionnaireUtil
import org.opentele.server.model.questionnaire.ChoiceValue
import org.opentele.server.model.questionnaire.DelayNode
import org.opentele.server.model.questionnaire.EndNode
import org.opentele.server.model.questionnaire.InputNode
import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.model.questionnaire.TextNode
import org.opentele.server.model.types.DataType
import org.opentele.server.model.types.Severity

/**
 * Created with IntelliJ IDEA.
 * User: henrik
 * Date: 28.01.13
 * Time: 21.39
 *
 * Helper class used for creating questionnaires for RH prod environment
 */
class RNQuestionnaireBuilder {

    QuestionnaireUtil questionnaireUtil = new QuestionnaireUtil()

    String createdByName

    def createKOLStandUpQuestionnaire(String title, String revision) {

        Questionnaire questionnaire = Questionnaire.findByNameAndRevision(title, revision)
        if (!questionnaire) {

            def nodes = []

            EndNode jEndNode = questionnaireUtil.createEndNode(createdBy: createdByName, modifiedBy: createdByName);
            nodes << jEndNode

            //--------- NODES AFTER EXERCISE IS COMPLETE

                /*
                    Hvordan udførte du rejse-sætte sig testen?
                        Uden brug af dine arme
                        Hænderne på dine lår
                        Hænderne på stolens armlæn
                 */
                InputNode methodNode = questionnaireUtil.createInputNode(text:"Hvordan udførte du rejse-sætte sig testen?",
                        shortText: "Rejse-sætte test. Modificeret?",
                        defaultNext: jEndNode,
                        inputType: DataType.RADIOCHOICE,
                        createdBy: createdByName, modifiedBy: createdByName)
                nodes << methodNode

                    ChoiceValue cv = questionnaireUtil.createChoiceValue(inputNode: methodNode,
                            createdBy: createdByName, modifiedBy: createdByName,
                            label: "Uden brug af dine arme", value: "Uden arme", ordering: 0)
                    methodNode.addToChoices(cv)

                    cv = questionnaireUtil.createChoiceValue(inputNode: methodNode,
                            createdBy: createdByName, modifiedBy: createdByName,
                            label: "Hænderne på dine lår", value: "Hænder på lår", ordering: 1)
                    methodNode.addToChoices(cv)

                    cv = questionnaireUtil.createChoiceValue(inputNode: methodNode,
                            createdBy: createdByName, modifiedBy: createdByName,
                            label: "Hænderne på stolens armlæn", value: "Hænder på armlæn", ordering: 2)
                    methodNode.addToChoices(cv)

                InputNode inputRepetitions = questionnaireUtil.createInputNode(text:"Indtast antallet af gentagelser, du klarede:",
                        shortText: "Rejse-sætte test. Antal gentagelser",
                        defaultNext: methodNode,
                        inputType: DataType.INTEGER,
                        createdBy: createdByName, modifiedBy: createdByName)

                nodes << inputRepetitions
            //---------

            //--------- NODES AFTER INSTRUCTION FLOW

                DelayNode countUpDuringExecise = questionnaireUtil.createDelayNode(text: "START og fortsæt i 30 sekunder.",
                        shortText: "Selve test tidsrum på 30 sek",
                        defaultNext: inputRepetitions,
                        countTime: 30,
                        countUp: true)

                nodes << countUpDuringExecise

                DelayNode countDownBeforeStart = questionnaireUtil.createDelayNode(text: "Nedtælling på 45 sek inden teststart",
                        shortText: "Nedtælling inden start på test.",
                        defaultNext: countUpDuringExecise,
                        countTime: 45,
                        countUp: false)

                nodes << countDownBeforeStart

                TextNode informCountDownBeforeStart = questionnaireUtil.createTextNode(defaultNext:countDownBeforeStart,
                        text:"Når du trykker \"Næste\", vil der gå 45 sekunder før testen automatisk starter. \n" +
                                "\n" +
                                "Du kan se, at der bliver talt ned indtil start på test.",
                        createdBy: createdByName,
                        modifiedBy: createdByName)

                nodes << informCountDownBeforeStart

            //---------

            //--------- NODES FOR INSTRUCTION
                TextNode instructionFourth = questionnaireUtil.createTextNode(defaultNext:informCountDownBeforeStart,
                        text:"Du tæller hver gang, du er oppe at stå med strakte ben. Det antal gange, du er oppe at stå, skal du efterfølgende indtaste.",
                        createdBy: createdByName,
                        modifiedBy: createdByName)

                nodes << instructionFourth

                TextNode instructionThird = questionnaireUtil.createTextNode(defaultNext:instructionFourth,
                        text:"Udfør helst testen på samme måde hver gang.\n" +
                                "\n" +
                                "Armene skal helst være over kors.\n" +
                                "\n" +
                                "Hvis du har behov for at bruge dine arme, kan du enten have dine hænder på stolens armlæn eller på dine lår.\n",
                        createdBy: createdByName,
                        modifiedBy: createdByName)

                nodes << instructionThird

                TextNode instructionSecond = questionnaireUtil.createTextNode(defaultNext:instructionThird,
                        text:"Det anbefales, at du anvender samme stol hver gang. Vælg en stol, der står godt fast. Placer gerne stoleryggen op ad en væg. Du kan bruge en spisebordsstol eventuelt med armlæn.\n" +
                                "\n" +
                                "Sæt dig lidt frem på stolens sæde med rank ryg og fødderne placeret fladt på gulvet.",
                        createdBy: createdByName,
                        modifiedBy: createdByName)

                nodes << instructionSecond

                TextNode instructionFirst = questionnaireUtil.createTextNode(defaultNext:instructionSecond,
                        text:"Du rejser og sætter dig fra en stol så mange gange, du kan klare på 30 sekunder. Hver gang du rejser dig, skal du stå, så dine ben er strakte.",
                        createdBy: createdByName,
                        modifiedBy: createdByName)

                nodes << instructionFirst
            //---------

            //--------- NODES FOR START + INTRO
                InputNode needInstruction = questionnaireUtil.createInputNode(
                        text:"Ønsker du at undvære instruktion og gå direkte til start på øvelsen?",
                        shortText:"Rejse-sætte. Undvære instruktion?",
                        inputType: DataType.BOOLEAN,
                        defaultNext: informCountDownBeforeStart,
                        defaultSeverity: Severity.GREEN,
                        alternativeNext: instructionFirst,
                        alternativeSeverity: Severity.GREEN,
                        createdBy: createdByName, modifiedBy: createdByName)

                nodes << needInstruction

                TextNode introNode = questionnaireUtil.createTextNode(defaultNext:needInstruction,
                        text:"Rejse-sætte sig test:\n" +
                                "Er for at teste/vurdere din muskelstyrke i underkroppen.",
                        createdBy: createdByName,
                        modifiedBy: createdByName)

                nodes << introNode

            questionnaire = questionnaireUtil.createQuestionnaire(name: title,
                    revision:revision,
                    startNode: introNode,
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
