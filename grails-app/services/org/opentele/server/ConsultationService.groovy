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
            Measurement m3 = new Measurement()
            m3.patient = patient
            m3.time = now
            m3.measurementType = MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE)
            m3.unit = Unit.MMHG
            m3.systolic = getDouble(params.systolic)
            m3.diastolic = getDouble(params.diastolic)
            consultation.addToMeasurements(m3)

            Measurement m4 = new Measurement()
            m4.patient = patient
            m4.time = now
            m4.measurementType = MeasurementType.findByName(MeasurementTypeName.PULSE)
            m4.unit = Unit.BPM
            m4.value = getDouble(params.pulse)
            consultation.addToMeasurements(m4)
        }

        if (params.showSATURATION) {
            Measurement m5 = new Measurement()
            m5.patient = patient
            m5.time = now
            m5.unit = Unit.PERCENTAGE
            m5.measurementType = MeasurementType.findByName(MeasurementTypeName.SATURATION)
            m5.value = getDouble(params.saturation)
            consultation.addToMeasurements(m5)

            Measurement m6 = new Measurement()
            m6.patient = patient
            m6.time = now
            m6.measurementType = MeasurementType.findByName(MeasurementTypeName.PULSE)
            m6.unit = Unit.BPM
            m6.value = getDouble(params.saturationPuls)
            consultation.addToMeasurements(m6)
        }

        if (params.showLUNG_FUNCTION) {
            Measurement m7 = new Measurement()
            m7.patient = patient
            m7.time = now
            m7.unit = Unit.LITER
            m7.measurementType = MeasurementType.findByName(MeasurementTypeName.LUNG_FUNCTION)
            m7.value = getDouble(params.lungfunction)
            consultation.addToMeasurements(m7)
        }

        if (params.showURINE_COMBI) {
            Measurement m8 = new Measurement()
            m8.patient = patient
            m8.time = now
            m8.unit = Unit.GLUCOSE
            m8.measurementType = MeasurementType.findByName(MeasurementTypeName.URINE_GLUCOSE)
            m8.glucoseInUrine = GlucoseInUrineValue.fromString(params.urine_glucose)
            consultation.addToMeasurements(m8)

            Measurement m9 = new Measurement()
            m9.patient = patient
            m9.time = now
            m9.unit = Unit.PROTEIN
            m9.measurementType = MeasurementType.findByName(MeasurementTypeName.URINE)
            m9.protein = ProteinValue.fromString(params.urine)
            consultation.addToMeasurements(m9)
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
