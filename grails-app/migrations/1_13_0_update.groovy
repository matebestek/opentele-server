databaseChangeLog = {
    changeSet(author: 'hra', id: 'add_show_acknowledgement_to_patient') {
        addColumn(tableName: 'completed_questionnaire') {
            column(name: 'show_acknowledgement_to_patient', type: '${boolean.type}')
        }
    }
}
