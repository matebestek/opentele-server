databaseChangeLog = {
    changeSet(author: "of", id: 'create_patient_overview') {
        createTable(tableName: 'patient_overview') {
            column(autoIncrement: 'true', name: 'id', type: '${id.type}') {
                constraints(nullable: 'false', primaryKey: 'true', primaryKeyName: 'patient_overviewPK')
            }

            column(name: 'version', type: '${id.type}') {
                constraints(nullable: 'false')
            }

            column(name: 'patient_id', type: '${id.type}') {
                constraints(nullable: 'false')
            }

            column(name: 'name', type: '${string.type}') {
                constraints(nullable: 'false')
            }

            column(name: 'cpr', type: '${string.type}') {
                constraints(nullable: 'false')
            }

            column(name: 'number_of_unacknowledged_questionnaires', type: '${integer.type}') {
                constraints(nullable: 'false')
            }

            column(name: 'questionnaire_severity', type: '${string.type}') {
                constraints(nullable: 'false')
            }

            column(name: 'most_severe_questionnaire_name', type: '${string.type}')

            column(name: 'most_severe_questionnaire_date', type: '${datetime.type}')

            column(name: 'blue_alarm_text', type: '${string.type}')

            column(name: 'green_questionnaire_ids', type: '${string.type}')

            column(name: 'number_of_unread_messages_to_patient', type: '${integer.type}') {
                constraints(nullable: 'false')
            }

            column(name: 'date_of_oldest_unread_message_to_patient', type: '${datetime.type}')

            column(name: 'number_of_unread_messages_from_patient', type: '${integer.type}') {
                constraints(nullable: 'false')
            }

            column(name: 'date_of_oldest_unread_message_from_patient', type: '${datetime.type}')

            column(name: 'important', type: '${boolean.type}') {
                constraints(nullable: 'false')
            }
        }

        addUniqueConstraint(constraintName: 'unique_patient_overview_to_patient',
                tableName: 'patient_overview',
                columnNames: 'patient_id')
    }

    changeSet(author: 'hra', id: 'add_device_identification_to_measurement') {
        addColumn(tableName: 'measurement') {
            column(name: 'device_identification', type: '${string.type}')
        }
    }

    changeSet(author: 'hra', id: 'remove_patient_reminder_from_note') {
        dropColumn(columnName: "remind_today", tableName: "patient_note")
    }
}