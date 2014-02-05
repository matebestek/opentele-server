databaseChangeLog = {

    changeSet(author: 'hra', id: 'add_uuid_to_measurement') {
        addColumn(tableName: 'measurement') {
            column(name: 'kih_uuid', type: '${string.type}')
        }
        addColumn(tableName: 'measurement') {
            column(name: 'kih_uuid_systolic', type: '${string.type}')
        }
        addColumn(tableName: 'measurement') {
            column(name: 'kih_uuid_diastolic', type: '${string.type}')
        }
    }
}