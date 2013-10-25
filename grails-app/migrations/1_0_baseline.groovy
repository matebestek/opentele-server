databaseChangeLog = {

    changeSet(author: "henrik (generated)", id: "1361794626972-1") {

        createTable(tableName: "audit_log_controller_entity") {

            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "audit_log_conPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "action_name", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "controller_name", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "number_of_calls", type: '${integer.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-2") {
        createTable(tableName: "audit_log_entry") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "audit_log_entPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "action_id", type: '${id.type}')

            column(name: "authority", type: '${string.type}')

            column(name: "calling_ip", type: '${string.type}')

            column(name: "correlation_id", type: '${string.type}')

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "duration", type: '${id.type}')

            column(name: "end_date", type: '${datetime.type}')

            column(name: "end_time", type: '${id.type}')

            column(name: "exception", type: '${boolean.type}') {
                constraints(nullable: "false")
            }

            column(name: "exception_message", type: '${string.type}')

            column(name: "http_session_id", type: '${string.type}')

            column(name: "id_card", type: '${string.type}')

            column(name: "message_id", type: '${string.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "operation", type: '${string.type}')

            column(name: "patient_cpr", type: '${string.type}')

            column(name: "request", type: '${string.type}')

            column(name: "response", type: '${string.type}')

            column(name: "result", type: '${string.type}')

            column(name: "service", type: '${string.type}')

            column(name: "start_date", type: '${datetime.type}')

            column(name: "start_time", type: '${id.type}')

            column(name: "success", type: '${boolean.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-3") {
        createTable(tableName: "audit_log_parameter") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "audit_log_parPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "entry_id", type: '${id.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "parameter_key", type: '${string.type}')

            column(name: "parameter_value", type: '${string.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-4") {
        createTable(tableName: "blood_pressure_threshold") {
            column(name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "blood_pressurPK")
            }

            column(name: "diastolic_alert_high", type: '${float.type}') {
                constraints(nullable: "false")
            }

            column(name: "diastolic_alert_low", type: '${float.type}') {
                constraints(nullable: "false")
            }

            column(name: "diastolic_warning_high", type: '${float.type}') {
                constraints(nullable: "false")
            }

            column(name: "diastolic_warning_low", type: '${float.type}') {
                constraints(nullable: "false")
            }

            column(name: "systolic_alert_high", type: '${float.type}') {
                constraints(nullable: "false")
            }

            column(name: "systolic_alert_low", type: '${float.type}') {
                constraints(nullable: "false")
            }

            column(name: "systolic_warning_high", type: '${float.type}') {
                constraints(nullable: "false")
            }

            column(name: "systolic_warning_low", type: '${float.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-5") {
        createTable(tableName: "choice_value") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "choice_valuePK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "input_node_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "label", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "value", type: '${string.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-6") {
        createTable(tableName: "clinician") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "clinicianPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "email", type: '${string.type}')

            column(name: "first_name", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "last_name", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "mobile_phone", type: '${string.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "phone", type: '${string.type}')

            column(name: "user_id", type: '${id.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-7") {
        createTable(tableName: "clinician2patient_group") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "clinician2patPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "clinician_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "patient_group_id", type: '${id.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-8") {
        createTable(tableName: "clinician_question_preference") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "clinician_quePK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "clinician_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "question_id", type: '${id.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-9") {
        createTable(tableName: "completed_questionnaire") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "completed_quePK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "_questionnaire_ignored", type: '${boolean.type}') {
                constraints(nullable: "false")
            }

            column(name: "acknowledged_by_id", type: '${id.type}')

            column(name: "acknowledged_date", type: '${datetime.type}')

            column(name: "acknowledged_note", type: '${text.type}')

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "patient_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "patient_questionnaire_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "questionnaire_ignored_reason", type: '${string.type}')

            column(name: "questionnare_ignored_by_id", type: '${id.type}')

            column(name: "severity", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "upload_date", type: '${datetime.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-10") {
        createTable(tableName: "department") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "departmentPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "name", type: '${string.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-11") {
        createTable(tableName: "measurement") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "measurementPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "diastolic", type: '${double.type}')

            column(name: "end_time", type: '${datetime.type}')

            column(name: "fhr", type: '${text.type}')

            column(name: "measurement_node_result_id", type: '${id.type}')

            column(name: "measurement_type_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "meter_id", type: '${id.type}')

            column(name: "mhr", type: '${text.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "patient_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "protein", type: '${string.type}')

            column(name: "qfhr", type: '${text.type}')

            column(name: "signals", type: '${text.type}')

            column(name: "start_time", type: '${datetime.type}')

            column(name: "systolic", type: '${double.type}')

            column(name: "time", type: '${datetime.type}') {
                constraints(nullable: "false")
            }

            column(name: "toco", type: '${text.type}')

            column(name: "unit", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "unread", type: '${boolean.type}') {
                constraints(nullable: "false")
            }

            column(name: "value", type: '${double.type}')

            column(name: "voltage_end", type: '${double.type}')

            column(name: "voltage_start", type: '${double.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-12") {
        createTable(tableName: "measurement_type") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "measurement_tPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "name", type: '${string255.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-13") {
        createTable(tableName: "message") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "messagePK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "department_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "in_reply_to_id", type: '${id.type}')

            column(name: "is_read", type: '${boolean.type}') {
                constraints(nullable: "false")
            }

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "patient_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "read_date", type: '${datetime.type}')

            column(name: "send_date", type: '${datetime.type}')

            column(name: "sent_by_patient", type: '${boolean.type}') {
                constraints(nullable: "false")
            }

            column(name: "text", type: '${string2000.type}') {
                constraints(nullable: "false")
            }

            column(name: "title", type: '${string.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-14") {
        createTable(tableName: "meter") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "meterPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "active", type: '${boolean.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "meter_id", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "meter_type_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "model", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "monitor_kit_id", type: '${id.type}')

            column(name: "patient_id", type: '${id.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-15") {
        createTable(tableName: "meter_type") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "meter_typePK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "name", type: '${string.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-16") {
        createTable(tableName: "monitor_kit") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "monitor_kitPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "department_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "name", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "patient_id", type: '${id.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-17") {
        createTable(tableName: "monitoring_plan") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "monitoring_plPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "patient_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "start_date", type: '${datetime.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-18") {
        createTable(tableName: "next_of_kin_person") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "next_of_kin_pPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "address", type: '${string.type}')

            column(name: "city", type: '${string.type}')

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "first_name", type: '${string.type}')

            column(name: "last_name", type: '${string.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "note", type: '${string.type}')

            column(name: "patient_id", type: '${id.type}')

            column(name: "phone", type: '${string.type}')

            column(name: "relation", type: '${string.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-19") {
        createTable(tableName: "node_result") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "node_resultPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "acknowledged_by_id", type: '${id.type}')

            column(name: "acknowledged_date", type: '${datetime.type}')

            column(name: "acknowledged_note", type: '${string.type}')

            column(name: "completed_questionnaire_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "completion_time", type: '${datetime.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "node_ignored", type: '${boolean.type}') {
                constraints(nullable: "false")
            }

            column(name: "node_ignored_by_id", type: '${id.type}')

            column(name: "node_ignored_reason", type: '${string.type}')

            column(name: "note", type: '${string.type}')

            column(name: "patient_questionnaire_node_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "severity", type: '${string.type}')

            column(name: "threshold_message", type: '${string.type}')

            column(name: "was_omitted", type: '${boolean.type}') {
                constraints(nullable: "false")
            }

            column(name: "class", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "result", type: '${blob.type}')

            column(name: "measurement_type_id", type: '${id.type}')

            column(name: "input", type: '${blob.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-20") {
        createTable(tableName: "numeric_threshold") {
            column(name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "numeric_thresPK")
            }

            column(name: "alert_high", type: '${float.type}') {
                constraints(nullable: "false")
            }

            column(name: "alert_low", type: '${float.type}') {
                constraints(nullable: "false")
            }

            column(name: "warning_high", type: '${float.type}') {
                constraints(nullable: "false")
            }

            column(name: "warning_low", type: '${float.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-21") {
        createTable(tableName: "patient") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "patientPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "address", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "city", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "comment", type: '${string2048.type}')

            column(name: "cpr", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "email", type: '${string.type}')

            column(name: "first_name", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "last_name", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "mobile_phone", type: '${string.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "monitoring_plan_id", type: '${id.type}')

            column(name: "phone", type: '${string.type}')

            column(name: "postal_code", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "sex", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "state", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "user_id", type: '${id.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-22") {
        createTable(tableName: "patient2patient_group") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "patient2patiePK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "patient_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "patient_group_id", type: '${id.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-23") {
        createTable(tableName: "patient_blue_alarm_questionnaireids") {
            column(name: "patient_id", type: '${id.type}')

            column(name: "blue_alarm_questionnaireids_long", type: '${id.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-24") {
        createTable(tableName: "patient_choice_value") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "patient_choicPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "label", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "patient_input_node_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "value", type: '${string.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-25") {
        createTable(tableName: "patient_group") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "patient_groupPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "department_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "name", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "standard_threshold_set_id", type: '${id.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-26") {
        createTable(tableName: "patient_note") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "patient_notePK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "note", type: '${text.type}') {
                constraints(nullable: "false")
            }

            column(name: "patient_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "remind_today", type: '${boolean.type}') {
                constraints(nullable: "false")
            }

            column(name: "reminder_date", type: '${datetime.type}')

            column(name: "type", type: '${string.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-27") {
        createTable(tableName: "patient_note_clinician") {
            column(name: "patient_note_seen_by_id", type: '${id.type}')

            column(name: "clinician_id", type: '${id.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-28") {
        createTable(tableName: "patient_questionnaire") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "patient_questPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "creation_date", type: '${datetime.type}') {
                constraints(nullable: "false")
            }

            column(name: "creator_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "deleted", type: '${boolean.type}') {
                constraints(nullable: "false")
            }

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "monitoring_plan_id", type: '${id.type}')

            column(name: "name", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "patient_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "revision", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "start_node_id", type: '${id.type}')

            column(name: "template_questionnaire_id", type: '${id.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-29") {
        createTable(tableName: "patient_questionnaire_node") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "patient_quest_node_PK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "default_next_id", type: '${id.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "questionnaire_id", type: '${id.type}')

            column(name: "short_text", type: '${string.type}')

            column(name: "template_questionnaire_node_id", type: '${id.type}')

            column(name: "class", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "map_to_input_fields", type: '${boolean.type}')

            column(name: "meter_type_id", type: '${id.type}')

            column(name: "monica_measuring_time_input_node_id", type: '${id.type}')

            column(name: "monica_measuring_time_input_var", type: '${string.type}')

            column(name: "next_fail_id", type: '${id.type}')

            column(name: "simulate", type: '${boolean.type}')

            column(name: "text", type: '${text.type}')

            column(name: "alternative_next_id", type: '${id.type}')

            column(name: "alternative_severity", type: '${string.type}')

            column(name: "data_type", type: '${string.type}')

            column(name: "default_severity", type: '${string.type}')

            column(name: "input_node_id", type: '${id.type}')

            column(name: "input_var", type: '${string.type}')

            column(name: "node_value", type: '${blob.type}')

            column(name: "operation", type: '${string.type}')

            column(name: "input_type", type: '${string.type}')

            column(name: "value", type: '${boolean.type}')

            column(name: "variable_name", type: '${string.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-30") {
        createTable(tableName: "patient_threshold") {
            column(name: "patient_thresholds_id", type: '${id.type}')

            column(name: "threshold_id", type: '${id.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-31") {
        createTable(tableName: "permission") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "permissionPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "permission", type: '${string.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-32") {
        createTable(tableName: "questionnaire") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "questionnairePK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "creation_date", type: '${datetime.type}') {
                constraints(nullable: "false")
            }

            column(name: "creator_id", type: '${id.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "name", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "revision", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "start_node_id", type: '${id.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-33") {
        createTable(tableName: "questionnaire2meter_type") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "questionnaire2meterPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "meter_type_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "questionnaire_id", type: '${id.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-34") {
        createTable(tableName: "questionnaire_node") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "questionnaireNodePK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "default_next_id", type: '${id.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "questionnaire_id", type: '${id.type}')

            column(name: "short_text", type: '${string.type}')

            column(name: "class", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "alternative_next_id", type: '${id.type}')

            column(name: "alternative_severity", type: '${string.type}')

            column(name: "default_severity", type: '${string.type}')

            column(name: "input_type", type: '${string.type}')

            column(name: "text", type: '${text.type}')

            column(name: "data_type", type: '${string.type}')

            column(name: "input_node_id", type: '${id.type}')

            column(name: "input_var", type: '${string.type}')

            column(name: "node_value", type: '${blob.type}')

            column(name: "operation", type: '${string.type}')

            column(name: "value", type: '${boolean.type}')

            column(name: "variable_name", type: '${string.type}')

            column(name: "map_to_input_fields", type: '${boolean.type}')

            column(name: "meter_type_id", type: '${id.type}')

            column(name: "monica_measuring_time_input_node_id", type: '${id.type}')

            column(name: "monica_measuring_time_input_var", type: '${string.type}')

            column(name: "next_fail_id", type: '${id.type}')

            column(name: "simulate", type: '${boolean.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-35") {
        createTable(tableName: "questionnaire_schedule") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "questionnaireSchedulePK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "day_interval", type: '${integer.type}') {
                constraints(nullable: "false")
            }

            column(name: "DAYS_IN_MONTH", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "STARTING_DATE", type: '${datetime.type}') {
                constraints(nullable: "false")
            }

            column(name: "TIMES_OF_DAY", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "WEEKDAYS", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "monitoring_plan_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "patient_questionnaire_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "type", type: '${string.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-36") {
        createTable(tableName: "role") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "rolePK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "authority", type: '${string128.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-37") {
        createTable(tableName: "role_permission") {
            column(name: "permission_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "role_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-38") {
        createTable(tableName: "standard_threshold_set") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "standard_threPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-39") {
        createTable(tableName: "standard_threshold_set_threshold") {
            column(name: "standard_threshold_set_thresholds_id", type: '${id.type}')

            column(name: "threshold_id", type: '${id.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-40") {
        createTable(tableName: "threshold") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "thresholdPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "type_id", type: '${id.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-41") {
        createTable(tableName: "urine_threshold") {
            column(name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "urine_threshoPK")
            }

            column(name: "alert_high", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "alert_low", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "warning_high", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "warning_low", type: '${string.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-42") {
        createTable(tableName: "user_role") {
            column(name: "role_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "user_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-43") {
        createTable(tableName: "users") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "usersPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "account_expired", type: '${boolean.type}') {
                constraints(nullable: "false")
            }

            column(name: "account_locked", type: '${boolean.type}') {
                constraints(nullable: "false")
            }

            column(name: "cleartext_password", type: '${string.type}')

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "enabled", type: '${boolean.type}') {
                constraints(nullable: "false")
            }

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "password", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "password_expired", type: '${boolean.type}') {
                constraints(nullable: "false")
            }

            column(name: "username", type: '${string128.type}') {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-44") {
        addPrimaryKey(columnNames: "permission_id, role_id", constraintName: "role_permissiPK", tableName: "role_permission")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-45") {
        addPrimaryKey(columnNames: "role_id, user_id", constraintName: "user_rolePK", tableName: "user_role")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-46") {
        addForeignKeyConstraint(baseColumnNames: "action_id", baseTableName: "audit_log_entry", constraintName: "FK40704C53F84F8915", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "audit_log_controller_entity", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-47") {
        addForeignKeyConstraint(baseColumnNames: "entry_id", baseTableName: "audit_log_parameter", constraintName: "FK4D351F0AF5DB4508", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "audit_log_entry", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-48") {
        addForeignKeyConstraint(baseColumnNames: "input_node_id", baseTableName: "choice_value", constraintName: "FKE637D55351F08944", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "questionnaire_node", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-49") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "clinician", constraintName: "FK9D8F786E1B21BFA", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "users", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-50") {
        addForeignKeyConstraint(baseColumnNames: "clinician_id", baseTableName: "clinician2patient_group", constraintName: "FK7A2AADC14C9484DA", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "clinician", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-51") {
        addForeignKeyConstraint(baseColumnNames: "patient_group_id", baseTableName: "clinician2patient_group", constraintName: "FK7A2AADC18260AB6F", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient_group", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-52") {
        addForeignKeyConstraint(baseColumnNames: "clinician_id", baseTableName: "clinician_question_preference", constraintName: "FK16BC0D834C9484DA", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "clinician", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-53") {
        addForeignKeyConstraint(baseColumnNames: "question_id", baseTableName: "clinician_question_preference", constraintName: "FK16BC0D833219E8AE", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "questionnaire_node", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-54") {
        addForeignKeyConstraint(baseColumnNames: "acknowledged_by_id", baseTableName: "completed_questionnaire", constraintName: "FK447176AF8FF1BA9A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "clinician", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-55") {
        addForeignKeyConstraint(baseColumnNames: "patient_id", baseTableName: "completed_questionnaire", constraintName: "FK447176AF41C6E17A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-56") {
        addForeignKeyConstraint(baseColumnNames: "patient_questionnaire_id", baseTableName: "completed_questionnaire", constraintName: "FK447176AFE09BD51F", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient_questionnaire", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-57") {
        addForeignKeyConstraint(baseColumnNames: "questionnare_ignored_by_id", baseTableName: "completed_questionnaire", constraintName: "FK447176AFA08DD7B1", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "clinician", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-58") {
        addForeignKeyConstraint(baseColumnNames: "measurement_node_result_id", baseTableName: "measurement", constraintName: "FK93F2DBBC167D75AE", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "node_result", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-59") {
        addForeignKeyConstraint(baseColumnNames: "measurement_type_id", baseTableName: "measurement", constraintName: "FK93F2DBBC7D05B1B3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "measurement_type", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-60") {
        addForeignKeyConstraint(baseColumnNames: "meter_id", baseTableName: "measurement", constraintName: "FK93F2DBBC2963DAFA", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "meter", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-61") {
        addForeignKeyConstraint(baseColumnNames: "patient_id", baseTableName: "measurement", constraintName: "FK93F2DBBC41C6E17A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-62") {
        addForeignKeyConstraint(baseColumnNames: "department_id", baseTableName: "message", constraintName: "FK38EB0007BE2A029A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "department", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-63") {
        addForeignKeyConstraint(baseColumnNames: "in_reply_to_id", baseTableName: "message", constraintName: "FK38EB000729309197", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "message", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-64") {
        addForeignKeyConstraint(baseColumnNames: "patient_id", baseTableName: "message", constraintName: "FK38EB000741C6E17A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-65") {
        addForeignKeyConstraint(baseColumnNames: "meter_type_id", baseTableName: "meter", constraintName: "FK62FAB89CBF36DCD", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "meter_type", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-66") {
        addForeignKeyConstraint(baseColumnNames: "monitor_kit_id", baseTableName: "meter", constraintName: "FK62FAB8952511EE5", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "monitor_kit", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-67") {
        addForeignKeyConstraint(baseColumnNames: "patient_id", baseTableName: "meter", constraintName: "FK62FAB8941C6E17A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-68") {
        addForeignKeyConstraint(baseColumnNames: "department_id", baseTableName: "monitor_kit", constraintName: "FK5E2B4E71BE2A029A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "department", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-69") {
        addForeignKeyConstraint(baseColumnNames: "patient_id", baseTableName: "monitor_kit", constraintName: "FK5E2B4E7141C6E17A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-70") {
        addForeignKeyConstraint(baseColumnNames: "patient_id", baseTableName: "monitoring_plan", constraintName: "FK4B2E0CC041C6E17A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-71") {
        addForeignKeyConstraint(baseColumnNames: "patient_id", baseTableName: "next_of_kin_person", constraintName: "FK3F1C72C041C6E17A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-72") {
        addForeignKeyConstraint(baseColumnNames: "acknowledged_by_id", baseTableName: "node_result", constraintName: "FK366CB0FA8FF1BA9A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "clinician", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-73") {
        addForeignKeyConstraint(baseColumnNames: "completed_questionnaire_id", baseTableName: "node_result", constraintName: "FK366CB0FA51B46113", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "completed_questionnaire", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-74") {
        addForeignKeyConstraint(baseColumnNames: "measurement_type_id", baseTableName: "node_result", constraintName: "FK366CB0FA7D05B1B3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "measurement_type", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-75") {
        addForeignKeyConstraint(baseColumnNames: "node_ignored_by_id", baseTableName: "node_result", constraintName: "FK366CB0FACA408B27", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "clinician", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-76") {
        addForeignKeyConstraint(baseColumnNames: "patient_questionnaire_node_id", baseTableName: "node_result", constraintName: "FK366CB0FABF8AE932", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient_questionnaire_node", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-77") {
        addForeignKeyConstraint(baseColumnNames: "monitoring_plan_id", baseTableName: "patient", constraintName: "FKD0D3EB05385A86AB", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "monitoring_plan", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-78") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "patient", constraintName: "FKD0D3EB051B21BFA", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "users", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-79") {
        addForeignKeyConstraint(baseColumnNames: "patient_group_id", baseTableName: "patient2patient_group", constraintName: "FK5C2B4C188260AB6F", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient_group", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-80") {
        addForeignKeyConstraint(baseColumnNames: "patient_id", baseTableName: "patient2patient_group", constraintName: "FK5C2B4C1841C6E17A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-81") {
        addForeignKeyConstraint(baseColumnNames: "patient_id", baseTableName: "patient_blue_alarm_questionnaireids", constraintName: "FK80C5BDAE41C6E17A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-82") {
        addForeignKeyConstraint(baseColumnNames: "patient_input_node_id", baseTableName: "patient_choice_value", constraintName: "FKC84E244DA59D9C00", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient_questionnaire_node", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-83") {
        addForeignKeyConstraint(baseColumnNames: "department_id", baseTableName: "patient_group", constraintName: "FKAF742CC5BE2A029A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "department", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-84") {
        addForeignKeyConstraint(baseColumnNames: "standard_threshold_set_id", baseTableName: "patient_group", constraintName: "FKAF742CC5373AD722", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "standard_threshold_set", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-85") {
        addForeignKeyConstraint(baseColumnNames: "patient_id", baseTableName: "patient_note", constraintName: "FKCBDD98EC41C6E17A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-86") {
        addForeignKeyConstraint(baseColumnNames: "clinician_id", baseTableName: "patient_note_clinician", constraintName: "FKAD64D4DB4C9484DA", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "clinician", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-87") {
        addForeignKeyConstraint(baseColumnNames: "patient_note_seen_by_id", baseTableName: "patient_note_clinician", constraintName: "FKAD64D4DB9C1AEAE9", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient_note", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-88") {
        addForeignKeyConstraint(baseColumnNames: "creator_id", baseTableName: "patient_questionnaire", constraintName: "FK24149DE923FA7C5C", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "clinician", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-89") {
        addForeignKeyConstraint(baseColumnNames: "monitoring_plan_id", baseTableName: "patient_questionnaire", constraintName: "FK24149DE9385A86AB", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "monitoring_plan", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-90") {
        addForeignKeyConstraint(baseColumnNames: "patient_id", baseTableName: "patient_questionnaire", constraintName: "FK24149DE941C6E17A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-91") {
        addForeignKeyConstraint(baseColumnNames: "start_node_id", baseTableName: "patient_questionnaire", constraintName: "FK24149DE98D386AB", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient_questionnaire_node", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-92") {
        addForeignKeyConstraint(baseColumnNames: "template_questionnaire_id", baseTableName: "patient_questionnaire", constraintName: "FK24149DE92A7814D4", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "questionnaire", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-93") {
        addForeignKeyConstraint(baseColumnNames: "alternative_next_id", baseTableName: "patient_questionnaire_node", constraintName: "FKC44D12987397A065", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient_questionnaire_node", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-94") {
        addForeignKeyConstraint(baseColumnNames: "default_next_id", baseTableName: "patient_questionnaire_node", constraintName: "FKC44D129868687AF9", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient_questionnaire_node", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-95") {
        addForeignKeyConstraint(baseColumnNames: "input_node_id", baseTableName: "patient_questionnaire_node", constraintName: "FKC44D12981FCE3213", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient_questionnaire_node", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-96") {
        addForeignKeyConstraint(baseColumnNames: "meter_type_id", baseTableName: "patient_questionnaire_node", constraintName: "FKC44D1298CBF36DCD", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "meter_type", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-97") {
        addForeignKeyConstraint(baseColumnNames: "monica_measuring_time_input_node_id", baseTableName: "patient_questionnaire_node", constraintName: "FKC44D12989F1CA09", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient_questionnaire_node", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-98") {
        addForeignKeyConstraint(baseColumnNames: "next_fail_id", baseTableName: "patient_questionnaire_node", constraintName: "FKC44D129844D1DA0", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient_questionnaire_node", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-99") {
        addForeignKeyConstraint(baseColumnNames: "questionnaire_id", baseTableName: "patient_questionnaire_node", constraintName: "FKC44D1298A0999325", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient_questionnaire", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-100") {
        addForeignKeyConstraint(baseColumnNames: "template_questionnaire_node_id", baseTableName: "patient_questionnaire_node", constraintName: "FKC44D12982DEB6871", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "questionnaire_node", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-101") {
        addForeignKeyConstraint(baseColumnNames: "patient_thresholds_id", baseTableName: "patient_threshold", constraintName: "FK48C487B17BBFF47D", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-102") {
        addForeignKeyConstraint(baseColumnNames: "threshold_id", baseTableName: "patient_threshold", constraintName: "FK48C487B146BA7BA", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "threshold", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-103") {
        addForeignKeyConstraint(baseColumnNames: "creator_id", baseTableName: "questionnaire", constraintName: "FKC3610DA323FA7C5C", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "clinician", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-104") {
        addForeignKeyConstraint(baseColumnNames: "start_node_id", baseTableName: "questionnaire", constraintName: "FKC3610DA36F0DA35", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "questionnaire_node", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-105") {
        addForeignKeyConstraint(baseColumnNames: "meter_type_id", baseTableName: "questionnaire2meter_type", constraintName: "FK4148725FCBF36DCD", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "meter_type", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-106") {
        addForeignKeyConstraint(baseColumnNames: "questionnaire_id", baseTableName: "questionnaire2meter_type", constraintName: "FK4148725FA3B23BAF", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "questionnaire", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-107") {
        addForeignKeyConstraint(baseColumnNames: "alternative_next_id", baseTableName: "questionnaire_node", constraintName: "FK7BD3671E71B4F3EF", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "questionnaire_node", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-108") {
        addForeignKeyConstraint(baseColumnNames: "default_next_id", baseTableName: "questionnaire_node", constraintName: "FK7BD3671E6685CE83", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "questionnaire_node", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-109") {
        addForeignKeyConstraint(baseColumnNames: "input_node_id", baseTableName: "questionnaire_node", constraintName: "FK7BD3671E1DEB859D", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "questionnaire_node", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-110") {
        addForeignKeyConstraint(baseColumnNames: "meter_type_id", baseTableName: "questionnaire_node", constraintName: "FK7BD3671ECBF36DCD", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "meter_type", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-111") {
        addForeignKeyConstraint(baseColumnNames: "monica_measuring_time_input_node_id", baseTableName: "questionnaire_node", constraintName: "FK7BD3671E80F1D93", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "questionnaire_node", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-112") {
        addForeignKeyConstraint(baseColumnNames: "next_fail_id", baseTableName: "questionnaire_node", constraintName: "FK7BD3671E26A712A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "questionnaire_node", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-113") {
        addForeignKeyConstraint(baseColumnNames: "questionnaire_id", baseTableName: "questionnaire_node", constraintName: "FK7BD3671EA3B23BAF", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "questionnaire", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-114") {
        addForeignKeyConstraint(baseColumnNames: "monitoring_plan_id", baseTableName: "questionnaire_schedule", constraintName: "FK9B0C5DB3385A86AB", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "monitoring_plan", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-115") {
        addForeignKeyConstraint(baseColumnNames: "patient_questionnaire_id", baseTableName: "questionnaire_schedule", constraintName: "FK9B0C5DB3E09BD51F", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient_questionnaire", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-116") {
        addForeignKeyConstraint(baseColumnNames: "permission_id", baseTableName: "role_permission", constraintName: "FKBD40D538DE1B957A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "permission", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-117") {
        addForeignKeyConstraint(baseColumnNames: "role_id", baseTableName: "role_permission", constraintName: "FKBD40D5385C87581A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "role", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-118") {
        addForeignKeyConstraint(baseColumnNames: "standard_threshold_set_thresholds_id", baseTableName: "standard_threshold_set_threshold", constraintName: "FKEDFC24787F2FA653", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "standard_threshold_set", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-119") {
        addForeignKeyConstraint(baseColumnNames: "threshold_id", baseTableName: "standard_threshold_set_threshold", constraintName: "FKEDFC247846BA7BA", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "threshold", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-120") {
        addForeignKeyConstraint(baseColumnNames: "type_id", baseTableName: "threshold", constraintName: "FKA3E1E46B14CAB36", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "measurement_type", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-121") {
        addForeignKeyConstraint(baseColumnNames: "role_id", baseTableName: "user_role", constraintName: "FK143BF46A5C87581A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "role", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-122") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "user_role", constraintName: "FK143BF46A1B21BFA", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "users", referencesUniqueColumn: "false")
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-123") {
        createIndex(indexName: "FK40704C53F84F8915", tableName: "audit_log_entry") {
            column(name: "action_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-124") {
        createIndex(indexName: "end_time_idx", tableName: "audit_log_entry") {
            column(name: "end_time")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-125") {
        createIndex(indexName: "patient_cpr_idx", tableName: "audit_log_entry") {
            column(name: "patient_cpr")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-126") {
        createIndex(indexName: "start_time_idx", tableName: "audit_log_entry") {
            column(name: "start_time")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-127") {
        createIndex(indexName: "FK4D351F0AF5DB4508", tableName: "audit_log_parameter") {
            column(name: "entry_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-128") {
        createIndex(indexName: "FKE637D55351F08944", tableName: "choice_value") {
            column(name: "input_node_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-129") {
        createIndex(indexName: "FK9D8F786E1B21BFA", tableName: "clinician") {
            column(name: "user_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-130") {
        createIndex(indexName: "FK7A2AADC14C9484DA", tableName: "clinician2patient_group") {
            column(name: "clinician_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-131") {
        createIndex(indexName: "FK7A2AADC18260AB6F", tableName: "clinician2patient_group") {
            column(name: "patient_group_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-132") {
        createIndex(indexName: "unique_clinician_id", tableName: "clinician2patient_group") {
            column(name: "patient_group_id")

            column(name: "clinician_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-133") {
        createIndex(indexName: "FK16BC0D833219E8AE", tableName: "clinician_question_preference") {
            column(name: "question_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-134") {
        createIndex(indexName: "FK16BC0D834C9484DA", tableName: "clinician_question_preference") {
            column(name: "clinician_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-135") {
        createIndex(indexName: "FK447176AF41C6E17A", tableName: "completed_questionnaire") {
            column(name: "patient_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-136") {
        createIndex(indexName: "FK447176AF8FF1BA9A", tableName: "completed_questionnaire") {
            column(name: "acknowledged_by_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-137") {
        createIndex(indexName: "FK447176AFA08DD7B1", tableName: "completed_questionnaire") {
            column(name: "questionnare_ignored_by_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-138") {
        createIndex(indexName: "FK447176AFE09BD51F", tableName: "completed_questionnaire") {
            column(name: "patient_questionnaire_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-139") {
        createIndex(indexName: "FK93F2DBBC167D75AE", tableName: "measurement") {
            column(name: "measurement_node_result_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-140") {
        createIndex(indexName: "FK93F2DBBC2963DAFA", tableName: "measurement") {
            column(name: "meter_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-141") {
        createIndex(indexName: "FK93F2DBBC41C6E17A", tableName: "measurement") {
            column(name: "patient_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-142") {
        createIndex(indexName: "FK93F2DBBC7D05B1B3", tableName: "measurement") {
            column(name: "measurement_type_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-143") {
        createIndex(indexName: "name_idx", tableName: "measurement_type") {
            column(name: "name")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-144") {
        createIndex(indexName: "FK38EB000729309197", tableName: "message") {
            column(name: "in_reply_to_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-145") {
        createIndex(indexName: "FK38EB000741C6E17A", tableName: "message") {
            column(name: "patient_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-146") {
        createIndex(indexName: "FK38EB0007BE2A029A", tableName: "message") {
            column(name: "department_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-147") {
        createIndex(indexName: "FK62FAB8941C6E17A", tableName: "meter") {
            column(name: "patient_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-148") {
        createIndex(indexName: "FK62FAB8952511EE5", tableName: "meter") {
            column(name: "monitor_kit_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-149") {
        createIndex(indexName: "FK62FAB89CBF36DCD", tableName: "meter") {
            column(name: "meter_type_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-150") {
        createIndex(indexName: "FK5E2B4E7141C6E17A", tableName: "monitor_kit") {
            column(name: "patient_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-151") {
        createIndex(indexName: "FK5E2B4E71BE2A029A", tableName: "monitor_kit") {
            column(name: "department_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-152") {
        createIndex(indexName: "monkit_deptid_name_unique", tableName: "monitor_kit") {
            column(name: "department_id")

            column(name: "name")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-153") {
        createIndex(indexName: "FK4B2E0CC041C6E17A", tableName: "monitoring_plan") {
            column(name: "patient_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-154") {
        createIndex(indexName: "FK3F1C72C041C6E17A", tableName: "next_of_kin_person") {
            column(name: "patient_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-155") {
        createIndex(indexName: "FK366CB0FA51B46113", tableName: "node_result") {
            column(name: "completed_questionnaire_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-156") {
        createIndex(indexName: "FK366CB0FA7D05B1B3", tableName: "node_result") {
            column(name: "measurement_type_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-157") {
        createIndex(indexName: "FK366CB0FA8FF1BA9A", tableName: "node_result") {
            column(name: "acknowledged_by_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-158") {
        createIndex(indexName: "FK366CB0FABF8AE932", tableName: "node_result") {
            column(name: "patient_questionnaire_node_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-159") {
        createIndex(indexName: "FK366CB0FACA408B27", tableName: "node_result") {
            column(name: "node_ignored_by_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-160") {
        createIndex(indexName: "FKD0D3EB051B21BFA", tableName: "patient") {
            column(name: "user_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-161") {
        createIndex(indexName: "FKD0D3EB05385A86AB", tableName: "patient") {
            column(name: "monitoring_plan_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-162") {
        createIndex(indexName: "FK5C2B4C1841C6E17A", tableName: "patient2patient_group") {
            column(name: "patient_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-163") {
        createIndex(indexName: "FK5C2B4C188260AB6F", tableName: "patient2patient_group") {
            column(name: "patient_group_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-164") {
        createIndex(indexName: "unique_patient_id", tableName: "patient2patient_group") {
            column(name: "patient_group_id")

            column(name: "patient_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-165") {
        createIndex(indexName: "FK80C5BDAE41C6E17A", tableName: "patient_blue_alarm_questionnaireids") {
            column(name: "patient_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-166") {
        createIndex(indexName: "FKC84E244DA59D9C00", tableName: "patient_choice_value") {
            column(name: "patient_input_node_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-167") {
        createIndex(indexName: "FKAF742CC5373AD722", tableName: "patient_group") {
            column(name: "standard_threshold_set_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-168") {
        createIndex(indexName: "FKAF742CC5BE2A029A", tableName: "patient_group") {
            column(name: "department_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-169") {
        createIndex(indexName: "standard_threshold_set_id_unique_1361794626886", tableName: "patient_group", unique: "true") {
            column(name: "standard_threshold_set_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-170") {
        createIndex(indexName: "ptgroup_deptid_name_unique", tableName: "patient_group") {
            column(name: "department_id")

            column(name: "name")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-171") {
        createIndex(indexName: "FKCBDD98EC41C6E17A", tableName: "patient_note") {
            column(name: "patient_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-172") {
        createIndex(indexName: "FKAD64D4DB4C9484DA", tableName: "patient_note_clinician") {
            column(name: "clinician_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-173") {
        createIndex(indexName: "FKAD64D4DB9C1AEAE9", tableName: "patient_note_clinician") {
            column(name: "patient_note_seen_by_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-174") {
        createIndex(indexName: "FK24149DE923FA7C5C", tableName: "patient_questionnaire") {
            column(name: "creator_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-175") {
        createIndex(indexName: "FK24149DE92A7814D4", tableName: "patient_questionnaire") {
            column(name: "template_questionnaire_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-176") {
        createIndex(indexName: "FK24149DE9385A86AB", tableName: "patient_questionnaire") {
            column(name: "monitoring_plan_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-177") {
        createIndex(indexName: "FK24149DE941C6E17A", tableName: "patient_questionnaire") {
            column(name: "patient_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-178") {
        createIndex(indexName: "FK24149DE98D386AB", tableName: "patient_questionnaire") {
            column(name: "start_node_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-179") {
        createIndex(indexName: "unique_revision", tableName: "patient_questionnaire") {
            column(name: "name")

            column(name: "patient_id")

            column(name: "revision")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-180") {
        createIndex(indexName: "FKC44D12981FCE3213", tableName: "patient_questionnaire_node") {
            column(name: "input_node_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-181") {
        createIndex(indexName: "FKC44D12982DEB6871", tableName: "patient_questionnaire_node") {
            column(name: "template_questionnaire_node_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-182") {
        createIndex(indexName: "FKC44D129844D1DA0", tableName: "patient_questionnaire_node") {
            column(name: "next_fail_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-183") {
        createIndex(indexName: "FKC44D129868687AF9", tableName: "patient_questionnaire_node") {
            column(name: "default_next_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-184") {
        createIndex(indexName: "FKC44D12987397A065", tableName: "patient_questionnaire_node") {
            column(name: "alternative_next_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-185") {
        createIndex(indexName: "FKC44D12989F1CA09", tableName: "patient_questionnaire_node") {
            column(name: "monica_measuring_time_input_node_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-186") {
        createIndex(indexName: "FKC44D1298A0999325", tableName: "patient_questionnaire_node") {
            column(name: "questionnaire_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-187") {
        createIndex(indexName: "FKC44D1298CBF36DCD", tableName: "patient_questionnaire_node") {
            column(name: "meter_type_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-188") {
        createIndex(indexName: "FK48C487B146BA7BA", tableName: "patient_threshold") {
            column(name: "threshold_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-189") {
        createIndex(indexName: "FK48C487B17BBFF47D", tableName: "patient_threshold") {
            column(name: "patient_thresholds_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-190") {
        createIndex(indexName: "FKC3610DA323FA7C5C", tableName: "questionnaire") {
            column(name: "creator_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-191") {
        createIndex(indexName: "FKC3610DA36F0DA35", tableName: "questionnaire") {
            column(name: "start_node_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-192") {
        createIndex(indexName: "FK4148725FA3B23BAF", tableName: "questionnaire2meter_type") {
            column(name: "questionnaire_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-193") {
        createIndex(indexName: "FK4148725FCBF36DCD", tableName: "questionnaire2meter_type") {
            column(name: "meter_type_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-194") {
        createIndex(indexName: "FK7BD3671E1DEB859D", tableName: "questionnaire_node") {
            column(name: "input_node_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-195") {
        createIndex(indexName: "FK7BD3671E26A712A", tableName: "questionnaire_node") {
            column(name: "next_fail_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-196") {
        createIndex(indexName: "FK7BD3671E6685CE83", tableName: "questionnaire_node") {
            column(name: "default_next_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-197") {
        createIndex(indexName: "FK7BD3671E71B4F3EF", tableName: "questionnaire_node") {
            column(name: "alternative_next_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-198") {
        createIndex(indexName: "FK7BD3671E80F1D93", tableName: "questionnaire_node") {
            column(name: "monica_measuring_time_input_node_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-199") {
        createIndex(indexName: "FK7BD3671EA3B23BAF", tableName: "questionnaire_node") {
            column(name: "questionnaire_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-200") {
        createIndex(indexName: "FK7BD3671ECBF36DCD", tableName: "questionnaire_node") {
            column(name: "meter_type_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-201") {
        createIndex(indexName: "FK9B0C5DB3385A86AB", tableName: "questionnaire_schedule") {
            column(name: "monitoring_plan_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-202") {
        createIndex(indexName: "FK9B0C5DB3E09BD51F", tableName: "questionnaire_schedule") {
            column(name: "patient_questionnaire_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-203") {
        createIndex(indexName: "unique_monitoring_plan_id", tableName: "questionnaire_schedule") {
            column(name: "patient_questionnaire_id")

            column(name: "monitoring_plan_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-204") {
        createIndex(indexName: "authority_unique_1361794626904", tableName: "role", unique: "true") {
            column(name: "authority")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-205") {
        createIndex(indexName: "FKBD40D5385C87581A", tableName: "role_permission") {
            column(name: "role_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-206") {
        createIndex(indexName: "FKBD40D538DE1B957A", tableName: "role_permission") {
            column(name: "permission_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-207") {
        createIndex(indexName: "FKEDFC247846BA7BA", tableName: "standard_threshold_set_threshold") {
            column(name: "threshold_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-208") {
        createIndex(indexName: "FKEDFC24787F2FA653", tableName: "standard_threshold_set_threshold") {
            column(name: "standard_threshold_set_thresholds_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-209") {
        createIndex(indexName: "FKA3E1E46B14CAB36", tableName: "threshold") {
            column(name: "type_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-210") {
        createIndex(indexName: "FK143BF46A1B21BFA", tableName: "user_role") {
            column(name: "user_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-211") {
        createIndex(indexName: "FK143BF46A5C87581A", tableName: "user_role") {
            column(name: "role_id")
        }
    }

    changeSet(author: "henrik (generated)", id: "1361794626972-212") {
        createIndex(indexName: "username_unique_1361794626910", tableName: "users", unique: "true") {
            column(name: "username")
        }
    }
}