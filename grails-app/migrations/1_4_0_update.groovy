databaseChangeLog = {

	changeSet(author: "erikmejerhansen (generated)", id: "1362907355500-1") {
		addColumn(tableName: "questionnaire") {
			column(name: "editor_state", type: '${text.type}')
		}
	}

    changeSet(author: "erikmejerhansen", id: "add_glucoseInUrine_to_measuremnt") {
        addColumn(tableName: "measurement") {
            column(name: "glucose_in_urine", type: '${text.type}')
        }
    }

    changeSet(author: "ErikMejerHansen", id: "addUrineGlucoseTresholdTable") {
        createTable(tableName: "urine_glucose_threshold") {
            column(name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "urine_glucose_threshoPK")
            }

            column(name: "alert_high", type: '${string.type}') {
                constraints(nullable: "true")
            }

            column(name: "alert_low", type: '${string.type}') {
                constraints(nullable: "true")
            }

            column(name: "warning_high", type: '${string.type}') {
                constraints(nullable: "true")
            }

            column(name: "warning_low", type: '${string.type}') {
                constraints(nullable: "true")
            }
        }
    }
}
