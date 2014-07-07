databaseChangeLog = {
    // Add flag telling app to show running CTG fields or not for selected patientgroup
    changeSet(author: "km", id: "add_show_running_ctg_messaging_to_patient_group") {
        addColumn(tableName: "patient_group") {
            column(name: "show_running_ctg_messaging", type: '${boolean.type}')
        }
        sql(''' update ${fullSchemaName}patient_group set show_running_ctg_messaging = 'FALSE' ''' )
        addNotNullConstraint(columnDataType: '${boolean.type}', columnName: "show_running_ctg_messaging", tableName: "patient_group")

    }
}