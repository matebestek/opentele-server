databaseChangeLog = {

    changeSet(author: "erikmejerhansen (generated)", id: "1361952929480-1") {
        addColumn(tableName: "measurement") {
            column(name: "has_temperature_warning", type: '${boolean.type}')
        }
        addColumn(tableName: "measurement") {
            column(name: "is_after_meal", type: '${boolean.type}')
        }
        addColumn(tableName: "measurement") {
            column(name: "is_before_meal", type: '${boolean.type}')
        }
        addColumn(tableName: "measurement") {
            column(name: "is_control_measurement", type: '${boolean.type}')
        }
        addColumn(tableName: "measurement") {
            column(name: "is_out_of_bounds", type: '${boolean.type}')
        }
        addColumn(tableName: "measurement") {
            column(name: "other_information", type: '${boolean.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361795627835-1") {

        addColumn(tableName: "measurement") {
            column(name: "exported", type: '${boolean.type}')
        }
        sql(''' update ${fullSchemaName}measurement set exported = 'FALSE' ''' )
        addNotNullConstraint(columnDataType: '${boolean.type}', columnName: "exported", tableName: "measurement ")
    }

    changeSet(author: "of (generated)", id: "1361965007066-1") {
		addColumn(tableName: "measurement") {
			column(name: "mean_arterial_pressure", type: '${double.type}')
		}
	}

    changeSet(author: "henrik (generated)", id: "1362040391774-1") {
        addColumn(tableName: "permission") {
            column(name: "created_by", type: '${string.type}')
        }
        sql(''' update ${fullSchemaName}permission set created_by = 'sprint11' ''' )
    }

    changeSet(author: "henrik (generated)", id: "1362040391774-2") {
        addColumn(tableName: "permission") {
            column(name: "created_date", type: '${datetime.type}')
        }
        sql(''' update ${fullSchemaName}permission set created_date = ${now} ''' )
    }

    changeSet(author: "henrik (generated)", id: "1362040391774-3") {
        addColumn(tableName: "permission") {
            column(name: "modified_by", type: '${string.type}')
        }
        sql(''' update ${fullSchemaName}permission set modified_by = 'sprint11' ''' )
    }

    changeSet(author: "henrik (generated)", id: "1362040391774-4") {
        addColumn(tableName: "permission") {
            column(name: "modified_date", type: '${datetime.type}')
        }
        sql(''' update ${fullSchemaName}permission set modified_date = ${now} ''' )
    }

    changeSet(author: "of", id: "1362993116610-1") {
        addColumn(tableName: "measurement") {
            column(name: "fetal_height", type: '${text.type}')
        }
        addColumn(tableName: "measurement") {
            column(name: "signal_to_noise", type: '${text.type}')
        }
    }


    changeSet(author: "msurrow (generated)", id: "1363083585912-1") {
        dropNotNullConstraint(columnDataType: '${float.type}', columnName: "alert_high", tableName: "numeric_threshold")
    }

    changeSet(author: "msurrow (generated)", id: "1363083585912-2") {
        dropNotNullConstraint(columnDataType: '${float.type}', columnName: "alert_low", tableName: "numeric_threshold")
    }

    changeSet(author: "msurrow (generated)", id: "1363083585912-3") {
        dropNotNullConstraint(columnDataType: '${float.type}', columnName: "warning_high", tableName: "numeric_threshold")
    }

    changeSet(author: "msurrow (generated)", id: "1363083585912-4") {
        dropNotNullConstraint(columnDataType: '${float.type}', columnName: "warning_low", tableName: "numeric_threshold")
    }


    changeSet(author: "msurrow (generated)", id: "1363083585912-5") {
        dropNotNullConstraint(columnDataType: '${string.type}', columnName: "alert_high", tableName: "urine_threshold")
    }

    changeSet(author: "msurrow (generated)", id: "1363083585912-6") {
        dropNotNullConstraint(columnDataType: '${string.type}', columnName: "alert_low", tableName: "urine_threshold")
    }

    changeSet(author: "msurrow (generated)", id: "1363083585912-7") {
        dropNotNullConstraint(columnDataType: '${string.type}', columnName: "warning_high", tableName: "urine_threshold")
    }

    changeSet(author: "msurrow (generated)", id: "1363083585912-8") {
        dropNotNullConstraint(columnDataType: '${string.type}', columnName: "warning_low", tableName: "urine_threshold")
    }


    changeSet(author: "msurrow (generated)", id: "1363083585912-9") {
        dropNotNullConstraint(columnDataType: '${float.type}', columnName: "diastolic_alert_high", tableName: "blood_pressure_threshold")
    }

    changeSet(author: "msurrow (generated)", id: "1363083585912-10") {
        dropNotNullConstraint(columnDataType: '${float.type}', columnName: "diastolic_alert_low", tableName: "blood_pressure_threshold")
    }

    changeSet(author: "msurrow (generated)", id: "1363083585912-11") {
        dropNotNullConstraint(columnDataType: '${float.type}', columnName: "diastolic_warning_high", tableName: "blood_pressure_threshold")
    }

    changeSet(author: "msurrow (generated)", id: "1363083585912-12") {
        dropNotNullConstraint(columnDataType: '${float.type}', columnName: "diastolic_warning_low", tableName: "blood_pressure_threshold")
    }

    changeSet(author: "msurrow (generated)", id: "1363083585912-13") {
        dropNotNullConstraint(columnDataType: '${float.type}', columnName: "systolic_alert_high", tableName: "blood_pressure_threshold")
    }

    changeSet(author: "msurrow (generated)", id: "1363083585912-14") {
        dropNotNullConstraint(columnDataType: '${float.type}', columnName: "systolic_alert_low", tableName: "blood_pressure_threshold")
    }

    changeSet(author: "msurrow (generated)", id: "1363083585912-15") {
        dropNotNullConstraint(columnDataType: '${float.type}', columnName: "systolic_warning_high", tableName: "blood_pressure_threshold")
    }

    changeSet(author: "msurrow (generated)", id: "1363083585912-16") {
        dropNotNullConstraint(columnDataType: '${float.type}', columnName: "systolic_warning_low", tableName: "blood_pressure_threshold")
    }


    changeSet(author: "msurrow (generated)", id: "1363086950247-1") {
        addColumn(tableName: "patient") {
            column(name: "data_responsible_id", type: '${id.type}') {
                constraints(nullable: "true")
            }
        }
    }

}
