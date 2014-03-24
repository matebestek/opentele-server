package org.opentele.server.model
import grails.plugins.springsecurity.Secured
import org.opentele.server.TimeFilter
import org.opentele.server.annotations.SecurityWhiteListController
import org.opentele.server.model.types.PermissionName

@Secured(PermissionName.NONE)
@SecurityWhiteListController
class PatientMeasurementMobileController {
    def measurementService
    def springSecurityService

    @Secured(PermissionName.PATIENT_LOGIN)
    def index() {
        def user = springSecurityService.currentUser
        def patient = Patient.findByUser(user)

        def tables = measurementService.dataForTables(patient, TimeFilter.all())
        def nonCTGTables = tables.findAll{it.type.toString() != 'CTG'} //We cannot meaningfully show CTG data

        [tables:nonCTGTables]
    }

    @Secured(PermissionName.PATIENT_LOGIN)
    def measurement() {
        def user = springSecurityService.currentUser
        def patient = Patient.findByUser(user)

        // Check for filter in params, otherwise set week.
        def timeFilter = TimeFilter.fromParams(params.filter ? params : [filter: "WEEK"])

        def (tables, bloodsugarData) = measurementService.dataForTablesAndBloodsugar(patient, timeFilter)
        def tableData = null

        if (params.type != "BLOODSUGAR") {
            def table = tables.find { it.type.toString() == params.type }
            tableData = table ? table.measurements*.measurement : []
        }
        def graphData = measurementService.dataForGraph(patient, timeFilter, params.type)

        [patientInstance:patient, type: params.type, tableData: tableData, bloodSugarData: bloodsugarData, graphData: graphData]
    }
}
