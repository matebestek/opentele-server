package org.opentele.server.model

class MeasurementRequest {
	// Weight
//	{
//		PatientName: "Nancy Ann Berggren",
//		PatientId: 1,
//		MeasurementLabel: "Weight",
//		MeterId: 1,
//		data:  [
//			{ id: 1, label: "Weight", value: 120 }
//		]
//	}

	def patientId
	def meterId
	def label
	def patientName
	def data = []

}
