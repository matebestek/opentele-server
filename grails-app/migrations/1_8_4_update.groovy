databaseChangeLog = {

    changeSet(author: "mby", id: "add_requires_manual_inspection_to_questionnaire_header") {
        addColumn(tableName: "questionnaire_header") {
            column(name: "requires_manual_inspection", type: '${boolean.type}')
        }

        addNotNullConstraint(tableName: "questionnaire_header", columnName: "requires_manual_inspection", columnDataType: '${boolean.type}', defaultNullValue: "FALSE")
    }

    changeSet(author: "of", id: "questionnaire_node_add_headline") {
        addColumn(tableName: "questionnaire_node") {
            column(name: "headline", type: '${text.type}')
        }

        addColumn(tableName: "patient_questionnaire_node") {
            column(name: "headline", type: '${text.type}')
        }
    }
}