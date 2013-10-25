package org.opentele.server.kihdb

import  dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.Request

import org.opentele.server.dgks.monitoringdataset.version1_0_1.CreateMonitoringDataset
import org.opentele.server.dgks.monitoringdataset.version1_0_1.generated.CreateMonitoringDatasetRequestMessage
import org.opentele.server.dgks.monitoringdataset.version1_0_1.generated.EncodingIdentifierType
import org.opentele.server.dgks.monitoringdataset.version1_0_1.generated.LaboratoryReportExtendedType
import org.opentele.server.dgks.monitoringdataset.version1_0_1.generated.MeasurementLocationType
import org.opentele.server.dgks.monitoringdataset.version1_0_1.generated.MeasurementScheduledType
import org.opentele.server.dgks.monitoringdataset.version1_0_1.generated.MeasurementTransferredByType
import org.opentele.server.dgks.monitoringdataset.version1_0_1.generated.ObjectFactory
import org.opentele.server.dgks.monitoringdataset.version1_0_1.generated.SelfMonitoredSampleType

import org.opentele.server.model.Measurement
import org.opentele.server.model.MeasurementType
import org.opentele.server.model.types.IupacCode
import org.opentele.server.model.types.MeasurementTypeName
import org.opentele.server.model.types.Unit
import org.opentele.server.util.SosiUtil
import org.opentele.server.util.XmlConverterUtil
import wslite.soap.SOAPClient

import java.text.SimpleDateFormat

class KihDbWebServiceClientService {

    def grailsApplication
    def messageSource

    boolean sendMeasurement(Measurement measurement, Request sosiRequest) {
        try {
            def result = send(measurement, sosiRequest);
            log.info("Result from KihDB: ${result}")
            true

        } catch (Exception ex) {
            log.error("Error occured while trying to upload measurement to KIH DB(id:'${measurement.id}')", ex)
            false
        }
    }

    private send(Measurement measurement, Request sosiRequest) {
        String kihdbUrl = grailsApplication.config.kihdb.service.url
        String createdByText = grailsApplication.config.kihdb.createdByText

        ObjectFactory objectFactory = new ObjectFactory()

        def soapClient = new SOAPClient(kihdbUrl)

        // Wrapper
        List<SelfMonitoredSampleType> selfMonitoredSampleTypes = new ArrayList<SelfMonitoredSampleType>()

        SelfMonitoredSampleType selfMonitoredSampleType = objectFactory.createSelfMonitoredSampleType()

        selfMonitoredSampleType.createdByText = createdByText
        selfMonitoredSampleType.laboratoryReportExtendedCollection = objectFactory.createLaboratoryReportExtendedCollectionType()

        handleMeasurement(measurement, selfMonitoredSampleType, objectFactory)

          // Add sample to container
        selfMonitoredSampleTypes.add(selfMonitoredSampleType)

        // Create request document

        CreateMonitoringDatasetRequestMessage requestMsg = objectFactory.createCreateMonitoringDatasetRequestMessage()
        requestMsg.personCivilRegistrationIdentifier = measurement.getPatient().getCpr()

        requestMsg.selfMonitoredSample = selfMonitoredSampleTypes

        CreateMonitoringDataset requestWrapper = new CreateMonitoringDataset()
        requestWrapper.createMonitoringDatasetRequestMessage = requestMsg

        // Send it
        RequestUtil requestUtil = new RequestUtil()
        String soapRequest = requestUtil.generateRequest(requestWrapper, sosiRequest)

        def response = soapClient.send(soapRequest)
        return response.CreateMonitoringDatasetResponse.CreateMonitoringDatasetResponseMessage.UuidIdentifier.list() // Extract the result set.
    }

    protected addGenericFields(LaboratoryReportExtendedType laboratoryReportExtendedType, ObjectFactory objectFactory) {
        // Generelt
        laboratoryReportExtendedType.producerOfLabResult = objectFactory.createProducerOfLabResultType()

        laboratoryReportExtendedType.producerOfLabResult.identifier = "Patient målt"
        laboratoryReportExtendedType.producerOfLabResult.identifierCode = "POT"

        laboratoryReportExtendedType.measurementTransferredBy = MeasurementTransferredByType.TYPED // TODO: Vi skelner ikke ml. automatiske og indtastede målinger!!

        laboratoryReportExtendedType.measurementLocation = MeasurementLocationType.HOME // TODO: Vi registrerer ikke stedet..
        laboratoryReportExtendedType.measurementScheduled = MeasurementScheduledType.SCHEDULED // TODO: Vi registrerer ikke om målingen var skeduleret.. (men kan nok regne det ud)
    }

    protected addInstrumentInfo(LaboratoryReportExtendedType laboratoryReportExtendedType, ObjectFactory objectFactory) {
    // TODO
    //        laboratoryReportExtendedType.instrument = objectFactory.createInstrumentType()
    //
    //        laboratoryReportExtendedType.instrument.medComID = report.Instrument[0]?.MedComID[0]?.value()?.getAt(0)
    //        laboratoryReportExtendedType.instrument.manufacturer = report.Instrument[0]?.Manufacturer[0]?.value()?.getAt(0)
    //        laboratoryReportExtendedType.instrument.model = report.Instrument[0]?.Model[0]?.value()?.getAt(0)
    //        laboratoryReportExtendedType.instrument.productType = report.Instrument[0]?.ProductType[0]?.value()?.getAt(0)
    //        laboratoryReportExtendedType.instrument.softwareVersion = report.Instrument[0]?.SoftwareVersion[0]?.value()?.getAt(0)

    }

    def handleMeasurement(Measurement measurement, SelfMonitoredSampleType selfMonitoredSampleType, ObjectFactory objectFactory) {

        if (measurement.getMeasurementType().getName().equals(MeasurementTypeName.WEIGHT)) {

            def type = addExtendedType(measurement.getValue(), "Kg", measurement.getTime(), IupacCode.WEIGHT, "VÆGT", objectFactory)
            selfMonitoredSampleType.laboratoryReportExtendedCollection.getLaboratoryReportExtended().add(type)

        } else if (measurement.getMeasurementType().getName().equals(MeasurementTypeName.PULSE)) {

            def type = addExtendedType(measurement.getValue(), "Slag/min", measurement.getTime(), IupacCode.PULSE, "PULS", objectFactory)
            selfMonitoredSampleType.laboratoryReportExtendedCollection.getLaboratoryReportExtended().add(type)

        } else if (measurement.getMeasurementType().getName().equals(MeasurementTypeName.BLOOD_PRESSURE)) {

            def diastolic = addExtendedType(measurement.getDiastolic(), "mm Hg", measurement.getTime(), IupacCode.DIASTOLIC_HOME, "DIASTOLISK", objectFactory)
            selfMonitoredSampleType.laboratoryReportExtendedCollection.getLaboratoryReportExtended().add(diastolic)
            def systolic = addExtendedType(measurement.getSystolic(), "mm Hg", measurement.getTime(), IupacCode.SYSTOLIC_HOME, "SYSTOLISK", objectFactory)
            selfMonitoredSampleType.laboratoryReportExtendedCollection.getLaboratoryReportExtended().add(systolic)

        } else if (measurement.getMeasurementType().getName().equals(MeasurementTypeName.BLOODSUGAR)) {

            def type = addExtendedType(measurement.getValue(), "mmol/L", measurement.getTime(), IupacCode.BLOODSUGAR, "BLODSUKKER", objectFactory)

            def circumstances

            if (measurement.getIsAfterMeal()) {
                circumstances = 'Efter måltid'
            } else if (measurement.getIsBeforeMeal()) {
                circumstances =  'Før måltid'
            } else if (measurement.getIsControlMeasurement()) {
                circumstances =  'Kontrolmåling'
            }
            if  (circumstances) {
                type.measuringCircumstances = circumstances
            }
            selfMonitoredSampleType.laboratoryReportExtendedCollection.getLaboratoryReportExtended().add(type)

        } else if (measurement.getMeasurementType().getName().equals(MeasurementTypeName.CRP)) {

            def type = addExtendedType(measurement.getValue(), Unit.MGRAM_L.value(), measurement.getTime(), IupacCode.CRP, "CRP", objectFactory)
            selfMonitoredSampleType.laboratoryReportExtendedCollection.getLaboratoryReportExtended().add(type)
        } else if (measurement.getMeasurementType().getName().equals(MeasurementTypeName.CTG)) {

            // Not supported
        } else if (measurement.getMeasurementType().getName().equals(MeasurementTypeName.HEMOGLOBIN)) {

            // Not supported
        } else if (measurement.getMeasurementType().getName().equals(MeasurementTypeName.LUNG_FUNCTION)) {

            def fev1 = addExtendedType(measurement.getValue(), "Liter/sekund", measurement.getTime(), IupacCode.FEV1, "FEV1", objectFactory)
            selfMonitoredSampleType.laboratoryReportExtendedCollection.getLaboratoryReportExtended().add(fev1)

            // TODO: FEV6 er ikke det samme som FVC, som er det KIH DB kan modtage!
//            def fev6 = addExtendedType(measurement.getFev6(), measurement.getMeasurementType(), measurement.getTime(), IupacCode.FVC, "FVC", objectFactory)
//            selfMonitoredSampleType.laboratoryReportExtendedCollection.getLaboratoryReportExtended().add(fev6)
//
//            def fev1Fev6Ratio = addExtendedType(measurement.getFev1Fev6Ratio(), measurement.getMeasurementType(), measurement.getTime(), IupacCode.FEV1_FVC, "FEV1/FVC", objectFactory)
//            selfMonitoredSampleType.laboratoryReportExtendedCollection.getLaboratoryReportExtended().add(fev1Fev6Ratio)


        } else if (measurement.getMeasurementType().getName().equals(MeasurementTypeName.SATURATION)) {

            def type = addExtendedType(measurement.getValue(), "%", measurement.getTime(), IupacCode.SATURATION, "ILTMÆTNING", objectFactory)
            selfMonitoredSampleType.laboratoryReportExtendedCollection.getLaboratoryReportExtended().add(type)

        } else if (measurement.getMeasurementType().getName().equals(MeasurementTypeName.TEMPERATURE)) {

            // Not supported
        } else if (measurement.getMeasurementType().getName().equals(MeasurementTypeName.URINE)) {

            // TODO: KIH DB Scale not compatible w/OpenTele..

            def type = addExtendedType(measurement.getProtein()?.toString(), "", measurement.getTime(), IupacCode.URINE, "PROTEINURIN", objectFactory)
            type.resultEncodingIdentifier = EncodingIdentifierType.ALPHANUMERIC

            selfMonitoredSampleType.laboratoryReportExtendedCollection.getLaboratoryReportExtended().add(type)

        } else if (measurement.getMeasurementType().getName().equals(MeasurementTypeName.URINE_GLUCOSE)) {

            // Not supported
        } else  {

            // Perhaps not exception. Just message saying this measurement is ignored.
            log.warn("Unsupported measurement type ${measurement.getMeasurementType().getName()} encountered.")

        }

        // Currently not handled - thus commented out
//                r.healthCareProfessionalComment = new FormattedTextType()
//                r.healthCareProfessionalComment = report?.HealthCareProfessionalComment[0].value()

//                r.measuringCircumstances = new FormattedTextType()
//                r.measuringCircumstances = report?.MeasuringCircumstances[0].value()

    }

    LaboratoryReportExtendedType addExtendedType(def value, String unitText, Date time, IupacCode iupacIdentifier, String analysisText, ObjectFactory objectFactory)  {

        LaboratoryReportExtendedType laboratoryReportExtendedType = new LaboratoryReportExtendedType()

        def uuid = UUID.randomUUID()

        laboratoryReportExtendedType.uuidIdentifier = uuid.toString()
        laboratoryReportExtendedType.createdDateTime = XmlConverterUtil.getDateAsXml(time)

        laboratoryReportExtendedType.analysisText = analysisText
        laboratoryReportExtendedType.resultText = value  // TODO: Some per type requirements for formatting

        laboratoryReportExtendedType.resultEncodingIdentifier = EncodingIdentifierType.NUMERIC

        laboratoryReportExtendedType.resultUnitText = unitText
        //laboratoryReportExtendedType.resultUnitText = messageSource.getMessage(code, null, Locale.getDefault())

        laboratoryReportExtendedType.nationalSampleIdentifier = "9999999999"
        laboratoryReportExtendedType.iupacIdentifier = iupacIdentifier
        laboratoryReportExtendedType.resultTypeOfInterval = "unspecified"

        addGenericFields(laboratoryReportExtendedType, objectFactory)

        addInstrumentInfo(laboratoryReportExtendedType, objectFactory)

        laboratoryReportExtendedType
    }
}