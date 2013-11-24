
-- Fix to KIH-942.. Making sure no data are inconsistent
update questionnaire_group2questionnaire_header
set standard_schedule_intro_period_weeks = 4
where standard_schedule_intro_period_weeks is null;

-- KIH-550
ALTER TABLE completed_questionnaire ADD show_acknowledgement_to_patient BIT;
