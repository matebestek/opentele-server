databaseChangeLog = {
    changeSet(author: 'of', id: 'add_automatic_and_waiting_to_conference_measurement_draft') {
        addColumn(tableName: 'conference_measurement_draft') {
            column(name: 'automatic', type: '${boolean.type}')
        }
        addColumn(tableName: 'conference_measurement_draft') {
            column(name: 'waiting', type: '${boolean.type}')
        }

        addNotNullConstraint(tableName: 'conference_measurement_draft', columnName: 'automatic', columnDataType: '${boolean.type}', defaultNullValue: "FALSE")
        addNotNullConstraint(tableName: 'conference_measurement_draft', columnName: 'waiting', columnDataType: '${boolean.type}', defaultNullValue: "FALSE")
    }

    changeSet(author: 'of', id: 'add_properties_to_lung_function_measurement_draft') {
        addColumn(tableName: 'conference_measurement_draft') {
            column(name: 'fev6', type: '${double.type}')
        }
        addColumn(tableName: 'conference_measurement_draft') {
            column(name: 'fev1_fev6_ratio', type: '${double.type}')
        }
        addColumn(tableName: 'conference_measurement_draft') {
            column(name: 'fef2575', type: '${double.type}')
        }
        addColumn(tableName: 'conference_measurement_draft') {
            column(name: 'good_test', type: '${boolean.type}')
        }
        addColumn(tableName: 'conference_measurement_draft') {
            column(name: 'software_version', type: '${integer.type}')
        }
    }
}
