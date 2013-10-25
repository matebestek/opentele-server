-- STEP 6
DECLARE @Questionnaire2PatientQuestionnaire TABLE
            (
              q_id BIGINT NOT NULL PRIMARY KEY,
              pq_id BIGINT NOT NULL
            );

-- questionnaire to patient_questionnaire mapping
INSERT INTO @Questionnaire2PatientQuestionnaire(q_id, pq_id)
        select distinct q.id as q_id, min(pq.id) as pq_id
        from [dbo].patient_questionnaire pq
        inner join [dbo].questionnaire q on pq.template_questionnaire_id = q.id
        group by q.id;

-- TODO: Delete unused PQ's and PQN's

/*-- Check statements
select * from [dbo].patient_questionnaire_node pqn
where pqn.questionnaire_id not in 
(select distinct pq_id from @Questionnaire2PatientQuestionnaire);

select * from completed_questionnaire cq 
where cq.patient_questionnaire_id not in (select distinct pq_id from @Questionnaire2PatientQuestionnaire);

select * from node_result nr 
where nr.patient_questionnaire_node_id in (select id FROM [dbo].patient_questionnaire_node qn
where qn.questionnaire_id not in (select distinct pq_id from @Questionnaire2PatientQuestionnaire))
*/

DELETE FROM [dbo].patient_choice_value
where patient_input_node_id in (select id from [dbo].patient_questionnaire_node
where questionnaire_id not in (select distinct pq_id from @Questionnaire2PatientQuestionnaire));

-- Disable pq.start_node_id constraint
ALTER TABLE [dbo].patient_questionnaire NOCHECK CONSTRAINT [FK24149DE98D386AB];

DELETE FROM [dbo].patient_questionnaire_node
where questionnaire_id not in (select distinct pq_id from @Questionnaire2PatientQuestionnaire);

DELETE FROM [dbo].patient_questionnaire
where id not in (select distinct pq_id from @Questionnaire2PatientQuestionnaire);

-- Re-enable constraint
ALTER TABLE [dbo].patient_questionnaire CHECK CONSTRAINT [FK24149DE98D386AB];

-- Delete blue alarms:
delete from [dbo].patient_blue_alarm_questionnaireids;
