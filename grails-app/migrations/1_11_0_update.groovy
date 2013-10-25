databaseChangeLog = {
    changeSet(author: 'hra', id: 'add_receive_date_to_completed_questionnaire') {
        addColumn(tableName: 'completed_questionnaire') {
            column(name: 'received_date', type: '${datetime.type}')
        }
        sql(''' update ${fullSchemaName}completed_questionnaire set RECEIVED_DATE = CREATED_DATE ''' )

        addNotNullConstraint(tableName: 'completed_questionnaire', columnName: 'received_date', columnDataType: '${datetime.type}')
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-1") {
        addColumn(tableName: "questionnaire") {
            column(name: "STANDARD_SCHEDULE_BLUE_ALARM_TIME", type: '${string.type}')
        }
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-2") {
        addColumn(tableName: "questionnaire") {
            column(name: "STANDARD_SCHEDULE_REMINDER_TIME", type: '${string.type}')
        }
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-3") {
        addColumn(tableName: "questionnaire") {
            column(name: "STANDARD_SCHEDULE_WEEKDAYS_INTRO_PERIOD", type: '${string.type}')
        }
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-4") {
        addColumn(tableName: "questionnaire") {
            column(name: "STANDARD_SCHEDULE_WEEKDAYS_SECOND_PERIOD", type: '${string.type}')
        }
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-5") {
        addColumn(tableName: "questionnaire") {
            column(name: "standard_schedule_intro_period_weeks", type: '${integer.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-6") {
        addColumn(tableName: "questionnaire_group2questionnaire_header") {
            column(name: "STANDARD_SCHEDULE_BLUE_ALARM_TIME", type: '${string.type}')
        }
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-7") {
        addColumn(tableName: "questionnaire_group2questionnaire_header") {
            column(name: "STANDARD_SCHEDULE_REMINDER_TIME", type: '${string.type}')
        }
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-8") {
        addColumn(tableName: "questionnaire_group2questionnaire_header") {
            column(name: "STANDARD_SCHEDULE_WEEKDAYS_INTRO_PERIOD", type: '${string.type}')
        }
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-9") {
        addColumn(tableName: "questionnaire_group2questionnaire_header") {
            column(name: "STANDARD_SCHEDULE_WEEKDAYS_SECOND_PERIOD", type: '${string.type}')
        }
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-10") {
        addColumn(tableName: "questionnaire_group2questionnaire_header") {
            column(name: "standard_schedule_intro_period_weeks", type: '${integer.type}')
        }
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-11") {
        addColumn(tableName: "questionnaire_schedule") {
            column(name: "BLUE_ALARM_TIME", type: '${string.type}')
        }
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-12") {
        addColumn(tableName: "questionnaire_schedule") {
            column(name: "REMINDER_TIME", type: '${string.type}')
        }
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-13") {
        addColumn(tableName: "questionnaire_schedule") {
            column(name: "WEEKDAYS_INTRO_PERIOD", type: '${string.type}')
        }
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-14") {
        addColumn(tableName: "questionnaire_schedule") {
            column(name: "WEEKDAYS_SECOND_PERIOD", type: '${string.type}')
        }
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-15") {
        addColumn(tableName: "questionnaire_schedule") {
            column(name: "intro_period_weeks", type: '${integer.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-18") {
        addNotNullConstraint(columnDataType: '${integer.type}', columnName: "STANDARD_SCHEDULE_INTERVAL_IN_DAYS", tableName: "QUESTIONNAIRE")
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-20") {
        addNotNullConstraint(columnDataType: "timestamp", columnName: "STANDARD_SCHEDULE_STARTING_DATE", tableName: "QUESTIONNAIRE")
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-27") {
        dropNotNullConstraint(columnDataType: '${string.type}', columnName: "DAYS_IN_MONTH", tableName: "QUESTIONNAIRE_SCHEDULE")
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-29") {
        dropNotNullConstraint(columnDataType: '${string.type}', columnName: "TIMES_OF_DAY", tableName: "QUESTIONNAIRE_SCHEDULE")
    }

    changeSet(author: "sbg (generated)", id: "1381833472169-31") {
        dropNotNullConstraint(columnDataType: '${string.type}', columnName: "WEEKDAYS", tableName: "QUESTIONNAIRE_SCHEDULE")
    }

    changeSet(author: 'of', id: 'add_mean_arterial_pressure_to_conference_measurement_draft') {
        addColumn(tableName: 'conference_measurement_draft') {
            column(name: 'mean_arterial_pressure', type: '${integer.type}')
        }
    }

    changeSet(author: 'of', id: 'add_device_id_to_conference_measurement_draft') {
        addColumn(tableName: 'conference_measurement_draft') {
            column(name: 'device_id', type: '${string.type}')
        }
    }
}
