package org.opentele.server.milou

import org.opentele.server.model.Measurement

class MilouExportService {
    def ctgMeasurementService
    def milouWebServiceClientService

    def exportToMilou() {
        ctgMeasurementService.ctgMeasurementsToExport().each { Measurement measurement ->

            try {

                def measurementExported = milouWebServiceClientService.sendCtgMeasurement(measurement)
                if (measurementExported) {
                    log.info("CTG Measurement (id:'${measurement.id}') was exported")
                    ctgMeasurementService.markAsExported(measurement)
                }
            } catch (Exception ex) {
                log.info("Failed exporting CTG Measurement (id:'${measurement.id}')", ex)
            }
        }
    }
}
