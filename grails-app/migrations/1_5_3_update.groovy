databaseChangeLog = {

    // Add Delay node
    changeSet(author: "msurrow", id: "1368430157548-1") {
        addColumn(tableName: "patient_questionnaire_node") {
            column(name: "count_up", type: '${boolean.type}')
            column(name: "count_time", type: '${integer.type}')
        }
    }

    changeSet(author: "msurrow", id: "1368430157548-2") {
        addColumn(tableName: "questionnaire_node") {
            column(name: "count_up", type: '${boolean.type}')
            column(name: "count_time", type: '${integer.type}')
        }
    }
}