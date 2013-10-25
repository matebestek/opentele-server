package org.opentele.server.milou

import org.opentele.server.model.Measurement
import org.opentele.server.model.types.MeasurementTypeName

class CtgMeasurementService {
    def ctgMeasurementsToExport() {

        return Measurement.where {
            exported == false
            measurementType.name == MeasurementTypeName.CTG
        }.list()
    }

    def markAsExported(Measurement ctgMeasurement) {
        ctgMeasurement.exported = true
        ctgMeasurement.save(failOnError: true)
    }
}
