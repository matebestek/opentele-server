import grails.util.Environment

class UrlMappings {

	static mappings = {
		"/login/$action?"(controller: "login")
		"/logout/$action?"(controller: "logout")

		
		// Default one
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
		name patientMeasurements: "/patient/$patientId/measurements"(controller:"measurement", action:"patientMeasurements")
		name patientGraphs: "/patient/$patientId/graphs"(controller:"measurement", action:"patientGraphs")
		name patientMeasurementGraph: "/patient/$patientId/measurements/$measurementType/graph"(controller:"measurement", action:"graph")
        name patientMeasurementBloodsugar: "/patient/$patientId/measurements/bloodsugar"(controller: "measurement", action: "bloodsugar")

        // Special case of questionnaire controller
        "/rest/$controller/download/$id"(parseRequest:true){
            action = [GET:"download"]
        }

        "/rest/$controller/login"(parseRequest:true){
            action = [GET:"login"]
        }

        "/rest/password/update"(parseRequest: true) {
            controller="password"
            action = [POST:"update"]
        }

        "/rest/$controller/listing"(parseRequest:true){
            action = [GET:"listing", POST: "upload"]
        }

        "/rest/$controller/patientHasPendingConference"(parseRequest: true) {
            action = [GET:"patientHasPendingConference"]
        }

        "/rest/$controller/patientHasPendingMeasurement"(parseRequest: true) {
            action = [GET:"patientHasPendingMeasurement"]
        }

        "/rest/$controller/measurementFromPatient"(parseRequest: true) {
            action = [POST:"measurementFromPatient"]
        }


        name patientMeasurementsMobile: "/rest/patient/measurements"(controller:"PatientMeasurementMobile", action:"index")
        name patientMeasurementsTypeMobile: "/rest/patient/measurements/$type"(controller:"PatientMeasurementMobile", action:"measurement")

        "/rest/measurements/lastContinuousBloodSugarRecordNumber" (controller: 'Questionnaire', action:'lastContinuousBloodSugarRecordNumber')

        "/rest/reminder/next"(controller:"Reminder", action = [GET:"next"])

        "/rest/$controller/new"(parseRequest:true){
            action = [GET:"newMessages"]
        }

        "/rest/$controller/recipients"(parseRequest:true){
            action = [GET:"messageRecipients"]
        }

        "/rest/$controller/acknowledgements"(parseRequest:true){
            action = [GET:"acknowledgements"]
        }

        "/rest/$controller/markAsRead"(parseRequest:true){
            action = [POST:"markAsRead"]
        }

        //For the meta controller
        "/currentVersion"(controller: "meta", action: "currentServerVersion")
        "/isAlive"(controller: "meta", action: "isAlive")
        "/isAlive/html"(controller: "meta", action: "isAlive")
        "/isAlive/json"(controller: "meta", action: "isAliveJSON")
        "/noAccess"(controller: "meta", action: "noAccess")

        // The REST API for mobile devices
		"/rest/$controller/element/$id"(parseRequest:true){
			action = [GET:"show", DELETE: "delete", PUT: "update", POST: "read"]
		}

		"/rest/$controller/list"(parseRequest:true){
			action = [GET:"list", POST: "save"]
		}

        "/"(controller: "home", action:"index")

        "500"(view:"${(Environment.current == Environment.DEVELOPMENT) ? '/error' : '/productionError'}")

//        "404"(controller: 'error', action:'index')
//        "500"(view:''/productionError'')
	}
}
