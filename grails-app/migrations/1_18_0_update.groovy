databaseChangeLog = {

    // Add flag telling app to show gestational age fields or not for selected patientgroup
    changeSet(author: "hra", id: "add_gestational_age_flag_to_patient_group") {
        addColumn(tableName: "patient_group") {
            column(name: "show_gestational_age", type: '${boolean.type}')
        }
        sql(''' update ${fullSchemaName}patient_group set show_gestational_age = 'FALSE' ''' )
        addNotNullConstraint(columnDataType: '${boolean.type}', columnName: "show_gestational_age", tableName: "patient_group")

        addColumn(tableName: "patient") {
            column(name: "due_date", type: '${datetime.type}')
        }
    }
}