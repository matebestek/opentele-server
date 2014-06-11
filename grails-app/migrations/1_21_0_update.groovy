databaseChangeLog = {

    changeSet(author: 'hra', id: 'add_no_alarm_if_unread_messages_to_patient_to_patient') {
        addColumn(tableName: 'patient') {
            column(name: 'no_alarm_if_unread_messages_to_patient', type: '${boolean.type}')
        }
        addNotNullConstraint(tableName: 'patient', columnName: 'no_alarm_if_unread_messages_to_patient', columnDataType: '${boolean.type}', defaultNullValue: "FALSE")
    }
}