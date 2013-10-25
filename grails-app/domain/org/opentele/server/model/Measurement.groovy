package org.opentele.server.model

import org.opentele.server.model.patientquestionnaire.MeasurementNodeResult
import org.opentele.server.model.types.GlucoseInUrineValue
import org.opentele.server.model.types.MeasurementTypeName
import org.opentele.server.model.types.ProteinValue
import org.opentele.server.model.types.Unit
import org.opentele.server.util.NumberFormatUtil

class Measurement extends AbstractObject {

    static belongsTo = [
        patient: Patient,
        meter: Meter,
        measurementType: MeasurementType,
        measurementNodeResult: MeasurementNodeResult,
        conference: Conference
    ]

    public static final ArrayList<MeasurementTypeName> notToBeExportedMeasurementTypes = [
            MeasurementTypeName.CTG,
            MeasurementTypeName.HEMOGLOBIN,
            MeasurementTypeName.TEMPERATURE,
            MeasurementTypeName.URINE_GLUCOSE
    ]

    Boolean exported = false
    Boolean exportedToKih = false

    Date time
    
    // "Normal" measurements
    Double value
    
    // BloodPressure measurements
    Double systolic
    Double diastolic
    Double meanArterialPressure

//    TODO mss/hra determine how these uuids should be represented, as csv's?
//    value => uuid1,
//    valueSystolic => uuid1,
//    valueDiastolic => uuid2
//
//    Uuid uuidValue
//    Uuid uuidDiastolic
//    ...

    // CTG measurements
    String fhr                //Double[]// Fetal Heart Rate, fostrets hjerterytme)
    String mhr          //Double[]//(Maternal Heart Rate - moderens hjerterytme)
    String qfhr         //Integer[]// (Quality measurements for FHR - noget med hvor godt signalet er 0..3)
    String toco         //Double[]// T'et i CTG (cardiotocography...)
    String signals  //String[]// tidspunkter for tryk på den lyserøde knap, i sekunder fra start.
    String signalToNoise
    String fetalHeight
    Double voltageStart // batterispænding ved start
    Double voltageEnd   // batterispænding ved afslutning
    Date startTime      // tidspunkt for start
    Date endTime        // tidspunkt for afslutning
    
	// Protein
	ProteinValue protein

    // Glucose
    GlucoseInUrineValue glucoseInUrine

    // BloodSugar
    Boolean isAfterMeal
    Boolean isControlMeasurement
    Boolean isOutOfBounds
    Boolean otherInformation
    Boolean isBeforeMeal
    Boolean hasTemperatureWarning

    // Lung function (fev1 value is stored in "value)
    Double fev6
    Double fev1Fev6Ratio
    Double fef2575
    Boolean isGoodTest
    Integer fevSoftwareVersion



    Unit unit
        
   	boolean unread
       
    static mapping = {
        fhr type:'text'
        mhr type:'text'
        qfhr type:'text'
        toco type:'text'
        signals type:'text'
        fetalHeight type:'text'
        signalToNoise type:'text'
    }
       
//    Serializable nodeValue // Value used for GT, LT and EQUAL operations if type int or float
//    static mapping = { nodeValue type: 'serializable' }

    String toString() {
        NumberFormatUtil.format(this)
    }

    boolean isIgnored() {
        measurementNodeResult != null && measurementNodeResult.nodeIgnored
    }
       
	static constraints = {

        exported(nullable:false)
        exportedToKih(nullable:false)
        time(nullable:false)
        value(nullable:true)
        systolic(nullable:true)
        diastolic(nullable:true)
        meanArterialPressure(nullable:true)
        unit(nullable:false)
        patient(nullable:false)
        meter(nullable:true)
        measurementType(nullable:false)
		unread(nullable:false)
        measurementNodeResult(nullable:true)
        conference(nullable:true)
		protein(nullable:true)
        glucoseInUrine(nullable: true)
    
        fhr(nullable:true)
        mhr(nullable:true)
        qfhr(nullable:true)
        toco(nullable:true)
        signals(nullable:true)
        signalToNoise(nullable:true)
        fetalHeight(nullable:true)
        voltageStart(nullable:true)
        voltageEnd(nullable:true)
        startTime(nullable:true)
        endTime(nullable:true)

        isAfterMeal(nullable:true)
        isControlMeasurement(nullable:true)
        isOutOfBounds(nullable:true)
        otherInformation(nullable:true)
        isBeforeMeal(nullable:true)
        hasTemperatureWarning(nullable:true)

        fev6(nullable:true)
        fev1Fev6Ratio(nullable:true)
        fef2575(nullable:true)
        isGoodTest(nullable:true)
        fevSoftwareVersion(nullable:true)


        value validator: { val, obj ->
           if (obj.measurementType.name == MeasurementTypeName.BLOOD_PRESSURE) {
               val == null
           } else if (obj.measurementType.name == MeasurementTypeName.CTG) {
               val == null
           } else if (obj.measurementType.name == MeasurementTypeName.TEMPERATURE) {
		   		val != null
		   } else if (obj.measurementType.name == MeasurementTypeName.URINE) {
		   		val == null
		   } else if(obj.measurementType.name == MeasurementTypeName.URINE_GLUCOSE) {
               val == null
           } else if (obj.measurementType.name == MeasurementTypeName.WEIGHT) {
		   		val != null
		   } else if (obj.measurementType.name == MeasurementTypeName.HEMOGLOBIN) {
		   		val != null
           } else if (obj.measurementType.name == MeasurementTypeName.CRP) {
               val != null
           } else if (obj.measurementType.name == MeasurementTypeName.BLOODSUGAR) {
               val != null;
           } else {
               val != null
           }
        }
        systolic validator: { val, obj ->
            if (obj.measurementType.name == MeasurementTypeName.BLOOD_PRESSURE) {
                val != null
            } else {
                val == null
            }
         }
        diastolic validator: { val, obj ->
            if (obj.measurementType.name == MeasurementTypeName.BLOOD_PRESSURE) {
                val != null
            } else {
                val == null
            }
         }
		protein validator: { val, obj ->
			if (obj.measurementType.name == MeasurementTypeName.URINE) {
				val != null
			} else {
				val == null
			}
		}
       glucoseInUrine validator: { val, obj ->
           if (obj.measurementType.name == MeasurementTypeName.URINE_GLUCOSE) {
               val != null
           } else {
               val == null
           }
       }
    }

    def shouldBeExportedToKih() {
        !notToBeExportedMeasurementTypes.contains( this.measurementType.name )
    }
}