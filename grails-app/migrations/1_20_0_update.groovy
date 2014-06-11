databaseChangeLog = {

    changeSet(author: 'hra', id: 'add_create_modified_to_patient_overview') {
        addColumn(tableName: 'patient_overview') {
            column(name: 'created_by', type: '${string.type}')
        }
        addColumn(tableName: 'patient_overview') {
            column(name: 'created_date', type: '${datetime.type}')
        }
        addColumn(tableName: 'patient_overview') {
            column(name: 'modified_by', type: '${string.type}')
        }
        addColumn(tableName: 'patient_overview') {
            column(name: 'modified_date', type: '${datetime.type}')
        }
    }
}