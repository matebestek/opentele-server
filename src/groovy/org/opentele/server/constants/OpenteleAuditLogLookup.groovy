package org.opentele.server.constants

import dk.silverbullet.kih.api.auditlog.AuditLogLookup
import org.slf4j.LoggerFactory

/**
 * Created with IntelliJ IDEA.
 * User: lch
 * Date: 11/16/12
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */
class OpenteleAuditLogLookup implements AuditLogLookup {

    def log = LoggerFactory.getLogger(OpenteleAuditLogLookup.class)
    Map lookup = [:]

    OpenteleAuditLogLookup() {
        // Setup controllers ....

        lookup["login"] = [
                "auth": "Log ind",
                "authFail": "Fejlet login",
                "denied": "Log ind afvist",
                "ajaxDenied": "Klient log ind afvist",
                "ajaxSuccess": "Klient log ind",
                "authAjax": "Klient login",
                "authfail": "Fejlet login",
                "full": "Log ind",
                "index": "Log ing"
        ]

        lookup["dbdoc"] = [
                "index": "DB Viewer"
        ]

        lookup["logout"] = [
                "logout": "Log ud",
                "index": "Vis log ind",
                "list": "Log ud list",
        ]

        lookup["password"] = [
                "index": "Index",
                "change": "Ændre adgangskode",
                "changed": "Adgangskode ændret",
                "update": "Opdaterer adgangskoden"
        ]

        lookup["home"] = [
                "index": "Forside"
        ]

        lookup["user"] = addStandardTexts("bruger", "brugere")
        lookup["role"] = addStandardTexts("rolle", "roller")
        lookup["rolePermission"] = addStandardTexts("Rolle rettighed", "Rolle rettigheder")
        lookup["urineThreshold"] = addStandardTexts("proteintærskel", "proteintærskler")
        lookup["urineGlucoseThreshold"] = addStandardTexts("proteintærskel", "proteintærskler")
        lookup["standardThresholdSet"] = addStandardTexts("Standard tærskelværdi", "Standard tærskelværdier")
        lookup["standardThresholdSet"] << [
                "addThreshold": "Tilføj tærskelværdi",
                "chooseThreshold": "Valgt tærskelværditype",
//                "convertStringParamsToFloats": "Konvertere data for tærskelværdier",
                "removeThreshold": "Fjern tærskelværdi",
                "saveThresholdToSet": "Gem tærskelværdi"
        ]


        lookup["bloodPressureThreshold"] = addStandardTexts("blodtrykstærskel", "blodtrykstærskler")
        lookup["numericThreshold"] = addStandardTexts("numerisk tærskel", "numeriske tærskler")
        lookup["completedQuestionnaire"] = addStandardTexts("afslut spørgeskema", "afslut spørgeskemaer")
        lookup["nodeResult"] = addStandardTexts("knuderesultat", "knuderesultater")
        lookup["patientQuestionnaire"] = addStandardTexts("spørgeskema", "spørgeskemaer")
        lookup["questionnaireNode"] = addStandardTexts("spørgeskemaknude", "spørgeskemaknuder")

        lookup["clinician2PatientGroup"] = addStandardTexts("kliniker til patientgruppe", "klinikere til patientgrupper")
        lookup["conference"] = addStandardTexts("videokonference", "videokonferencer")
        lookup["conference"] << [
            'initializeCall': 'Ring op',
            'noVideo': 'Video ikke tilgængelig',
            'unauthorized': 'Forkert video-brugernavn eller -password',
            'unknownVideoProblem': 'Ukendt video-problem',
            'linkEndpoint': 'Knytter klient til server',
            'joinConference': 'Melder sig til videokonference',
            'conferenceJoined': 'Er kliniker meldt til videokonference?',
            'finishSettingUpConference': 'Afsluttende opsætning af videokonference',
            'activeConference': 'Deltager i videokonference',
            'conferenceActiveWithOtherPatient': 'Deltager i videokonference med anden patient',
            'startVideoClient': 'Start videoklient',
            'endpointIdCallback': 'Callback for videoklient',
            'endConference': 'Afslut videokonference',
            'conferenceEnded': 'Videokonference afsluttet',
            'patientHasPendingConference': 'Har patient en kørende konference?',
            'patientHasPendingMeasurement': 'Har patient en igangværende måling?',
            'measurementFromPatient': 'Måling under videokonference'
        ]
        lookup["conferenceMeasurements"] = addStandardTexts("videokonferencemåling", "videokonferencemålinger")
        lookup["conferenceMeasurements"] << [
            'keepAlive': 'Opretholdelse af tilstand',
            'alreadyCompleted': 'Allerede færdig med målingskladder',
            'loadForm': 'Opret ny målingskladde',
            'updateMeasurement': 'Opdater målingskladde',
            'deleteMeasurement': 'Slet målingskladde',
            'loadAutomaticMeasurement': 'Opdater målingskladde fra klient',
            'confirm': 'Bekræft målingskladder',
            'finish': 'Færdiggør målingskladder',
            'close': 'Luk målingskladde-vindue'
        ]
        lookup["auditLogEntry"] = addStandardTexts("hændelseslogning", "hændelseslogninger")
        lookup["auditLogEntry"] << [
                "ajaxGetActions": "Hent handlinger",
                "createDateFromParams": "Opret dato felter",
                "likeExpression": "Søg hændelseslog",
                "search": "Søg hændelseslog"
        ]

        lookup["example"] = addStandardTexts("eksempel", "eksempler")
        lookup["example"] << [
                "custom": "Eksempler"
        ]

        lookup["defaultValueThreshold"] = addStandardTexts("standard tærskelværdi", "standard tærskelværdier")

        lookup["clinician"] = addStandardTexts("kliniker", "klinikere")
        lookup["clinician"] << [
                "doRemovePatientGroup": "Fjern kliniker fra patientgruppe",
                "removePatientGroup": "Fjern kliniker fra patientgruppe",
                "changePassword": "Ændre kliniker-password",
                "doChangePassword": "Gem nyt kliniker-password",
                "resetPassword": "Tildeler kliniker midlertidig password",
                "unlockAccount": "Fjerner lås på konto",

        ]

        lookup["measurement"] = addStandardTexts("måling", "målinger")
        lookup["measurement"] << [
                "patientGraphs": "Grafer",
                "patientMeasurements": "Målingsperiode",
                "graph": "Graf over målinger",
                "measurementTypes": "Målingstyper",
                "overview": "Målingsoverblik",
                "upload": "Upload målinger",
                "bloodsugar": "Blodsukkervisning",
        ]

        lookup["measurementType"] = addStandardTexts("målingstype", "målingstyper")

        lookup["message"] = addStandardTexts("besked", "beskeder")
        lookup["message"] << [
                "listToDepartment": "Beskeder til afdeling",
                "listToPatient": "Beskeder til patient",
                "messageRecipients": "Hent beskedmodtager-liste",
                "unread": "Marker som ulæst",
                "reply": "Besvar besked",
                "createJsonForClinicians": "Send afdelinger til klient",
                "messageAsJson": "Besked til afdeling fra klient",
                "newMessages": "Nye beskeder til patient",
                "read": "Marker besked som læst",
                "markAsRead": "Marker beskeder som læst"
        ]

        lookup["meter"] = addStandardTexts("måler", "målere")
        lookup["meter"] << [
                "attachMeter": "Tilknyt måler"
        ]

        lookup["monitoringPlan"] = addStandardTexts("monitoreringsplan", "monitoreringsplaner")

        lookup["monitorKit"] = addStandardTexts("monitorkit", "monitoreringkits")
        lookup["monitorKit"] << [
                "attachMeter": "Tilføj måler",
                "removeMeter": "Fjern måler"
        ]

        lookup["nextOfKinPerson"] = addStandardTexts("pårørende", "pårørende")


        lookup["patient"] = addStandardTexts("patient", "patienter")
        lookup["patient"] << addPatientText()
        lookup["patient"] << [
                "resetPassword": "Tildeler patient midlertidig password",
                "sendPassword": "Sender midlertidig password til patient",
                "unlockAccount": "Fjerner lås på konto",
                "chooseThreshold": "Vælg tærskelværdi"

        ]

        lookup["patientOverview"] = [
            "index": "Patientoverblik",
            "details": "Patientoverbliksdetaljer",
            "acknowledgeAll": "Kvittér alle",
            "acknowledgeAllForAll": "Kvittér alle for alle",
            "acknowledgeQuestionnaireAndRenderDetails": "Kvittér og genopfrisk patientdetaljer",
        ]

        lookup["patientNote"] = addStandardTexts("patient note", "patient noter")
        lookup["patientNote"] << [
                "markSeen": "Markér set",
                "listTeam": "Vis alle noter for team"
        ]

        lookup["questionnaire"] = addStandardTexts("spørgeskema", "spørgeskemaer")
        lookup["questionnaire"] << [
                "download": "Hent spørgeskema",
                "upload": "Indsend spørgeskema",
                "toggleIgnoreQuestionnaire": "Ignorer spørgeskema",
                "toggleIgnoreNode": "Ignorer måling",
                "acknowledge": "Kvittér",
                "acknowledgements": "Hent kvitteringer",
                "sendAcknowledgeAutoMessage": "Send automatisk besked til patient om godkendt (grøn) måling",
                "listing": "Spørgeskema liste",
        ]

        lookup["questionnaireGroup"] = addStandardTexts("spørgeskemagruppe", "spørgeskemagrupper")
        lookup["questionnaireGroup2QuestionnaireHeader"] = addStandardTexts("spørgeskema til spørgeskemagruppe", "spørgeskemaer til spørgeskemagruppe")  << [
                "del": "Fjerner spørgeskema fra spørgeskemagruppe"
        ]

        lookup["questionnaireHeader"] = addStandardTexts("spørgeskema", "spørgeskemaer")
        lookup["questionnaireHeader"] << [
                "saveAndEdit": "Gem Spørgeskema",
                "unpublish": "Fjern aktuel version",
                "publishDraft": "Publicer kladde",
                "deleteDraft": "Sletter kladde",
                "editDraft": "Rediger kladde",
                "createDraft": "Opret kladde",
                "doCreateDraft": "Opretter kladde"
        ]

        lookup["questionnaireEditor"] = [
                        "index": "-",
                        "save": "Gemmer spørgeskema som kladde",
                        "edit": "Redigerer spørgeskema som kladde",
                        "editorState": "Henter spørgeskema til editor",
                        "keepAlive": "Opretholdelse af tilstand"
                ]

        lookup["questionnaireSchedule"] = addStandardTexts("spørgeskema", "spørgeskemaer")
        lookup["questionnaireSchedule"] << [
                "questionnaireScheduleData": "Hent skemalægning for enkelt spørgeskema",
                "del": "Slet spørgeskemaplan",
                "validateViewModel": "Valider spørgeskemaplan",
                "showAddQuestionnaireGroup": "Tilføj gruppe af spørgeskemaer",
                "pickQuestionnaireGroup": "Vælg spørgeskemagruppe",
                "addQuestionnaireGroup": "Tilføjer gruppe af spørgeskemaer"
        ]

        lookup["patientGroup"] = [
                "index": "Index",
                "list": "List patientgrupper",
                "create": "Opret patientgruppe",
                "save": "Gem patientgruppe",
                "show": "Vis patientgruppe",
                "edit": "Rediger patientgruppe",
                "update": "Opdater patientgruppe",
                "delete": "Slet patientgruppe"
        ]

        lookup["meta"] = [
                "index": "Index",
                "currentServerVersion": "Get current server version",
                "isAlive": "isAlive",
                "isAliveJSON": "isAlive JSON",
                "noAccess": "Access denied - user not allowed to view patient",
                "registerCurrentPage": "Registrer aktuelle besøgte side"
        ]



        lookup["scheduleWindow"] = [
                "index": "Index skematjekvinduer",
                "show": "Vis skematjekvindue",
                "list": "List skematjekvinduer",
                "edit": "Rediger skematjekvindue",
                "update": "Opdater skematjekvindue"
        ]

        lookup["patientMeasurementMobile"] = [
                "index" : "Index",
                "measurement":"Vis målinger"
        ]

        lookup["videoResource"] = [
                "vidyoDesktopClientStarterJar": "Video-desktop-opstartsprogram hentet"
        ]

        lookup["passiveInterval"] = [
                "index": "Index",
                "list": "List pauseringer for patient",
                "create": "Opret  pausering for patient",
                "save": "Gem  pausering for patient",
                "show": "Vis  pausering for patient",
                "edit": "Rediger  pausering for patient",
                "update": "Opdater  pausering for patient",
                "delete": "Slet  pausering for patient"
        ]

        lookup["reminder"] = [
                "next": "Hent tidspunkt for næste påmindelse"
        ]

        lookup["error"] = [
                "index": "Ukendt fejl"
        ]
    }

    def addStandardTexts(String singular, String plural) {
        return [
                "create": "Opret ${singular}",
                "delete": "Slet ${singular}",
                "edit": "Rediger ${singular}",
                "index": "Liste af ${plural}",
                "list": "Liste af ${plural}",
                "save": "Gem ${singular}",
                "show": "Vis ${singular}",
                "update": "Opdater ${singular}"
        ]
    }

    def addPatientText() {
        return [
                "questionnaire": "Vis besvaret spørgeskema",
                "conference": "Vis videokonference",
                "questionnaires": "Målingsoverblik",
                "search": "Patientsøgning",
                "doSearch": "Udfør patientsøgning",
                "resetSearch": "Nulstil patientsøgning",
                "equipment": "Udstyr",
                "measurements": "Vis grafer/alle målinger",
                "messages": "Beskeder til/fra patient",
                "unacknowledged": "Ukvitterede spørgeskemaer",
                "login": "Patient login",
                "createPatient": "Opret patient",
                "addKit": "Tilføj kit til patient",
                "addMeter": "Tilføj måler til patient",
                "addThreshold": "Sæt tærskelværdi for patient",
                "convertStringParamsToFloats": "Konverter data for patient",
                "doRemoveNextOfKin": "Fjern slægtning fra patient",
                "filterPatientList2": "Sorter patient liste",
                "filterPatientListX": "Sorter patient liste",
                "getNextOfKin": "Hent slægtninge for patient",
                "getPatientGroups": "Hent patientgrupper for patient",
                "json": "Hent patient til klient",
                "monitoringplan": "Hent moniteringsplan for patient",
                "removeAllBlue": "Fjern blå alarmer fra patient",
                "removeKit": "Fjern kit fra patient",
                "removeMeter": "Fjern måler fra patient",
                "removeNextOfKin": "Fjern slægtning fra paitnTODO::patient.removeNextOfKin",
                "removeThreshold": "Fjern tærskelværdi fra patient",
                "savePref": "Gem opsætning for patient",
                "savePrefs": "Gem opsætning for patient",
                "saveThresholdToPatient": "Gem tærskelværdier for patient",
                "showJson": "Send patient data til klient",
                "editResponsability": "Opdater patient dataansvar",
                "updateDataResponsible": "Opdater patient ansvarlig"
        ]
    }

    @Override
    Map retrieve() {
        return lookup
    }
}
