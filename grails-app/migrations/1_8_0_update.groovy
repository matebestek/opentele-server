databaseChangeLog = {

    // Fix to error in earlier mappings
    changeSet(author: "Henrik (fix)", id: "fix_glucose_in_urine_1") {
        modifyDataType(tableName: "measurement", columnName: "glucose_in_urine", newDataType: '${string.type}')
    }


    // FEV Measurement
    changeSet(author: "of", id: "add_fev_to_measuremnt") {
        addColumn(tableName: "measurement") {
            column(name: "fev6", type: '${double.type}')
            column(name: "fev1fev6ratio", type: '${double.type}')
            column(name: "fef2575", type: '${double.type}')
            column(name: "is_good_test", type: '${boolean.type}')
            column(name: "fev_software_version", type: '${integer.type}')
        }
    }

    // Standard schedules
    changeSet(author: "mss", id: "add_standard_schedules") {
        // static embedded
        addColumn(tableName: "questionnaire") {
            column(name: "STANDARD_SCHEDULE_TYPE", type: '${string.type}')
        }
        addColumn(tableName: "questionnaire") {
            column(name: "STANDARD_SCHEDULE_WEEKDAYS", type: '${string.type}')
        }
        addColumn(tableName: "questionnaire") {
            column(name: "STANDARD_SCHEDULE_TIMES_OF_DAY", type: '${string.type}')
        }
        addColumn(tableName: "questionnaire") {
            column(name: "STANDARD_SCHEDULE_DAYS_IN_MONTH", type: '${string.type}')
        }
        addColumn(tableName: "questionnaire") {
            column(name: "STANDARD_SCHEDULE_INTERVAL_IN_DAYS", type: '${integer.type}')
        }
        addColumn(tableName: "questionnaire") {
            column(name: "STANDARD_SCHEDULE_STARTING_DATE", type: '${datetime.type}')
        }
        addColumn(tableName: "questionnaire") {
            column(name: "STANDARD_SCHEDULE_SPECIFIC_DATE", type: '${datetime.type}')
        }
        addColumn(tableName: "questionnaire") {
            column(name: "STANDARD_SCHEDULE_REMINDER_START_MINUTES", type: '${integer.type}')
        }

        addColumn(tableName: "questionnaire_schedule") {
            column(name: "SPECIFIC_DATE", type: '${datetime.type}')
        }
        addColumn(tableName: "questionnaire_schedule") {
            column(name: "REMINDER_START_MINUTES", type: '${integer.type}')
        }

        // migrate existing questionnaires
        sql(''' update ${fullSchemaName}questionnaire set STANDARD_SCHEDULE_TYPE = 'UNSCHEDULED' ''' )
        sql(''' update ${fullSchemaName}questionnaire set STANDARD_SCHEDULE_WEEKDAYS = '' ''' )
        sql(''' update ${fullSchemaName}questionnaire set STANDARD_SCHEDULE_TIMES_OF_DAY = '' ''' )
        sql(''' update ${fullSchemaName}questionnaire set STANDARD_SCHEDULE_DAYS_IN_MONTH = '' ''' )
        sql(''' update ${fullSchemaName}questionnaire set STANDARD_SCHEDULE_INTERVAL_IN_DAYS = 2 ''' )
        sql(''' update ${fullSchemaName}questionnaire set STANDARD_SCHEDULE_STARTING_DATE = ${now} ''' )
        sql(''' update ${fullSchemaName}questionnaire set STANDARD_SCHEDULE_SPECIFIC_DATE = ${now} ''' )
        sql(''' update ${fullSchemaName}questionnaire set STANDARD_SCHEDULE_REMINDER_START_MINUTES = 30 ''' )

        // and the constraints
        addNotNullConstraint(columnDataType: '${string.type}', columnName: "STANDARD_SCHEDULE_TYPE", tableName: "questionnaire")

        // migrate existing questionnaireSchedules
        sql(''' update ${fullSchemaName}questionnaire_schedule set REMINDER_START_MINUTES = '30' ''')

    }

    // Video username
    changeSet(author: "emh", id: "add_video_username_password_to_clinician") {
        addColumn(tableName: "Clinician") {
            column(name: "video_user", type: '${string.type}')
            column(name: "video_password", type: '${string.type}')
        }
    }

    // KIH exported flag on measurement
    changeSet(author: "hra", id: "add_exported_to_kih_to_measurement") {
        addColumn(tableName: "measurement") {
            column(name: "exported_to_kih", type: '${boolean.type}')
        }
        sql(''' update ${fullSchemaName}measurement set exported_to_kih = 'FALSE' ''' )
        addNotNullConstraint(columnDataType: '${boolean.type}', columnName: "exported_to_kih", tableName: "measurement")
    }

    // Pending video conferences table
    changeSet(author: "emh", id: "add_pending_conference_table") {
        createTable(tableName: "pending_conference") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "pending_conference_PK")
            }

            column(name: "room_key", type: '${string.type}')
            column(name: "patient_id", type: '${id.type}')
            column(name: "clinician_id", type: '${id.type}')
            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }
            column(name: "created_by", type: '${string.type}')
            column(name: "created_date", type: '${datetime.type}')
            column(name: "modified_by", type: '${string.type}')
            column(name: "modified_date", type: '${datetime.type}')
        }
    }

    // Schedule window
    changeSet(author: "mark", id: "1371200701109-39") {
        createTable(tableName: "schedule_window") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "schedule_windPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string255.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string255.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "schedule_type", type: '${string255.type}') {
                constraints(nullable: "false")
            }

            column(name: "window_size_minutes", type: '${integer.type}') {
                constraints(nullable: "false")
            }
        }
    }

    // Patient pause intervals
    changeSet(author: "msu", id: "1370876709019-21") {
        createTable(tableName: "passive_interval") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "passive_interPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "patient_id", type: '${id.type}')

            column(name: "created_by", type: '${string255.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "interval_end_date", type: '${datetime.type}')

            column(name: "interval_start_date", type: '${datetime.type}')

            column(name: "comment", type: '${string2048.type}')  {
                constraints(nullable: "true")
            }


            column(name: "modified_by", type: '${string255.type}')

            column(name: "modified_date", type: '${datetime.type}')
        }
    }
    changeSet(author: "mark (generated)", id: "1370876709019-22") {
        addForeignKeyConstraint(baseColumnNames: "patient_id", baseTableName: "passive_interval", constraintName: "FK83F2DBBC167D75AE", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "patient", referencesUniqueColumn: "false")
    }

    // Remove monitoringplan from patient_questionnaire

    changeSet(author: "Henrik", id: "1371200701111-0") {
        dropForeignKeyConstraint(constraintName: "FK24149DE9385A86AB", baseTableName: "patient_questionnaire")
    }

    changeSet(author: "Henrik", id: "1371200701111-0.1") {
        dropIndex(tableName: "patient_questionnaire", indexName: "FK24149DE9385A86AB")
    }

    changeSet(author: "Henrik + Søren", id: "1371200701111-1") {
        dropColumn(columnName: "monitoring_plan_id", tableName: "patient_questionnaire")
    }

    changeSet(author: "Henrik", id: "custom-conversions-101") {
        comment { 'Step 1: CompletedQuestionnaires skal pege på det PatientQuestionnaire, som for hver Questionnaire har det mindste ID. ' +
                'Det er et designvalg, at vi lader patientQuestionnaire med den mindste ID være det PQ som overlever konverteringen.'
        }

        sql(''' update ${fullSchemaName}completed_questionnaire
            set patient_questionnaire_id = (select min(inn.TARGET_PQ) from (
                    select cq.patient_id,cq.id , cqq.id as CQ_Q, dist.qid as target_Q, cq.patient_questionnaire_id as CQ_PQ, dist.minpqid as TARGET_PQ
                    from ${fullSchemaName}completed_questionnaire cq
                    inner join ${fullSchemaName}patient_questionnaire cqpq on cq.patient_questionnaire_id = cqpq.id
                    inner join ${fullSchemaName}questionnaire cqq on cqq.id = cqpq.template_questionnaire_id
                    inner join (
                    select distinct q.id as qid, min(pq.id) as minpqid
                    from ${fullSchemaName}patient_questionnaire pq
                    inner join ${fullSchemaName}questionnaire q on pq.template_questionnaire_id = q.id
                    group by q.id
            ) dist on dist.qid = cqq.id
            ) inn
            where inn.CQ_PQ = completed_questionnaire.patient_questionnaire_id
            ) ''' )
    }

    // Add questionnaire_header
    changeSet(author: "Henrik + Søren", id: "1371200712314-1") {
        createTable(tableName: "questionnaire_header") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "questionnaire_header_PK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string255.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string255.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "name", type: '${string255.type}') {
                constraints(nullable: "false", unique: "true")
            }
            column(name: "active_questionnaire_id", type: '${id.type}') {
                constraints(nullable: "true")
            }
            column(name: "draft_questionnaire_id", type: '${id.type}') {
                constraints(nullable: "true")
            }
        }

        // TODO: Assumes no existing questionnaires. Only in env's where we want to migrate data...

        comment { '/* Step 2: Opret QuestionnaireHeaders ud fra Questionnaires.' +
                'Antagelse: 1) QuestionnaireHeader tabel er oprettet.. ' +
                '           2) Der må ikke findes flere med samme navn */'}

        sql(''' INSERT INTO ${fullSchemaName}questionnaire_header (version, created_by, created_date, modified_by, modified_date, name, active_questionnaire_id, draft_questionnaire_id)
            select 0, created_by, ${now}, modified_by, ${now}, name, id, null from ${fullSchemaName}questionnaire ''')

    }
    changeSet(author: "Henrik + Søren", id: "1371200712314-2") {

        addColumn(tableName: "questionnaire") {
            column(name: "questionnaire_header_id", type: '${id.type}')
        }
    }

    changeSet(author: "Henrik + Søren", id: "1371200712314-2.1_MS", dbms: 'mssql') {

        comment { 'Step 3 (mssql): Opret pointer fra Questionnaire til QuestionnaireHeader. ' }

        sql(''' UPDATE q
            SET q.questionnaire_header_id = qh.id
            FROM ${fullSchemaName}questionnaire q
            INNER JOIN  ${fullSchemaName}questionnaire_header qh ON qh.active_questionnaire_id = q.id ''')

    }
    changeSet(author: "Henrik + Søren", id: "1371200712314-2.1_H2", dbms: 'h2') {
        comment { 'Step 3 (h2): Ignore when on h2. ' }
    }
    changeSet(author: "Henrik + Søren", id: "1371200712314-2.1_MY", dbms: 'mysql') {

        comment { 'Step 3 mysql: Opret pointer fra Questionnaire til QuestionnaireHeader. ' }

        sql(''' UPDATE ${fullSchemaName}questionnaire q
            INNER JOIN ${fullSchemaName}questionnaire_header qh ON qh.active_questionnaire_id = q.id
            SET q.questionnaire_header_id = qh.id ''')
    }

    changeSet(author: "Henrik + Søren", id: "1371200712314-2.2") {
        addNotNullConstraint(columnDataType: '${id.type}', columnName: "questionnaire_header_id", tableName: "questionnaire")
    }

    changeSet(author: "Henrik + Søren", id: "1371200712314-3") {
        addForeignKeyConstraint(baseColumnNames: "questionnaire_header_id", baseTableName: "questionnaire", constraintName: "questionnaire_header_FK",
                referencedColumnNames: "id", referencedTableName: "questionnaire_header")
    }

    changeSet(author: "Henrik + Søren", id: "1371200712314-3.1") {
        addForeignKeyConstraint(baseColumnNames: "active_questionnaire_id", baseTableName: "questionnaire_header", constraintName: "active_quest_PK",
                referencedColumnNames: "id", referencedTableName: "questionnaire")
    }

    changeSet(author: "Henrik + Søren", id: "1371200712314-3.2") {
        addForeignKeyConstraint(baseColumnNames: "draft_questionnaire_id", baseTableName: "questionnaire_header", constraintName: "draft_quest_PK",
                referencedColumnNames: "id", referencedTableName: "questionnaire")
    }

    // Step 4 (before step 3!): Remove pq.patient ref

    changeSet(author: "Henrik", id: "1371200712314-5") {
        comment {'Step 4a: Remove patient_questionnaire.patient_id' }
        dropIndex(tableName: "patient_questionnaire", indexName: "unique_revision") // TODO: Korrekt på target DB'er?
    }

    changeSet(author: "Henrik", id: "1371200701111-7") {
        comment {'Step 4b: Remove patient_questionnaire.patient_id' }
        dropForeignKeyConstraint(constraintName: "FK24149DE941C6E17A", baseTableName: "patient_questionnaire")
    }

    changeSet(author: "Henrik", id: "1371200701111-7.1") {
        comment {'Step 4c: Remove patient_questionnaire.patient_id' }
        dropIndex(tableName: "patient_questionnaire", indexName: "FK24149DE941C6E17A")
    }

    changeSet(author: "Henrik", id: "1371200712314-8") {
        comment {'Step 4d: Remove patient_questionnaire.patient_id' }
        dropColumn(columnName: "patient_id", tableName: "patient_questionnaire")
    }


    changeSet(author: "Henrik", id: "1371200712314-9") {
        dropColumn(columnName: "deleted", tableName: "patient_questionnaire")
    }
    changeSet(author: "Henrik", id: "1371200712314-10") {
        dropColumn(columnName: "revision", tableName: "patient_questionnaire")
    }

    changeSet(author: "Henrik", id: "1371200712314-11") {

        addColumn(tableName: "questionnaire_schedule") {
            column(name: "questionnaire_header_id", type: '${id.type}')
        }

        comment { 'Step 3.1 : Opret pointer fra QuestionnaireSchedule til QuestionnaireHeader. ' }

        sql(''' update ${fullSchemaName}questionnaire_schedule set questionnaire_header_id = (select qh.id
            from ${fullSchemaName}questionnaire_header qh
                    inner join ${fullSchemaName}questionnaire q on qh.active_questionnaire_id = q.id
                    inner join ${fullSchemaName}patient_questionnaire pq on pq.template_questionnaire_id = q.id
                    where pq.id = questionnaire_schedule.patient_questionnaire_id) ''')

        addNotNullConstraint(columnDataType: '${id.type}', columnName: "questionnaire_header_id", tableName: "questionnaire_schedule")

    }
    changeSet(author: "Henrik", id: "1371200712314-12") {
        addForeignKeyConstraint(baseColumnNames: "questionnaire_header_id", baseTableName: "questionnaire_schedule", constraintName: "q_header_qsch_FK",
                referencedColumnNames: "id", referencedTableName: "questionnaire_header")
    }

    // Remove ref from questionnaire_schedule to patient_questionnaire
    changeSet(author: "henrik (generated)", id: "1371200712314-13") {
        comment {'Step 3.2a: Remove questionnaire_schedule.patient_questionnaire_id' }
        dropIndex(tableName: "questionnaire_schedule", indexName: "unique_monitoring_plan_id") // TODO: Korrekt på target DB'er?
    }

    changeSet(author: "Henrik", id: "1371200712314-14") {
        comment {'Step 3.2b: Remove questionnaire_schedule.patient_questionnaire_id' }
        dropForeignKeyConstraint(constraintName: "FK9B0C5DB3E09BD51F", baseTableName: "questionnaire_schedule")
    }

    changeSet(author: "henrik (generated)", id: "1371200712314-14.1") {
        comment {'Step 3.2c: Remove questionnaire_schedule.patient_questionnaire_id' }
        dropIndex(tableName: "questionnaire_schedule", indexName: "FK9B0C5DB3E09BD51F")
    }

    changeSet(author: "Henrik", id: "1371200712314-15") {
        comment {'Step 3.2d: Remove questionnaire_schedule.patient_questionnaire_id' }
        dropColumn(columnName: "patient_questionnaire_id", tableName: "questionnaire_schedule")
    }



    changeSet(author: "henrik (generated)", id: "1371200712314-16") {
        createIndex(indexName: "unique_monplan_header", tableName: "questionnaire_schedule") {
            column(name: "questionnaire_header_id")

            column(name: "monitoring_plan_id")
        }
    }

    changeSet(author: "Henrik", id: "1371200712314-17") {

        // TODO: Før constraint sættes skal completed_questionnaire.questionnaire_header_id vel sættes????

        addColumn(tableName: "completed_questionnaire") {
            column(name: "questionnaire_header_id", type: '${id.type}') {
                constraints(nullable: "true")
            }
        }
    }

    changeSet(author: "Henrik", id: "1371200712314-17.1" , dbms: 'mssql') {
        // TODO: Test
        sql(''' UPDATE cq
                     SET cq.questionnaire_header_id = qh.id
                     FROM ${fullSchemaName}completed_questionnaire cq
                     INNER JOIN ${fullSchemaName}patient_questionnaire pq ON cq.patient_questionnaire_id = pq.id
                     INNER JOIN ${fullSchemaName}questionnaire q ON pq.template_questionnaire_id = q.id
                     INNER JOIN ${fullSchemaName}questionnaire_header qh on q.questionnaire_header_id = qh.id ''')

    }
    changeSet(author: "Henrik", id: "1371200712314-17.2") {
        addNotNullConstraint(columnDataType: '${id.type}', columnName: "questionnaire_header_id", tableName: "completed_questionnaire")
    }

    changeSet(author: "Henrik", id: "1371200712314-18") {
        addForeignKeyConstraint(baseColumnNames: "questionnaire_header_id", baseTableName: "completed_questionnaire", constraintName: "q_header_comp_FK",
                referencedColumnNames: "id", referencedTableName: "questionnaire_header")
    }

    // Step 5: For hver PQnode, skal den flyttes til at pege på det PQ, som lever videre (min(id)

//    changeSet(author: "Henrik", id: "update_node_results-1" , dbms: 'mssql') {
//        // TODO: Test
//        // Liquibase erroneously replaces ; with GO statements.
//        // When generating SQL using dbm-update-sql GO must be changed back to ; manually!
//        // See: https://liquibase.jira.com/browse/CORE-948
//
//
//        sql('''
//    DECLARE @Questionnaire2PatientQuestionnaire TABLE
//    (
//      q_id BIGINT NOT NULL PRIMARY KEY,
//      pq_id BIGINT NOT NULL
//    );
//
//    DECLARE @QuestionnaireNode2PatientQuestionnaireNode TABLE
//    (
//      qn_id BIGINT NOT NULL PRIMARY KEY,
//      pqn_id BIGINT NOT NULL
//    );
//
//    DECLARE @OldPqn2NewPqn TABLE
//    (
//      old_pqn_id BIGINT NOT NULL PRIMARY KEY,
//      new_pqn_id BIGINT NOT NULL
//    );
//
//    -- questionnaire to patient_questionnaire mapping
//    INSERT INTO @Questionnaire2PatientQuestionnaire(q_id, pq_id)
//    select distinct q.id as q_id, min(pq.id) as pq_id
//    from ${fullSchemaName}patient_questionnaire pq
//    inner join ${fullSchemaName}questionnaire q on pq.template_questionnaire_id = q.id
//    group by q.id;
//
//
//    -- questionnaire_node to patient_questionnaire_node mapping
//    INSERT INTO @QuestionnaireNode2PatientQuestionnaireNode(qn_id, pqn_id)
//    select distinct qn.id as qn_id, min(pqn.id) as pqn_id
//    from ${fullSchemaName}patient_questionnaire_node pqn
//    inner join ${fullSchemaName}questionnaire_node qn on pqn.template_questionnaire_node_id = qn.id
//    group by qn.id;
//
//    -- create old_pqn_id to new pqn_id mapping
//    INSERT INTO @OldPqn2NewPqn(old_pqn_id, new_pqn_id)
//    SELECT old_pqn.id as old_pqn_id, new_pq.pqn_id as new_pqn_id
//    from @QuestionnaireNode2PatientQuestionnaireNode new_pq
//    inner join ${fullSchemaName}patient_questionnaire_node old_pqn on old_pqn.template_questionnaire_node_id = new_pq.qn_id
//    ;
//
//    -- Update node_results
//    update nr
//    set nr.patient_questionnaire_node_id = new_pqn_id
//    from ${fullSchemaName}node_result nr
//    inner join @OldPqn2NewPqn map on nr.patient_questionnaire_node_id = map.old_pqn_id;
//    ''')
//
//    }

//    // Step 6: Cleanup PQN'er som ikke skal overleve
//    changeSet(author: "Henrik", id: "cleanup_PQs_1" , dbms: 'mssql') {
//
//        // Liquibase erroneously replaces ; with GO statements.
//        // When generating SQL using dbm-update-sql GO must be changed back to ; manually!
//        // See: https://liquibase.jira.com/browse/CORE-948
//
//        sql('''
//DECLARE @Questionnaire2PatientQuestionnaire TABLE
//            (
//              q_id BIGINT NOT NULL PRIMARY KEY,
//              pq_id BIGINT NOT NULL
//            );
//
//-- questionnaire to patient_questionnaire mapping
//INSERT INTO @Questionnaire2PatientQuestionnaire(q_id, pq_id)
//        select distinct q.id as q_id, min(pq.id) as pq_id
//        from ${fullSchemaName}patient_questionnaire pq
//        inner join ${fullSchemaName}questionnaire q on pq.template_questionnaire_id = q.id
//        group by q.id;
//
//DELETE FROM ${fullSchemaName}patient_questionnaire_node
//where template_questionnaire_node_id not in (select distinct pq_id from @Questionnaire2PatientQuestionnaire);
//
//DELETE FROM ${fullSchemaName}patient_questionnaire
//where id not in (select distinct pq_id from @Questionnaire2PatientQuestionnaire);
//
//        ''')
//    }




    changeSet(author:"msu", id: "147120071345-5") {
        addColumn(tableName: "choice_value") {
            column(name: "ordering", type: '${integer.type}') {
                constraints(nullable: "true")
            }
        }

        sql(''' update ${fullSchemaName}choice_value set ordering = 1 ''' )

        addNotNullConstraint(columnDataType: '${integer.type}', columnName: "ordering", tableName: "choice_value")



    }
    changeSet(author:"msu", id: "147120071345-6") {
        addColumn(tableName: "patient_choice_value") {
            column(name: "ordering", type: '${integer.type}') {
                constraints(nullable: "true")
            }
        }
        sql(''' update ${fullSchemaName}patient_choice_value set ordering = 1 ''' )
        addNotNullConstraint(columnDataType: '${integer.type}', columnName: "ordering", tableName: "patient_choice_value")
    }

    // Add disable messaging to patient group
    changeSet(author: "emh", id: "add_disable_messaging_to_patient_group") {
        addColumn(tableName: "patient_group") {
            column(name: "disable_messaging", type: '${boolean.type}')
        }
        sql(''' update ${fullSchemaName}patient_group set disable_messaging = 'FALSE' ''' )
        addNotNullConstraint(columnDataType: '${boolean.type}', columnName: "disable_messaging", tableName: "patient_group")
    }

    // Add video conference
    changeSet(author: "of", id: "add_conference") {
        createTable(tableName: "conference") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "conference_PK")
            }

            column(name: "patient_id", type: '${id.type}') {
                constraints(nullable: "false")
            }
            column(name: "clinician_id", type: '${id.type}') {
                constraints(nullable: "false")
            }
            column(name: 'completed', type: '${boolean.type}')

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }
            column(name: "created_by", type: '${string.type}')
            column(name: "created_date", type: '${datetime.type}')
            column(name: "modified_by", type: '${string.type}')
            column(name: "modified_date", type: '${datetime.type}')
        }

        addForeignKeyConstraint(baseColumnNames: "patient_id", baseTableName: "conference",
                constraintName: "conf_patient_FK",
                referencedColumnNames: "id", referencedTableName: "patient", referencesUniqueColumn: "false")

        addForeignKeyConstraint(baseColumnNames: "clinician_id", baseTableName: "conference",
                constraintName: "conf_clinician_FK",
                referencedColumnNames: "id", referencedTableName: "clinician", referencesUniqueColumn: "false")

        createTable(tableName: "conference_measurement_draft") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "conference_measurement_draft_PK")
            }
            column(name: "class", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "included", type: '${boolean.type}') {
                constraints(nullable: "false")
            }
            column(name: "conference_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "weight", type: '${double.type}')
            column(name: "fev1", type: '${double.type}')
            column(name: "saturation", type: '${double.type}')
            column(name: "pulse", type: '${integer.type}')
            column(name: "systolic", type: '${integer.type}')
            column(name: "diastolic", type: '${integer.type}')

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }
            column(name: "created_by", type: '${string.type}')
            column(name: "created_date", type: '${datetime.type}')
            column(name: "modified_by", type: '${string.type}')
            column(name: "modified_date", type: '${datetime.type}')
        }

        addForeignKeyConstraint(baseColumnNames: "conference_id", baseTableName: "conference_measurement_draft",
                constraintName: "confmeasdraft_conf_FK",
                referencedColumnNames: "id", referencedTableName: "conference", referencesUniqueColumn: "false")

        addColumn(tableName: "measurement") {
            column(name: "conference_id", type: '${id.type}')
        }

        addForeignKeyConstraint(baseColumnNames: "conference_id", baseTableName: "measurement",
                constraintName: "meas_conf_FK",
                referencedColumnNames: "id", referencedTableName: "conference", referencesUniqueColumn: "false")
    }

    // Add questionnaire_group etc
    changeSet(author: "Søren", id: "147120071345-1") {
        createTable(tableName: "questionnaire_group") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "questionnaire_group_PK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string255.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string255.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "name", type: '${string255.type}') {
                constraints(nullable: "false", unique: "true")
            }
        }
    }
    changeSet(author: "Søren", id: "147120071345-2") {
        createTable(tableName: "questionnaire_group2questionnaire_header") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "questionnaire_group2questionnaire_header_PK")
            }

            column(name: "questionnaire_group_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "questionnaire_header_id", type: '${id.type}') {
                constraints(nullable: "false")
            }
            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }
            column(name: "created_by", type: '${string255.type}')
            column(name: "created_date", type: '${datetime.type}')
            column(name: "modified_by", type: '${string255.type}')
            column(name: "modified_date", type: '${datetime.type}')
            column(name: "STANDARD_SCHEDULE_TYPE", type: '${string.type}')
            column(name: "STANDARD_SCHEDULE_WEEKDAYS", type: '${string.type}')
            column(name: "STANDARD_SCHEDULE_TIMES_OF_DAY", type: '${string.type}')
            column(name: "STANDARD_SCHEDULE_DAYS_IN_MONTH", type: '${string.type}')
            column(name: "STANDARD_SCHEDULE_INTERVAL_IN_DAYS", type: '${integer.type}')
            column(name: "STANDARD_SCHEDULE_STARTING_DATE", type: '${datetime.type}')
            column(name: "STANDARD_SCHEDULE_SPECIFIC_DATE", type: '${datetime.type}')
            column(name: "STANDARD_SCHEDULE_REMINDER_START_MINUTES", type: '${integer.type}')
            column(name: "SPECIFIC_DATE", type: '${datetime.type}')
            column(name: "REMINDER_START_MINUTES", type: '${integer.type}')
        }
    }

    changeSet(author: "Søren", id: "147120071345-3") {
        addForeignKeyConstraint(baseColumnNames: "questionnaire_group_id", baseTableName: "questionnaire_group2questionnaire_header", constraintName: "questionnaire_groupFK", referencedColumnNames: "id", referencedTableName: "questionnaire_group", referencesUniqueColumn: "false")
    }

    changeSet(author: "Søren", id: "147120071345-4") {
        addForeignKeyConstraint(baseColumnNames: "questionnaire_header_id", baseTableName: "questionnaire_group2questionnaire_header", constraintName: "questionnaire_headerFK", referencedColumnNames: "id", referencedTableName: "questionnaire_header", referencesUniqueColumn: "false")
    }

    // Remove name from questionnaire + patient_questionnaires

    changeSet(author: "mss", id: "remove_name_from_questionnaire_and_patient_questionnaire") {
        dropColumn(columnName: "name", tableName: "questionnaire")
        dropColumn(columnName: "name", tableName: "patient_questionnaire")
    }

    // blue alarm check
    changeSet(author: "mss", id: "blue_alarm_check") {
        createTable(tableName: "blue_alarm_check") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "blue_alarm_checkPK")
            }
            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }
            column(name: "check_date", type: '${datetime.type}') {
                constraints(nullable: "false")
            }
            column(name: "created_by", type: '${string.type}')
            column(name: "created_date", type: '${datetime.type}')
            column(name: "modified_by", type: '${string.type}')
            column(name: "modified_date", type: '${datetime.type}')
        }
        createIndex(indexName: "check_date_idx", tableName: "blue_alarm_check") {
            column(name: "check_date")
        }
    }

    // Bad login attempts
    changeSet(author: "msu", id: "20130710_add_badLoginAttemps_to_user") {
        addColumn(tableName: "Users") {
            column(name: "bad_login_attemps", type: '${integer.type}')
        }

        sql(''' update ${fullSchemaName}users set bad_login_attemps = 0 ''' )
    }
}
