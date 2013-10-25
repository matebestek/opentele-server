package org.opentele.server.service

import org.opentele.server.model.Measurement
import org.opentele.server.model.MeasurementRequest
import org.opentele.server.model.MeasurementType
import org.opentele.server.model.Meter
import org.opentele.server.model.Patient
import grails.converters.JSON

class DataService {
	
	/*
	* EXAMPLE JSON FOR UPLOADS
	*
	// Weight
	{
	PatientName: "Nancy Ann Berggren",
	PatientId: 1,
	MeasurementLabel: "Weight",
	MeterId: 1,
	data:  [
	{ id: 1, label: "Weight", value: 120 }
	]
	}
	// blodtryk
	{
	PatientName: "Nancy Ann Berggren",
	PatientId: 1,
	MeasurementLabel: "Bloodpressure",
	MeterId: 2,
	data:  [
	{ id: 3, label: "diastolic pressure", value: 120 },
	{ id: 4, label: "systolic pressure", value: 75 },
	 { id: 2, label: "pulse", value: 75 }
	]
	}
	*/


   def addMeasurements (MeasurementRequest mr) {
	   def patientId = mr.patientId
	   def meterId = mr.meterId
	   log.debug "Meter id: " + meterId
	   def m = Meter.get(Integer.valueOf(meterId).value)
	   log.debug "Using meter_: " + m
	   def p = Patient.get(patientId)
	   log.debug "Updating patient: " + p

	   MeasurementType mt = null
 
	   Date now = new Date()

	   Measurement mea = null
	   def results = []
	   results << ["success"]
	   
	   boolean errors = false
	   mr.data.each { measure ->
		   log.debug "M: " + measure
		   log.debug "Label: " + measure.label + " value: " + measure.value
		   mt = MeasurementType.get(measure.id)
		   
		   if (measure.label.equalsIgnoreCase("weight")) {
			   
			   mea = new Measurement(meter:m,
				   measurementType:mt,
				   patient:p,
				   value:measure.value,
				   time:now,
				   unread:true,
				   unit:"Kg", createdBy: "WS", modifiedBy: "WS", createdDate: now, modifiedDate: now)

		   }
		   if (measure.label.equalsIgnoreCase("pulse")) {
			   mea = new Measurement(meter:m,
				   measurementType:mt,
				   patient:p,
				   value:measure.value,
				   time:now,
				   unread:true,
				   unit:"BPM", createdBy: "WS", modifiedBy: "WS", createdDate: now, modifiedDate: now)

			   
		   }
		   if (measure.label.equalsIgnoreCase("diastolic pressure")) {
			   mea = new Measurement(meter:m,
				   measurementType:mt,
				   patient:p,
				   value:measure.value,
				   time:now,
				   unread:true,
				   unit:"mmHg", createdBy: "WS", modifiedBy: "WS", createdDate: now, modifiedDate: now)

		   }
		   if (measure.label.equalsIgnoreCase("systolic pressure")) {
			   mea = new Measurement(meter:m,
				   measurementType:mt,
				   patient:p,
				   value:measure.value,
				   time:now,
				   unread:true,
				   unit:"mmHg", createdBy: "WS", modifiedBy: "WS", createdDate: now, modifiedDate: now)

		   }
		   log.debug "Saving: " + mea.save()
		   results << mea
		   log.debug "Errors?: " + mea.hasErrors()
		   if (mea.hasErrors()) {
			   errors = true
			   log.error "Errors: " + mea.errors
		   }
	   }

	   if (mea.hasErrors()) {
		   results = []
		   results << ["failure"]
		   results << mea.errors
	   }
   }
}
