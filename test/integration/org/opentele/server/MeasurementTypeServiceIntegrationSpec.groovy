package org.opentele.server

import grails.plugin.spock.IntegrationSpec
import org.opentele.server.model.Clinician
import org.opentele.server.model.Patient
import org.opentele.server.model.PatientGroup
import org.opentele.server.model.PatientNote
import org.opentele.server.model.types.MeasurementTypeName
import org.opentele.server.model.types.NoteType
import org.opentele.server.model.types.PatientState

class MeasurementTypeServiceIntegrationSpec extends IntegrationSpec {
    MeasurementTypeService measurementTypeService

    def setup() {
    }

    def "when I retrieve types supporting thresholds, only relevant types (and never CTG) is returned"() {

        when:
        def types = measurementTypeService.getUnusedMeasurementTypesForThresholds(list)

        then:
        notContained.each {
            !types.contains(it)
        }

        where:
        list                                                           | notContained
        []                                                             | [MeasurementTypeName.CTG]
        [MeasurementTypeName.CRP, MeasurementTypeName.BLOOD_PRESSURE]  | [MeasurementTypeName.CTG, MeasurementTypeName.CRP, MeasurementTypeName.BLOOD_PRESSURE]

    }
}
