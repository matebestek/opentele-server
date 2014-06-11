databaseChangeLog = {

    changeSet(author: 'of', id: 'add_user_agent_to_audit_log_entry') {
        addColumn(tableName: 'audit_log_entry') {
            column(name: 'user_agent', type: '${string.type}')
        }
    }

    changeSet(author: "hra", id: "add_next_fail_severity_to_measurement_nodes") {
        addColumn(tableName: "questionnaire_node") {
            column(name: "next_fail_severity", type: '${string.type}')
        }
        addColumn(tableName: "patient_questionnaire_node") {
            column(name: "next_fail_severity", type: '${string.type}')
        }
    }

    changeSet(author: 'of', id: 'add_continuous_blood_sugar_measurement') {
        createTable(tableName: 'continuous_blood_sugar_measurement') {
            column(autoIncrement: 'true', name: 'id', type: '${id.type}') {
                constraints(nullable: 'false', primaryKey: 'true', primaryKeyName: 'continuous_blood_sugar_measurement_PK')
            }

            column(name: 'version', type: '${id.type}') {
                constraints(nullable: 'false')
            }

            column(name: 'measurement_id', type: '${id.type}') {
                constraints(nullable: 'false')
            }

            column(name: 'record_number', type: '${long.type}') {
                constraints(nullable: 'false')
            }

            column(name: 'time', type: '${datetime.type}') {
                constraints(nullable: 'false')
            }

            column(name: 'value', type: '${double.type}') {
                constraints(nullable: 'false')
            }

            column(name: 'created_by', type: '${string.type}')
            column(name: 'created_date', type: '${datetime.type}')
            column(name: 'modified_by', type: '${string.type}')
            column(name: 'modified_date', type: '${datetime.type}')
        }

        addForeignKeyConstraint(baseColumnNames: "measurement_id", baseTableName: "continuous_blood_sugar_measurement", constraintName: "FK_CGM_MEASUREMENT", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "measurement", referencesUniqueColumn: "false")
    }

    changeSet(author: 'of', id: 'add_cgm_graph_to_measurement') {
        addColumn(tableName: 'measurement') {
            column(name: 'cgm_graphs_created', type: '${boolean.type}')
        }
    }

    changeSet(author: 'of', id: 'add_cgm_graphs') {
        createTable(tableName: 'cgm_graphs') {
            column(autoIncrement: 'true', name: 'id', type: '${id.type}') {
                constraints(nullable: 'false', primaryKey: 'true', primaryKeyName: 'cgm_graphs_PK')
            }

            column(name: 'version', type: '${id.type}') {
                constraints(nullable: 'false')
            }

            column(name: 'patient_id', type: '${id.type}') {
                constraints(nullable: 'false')
            }

            column(name: 'normal_graph', type: '${blob.type}')
            column(name: 'average_graph', type: '${blob.type}')

            column(name: 'created_by', type: '${string.type}')
            column(name: 'created_date', type: '${datetime.type}')
            column(name: 'modified_by', type: '${string.type}')
            column(name: 'modified_date', type: '${datetime.type}')
        }

        addForeignKeyConstraint(baseColumnNames: 'patient_id', baseTableName: 'cgm_graphs', constraintName: 'FK_CGM_GRAPHS_PATIENT', deferrable: 'false', initiallyDeferred: 'false', referencedColumnNames: 'id', referencedTableName: 'patient', referencesUniqueColumn: 'false')
    }
}