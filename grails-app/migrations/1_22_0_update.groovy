databaseChangeLog = {

    changeSet(author: 'km', id: 'add_consultation') {
        createTable(tableName: 'consultation') {
            column(autoIncrement: 'true', name: 'id', type: '${id.type}') {
                constraints(nullable: 'false', primaryKey: 'true', primaryKeyName: 'consultation_PK')
            }

            column(name: "patient_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "clinician_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "consultation_date", type: '${datetime.type}') {
                constraints(nullable: "false")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }
            column(name: "created_by", type: '${string.type}')
            column(name: "created_date", type: '${datetime.type}')
            column(name: "modified_by", type: '${string.type}')
            column(name: "modified_date", type: '${datetime.type}')
        }

        addForeignKeyConstraint(baseColumnNames: "patient_id", baseTableName: "consultation",
                constraintName: "consultation_patient_FK",
                referencedColumnNames: "id", referencedTableName: "patient", referencesUniqueColumn: "false")

        addForeignKeyConstraint(baseColumnNames: "clinician_id", baseTableName: "consultation",
                constraintName: "consultation_clinician_FK",
                referencedColumnNames: "id", referencedTableName: "clinician", referencesUniqueColumn: "false")

        addColumn(tableName: "measurement") {
            column(name: "consultation_id", type: '${id.type}')
        }

        addForeignKeyConstraint(baseColumnNames: "consultation_id", baseTableName: "measurement",
            constraintName: "meas_consultation_FK",
            referencedColumnNames: "id", referencedTableName: "consultation", referencesUniqueColumn: "false")
    }
}