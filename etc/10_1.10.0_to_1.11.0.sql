
-- HRA
ALTER TABLE completed_questionnaire ADD received_date DATETIME
GO

UPDATE completed_questionnaire SET received_date = created_date
GO

ALTER TABLE completed_questionnaire ALTER COLUMN received_date DATETIME NOT NULL
GO


-- changeSet(author: "sbg (generated)", id: "1381833472169-1") {
ALTER TABLE questionnaire ADD STANDARD_SCHEDULE_BLUE_ALARM_TIME nvarchar(1024)
GO

ALTER TABLE questionnaire ADD STANDARD_SCHEDULE_REMINDER_TIME nvarchar(1024)
GO

ALTER TABLE questionnaire ADD STANDARD_SCHEDULE_WEEKDAYS_INTRO_PERIOD nvarchar(1024)
GO

ALTER TABLE questionnaire ADD STANDARD_SCHEDULE_WEEKDAYS_SECOND_PERIOD nvarchar(1024)
GO

UPDATE questionnaire SET STANDARD_SCHEDULE_INTERVAL_IN_DAYS = 2
where STANDARD_SCHEDULE_INTERVAL_IN_DAYS IS NULL
GO

ALTER TABLE questionnaire ALTER COLUMN STANDARD_SCHEDULE_INTERVAL_IN_DAYS INT NOT NULL
GO


-- Update in app-layer..???
-- changeSet(author: "sbg (generated)", id: "1381833472169-20") {
-- addNotNullConstraint(columnDataType: "timestamp", columnName: "STANDARD_SCHEDULE_STARTING_DATE", tableName: "QUESTIONNAIRE")
-- }

ALTER TABLE questionnaire ADD standard_schedule_intro_period_weeks INT
GO

UPDATE QUESTIONNAIRE SET standard_schedule_intro_period_weeks = 2
GO

ALTER TABLE questionnaire ALTER COLUMN standard_schedule_intro_period_weeks INT NOT NULL
GO


-- QUESTIONNAIRE_GROUP2QUESTIONNAIRE_HEADER
ALTER TABLE questionnaire_group2questionnaire_header ADD STANDARD_SCHEDULE_BLUE_ALARM_TIME nvarchar(1024)
GO

ALTER TABLE questionnaire_group2questionnaire_header ADD STANDARD_SCHEDULE_REMINDER_TIME nvarchar(1024)
GO

ALTER TABLE questionnaire_group2questionnaire_header ADD STANDARD_SCHEDULE_WEEKDAYS_INTRO_PERIOD nvarchar(1024)
GO

ALTER TABLE questionnaire_group2questionnaire_header ADD STANDARD_SCHEDULE_WEEKDAYS_SECOND_PERIOD nvarchar(1024)
GO

ALTER TABLE questionnaire_group2questionnaire_header ADD standard_schedule_intro_period_weeks nvarchar(1024)
GO

-- Fix to KIH-942.. Making sure no data are inconsistent
update questionnaire_group2questionnaire_header
set standard_schedule_intro_period_weeks = 4
where standard_schedule_intro_period_weeks is null
GO


-- QUESTIONNAIRE_SCHEDULE
ALTER TABLE questionnaire_schedule ADD BLUE_ALARM_TIME nvarchar(1024)
GO

ALTER TABLE questionnaire_schedule ADD REMINDER_TIME nvarchar(1024)
GO

ALTER TABLE questionnaire_schedule ADD WEEKDAYS_INTRO_PERIOD nvarchar(1024)
GO

ALTER TABLE questionnaire_schedule ADD WEEKDAYS_SECOND_PERIOD nvarchar(1024)
GO

ALTER TABLE questionnaire_schedule ADD intro_period_weeks INT NULL
GO

UPDATE questionnaire_schedule SET intro_period_weeks = 4
GO

ALTER TABLE questionnaire_schedule ALTER COLUMN intro_period_weeks INT NOT NULL
GO

ALTER TABLE QUESTIONNAIRE_SCHEDULE ALTER COLUMN DAYS_IN_MONTH nvarchar(1024) NULL
GO

ALTER TABLE QUESTIONNAIRE_SCHEDULE ALTER COLUMN TIMES_OF_DAY nvarchar(1024) NULL
GO

ALTER TABLE QUESTIONNAIRE_SCHEDULE ALTER COLUMN WEEKDAYS nvarchar(1024) NULL
GO


-- changeSet(author: 'of', id: 'add_mean_arterial_pressure_to_conference_measurement_draft') {
ALTER TABLE conference_measurement_draft ADD mean_arterial_pressure INT
GO

-- changeSet(author: 'of', id: 'add_device_id_to_conference_measurement_draft') {
ALTER TABLE conference_measurement_draft ADD device_id nvarchar(1024)
GO












