package org.opentele.server

import org.opentele.server.model.Clinician
import org.opentele.server.model.Consultation
import org.opentele.server.model.Measurement
import org.opentele.server.model.MeasurementType
import org.opentele.server.model.Patient
import org.opentele.server.model.types.GlucoseInUrineValue
import org.opentele.server.model.types.MeasurementTypeName
import org.opentele.server.model.types.ProteinValue
import org.opentele.server.model.types.Unit
import org.springframework.web.context.request.RequestContextHolder

class ConsultationService {

    def Consultation addConsultation(Patient patient, Clinician clinician, def params) {

        Date now = new Date()
        Consultation consultation = new Consultation(clinician: clinician, patient: patient, consultationDate: now)

        if (!params)
            return consultation

        if (params.showWEIGHT) {
            Measurement m = new Measurement()
            m.patient = patient
            m.time = now
            m.unit = Unit.KILO
            m.measurementType = MeasurementType.findByName(MeasurementTypeName.WEIGHT)
            m.value = getDouble(params.weight)
            consultation.addToMeasurements(m)
        }

        if (params.showBLOOD_PRESSURE) {
            Measurement m = new Measurement()
            m.patient = patient
            m.time = now
            m.measurementType = MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE)
            m.unit = Unit.MMHG
            m.systolic = getDouble(params.systolic)
            m.diastolic = getDouble(params.diastolic)
            consultation.addToMeasurements(m)

            Measurement m2 = new Measurement()
            m2.patient = m.patient
            m2.time = now
            m2.measurementType = MeasurementType.findByName(MeasurementTypeName.PULSE)
            m2.unit = Unit.BPM
            m2.value = getDouble(params.pulse)
            consultation.addToMeasurements(m2)
        }

        if (params.showSATURATION) {
            Measurement m = new Measurement()
            m.patient = patient
            m.time = now
            m.unit = Unit.PERCENTAGE
            m.measurementType = MeasurementType.findByName(MeasurementTypeName.SATURATION)
            m.value = getDouble(params.saturation)
            consultation.addToMeasurements(m)

            Measurement m2 = new Measurement()
            m2.patient = m.patient
            m2.time = now
            m2.measurementType = MeasurementType.findByName(MeasurementTypeName.PULSE)
            m2.unit = Unit.BPM
            m2.value = getDouble(params.saturationPuls)
            consultation.addToMeasurements(m2)
        }

        if (params.showLUNG_FUNCTION) {
            Measurement m = new Measurement()
            m.patient = patient
            m.time = now
            m.unit = Unit.LITER
            m.measurementType = MeasurementType.findByName(MeasurementTypeName.LUNG_FUNCTION)
            m.value = getDouble(params.lungfunction)
            consultation.addToMeasurements(m)
        }

        if (params.showURINE_COMBI) {
            Measurement m = new Measurement()
            m.patient = patient
            m.time = now
            m.unit = Unit.GLUCOSE
            m.measurementType = MeasurementType.findByName(MeasurementTypeName.URINE_GLUCOSE)
            m.glucoseInUrine = GlucoseInUrineValue.fromString(params.urine_glucose)
            consultation.addToMeasurements(m)

            Measurement m2 = new Measurement()
            m2.patient = patient
            m2.time = now
            m2.unit = Unit.PROTEIN
            m2.measurementType = MeasurementType.findByName(MeasurementTypeName.URINE)
            m2.protein = ProteinValue.fromString(params.urine)
            consultation.addToMeasurements(m2)
        }

        if (consultation.measurements)
            consultation.save(flush: true)

        return consultation
    }

    Double getDouble(String value) {
        try {
            value.replaceAll(',', '.').toDouble()
        } catch (Exception e) {
            null
        }
    }
}
