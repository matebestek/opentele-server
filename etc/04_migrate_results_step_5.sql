-- STEP 5..

    DECLARE @Questionnaire2PatientQuestionnaire TABLE
    (
      q_id BIGINT NOT NULL PRIMARY KEY,
      pq_id BIGINT NOT NULL
    );

    DECLARE @QuestionnaireNode2PatientQuestionnaireNode TABLE
    (
      qn_id BIGINT NOT NULL PRIMARY KEY,
      pqn_id BIGINT NOT NULL
    );

    DECLARE @OldPqn2NewPqn TABLE
    (
      old_pqn_id BIGINT NOT NULL PRIMARY KEY,
      new_pqn_id BIGINT NOT NULL
    );

    -- questionnaire to patient_questionnaire mapping
    INSERT INTO @Questionnaire2PatientQuestionnaire(q_id, pq_id)
    select distinct q.id as q_id, min(pq.id) as pq_id
    from [dbo].patient_questionnaire pq
    inner join [dbo].questionnaire q on pq.template_questionnaire_id = q.id
    group by q.id;


    -- questionnaire_node to patient_questionnaire_node mapping
    INSERT INTO @QuestionnaireNode2PatientQuestionnaireNode(qn_id, pqn_id)
    select distinct qn.id as qn_id, min(pqn.id) as pqn_id
    from [dbo].patient_questionnaire_node pqn
    inner join [dbo].questionnaire_node qn on pqn.template_questionnaire_node_id = qn.id
    group by qn.id;

    -- create old_pqn_id to new pqn_id mapping
    INSERT INTO @OldPqn2NewPqn(old_pqn_id, new_pqn_id)
    SELECT old_pqn.id as old_pqn_id, new_pq.pqn_id as new_pqn_id
    from @QuestionnaireNode2PatientQuestionnaireNode new_pq
    inner join [dbo].patient_questionnaire_node old_pqn on old_pqn.template_questionnaire_node_id = new_pq.qn_id
    ;

    -- Update node_results
    update nr
    set nr.patient_questionnaire_node_id = new_pqn_id
    from [dbo].node_result nr
    inner join @OldPqn2NewPqn map on nr.patient_questionnaire_node_id = map.old_pqn_id;
