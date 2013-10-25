-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog.groovy
-- Ran at: 10-07-13 08:55
-- Against: opentele153@jdbc:jtds:sqlserver://172.16.175.184:1433:opentele
-- Liquibase version: 2.0.5
-- *********************************************************************

/*
HRA: Husk at opdatere use clause nedenfor.. Evt. skal der laves search replace, så vi altid kører mod den rette database !
*/

-- Create Database Lock Table
CREATE TABLE [dbo].[DATABASECHANGELOGLOCK] ([ID] INT NOT NULL, [LOCKED] BIT NOT NULL, [LOCKGRANTED] DATETIME, [LOCKEDBY] VARCHAR(255), CONSTRAINT [PK_DATABASECHANGELOGLOCK] PRIMARY KEY ([ID]))
GO

INSERT INTO [dbo].[DATABASECHANGELOGLOCK] ([ID], [LOCKED]) VALUES (1, 0)
GO

-- Lock Database
-- Create Database Change Log Table
CREATE TABLE [dbo].[DATABASECHANGELOG] ([ID] VARCHAR(63) NOT NULL, [AUTHOR] VARCHAR(63) NOT NULL, [FILENAME] VARCHAR(200) NOT NULL, [DATEEXECUTED] DATETIME NOT NULL, [ORDEREXECUTED] INT NOT NULL, [EXECTYPE] VARCHAR(10) NOT NULL, [MD5SUM] VARCHAR(35), [DESCRIPTION] VARCHAR(255), [COMMENTS] VARCHAR(255), [TAG] VARCHAR(255), [LIQUIBASE] VARCHAR(20), CONSTRAINT [PK_DATABASECHANGELOG] PRIMARY KEY ([ID], [AUTHOR], [FILENAME]))
GO

-- Changeset 1_0_baseline.groovy::1361794626972-1::henrik (generated)::(Checksum: 3:9c2830542c3d7ab3152b9376d61a4ba6)
CREATE TABLE [dbo].[audit_log_controller_entity] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [action_name] NVARCHAR(1024) NOT NULL, [controller_name] NVARCHAR(1024) NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [number_of_calls] INT NOT NULL, CONSTRAINT [audit_log_conPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-1', '2.0.5', '3:9c2830542c3d7ab3152b9376d61a4ba6', 1)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-2::henrik (generated)::(Checksum: 3:e6612fd3425107065f74d039417912db)
CREATE TABLE [dbo].[audit_log_entry] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [action_id] BIGINT, [authority] NVARCHAR(1024), [calling_ip] NVARCHAR(1024), [correlation_id] NVARCHAR(1024), [created_by] NVARCHAR(1024), [created_date] datetime2(7), [duration] BIGINT, [end_date] datetime2(7), [end_time] BIGINT, [exception] bit NOT NULL, [exception_message] NVARCHAR(1024), [http_session_id] NVARCHAR(1024), [id_card] NVARCHAR(1024), [message_id] NVARCHAR(1024), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [operation] NVARCHAR(1024), [patient_cpr] NVARCHAR(1024), [request] NVARCHAR(1024), [response] NVARCHAR(1024), [result] NVARCHAR(1024), [service] NVARCHAR(1024), [start_date] datetime2(7), [start_time] BIGINT, [success] bit NOT NULL, CONSTRAINT [audit_log_entPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-2', '2.0.5', '3:e6612fd3425107065f74d039417912db', 2)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-3::henrik (generated)::(Checksum: 3:12a06c63d7630b71c903ccc7fe311551)
CREATE TABLE [dbo].[audit_log_parameter] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [entry_id] BIGINT, [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [parameter_key] NVARCHAR(1024), [parameter_value] NVARCHAR(1024), CONSTRAINT [audit_log_parPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-3', '2.0.5', '3:12a06c63d7630b71c903ccc7fe311551', 3)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-4::henrik (generated)::(Checksum: 3:e820bc01af47c2a1384779c536fe0929)
CREATE TABLE [dbo].[blood_pressure_threshold] ([id] BIGINT NOT NULL, [diastolic_alert_high] FLOAT NOT NULL, [diastolic_alert_low] FLOAT NOT NULL, [diastolic_warning_high] FLOAT NOT NULL, [diastolic_warning_low] FLOAT NOT NULL, [systolic_alert_high] FLOAT NOT NULL, [systolic_alert_low] FLOAT NOT NULL, [systolic_warning_high] FLOAT NOT NULL, [systolic_warning_low] FLOAT NOT NULL, CONSTRAINT [blood_pressurPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-4', '2.0.5', '3:e820bc01af47c2a1384779c536fe0929', 4)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-5::henrik (generated)::(Checksum: 3:6483579ebaf768f2e645df05ec4df0ef)
CREATE TABLE [dbo].[choice_value] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [input_node_id] BIGINT NOT NULL, [label] NVARCHAR(1024) NOT NULL, [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [value] NVARCHAR(1024) NOT NULL, CONSTRAINT [choice_valuePK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-5', '2.0.5', '3:6483579ebaf768f2e645df05ec4df0ef', 5)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-6::henrik (generated)::(Checksum: 3:34706d0daeb5ddb45587793e28afd27b)
CREATE TABLE [dbo].[clinician] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [email] NVARCHAR(1024), [first_name] NVARCHAR(1024) NOT NULL, [last_name] NVARCHAR(1024) NOT NULL, [mobile_phone] NVARCHAR(1024), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [phone] NVARCHAR(1024), [user_id] BIGINT, CONSTRAINT [clinicianPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-6', '2.0.5', '3:34706d0daeb5ddb45587793e28afd27b', 6)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-7::henrik (generated)::(Checksum: 3:887fa80cffea224e5dd9ef1fc24bad22)
CREATE TABLE [dbo].[clinician2patient_group] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [clinician_id] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [patient_group_id] BIGINT NOT NULL, CONSTRAINT [clinician2patPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-7', '2.0.5', '3:887fa80cffea224e5dd9ef1fc24bad22', 7)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-8::henrik (generated)::(Checksum: 3:2dae88a4118ef53c687cd5462ca86684)
CREATE TABLE [dbo].[clinician_question_preference] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [clinician_id] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [question_id] BIGINT NOT NULL, CONSTRAINT [clinician_quePK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-8', '2.0.5', '3:2dae88a4118ef53c687cd5462ca86684', 8)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-9::henrik (generated)::(Checksum: 3:43d85ad7402817e8db1ade0b983972ee)
CREATE TABLE [dbo].[completed_questionnaire] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [_questionnaire_ignored] bit NOT NULL, [acknowledged_by_id] BIGINT, [acknowledged_date] datetime2(7), [acknowledged_note] VARCHAR(max), [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [patient_id] BIGINT NOT NULL, [patient_questionnaire_id] BIGINT NOT NULL, [questionnaire_ignored_reason] NVARCHAR(1024), [questionnare_ignored_by_id] BIGINT, [severity] NVARCHAR(1024) NOT NULL, [upload_date] datetime2(7) NOT NULL, CONSTRAINT [completed_quePK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-9', '2.0.5', '3:43d85ad7402817e8db1ade0b983972ee', 9)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-10::henrik (generated)::(Checksum: 3:724be6e83a57582e7ca6099ba93498a7)
CREATE TABLE [dbo].[department] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [name] NVARCHAR(1024) NOT NULL, CONSTRAINT [departmentPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-10', '2.0.5', '3:724be6e83a57582e7ca6099ba93498a7', 10)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-11::henrik (generated)::(Checksum: 3:759ffcbadfb35e661e0e72bf8fa0788f)
CREATE TABLE [dbo].[measurement] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [diastolic] double precision, [end_time] datetime2(7), [fhr] VARCHAR(max), [measurement_node_result_id] BIGINT, [measurement_type_id] BIGINT NOT NULL, [meter_id] BIGINT, [mhr] VARCHAR(max), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [patient_id] BIGINT NOT NULL, [protein] NVARCHAR(1024), [qfhr] VARCHAR(max), [signals] VARCHAR(max), [start_time] datetime2(7), [systolic] double precision, [time] datetime2(7) NOT NULL, [toco] VARCHAR(max), [unit] NVARCHAR(1024) NOT NULL, [unread] bit NOT NULL, [value] double precision, [voltage_end] double precision, [voltage_start] double precision, CONSTRAINT [measurementPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-11', '2.0.5', '3:759ffcbadfb35e661e0e72bf8fa0788f', 11)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-12::henrik (generated)::(Checksum: 3:2eb3d09a8579cc1ad32e0bfada247a39)
CREATE TABLE [dbo].[measurement_type] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [name] VARCHAR(255) NOT NULL, CONSTRAINT [measurement_tPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-12', '2.0.5', '3:2eb3d09a8579cc1ad32e0bfada247a39', 12)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-13::henrik (generated)::(Checksum: 3:9b79d8c84d277a780311ab70ba0cec69)
CREATE TABLE [dbo].[message] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [department_id] BIGINT NOT NULL, [in_reply_to_id] BIGINT, [is_read] bit NOT NULL, [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [patient_id] BIGINT NOT NULL, [read_date] datetime2(7), [send_date] datetime2(7), [sent_by_patient] bit NOT NULL, [text] VARCHAR(2000) NOT NULL, [title] NVARCHAR(1024) NOT NULL, CONSTRAINT [messagePK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-13', '2.0.5', '3:9b79d8c84d277a780311ab70ba0cec69', 13)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-14::henrik (generated)::(Checksum: 3:dbec5043ae1290dda6db43c4c2589d04)
CREATE TABLE [dbo].[meter] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [active] bit NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [meter_id] NVARCHAR(1024) NOT NULL, [meter_type_id] BIGINT NOT NULL, [model] NVARCHAR(1024) NOT NULL, [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [monitor_kit_id] BIGINT, [patient_id] BIGINT, CONSTRAINT [meterPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-14', '2.0.5', '3:dbec5043ae1290dda6db43c4c2589d04', 14)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-15::henrik (generated)::(Checksum: 3:3a862858e4cb2fbef3b63f315d57fd04)
CREATE TABLE [dbo].[meter_type] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [name] NVARCHAR(1024) NOT NULL, CONSTRAINT [meter_typePK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-15', '2.0.5', '3:3a862858e4cb2fbef3b63f315d57fd04', 15)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-16::henrik (generated)::(Checksum: 3:c50cbf72b0163b8c2529e311e189d038)
CREATE TABLE [dbo].[monitor_kit] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [department_id] BIGINT NOT NULL, [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [name] NVARCHAR(1024) NOT NULL, [patient_id] BIGINT, CONSTRAINT [monitor_kitPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-16', '2.0.5', '3:c50cbf72b0163b8c2529e311e189d038', 16)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-17::henrik (generated)::(Checksum: 3:db813b81ed569d7a15c6e815b13f0a66)
CREATE TABLE [dbo].[monitoring_plan] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [patient_id] BIGINT NOT NULL, [start_date] datetime2(7) NOT NULL, CONSTRAINT [monitoring_plPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-17', '2.0.5', '3:db813b81ed569d7a15c6e815b13f0a66', 17)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-18::henrik (generated)::(Checksum: 3:2b023ad175c037b497bbc08b05a39010)
CREATE TABLE [dbo].[next_of_kin_person] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [address] NVARCHAR(1024), [city] NVARCHAR(1024), [created_by] NVARCHAR(1024), [created_date] datetime2(7), [first_name] NVARCHAR(1024), [last_name] NVARCHAR(1024), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [note] NVARCHAR(1024), [patient_id] BIGINT, [phone] NVARCHAR(1024), [relation] NVARCHAR(1024), CONSTRAINT [next_of_kin_pPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-18', '2.0.5', '3:2b023ad175c037b497bbc08b05a39010', 18)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-19::henrik (generated)::(Checksum: 3:028aa0ec1e3829f0e733feeae33fbbba)
CREATE TABLE [dbo].[node_result] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [acknowledged_by_id] BIGINT, [acknowledged_date] datetime2(7), [acknowledged_note] NVARCHAR(1024), [completed_questionnaire_id] BIGINT NOT NULL, [completion_time] datetime2(7) NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [node_ignored] bit NOT NULL, [node_ignored_by_id] BIGINT, [node_ignored_reason] NVARCHAR(1024), [note] NVARCHAR(1024), [patient_questionnaire_node_id] BIGINT NOT NULL, [severity] NVARCHAR(1024), [threshold_message] NVARCHAR(1024), [was_omitted] bit NOT NULL, [class] NVARCHAR(1024) NOT NULL, [result] varbinary(max), [measurement_type_id] BIGINT, [input] varbinary(max), CONSTRAINT [node_resultPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-19', '2.0.5', '3:028aa0ec1e3829f0e733feeae33fbbba', 19)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-20::henrik (generated)::(Checksum: 3:94e96fdc2401bb544bbe081a2e73af65)
CREATE TABLE [dbo].[numeric_threshold] ([id] BIGINT NOT NULL, [alert_high] FLOAT NOT NULL, [alert_low] FLOAT NOT NULL, [warning_high] FLOAT NOT NULL, [warning_low] FLOAT NOT NULL, CONSTRAINT [numeric_thresPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-20', '2.0.5', '3:94e96fdc2401bb544bbe081a2e73af65', 20)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-21::henrik (generated)::(Checksum: 3:3c577114089735b6b487d801a632c6f2)
CREATE TABLE [dbo].[patient] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [address] NVARCHAR(1024) NOT NULL, [city] NVARCHAR(1024) NOT NULL, [comment] VARCHAR(2048), [cpr] NVARCHAR(1024) NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [email] NVARCHAR(1024), [first_name] NVARCHAR(1024) NOT NULL, [last_name] NVARCHAR(1024) NOT NULL, [mobile_phone] NVARCHAR(1024), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [monitoring_plan_id] BIGINT, [phone] NVARCHAR(1024), [postal_code] NVARCHAR(1024) NOT NULL, [sex] NVARCHAR(1024) NOT NULL, [state] NVARCHAR(1024) NOT NULL, [user_id] BIGINT, CONSTRAINT [patientPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-21', '2.0.5', '3:3c577114089735b6b487d801a632c6f2', 21)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-22::henrik (generated)::(Checksum: 3:8f3f46105f8e990c2d35394084b83d7e)
CREATE TABLE [dbo].[patient2patient_group] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [patient_id] BIGINT NOT NULL, [patient_group_id] BIGINT NOT NULL, CONSTRAINT [patient2patiePK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-22', '2.0.5', '3:8f3f46105f8e990c2d35394084b83d7e', 22)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-23::henrik (generated)::(Checksum: 3:f7ccb8fdb92b23ccbf48ba10a36ac6ad)
CREATE TABLE [dbo].[patient_blue_alarm_questionnaireids] ([patient_id] BIGINT, [blue_alarm_questionnaireids_long] BIGINT)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-23', '2.0.5', '3:f7ccb8fdb92b23ccbf48ba10a36ac6ad', 23)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-24::henrik (generated)::(Checksum: 3:53fdc14023955a23dd8a01452af801ac)
CREATE TABLE [dbo].[patient_choice_value] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [label] NVARCHAR(1024) NOT NULL, [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [patient_input_node_id] BIGINT NOT NULL, [value] NVARCHAR(1024) NOT NULL, CONSTRAINT [patient_choicPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-24', '2.0.5', '3:53fdc14023955a23dd8a01452af801ac', 24)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-25::henrik (generated)::(Checksum: 3:cae0891f2a7f1708db2c7fddc405a326)
CREATE TABLE [dbo].[patient_group] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [department_id] BIGINT NOT NULL, [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [name] NVARCHAR(1024) NOT NULL, [standard_threshold_set_id] BIGINT NOT NULL, CONSTRAINT [patient_groupPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-25', '2.0.5', '3:cae0891f2a7f1708db2c7fddc405a326', 25)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-26::henrik (generated)::(Checksum: 3:0a6489e5e9e6e490dc5091892f27edd8)
CREATE TABLE [dbo].[patient_note] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [note] VARCHAR(max) NOT NULL, [patient_id] BIGINT NOT NULL, [remind_today] bit NOT NULL, [reminder_date] datetime2(7), [type] NVARCHAR(1024) NOT NULL, CONSTRAINT [patient_notePK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-26', '2.0.5', '3:0a6489e5e9e6e490dc5091892f27edd8', 26)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-27::henrik (generated)::(Checksum: 3:ed32404bb0d0bc29448ae6ab3c4f5829)
CREATE TABLE [dbo].[patient_note_clinician] ([patient_note_seen_by_id] BIGINT, [clinician_id] BIGINT)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-27', '2.0.5', '3:ed32404bb0d0bc29448ae6ab3c4f5829', 27)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-28::henrik (generated)::(Checksum: 3:09b88a584e5dfe5707d6a808ea367e9c)
CREATE TABLE [dbo].[patient_questionnaire] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [creation_date] datetime2(7) NOT NULL, [creator_id] BIGINT NOT NULL, [deleted] bit NOT NULL, [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [monitoring_plan_id] BIGINT, [name] NVARCHAR(1024) NOT NULL, [patient_id] BIGINT NOT NULL, [revision] NVARCHAR(1024) NOT NULL, [start_node_id] BIGINT, [template_questionnaire_id] BIGINT NOT NULL, CONSTRAINT [patient_questPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-28', '2.0.5', '3:09b88a584e5dfe5707d6a808ea367e9c', 28)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-29::henrik (generated)::(Checksum: 3:d193b612227f66926d1126cc046321a3)
CREATE TABLE [dbo].[patient_questionnaire_node] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [default_next_id] BIGINT, [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [questionnaire_id] BIGINT, [short_text] NVARCHAR(1024), [template_questionnaire_node_id] BIGINT, [class] NVARCHAR(1024) NOT NULL, [map_to_input_fields] bit, [meter_type_id] BIGINT, [monica_measuring_time_input_node_id] BIGINT, [monica_measuring_time_input_var] NVARCHAR(1024), [next_fail_id] BIGINT, [simulate] bit, [text] VARCHAR(max), [alternative_next_id] BIGINT, [alternative_severity] NVARCHAR(1024), [data_type] NVARCHAR(1024), [default_severity] NVARCHAR(1024), [input_node_id] BIGINT, [input_var] NVARCHAR(1024), [node_value] varbinary(max), [operation] NVARCHAR(1024), [input_type] NVARCHAR(1024), [value] bit, [variable_name] NVARCHAR(1024), CONSTRAINT [patient_quest_node_PK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-29', '2.0.5', '3:d193b612227f66926d1126cc046321a3', 29)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-30::henrik (generated)::(Checksum: 3:6c51276d21e02afdeb79f7f7407ec9e1)
CREATE TABLE [dbo].[patient_threshold] ([patient_thresholds_id] BIGINT, [threshold_id] BIGINT)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-30', '2.0.5', '3:6c51276d21e02afdeb79f7f7407ec9e1', 30)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-31::henrik (generated)::(Checksum: 3:951a91b72c84ae87ff1d9adc3a3433c1)
CREATE TABLE [dbo].[permission] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [permission] NVARCHAR(1024) NOT NULL, CONSTRAINT [permissionPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-31', '2.0.5', '3:951a91b72c84ae87ff1d9adc3a3433c1', 31)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-32::henrik (generated)::(Checksum: 3:5c6c675cf434f977ce5bcdb41fbdc90c)
CREATE TABLE [dbo].[questionnaire] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [creation_date] datetime2(7) NOT NULL, [creator_id] BIGINT, [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [name] NVARCHAR(1024) NOT NULL, [revision] NVARCHAR(1024) NOT NULL, [start_node_id] BIGINT, CONSTRAINT [questionnairePK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-32', '2.0.5', '3:5c6c675cf434f977ce5bcdb41fbdc90c', 32)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-33::henrik (generated)::(Checksum: 3:76bb828d46733a26ff67a5523d3b242b)
CREATE TABLE [dbo].[questionnaire2meter_type] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [meter_type_id] BIGINT NOT NULL, [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [questionnaire_id] BIGINT NOT NULL, CONSTRAINT [questionnaire2meterPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-33', '2.0.5', '3:76bb828d46733a26ff67a5523d3b242b', 33)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-34::henrik (generated)::(Checksum: 3:d5bb9b43a3dd3dded5c689eb1aa5c0c0)
CREATE TABLE [dbo].[questionnaire_node] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [default_next_id] BIGINT, [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [questionnaire_id] BIGINT, [short_text] NVARCHAR(1024), [class] NVARCHAR(1024) NOT NULL, [alternative_next_id] BIGINT, [alternative_severity] NVARCHAR(1024), [default_severity] NVARCHAR(1024), [input_type] NVARCHAR(1024), [text] VARCHAR(max), [data_type] NVARCHAR(1024), [input_node_id] BIGINT, [input_var] NVARCHAR(1024), [node_value] varbinary(max), [operation] NVARCHAR(1024), [value] bit, [variable_name] NVARCHAR(1024), [map_to_input_fields] bit, [meter_type_id] BIGINT, [monica_measuring_time_input_node_id] BIGINT, [monica_measuring_time_input_var] NVARCHAR(1024), [next_fail_id] BIGINT, [simulate] bit, CONSTRAINT [questionnaireNodePK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-34', '2.0.5', '3:d5bb9b43a3dd3dded5c689eb1aa5c0c0', 34)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-35::henrik (generated)::(Checksum: 3:a1e576122fb734c66ff691543270eff0)
CREATE TABLE [dbo].[questionnaire_schedule] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [day_interval] INT NOT NULL, [DAYS_IN_MONTH] NVARCHAR(1024) NOT NULL, [STARTING_DATE] datetime2(7) NOT NULL, [TIMES_OF_DAY] NVARCHAR(1024) NOT NULL, [WEEKDAYS] NVARCHAR(1024) NOT NULL, [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [monitoring_plan_id] BIGINT NOT NULL, [patient_questionnaire_id] BIGINT NOT NULL, [type] NVARCHAR(1024) NOT NULL, CONSTRAINT [questionnaireSchedulePK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-35', '2.0.5', '3:a1e576122fb734c66ff691543270eff0', 35)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-36::henrik (generated)::(Checksum: 3:e0d4d5d4ee108ac1814c2a7dc615df0a)
CREATE TABLE [dbo].[role] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [authority] NVARCHAR(1024) NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), CONSTRAINT [rolePK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-36', '2.0.5', '3:e0d4d5d4ee108ac1814c2a7dc615df0a', 36)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-37::henrik (generated)::(Checksum: 3:0e8415037093ff360751b0bd770b44d9)
CREATE TABLE [dbo].[role_permission] ([permission_id] BIGINT NOT NULL, [role_id] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-37', '2.0.5', '3:0e8415037093ff360751b0bd770b44d9', 37)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-38::henrik (generated)::(Checksum: 3:2524cb14eaaf9dfb92e184b0f45c0503)
CREATE TABLE [dbo].[standard_threshold_set] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), CONSTRAINT [standard_threPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-38', '2.0.5', '3:2524cb14eaaf9dfb92e184b0f45c0503', 38)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-39::henrik (generated)::(Checksum: 3:7592571ee1a93c18989c73827fc30e59)
CREATE TABLE [dbo].[standard_threshold_set_threshold] ([standard_threshold_set_thresholds_id] BIGINT, [threshold_id] BIGINT)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-39', '2.0.5', '3:7592571ee1a93c18989c73827fc30e59', 39)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-40::henrik (generated)::(Checksum: 3:53ebcc3a8e7a92511841f0ca984a7c1b)
CREATE TABLE [dbo].[threshold] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [type_id] BIGINT NOT NULL, CONSTRAINT [thresholdPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-40', '2.0.5', '3:53ebcc3a8e7a92511841f0ca984a7c1b', 40)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-41::henrik (generated)::(Checksum: 3:e774bfda222a95e2ee58dea074f728fd)
CREATE TABLE [dbo].[urine_threshold] ([id] BIGINT NOT NULL, [alert_high] NVARCHAR(1024) NOT NULL, [alert_low] NVARCHAR(1024) NOT NULL, [warning_high] NVARCHAR(1024) NOT NULL, [warning_low] NVARCHAR(1024) NOT NULL, CONSTRAINT [urine_threshoPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-41', '2.0.5', '3:e774bfda222a95e2ee58dea074f728fd', 41)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-42::henrik (generated)::(Checksum: 3:f8be845232ebe5d0579e862fd28dfa5f)
CREATE TABLE [dbo].[user_role] ([role_id] BIGINT NOT NULL, [user_id] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-42', '2.0.5', '3:f8be845232ebe5d0579e862fd28dfa5f', 42)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-43::henrik (generated)::(Checksum: 3:56e7bfe9d03de886798c036f20c00c85)
CREATE TABLE [dbo].[users] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [account_expired] bit NOT NULL, [account_locked] bit NOT NULL, [cleartext_password] NVARCHAR(1024), [created_by] NVARCHAR(1024), [created_date] datetime2(7), [enabled] bit NOT NULL, [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), [password] NVARCHAR(1024) NOT NULL, [password_expired] bit NOT NULL, [username] NVARCHAR(1024) NOT NULL, CONSTRAINT [usersPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Table', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-43', '2.0.5', '3:56e7bfe9d03de886798c036f20c00c85', 43)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-44::henrik (generated)::(Checksum: 3:682d32be34d500b807cf86f291c8f4e3)
ALTER TABLE [dbo].[role_permission] ADD CONSTRAINT [role_permissiPK] PRIMARY KEY ([permission_id], [role_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Primary Key', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-44', '2.0.5', '3:682d32be34d500b807cf86f291c8f4e3', 44)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-45::henrik (generated)::(Checksum: 3:47400d224c28391706fcc4d6e1a3c9ae)
ALTER TABLE [dbo].[user_role] ADD CONSTRAINT [user_rolePK] PRIMARY KEY ([role_id], [user_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Primary Key', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-45', '2.0.5', '3:47400d224c28391706fcc4d6e1a3c9ae', 45)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-46::henrik (generated)::(Checksum: 3:ec0d17c70b95f6793fd908e54b44a502)
ALTER TABLE [dbo].[audit_log_entry] ADD CONSTRAINT [FK40704C53F84F8915] FOREIGN KEY ([action_id]) REFERENCES [dbo].[audit_log_controller_entity] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-46', '2.0.5', '3:ec0d17c70b95f6793fd908e54b44a502', 46)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-47::henrik (generated)::(Checksum: 3:09c1c9a1b2189780af7cb4d8ff36925f)
ALTER TABLE [dbo].[audit_log_parameter] ADD CONSTRAINT [FK4D351F0AF5DB4508] FOREIGN KEY ([entry_id]) REFERENCES [dbo].[audit_log_entry] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-47', '2.0.5', '3:09c1c9a1b2189780af7cb4d8ff36925f', 47)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-48::henrik (generated)::(Checksum: 3:e0fa118d34d6d229fae5e71b2a8ff565)
ALTER TABLE [dbo].[choice_value] ADD CONSTRAINT [FKE637D55351F08944] FOREIGN KEY ([input_node_id]) REFERENCES [dbo].[questionnaire_node] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-48', '2.0.5', '3:e0fa118d34d6d229fae5e71b2a8ff565', 48)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-49::henrik (generated)::(Checksum: 3:8ecad98509f54c0c72b8008e95fa64cd)
ALTER TABLE [dbo].[clinician] ADD CONSTRAINT [FK9D8F786E1B21BFA] FOREIGN KEY ([user_id]) REFERENCES [dbo].[users] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-49', '2.0.5', '3:8ecad98509f54c0c72b8008e95fa64cd', 49)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-50::henrik (generated)::(Checksum: 3:7df1db0ad52bcda6950fdd4871482be5)
ALTER TABLE [dbo].[clinician2patient_group] ADD CONSTRAINT [FK7A2AADC14C9484DA] FOREIGN KEY ([clinician_id]) REFERENCES [dbo].[clinician] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-50', '2.0.5', '3:7df1db0ad52bcda6950fdd4871482be5', 50)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-51::henrik (generated)::(Checksum: 3:e40ff331488f5eb94af0b356db720cc3)
ALTER TABLE [dbo].[clinician2patient_group] ADD CONSTRAINT [FK7A2AADC18260AB6F] FOREIGN KEY ([patient_group_id]) REFERENCES [dbo].[patient_group] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-51', '2.0.5', '3:e40ff331488f5eb94af0b356db720cc3', 51)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-52::henrik (generated)::(Checksum: 3:57f163f8cfa070a16336f686dba1c15f)
ALTER TABLE [dbo].[clinician_question_preference] ADD CONSTRAINT [FK16BC0D834C9484DA] FOREIGN KEY ([clinician_id]) REFERENCES [dbo].[clinician] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-52', '2.0.5', '3:57f163f8cfa070a16336f686dba1c15f', 52)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-53::henrik (generated)::(Checksum: 3:7181322625186c55801de87a1965e914)
ALTER TABLE [dbo].[clinician_question_preference] ADD CONSTRAINT [FK16BC0D833219E8AE] FOREIGN KEY ([question_id]) REFERENCES [dbo].[questionnaire_node] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-53', '2.0.5', '3:7181322625186c55801de87a1965e914', 53)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-54::henrik (generated)::(Checksum: 3:e03537b15f638ddba8e55b6b787c928d)
ALTER TABLE [dbo].[completed_questionnaire] ADD CONSTRAINT [FK447176AF8FF1BA9A] FOREIGN KEY ([acknowledged_by_id]) REFERENCES [dbo].[clinician] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-54', '2.0.5', '3:e03537b15f638ddba8e55b6b787c928d', 54)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-55::henrik (generated)::(Checksum: 3:b19c243c978ef2f2f8156e444f1a8b4e)
ALTER TABLE [dbo].[completed_questionnaire] ADD CONSTRAINT [FK447176AF41C6E17A] FOREIGN KEY ([patient_id]) REFERENCES [dbo].[patient] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-55', '2.0.5', '3:b19c243c978ef2f2f8156e444f1a8b4e', 55)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-56::henrik (generated)::(Checksum: 3:d24e085c1b3c05ab748afb125d4f10d4)
ALTER TABLE [dbo].[completed_questionnaire] ADD CONSTRAINT [FK447176AFE09BD51F] FOREIGN KEY ([patient_questionnaire_id]) REFERENCES [dbo].[patient_questionnaire] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-56', '2.0.5', '3:d24e085c1b3c05ab748afb125d4f10d4', 56)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-57::henrik (generated)::(Checksum: 3:808492f730836414746cc2631758e280)
ALTER TABLE [dbo].[completed_questionnaire] ADD CONSTRAINT [FK447176AFA08DD7B1] FOREIGN KEY ([questionnare_ignored_by_id]) REFERENCES [dbo].[clinician] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-57', '2.0.5', '3:808492f730836414746cc2631758e280', 57)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-58::henrik (generated)::(Checksum: 3:335757055de2deb816a3596de11b92ca)
ALTER TABLE [dbo].[measurement] ADD CONSTRAINT [FK93F2DBBC167D75AE] FOREIGN KEY ([measurement_node_result_id]) REFERENCES [dbo].[node_result] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-58', '2.0.5', '3:335757055de2deb816a3596de11b92ca', 58)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-59::henrik (generated)::(Checksum: 3:c87f624451e7381f4454d229969f756a)
ALTER TABLE [dbo].[measurement] ADD CONSTRAINT [FK93F2DBBC7D05B1B3] FOREIGN KEY ([measurement_type_id]) REFERENCES [dbo].[measurement_type] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-59', '2.0.5', '3:c87f624451e7381f4454d229969f756a', 59)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-60::henrik (generated)::(Checksum: 3:fd50872229dc72eb30bec14d4c6a8817)
ALTER TABLE [dbo].[measurement] ADD CONSTRAINT [FK93F2DBBC2963DAFA] FOREIGN KEY ([meter_id]) REFERENCES [dbo].[meter] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-60', '2.0.5', '3:fd50872229dc72eb30bec14d4c6a8817', 60)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-61::henrik (generated)::(Checksum: 3:c13e4867738753d5597d42c800cbe9b4)
ALTER TABLE [dbo].[measurement] ADD CONSTRAINT [FK93F2DBBC41C6E17A] FOREIGN KEY ([patient_id]) REFERENCES [dbo].[patient] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-61', '2.0.5', '3:c13e4867738753d5597d42c800cbe9b4', 61)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-62::henrik (generated)::(Checksum: 3:c5531b94702d2889faa1de033f84c3af)
ALTER TABLE [dbo].[message] ADD CONSTRAINT [FK38EB0007BE2A029A] FOREIGN KEY ([department_id]) REFERENCES [dbo].[department] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-62', '2.0.5', '3:c5531b94702d2889faa1de033f84c3af', 62)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-63::henrik (generated)::(Checksum: 3:a4e0d929443f584c747f1eafdfdae713)
ALTER TABLE [dbo].[message] ADD CONSTRAINT [FK38EB000729309197] FOREIGN KEY ([in_reply_to_id]) REFERENCES [dbo].[message] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-63', '2.0.5', '3:a4e0d929443f584c747f1eafdfdae713', 63)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-64::henrik (generated)::(Checksum: 3:7ac423818b61560eda996834ac2ff22b)
ALTER TABLE [dbo].[message] ADD CONSTRAINT [FK38EB000741C6E17A] FOREIGN KEY ([patient_id]) REFERENCES [dbo].[patient] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-64', '2.0.5', '3:7ac423818b61560eda996834ac2ff22b', 64)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-65::henrik (generated)::(Checksum: 3:ef14032fb4178de648be0610ba686926)
ALTER TABLE [dbo].[meter] ADD CONSTRAINT [FK62FAB89CBF36DCD] FOREIGN KEY ([meter_type_id]) REFERENCES [dbo].[meter_type] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-65', '2.0.5', '3:ef14032fb4178de648be0610ba686926', 65)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-66::henrik (generated)::(Checksum: 3:373f5725bd2ce0076daaa49f6bf2d2a1)
ALTER TABLE [dbo].[meter] ADD CONSTRAINT [FK62FAB8952511EE5] FOREIGN KEY ([monitor_kit_id]) REFERENCES [dbo].[monitor_kit] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-66', '2.0.5', '3:373f5725bd2ce0076daaa49f6bf2d2a1', 66)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-67::henrik (generated)::(Checksum: 3:e0ccd4e044c9491a90652a8152ac37e5)
ALTER TABLE [dbo].[meter] ADD CONSTRAINT [FK62FAB8941C6E17A] FOREIGN KEY ([patient_id]) REFERENCES [dbo].[patient] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-67', '2.0.5', '3:e0ccd4e044c9491a90652a8152ac37e5', 67)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-68::henrik (generated)::(Checksum: 3:e6da049d4dc4251734a9b9a96abcebcb)
ALTER TABLE [dbo].[monitor_kit] ADD CONSTRAINT [FK5E2B4E71BE2A029A] FOREIGN KEY ([department_id]) REFERENCES [dbo].[department] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-68', '2.0.5', '3:e6da049d4dc4251734a9b9a96abcebcb', 68)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-69::henrik (generated)::(Checksum: 3:7029ea41fbf66c163c739eb4e1bd53a6)
ALTER TABLE [dbo].[monitor_kit] ADD CONSTRAINT [FK5E2B4E7141C6E17A] FOREIGN KEY ([patient_id]) REFERENCES [dbo].[patient] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-69', '2.0.5', '3:7029ea41fbf66c163c739eb4e1bd53a6', 69)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-70::henrik (generated)::(Checksum: 3:be797b91c4cb141766503a85735010fc)
ALTER TABLE [dbo].[monitoring_plan] ADD CONSTRAINT [FK4B2E0CC041C6E17A] FOREIGN KEY ([patient_id]) REFERENCES [dbo].[patient] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-70', '2.0.5', '3:be797b91c4cb141766503a85735010fc', 70)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-71::henrik (generated)::(Checksum: 3:70fb5d598ca84e9a7e4aad434b22d2f1)
ALTER TABLE [dbo].[next_of_kin_person] ADD CONSTRAINT [FK3F1C72C041C6E17A] FOREIGN KEY ([patient_id]) REFERENCES [dbo].[patient] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-71', '2.0.5', '3:70fb5d598ca84e9a7e4aad434b22d2f1', 71)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-72::henrik (generated)::(Checksum: 3:9915dab6d068efbd4f2f2f3cc7488966)
ALTER TABLE [dbo].[node_result] ADD CONSTRAINT [FK366CB0FA8FF1BA9A] FOREIGN KEY ([acknowledged_by_id]) REFERENCES [dbo].[clinician] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-72', '2.0.5', '3:9915dab6d068efbd4f2f2f3cc7488966', 72)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-73::henrik (generated)::(Checksum: 3:9c6232d43155c3478cb22a080363f7a6)
ALTER TABLE [dbo].[node_result] ADD CONSTRAINT [FK366CB0FA51B46113] FOREIGN KEY ([completed_questionnaire_id]) REFERENCES [dbo].[completed_questionnaire] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-73', '2.0.5', '3:9c6232d43155c3478cb22a080363f7a6', 73)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-74::henrik (generated)::(Checksum: 3:ac5993d31369e4215a99a1cd68ff57d3)
ALTER TABLE [dbo].[node_result] ADD CONSTRAINT [FK366CB0FA7D05B1B3] FOREIGN KEY ([measurement_type_id]) REFERENCES [dbo].[measurement_type] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-74', '2.0.5', '3:ac5993d31369e4215a99a1cd68ff57d3', 74)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-75::henrik (generated)::(Checksum: 3:b43156263fd401e7393f87d2847c914b)
ALTER TABLE [dbo].[node_result] ADD CONSTRAINT [FK366CB0FACA408B27] FOREIGN KEY ([node_ignored_by_id]) REFERENCES [dbo].[clinician] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-75', '2.0.5', '3:b43156263fd401e7393f87d2847c914b', 75)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-76::henrik (generated)::(Checksum: 3:c17b53134e675b30efbf28f293f05f71)
ALTER TABLE [dbo].[node_result] ADD CONSTRAINT [FK366CB0FABF8AE932] FOREIGN KEY ([patient_questionnaire_node_id]) REFERENCES [dbo].[patient_questionnaire_node] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-76', '2.0.5', '3:c17b53134e675b30efbf28f293f05f71', 76)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-77::henrik (generated)::(Checksum: 3:30bcf6547d2ba9607c9a0fafd8c26f4c)
ALTER TABLE [dbo].[patient] ADD CONSTRAINT [FKD0D3EB05385A86AB] FOREIGN KEY ([monitoring_plan_id]) REFERENCES [dbo].[monitoring_plan] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-77', '2.0.5', '3:30bcf6547d2ba9607c9a0fafd8c26f4c', 77)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-78::henrik (generated)::(Checksum: 3:031395ec25efa13ba3566a7d21b86f9c)
ALTER TABLE [dbo].[patient] ADD CONSTRAINT [FKD0D3EB051B21BFA] FOREIGN KEY ([user_id]) REFERENCES [dbo].[users] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-78', '2.0.5', '3:031395ec25efa13ba3566a7d21b86f9c', 78)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-79::henrik (generated)::(Checksum: 3:a95243e9684a55556a4cf63a68854afe)
ALTER TABLE [dbo].[patient2patient_group] ADD CONSTRAINT [FK5C2B4C188260AB6F] FOREIGN KEY ([patient_group_id]) REFERENCES [dbo].[patient_group] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-79', '2.0.5', '3:a95243e9684a55556a4cf63a68854afe', 79)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-80::henrik (generated)::(Checksum: 3:eb7a46576020674f226efcb21bf15a1c)
ALTER TABLE [dbo].[patient2patient_group] ADD CONSTRAINT [FK5C2B4C1841C6E17A] FOREIGN KEY ([patient_id]) REFERENCES [dbo].[patient] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-80', '2.0.5', '3:eb7a46576020674f226efcb21bf15a1c', 80)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-81::henrik (generated)::(Checksum: 3:d340fb36670afe1db64f7fc21a20e395)
ALTER TABLE [dbo].[patient_blue_alarm_questionnaireids] ADD CONSTRAINT [FK80C5BDAE41C6E17A] FOREIGN KEY ([patient_id]) REFERENCES [dbo].[patient] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-81', '2.0.5', '3:d340fb36670afe1db64f7fc21a20e395', 81)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-82::henrik (generated)::(Checksum: 3:031d620ff567303e48e0650e1631e4c7)
ALTER TABLE [dbo].[patient_choice_value] ADD CONSTRAINT [FKC84E244DA59D9C00] FOREIGN KEY ([patient_input_node_id]) REFERENCES [dbo].[patient_questionnaire_node] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-82', '2.0.5', '3:031d620ff567303e48e0650e1631e4c7', 82)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-83::henrik (generated)::(Checksum: 3:92d3591d383394bc358d1be982c6e010)
ALTER TABLE [dbo].[patient_group] ADD CONSTRAINT [FKAF742CC5BE2A029A] FOREIGN KEY ([department_id]) REFERENCES [dbo].[department] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-83', '2.0.5', '3:92d3591d383394bc358d1be982c6e010', 83)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-84::henrik (generated)::(Checksum: 3:634009ddbe701c005bb8b3be367176e6)
ALTER TABLE [dbo].[patient_group] ADD CONSTRAINT [FKAF742CC5373AD722] FOREIGN KEY ([standard_threshold_set_id]) REFERENCES [dbo].[standard_threshold_set] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-84', '2.0.5', '3:634009ddbe701c005bb8b3be367176e6', 84)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-85::henrik (generated)::(Checksum: 3:bd63339021015ab338d09b1cd2c60aa4)
ALTER TABLE [dbo].[patient_note] ADD CONSTRAINT [FKCBDD98EC41C6E17A] FOREIGN KEY ([patient_id]) REFERENCES [dbo].[patient] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-85', '2.0.5', '3:bd63339021015ab338d09b1cd2c60aa4', 85)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-86::henrik (generated)::(Checksum: 3:2d8c443408ce176e54b807f98ec06c25)
ALTER TABLE [dbo].[patient_note_clinician] ADD CONSTRAINT [FKAD64D4DB4C9484DA] FOREIGN KEY ([clinician_id]) REFERENCES [dbo].[clinician] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-86', '2.0.5', '3:2d8c443408ce176e54b807f98ec06c25', 86)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-87::henrik (generated)::(Checksum: 3:5bbc506f74386735d7a312b69e912b55)
ALTER TABLE [dbo].[patient_note_clinician] ADD CONSTRAINT [FKAD64D4DB9C1AEAE9] FOREIGN KEY ([patient_note_seen_by_id]) REFERENCES [dbo].[patient_note] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-87', '2.0.5', '3:5bbc506f74386735d7a312b69e912b55', 87)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-88::henrik (generated)::(Checksum: 3:bd1888493fa15c4e5f0aadd83e96dfd0)
ALTER TABLE [dbo].[patient_questionnaire] ADD CONSTRAINT [FK24149DE923FA7C5C] FOREIGN KEY ([creator_id]) REFERENCES [dbo].[clinician] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-88', '2.0.5', '3:bd1888493fa15c4e5f0aadd83e96dfd0', 88)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-89::henrik (generated)::(Checksum: 3:7056e904b6ebb65a394cbb7f24ca55b1)
ALTER TABLE [dbo].[patient_questionnaire] ADD CONSTRAINT [FK24149DE9385A86AB] FOREIGN KEY ([monitoring_plan_id]) REFERENCES [dbo].[monitoring_plan] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-89', '2.0.5', '3:7056e904b6ebb65a394cbb7f24ca55b1', 89)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-90::henrik (generated)::(Checksum: 3:64507da15d0e8487f334026d1d5004bb)
ALTER TABLE [dbo].[patient_questionnaire] ADD CONSTRAINT [FK24149DE941C6E17A] FOREIGN KEY ([patient_id]) REFERENCES [dbo].[patient] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-90', '2.0.5', '3:64507da15d0e8487f334026d1d5004bb', 90)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-91::henrik (generated)::(Checksum: 3:8efe71674509aeaf62ae253099dc2db0)
ALTER TABLE [dbo].[patient_questionnaire] ADD CONSTRAINT [FK24149DE98D386AB] FOREIGN KEY ([start_node_id]) REFERENCES [dbo].[patient_questionnaire_node] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-91', '2.0.5', '3:8efe71674509aeaf62ae253099dc2db0', 91)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-92::henrik (generated)::(Checksum: 3:ed04109248bb6689c76ecc25ddcdd326)
ALTER TABLE [dbo].[patient_questionnaire] ADD CONSTRAINT [FK24149DE92A7814D4] FOREIGN KEY ([template_questionnaire_id]) REFERENCES [dbo].[questionnaire] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-92', '2.0.5', '3:ed04109248bb6689c76ecc25ddcdd326', 92)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-93::henrik (generated)::(Checksum: 3:0445aeb373bb3abc8ec890d79237261e)
ALTER TABLE [dbo].[patient_questionnaire_node] ADD CONSTRAINT [FKC44D12987397A065] FOREIGN KEY ([alternative_next_id]) REFERENCES [dbo].[patient_questionnaire_node] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-93', '2.0.5', '3:0445aeb373bb3abc8ec890d79237261e', 93)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-94::henrik (generated)::(Checksum: 3:f53da98284c83e18e3f2f2d1b07441a1)
ALTER TABLE [dbo].[patient_questionnaire_node] ADD CONSTRAINT [FKC44D129868687AF9] FOREIGN KEY ([default_next_id]) REFERENCES [dbo].[patient_questionnaire_node] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-94', '2.0.5', '3:f53da98284c83e18e3f2f2d1b07441a1', 94)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-95::henrik (generated)::(Checksum: 3:67bfac57c873cb4aa5b3688605179b46)
ALTER TABLE [dbo].[patient_questionnaire_node] ADD CONSTRAINT [FKC44D12981FCE3213] FOREIGN KEY ([input_node_id]) REFERENCES [dbo].[patient_questionnaire_node] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-95', '2.0.5', '3:67bfac57c873cb4aa5b3688605179b46', 95)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-96::henrik (generated)::(Checksum: 3:d9809d6d02866b6d879321bbd3c607d3)
ALTER TABLE [dbo].[patient_questionnaire_node] ADD CONSTRAINT [FKC44D1298CBF36DCD] FOREIGN KEY ([meter_type_id]) REFERENCES [dbo].[meter_type] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-96', '2.0.5', '3:d9809d6d02866b6d879321bbd3c607d3', 96)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-97::henrik (generated)::(Checksum: 3:b227cd8fc2e09fd4816d470816ac3621)
ALTER TABLE [dbo].[patient_questionnaire_node] ADD CONSTRAINT [FKC44D12989F1CA09] FOREIGN KEY ([monica_measuring_time_input_node_id]) REFERENCES [dbo].[patient_questionnaire_node] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-97', '2.0.5', '3:b227cd8fc2e09fd4816d470816ac3621', 97)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-98::henrik (generated)::(Checksum: 3:ced7b3614a4d8876c0d1540d573351ab)
ALTER TABLE [dbo].[patient_questionnaire_node] ADD CONSTRAINT [FKC44D129844D1DA0] FOREIGN KEY ([next_fail_id]) REFERENCES [dbo].[patient_questionnaire_node] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-98', '2.0.5', '3:ced7b3614a4d8876c0d1540d573351ab', 98)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-99::henrik (generated)::(Checksum: 3:d1382526335c3a861c535cc3369718ff)
ALTER TABLE [dbo].[patient_questionnaire_node] ADD CONSTRAINT [FKC44D1298A0999325] FOREIGN KEY ([questionnaire_id]) REFERENCES [dbo].[patient_questionnaire] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-99', '2.0.5', '3:d1382526335c3a861c535cc3369718ff', 99)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-100::henrik (generated)::(Checksum: 3:6b035c76df6b269a9b26ae4742c4bef0)
ALTER TABLE [dbo].[patient_questionnaire_node] ADD CONSTRAINT [FKC44D12982DEB6871] FOREIGN KEY ([template_questionnaire_node_id]) REFERENCES [dbo].[questionnaire_node] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-100', '2.0.5', '3:6b035c76df6b269a9b26ae4742c4bef0', 100)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-101::henrik (generated)::(Checksum: 3:ca1917c745fe71c1befae8a08b5d513a)
ALTER TABLE [dbo].[patient_threshold] ADD CONSTRAINT [FK48C487B17BBFF47D] FOREIGN KEY ([patient_thresholds_id]) REFERENCES [dbo].[patient] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-101', '2.0.5', '3:ca1917c745fe71c1befae8a08b5d513a', 101)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-102::henrik (generated)::(Checksum: 3:a83a279fc5618b21f880319e0bb7e841)
ALTER TABLE [dbo].[patient_threshold] ADD CONSTRAINT [FK48C487B146BA7BA] FOREIGN KEY ([threshold_id]) REFERENCES [dbo].[threshold] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-102', '2.0.5', '3:a83a279fc5618b21f880319e0bb7e841', 102)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-103::henrik (generated)::(Checksum: 3:f5842f5345fb219dd45f8ad1890e16da)
ALTER TABLE [dbo].[questionnaire] ADD CONSTRAINT [FKC3610DA323FA7C5C] FOREIGN KEY ([creator_id]) REFERENCES [dbo].[clinician] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-103', '2.0.5', '3:f5842f5345fb219dd45f8ad1890e16da', 103)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-104::henrik (generated)::(Checksum: 3:61876951cd39d391ea5acb75bb3f8fe9)
ALTER TABLE [dbo].[questionnaire] ADD CONSTRAINT [FKC3610DA36F0DA35] FOREIGN KEY ([start_node_id]) REFERENCES [dbo].[questionnaire_node] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-104', '2.0.5', '3:61876951cd39d391ea5acb75bb3f8fe9', 104)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-105::henrik (generated)::(Checksum: 3:3976aa8bb07b545e3dc6d36ba34192ae)
ALTER TABLE [dbo].[questionnaire2meter_type] ADD CONSTRAINT [FK4148725FCBF36DCD] FOREIGN KEY ([meter_type_id]) REFERENCES [dbo].[meter_type] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-105', '2.0.5', '3:3976aa8bb07b545e3dc6d36ba34192ae', 105)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-106::henrik (generated)::(Checksum: 3:ff1630ef2927c01cade8100bcd854291)
ALTER TABLE [dbo].[questionnaire2meter_type] ADD CONSTRAINT [FK4148725FA3B23BAF] FOREIGN KEY ([questionnaire_id]) REFERENCES [dbo].[questionnaire] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-106', '2.0.5', '3:ff1630ef2927c01cade8100bcd854291', 106)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-107::henrik (generated)::(Checksum: 3:4c9ebdc1ed60fa13873027ab93da3b00)
ALTER TABLE [dbo].[questionnaire_node] ADD CONSTRAINT [FK7BD3671E71B4F3EF] FOREIGN KEY ([alternative_next_id]) REFERENCES [dbo].[questionnaire_node] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-107', '2.0.5', '3:4c9ebdc1ed60fa13873027ab93da3b00', 107)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-108::henrik (generated)::(Checksum: 3:2313cc39ecbee900974ff1a49ba03601)
ALTER TABLE [dbo].[questionnaire_node] ADD CONSTRAINT [FK7BD3671E6685CE83] FOREIGN KEY ([default_next_id]) REFERENCES [dbo].[questionnaire_node] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-108', '2.0.5', '3:2313cc39ecbee900974ff1a49ba03601', 108)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-109::henrik (generated)::(Checksum: 3:a19ee777520549a1132961924ca6b851)
ALTER TABLE [dbo].[questionnaire_node] ADD CONSTRAINT [FK7BD3671E1DEB859D] FOREIGN KEY ([input_node_id]) REFERENCES [dbo].[questionnaire_node] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-109', '2.0.5', '3:a19ee777520549a1132961924ca6b851', 109)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-110::henrik (generated)::(Checksum: 3:6e81f7758f85630c3a71d9d522ab9fc0)
ALTER TABLE [dbo].[questionnaire_node] ADD CONSTRAINT [FK7BD3671ECBF36DCD] FOREIGN KEY ([meter_type_id]) REFERENCES [dbo].[meter_type] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-110', '2.0.5', '3:6e81f7758f85630c3a71d9d522ab9fc0', 110)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-111::henrik (generated)::(Checksum: 3:95fcf818ae9c9cd7e51de3777fc8c744)
ALTER TABLE [dbo].[questionnaire_node] ADD CONSTRAINT [FK7BD3671E80F1D93] FOREIGN KEY ([monica_measuring_time_input_node_id]) REFERENCES [dbo].[questionnaire_node] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-111', '2.0.5', '3:95fcf818ae9c9cd7e51de3777fc8c744', 111)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-112::henrik (generated)::(Checksum: 3:4b4081d58b5d1073312e963110895472)
ALTER TABLE [dbo].[questionnaire_node] ADD CONSTRAINT [FK7BD3671E26A712A] FOREIGN KEY ([next_fail_id]) REFERENCES [dbo].[questionnaire_node] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-112', '2.0.5', '3:4b4081d58b5d1073312e963110895472', 112)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-113::henrik (generated)::(Checksum: 3:bc5c9ad7795a267b56e9c1da8172852b)
ALTER TABLE [dbo].[questionnaire_node] ADD CONSTRAINT [FK7BD3671EA3B23BAF] FOREIGN KEY ([questionnaire_id]) REFERENCES [dbo].[questionnaire] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-113', '2.0.5', '3:bc5c9ad7795a267b56e9c1da8172852b', 113)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-114::henrik (generated)::(Checksum: 3:18ea28e69047d328cef791c066b49c9d)
ALTER TABLE [dbo].[questionnaire_schedule] ADD CONSTRAINT [FK9B0C5DB3385A86AB] FOREIGN KEY ([monitoring_plan_id]) REFERENCES [dbo].[monitoring_plan] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-114', '2.0.5', '3:18ea28e69047d328cef791c066b49c9d', 114)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-115::henrik (generated)::(Checksum: 3:b6d793db5c4affbc3668b565a7a40bdd)
ALTER TABLE [dbo].[questionnaire_schedule] ADD CONSTRAINT [FK9B0C5DB3E09BD51F] FOREIGN KEY ([patient_questionnaire_id]) REFERENCES [dbo].[patient_questionnaire] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-115', '2.0.5', '3:b6d793db5c4affbc3668b565a7a40bdd', 115)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-116::henrik (generated)::(Checksum: 3:3caa6af131cbfda019a39430c3662a1b)
ALTER TABLE [dbo].[role_permission] ADD CONSTRAINT [FKBD40D538DE1B957A] FOREIGN KEY ([permission_id]) REFERENCES [dbo].[permission] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-116', '2.0.5', '3:3caa6af131cbfda019a39430c3662a1b', 116)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-117::henrik (generated)::(Checksum: 3:ebd4024f1f288063dbe589f505d56f92)
ALTER TABLE [dbo].[role_permission] ADD CONSTRAINT [FKBD40D5385C87581A] FOREIGN KEY ([role_id]) REFERENCES [dbo].[role] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-117', '2.0.5', '3:ebd4024f1f288063dbe589f505d56f92', 117)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-118::henrik (generated)::(Checksum: 3:92ab05d423490778b236ff1050bd89bd)
ALTER TABLE [dbo].[standard_threshold_set_threshold] ADD CONSTRAINT [FKEDFC24787F2FA653] FOREIGN KEY ([standard_threshold_set_thresholds_id]) REFERENCES [dbo].[standard_threshold_set] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-118', '2.0.5', '3:92ab05d423490778b236ff1050bd89bd', 118)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-119::henrik (generated)::(Checksum: 3:fe4a170b33f8560a322b39546d27c51a)
ALTER TABLE [dbo].[standard_threshold_set_threshold] ADD CONSTRAINT [FKEDFC247846BA7BA] FOREIGN KEY ([threshold_id]) REFERENCES [dbo].[threshold] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-119', '2.0.5', '3:fe4a170b33f8560a322b39546d27c51a', 119)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-120::henrik (generated)::(Checksum: 3:8dd216b4b0cbd344a4ca547f830d3ae0)
ALTER TABLE [dbo].[threshold] ADD CONSTRAINT [FKA3E1E46B14CAB36] FOREIGN KEY ([type_id]) REFERENCES [dbo].[measurement_type] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-120', '2.0.5', '3:8dd216b4b0cbd344a4ca547f830d3ae0', 120)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-121::henrik (generated)::(Checksum: 3:4cebb3207bb56de15c3511bf05d76aae)
ALTER TABLE [dbo].[user_role] ADD CONSTRAINT [FK143BF46A5C87581A] FOREIGN KEY ([role_id]) REFERENCES [dbo].[role] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-121', '2.0.5', '3:4cebb3207bb56de15c3511bf05d76aae', 121)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-122::henrik (generated)::(Checksum: 3:d96d4d5245e25890e84a30d57ec96b5c)
ALTER TABLE [dbo].[user_role] ADD CONSTRAINT [FK143BF46A1B21BFA] FOREIGN KEY ([user_id]) REFERENCES [dbo].[users] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-122', '2.0.5', '3:d96d4d5245e25890e84a30d57ec96b5c', 122)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-123::henrik (generated)::(Checksum: 3:b9a82296d02b48374f75f7615f02b8ec)
CREATE INDEX [FK40704C53F84F8915] ON [dbo].[audit_log_entry]([action_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-123', '2.0.5', '3:b9a82296d02b48374f75f7615f02b8ec', 123)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-124::henrik (generated)::(Checksum: 3:b3a1173db8b9feff7c6acc6f49104fe9)
CREATE INDEX [end_time_idx] ON [dbo].[audit_log_entry]([end_time])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-124', '2.0.5', '3:b3a1173db8b9feff7c6acc6f49104fe9', 124)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-125::henrik (generated)::(Checksum: 3:f0833a7c69ade053a4a2cd2923b4472c)
CREATE INDEX [patient_cpr_idx] ON [dbo].[audit_log_entry]([patient_cpr])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-125', '2.0.5', '3:f0833a7c69ade053a4a2cd2923b4472c', 125)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-126::henrik (generated)::(Checksum: 3:3bd6a8bcdbd1f5ef054c1dfc403094fe)
CREATE INDEX [start_time_idx] ON [dbo].[audit_log_entry]([start_time])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-126', '2.0.5', '3:3bd6a8bcdbd1f5ef054c1dfc403094fe', 126)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-127::henrik (generated)::(Checksum: 3:7e286bd7a177e181293acc52c481dfac)
CREATE INDEX [FK4D351F0AF5DB4508] ON [dbo].[audit_log_parameter]([entry_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-127', '2.0.5', '3:7e286bd7a177e181293acc52c481dfac', 127)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-128::henrik (generated)::(Checksum: 3:848d586be8d28c3d75b43d26e229627c)
CREATE INDEX [FKE637D55351F08944] ON [dbo].[choice_value]([input_node_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-128', '2.0.5', '3:848d586be8d28c3d75b43d26e229627c', 128)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-129::henrik (generated)::(Checksum: 3:dd8099e4f49d8e643bbda5ee4bd87a7b)
CREATE INDEX [FK9D8F786E1B21BFA] ON [dbo].[clinician]([user_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-129', '2.0.5', '3:dd8099e4f49d8e643bbda5ee4bd87a7b', 129)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-130::henrik (generated)::(Checksum: 3:1b33e42f460e6b413e1a251e7232cf0d)
CREATE INDEX [FK7A2AADC14C9484DA] ON [dbo].[clinician2patient_group]([clinician_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-130', '2.0.5', '3:1b33e42f460e6b413e1a251e7232cf0d', 130)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-131::henrik (generated)::(Checksum: 3:92ced4dc77ff48dd2d81d2ac54d668fa)
CREATE INDEX [FK7A2AADC18260AB6F] ON [dbo].[clinician2patient_group]([patient_group_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-131', '2.0.5', '3:92ced4dc77ff48dd2d81d2ac54d668fa', 131)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-132::henrik (generated)::(Checksum: 3:1a062ac2868412e556d950e6696dc838)
CREATE INDEX [unique_clinician_id] ON [dbo].[clinician2patient_group]([patient_group_id], [clinician_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-132', '2.0.5', '3:1a062ac2868412e556d950e6696dc838', 132)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-133::henrik (generated)::(Checksum: 3:fd5f9b8527ea9345cf997387ce64d918)
CREATE INDEX [FK16BC0D833219E8AE] ON [dbo].[clinician_question_preference]([question_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-133', '2.0.5', '3:fd5f9b8527ea9345cf997387ce64d918', 133)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-134::henrik (generated)::(Checksum: 3:8521e069f67aead18124ad83846704e0)
CREATE INDEX [FK16BC0D834C9484DA] ON [dbo].[clinician_question_preference]([clinician_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-134', '2.0.5', '3:8521e069f67aead18124ad83846704e0', 134)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-135::henrik (generated)::(Checksum: 3:8a063b572f321cba088638b8fab7f5ed)
CREATE INDEX [FK447176AF41C6E17A] ON [dbo].[completed_questionnaire]([patient_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-135', '2.0.5', '3:8a063b572f321cba088638b8fab7f5ed', 135)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-136::henrik (generated)::(Checksum: 3:0ec95508bcff6155cf2e62a6f755b3de)
CREATE INDEX [FK447176AF8FF1BA9A] ON [dbo].[completed_questionnaire]([acknowledged_by_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-136', '2.0.5', '3:0ec95508bcff6155cf2e62a6f755b3de', 136)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-137::henrik (generated)::(Checksum: 3:9541112b3044494f5789823008c3716b)
CREATE INDEX [FK447176AFA08DD7B1] ON [dbo].[completed_questionnaire]([questionnare_ignored_by_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-137', '2.0.5', '3:9541112b3044494f5789823008c3716b', 137)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-138::henrik (generated)::(Checksum: 3:41d774bd1213aba382956b84af60ea97)
CREATE INDEX [FK447176AFE09BD51F] ON [dbo].[completed_questionnaire]([patient_questionnaire_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-138', '2.0.5', '3:41d774bd1213aba382956b84af60ea97', 138)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-139::henrik (generated)::(Checksum: 3:b6427bd21183a40ff245f4c2e0897286)
CREATE INDEX [FK93F2DBBC167D75AE] ON [dbo].[measurement]([measurement_node_result_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-139', '2.0.5', '3:b6427bd21183a40ff245f4c2e0897286', 139)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-140::henrik (generated)::(Checksum: 3:52e0f915f1015848fa60f09b15836928)
CREATE INDEX [FK93F2DBBC2963DAFA] ON [dbo].[measurement]([meter_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-140', '2.0.5', '3:52e0f915f1015848fa60f09b15836928', 140)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-141::henrik (generated)::(Checksum: 3:80ae57771d2c63f1f80decfed3ffa407)
CREATE INDEX [FK93F2DBBC41C6E17A] ON [dbo].[measurement]([patient_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-141', '2.0.5', '3:80ae57771d2c63f1f80decfed3ffa407', 141)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-142::henrik (generated)::(Checksum: 3:cb7be526d01bb4e63762abd26678bc0e)
CREATE INDEX [FK93F2DBBC7D05B1B3] ON [dbo].[measurement]([measurement_type_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-142', '2.0.5', '3:cb7be526d01bb4e63762abd26678bc0e', 142)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-143::henrik (generated)::(Checksum: 3:09197c352389062bc5ed983cbf549b04)
CREATE INDEX [name_idx] ON [dbo].[measurement_type]([name])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-143', '2.0.5', '3:09197c352389062bc5ed983cbf549b04', 143)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-144::henrik (generated)::(Checksum: 3:a0af5ad9567a6b285290ded06a3b8b74)
CREATE INDEX [FK38EB000729309197] ON [dbo].[message]([in_reply_to_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-144', '2.0.5', '3:a0af5ad9567a6b285290ded06a3b8b74', 144)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-145::henrik (generated)::(Checksum: 3:133192d900c019e08866620a4dd7ae4e)
CREATE INDEX [FK38EB000741C6E17A] ON [dbo].[message]([patient_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-145', '2.0.5', '3:133192d900c019e08866620a4dd7ae4e', 145)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-146::henrik (generated)::(Checksum: 3:eb0f5c51b9bad3fa86c022b8b3c368ed)
CREATE INDEX [FK38EB0007BE2A029A] ON [dbo].[message]([department_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-146', '2.0.5', '3:eb0f5c51b9bad3fa86c022b8b3c368ed', 146)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-147::henrik (generated)::(Checksum: 3:fb21ce3399dcbcbd5539c322a2ab92fd)
CREATE INDEX [FK62FAB8941C6E17A] ON [dbo].[meter]([patient_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-147', '2.0.5', '3:fb21ce3399dcbcbd5539c322a2ab92fd', 147)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-148::henrik (generated)::(Checksum: 3:fc69cb9f845621c1a9b1d23bfae93c88)
CREATE INDEX [FK62FAB8952511EE5] ON [dbo].[meter]([monitor_kit_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-148', '2.0.5', '3:fc69cb9f845621c1a9b1d23bfae93c88', 148)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-149::henrik (generated)::(Checksum: 3:f1baa9c085d2b451a6f1c8eb3ff9d300)
CREATE INDEX [FK62FAB89CBF36DCD] ON [dbo].[meter]([meter_type_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-149', '2.0.5', '3:f1baa9c085d2b451a6f1c8eb3ff9d300', 149)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-150::henrik (generated)::(Checksum: 3:ee076a961c1b3735cb0f90d333631ef1)
CREATE INDEX [FK5E2B4E7141C6E17A] ON [dbo].[monitor_kit]([patient_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-150', '2.0.5', '3:ee076a961c1b3735cb0f90d333631ef1', 150)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-151::henrik (generated)::(Checksum: 3:f3973357562e91a80f520923286cb001)
CREATE INDEX [FK5E2B4E71BE2A029A] ON [dbo].[monitor_kit]([department_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-151', '2.0.5', '3:f3973357562e91a80f520923286cb001', 151)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-152::henrik (generated)::(Checksum: 3:403610915dafbb528c26794ffd83a003)
CREATE INDEX [monkit_deptid_name_unique] ON [dbo].[monitor_kit]([department_id], [name])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-152', '2.0.5', '3:403610915dafbb528c26794ffd83a003', 152)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-153::henrik (generated)::(Checksum: 3:8f53cf08c1307184331928331b34bf30)
CREATE INDEX [FK4B2E0CC041C6E17A] ON [dbo].[monitoring_plan]([patient_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-153', '2.0.5', '3:8f53cf08c1307184331928331b34bf30', 153)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-154::henrik (generated)::(Checksum: 3:3e20997a25bbd71bc6f8157579f400fe)
CREATE INDEX [FK3F1C72C041C6E17A] ON [dbo].[next_of_kin_person]([patient_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-154', '2.0.5', '3:3e20997a25bbd71bc6f8157579f400fe', 154)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-155::henrik (generated)::(Checksum: 3:fdd82e3a5ac6befa360277b60a6e5ed8)
CREATE INDEX [FK366CB0FA51B46113] ON [dbo].[node_result]([completed_questionnaire_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-155', '2.0.5', '3:fdd82e3a5ac6befa360277b60a6e5ed8', 155)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-156::henrik (generated)::(Checksum: 3:6c2a57bbeac1b0620b19880b1377c796)
CREATE INDEX [FK366CB0FA7D05B1B3] ON [dbo].[node_result]([measurement_type_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-156', '2.0.5', '3:6c2a57bbeac1b0620b19880b1377c796', 156)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-157::henrik (generated)::(Checksum: 3:488f4d3da3276def183c88b1571ab91d)
CREATE INDEX [FK366CB0FA8FF1BA9A] ON [dbo].[node_result]([acknowledged_by_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-157', '2.0.5', '3:488f4d3da3276def183c88b1571ab91d', 157)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-158::henrik (generated)::(Checksum: 3:3f0097299f9635a929c5d049d4b43ae1)
CREATE INDEX [FK366CB0FABF8AE932] ON [dbo].[node_result]([patient_questionnaire_node_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-158', '2.0.5', '3:3f0097299f9635a929c5d049d4b43ae1', 158)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-159::henrik (generated)::(Checksum: 3:9c4d8439c60202521d0a991c5915186f)
CREATE INDEX [FK366CB0FACA408B27] ON [dbo].[node_result]([node_ignored_by_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-159', '2.0.5', '3:9c4d8439c60202521d0a991c5915186f', 159)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-160::henrik (generated)::(Checksum: 3:df25e9473b3cd331d0129c16a341db09)
CREATE INDEX [FKD0D3EB051B21BFA] ON [dbo].[patient]([user_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-160', '2.0.5', '3:df25e9473b3cd331d0129c16a341db09', 160)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-161::henrik (generated)::(Checksum: 3:d50ace9fc0a34c4c5c5de79099798349)
CREATE INDEX [FKD0D3EB05385A86AB] ON [dbo].[patient]([monitoring_plan_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-161', '2.0.5', '3:d50ace9fc0a34c4c5c5de79099798349', 161)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-162::henrik (generated)::(Checksum: 3:61c9c8eb66456fd05f147e8eb17055e5)
CREATE INDEX [FK5C2B4C1841C6E17A] ON [dbo].[patient2patient_group]([patient_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-162', '2.0.5', '3:61c9c8eb66456fd05f147e8eb17055e5', 162)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-163::henrik (generated)::(Checksum: 3:ce6278b7a3e5d85cc92ab59efccd08b6)
CREATE INDEX [FK5C2B4C188260AB6F] ON [dbo].[patient2patient_group]([patient_group_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-163', '2.0.5', '3:ce6278b7a3e5d85cc92ab59efccd08b6', 163)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-164::henrik (generated)::(Checksum: 3:67bff13607fb7f1b959e7060e458ed9d)
CREATE INDEX [unique_patient_id] ON [dbo].[patient2patient_group]([patient_group_id], [patient_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-164', '2.0.5', '3:67bff13607fb7f1b959e7060e458ed9d', 164)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-165::henrik (generated)::(Checksum: 3:da1636227cfe5ae0ebb072fef07ea110)
CREATE INDEX [FK80C5BDAE41C6E17A] ON [dbo].[patient_blue_alarm_questionnaireids]([patient_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-165', '2.0.5', '3:da1636227cfe5ae0ebb072fef07ea110', 165)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-166::henrik (generated)::(Checksum: 3:8c3cfedd415c9ac0b8e39b0a8fc8ae21)
CREATE INDEX [FKC84E244DA59D9C00] ON [dbo].[patient_choice_value]([patient_input_node_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-166', '2.0.5', '3:8c3cfedd415c9ac0b8e39b0a8fc8ae21', 166)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-167::henrik (generated)::(Checksum: 3:1eb13320c8c7520debace19fa89ff824)
CREATE INDEX [FKAF742CC5373AD722] ON [dbo].[patient_group]([standard_threshold_set_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-167', '2.0.5', '3:1eb13320c8c7520debace19fa89ff824', 167)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-168::henrik (generated)::(Checksum: 3:f7333e121bc5b592496f7cfdb116307d)
CREATE INDEX [FKAF742CC5BE2A029A] ON [dbo].[patient_group]([department_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-168', '2.0.5', '3:f7333e121bc5b592496f7cfdb116307d', 168)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-169::henrik (generated)::(Checksum: 3:fb733426f69f85c8fd538dd9341abb67)
CREATE UNIQUE INDEX [standard_threshold_set_id_unique_1361794626886] ON [dbo].[patient_group]([standard_threshold_set_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-169', '2.0.5', '3:fb733426f69f85c8fd538dd9341abb67', 169)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-170::henrik (generated)::(Checksum: 3:cac2d38637d0f7ef79aa636ebd8677ac)
CREATE INDEX [ptgroup_deptid_name_unique] ON [dbo].[patient_group]([department_id], [name])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-170', '2.0.5', '3:cac2d38637d0f7ef79aa636ebd8677ac', 170)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-171::henrik (generated)::(Checksum: 3:d590dec0bab2f13a5c7a3b8fd37d861d)
CREATE INDEX [FKCBDD98EC41C6E17A] ON [dbo].[patient_note]([patient_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-171', '2.0.5', '3:d590dec0bab2f13a5c7a3b8fd37d861d', 171)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-172::henrik (generated)::(Checksum: 3:4e1e251de5741c605f9a43e9020c53ef)
CREATE INDEX [FKAD64D4DB4C9484DA] ON [dbo].[patient_note_clinician]([clinician_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-172', '2.0.5', '3:4e1e251de5741c605f9a43e9020c53ef', 172)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-173::henrik (generated)::(Checksum: 3:db0c0525a8807c09ad5aca5aa1bee01b)
CREATE INDEX [FKAD64D4DB9C1AEAE9] ON [dbo].[patient_note_clinician]([patient_note_seen_by_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-173', '2.0.5', '3:db0c0525a8807c09ad5aca5aa1bee01b', 173)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-174::henrik (generated)::(Checksum: 3:1d21878a7ccd41847c080974f008f03d)
CREATE INDEX [FK24149DE923FA7C5C] ON [dbo].[patient_questionnaire]([creator_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-174', '2.0.5', '3:1d21878a7ccd41847c080974f008f03d', 174)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-175::henrik (generated)::(Checksum: 3:6da3e9576a0895524f3287a515ad5848)
CREATE INDEX [FK24149DE92A7814D4] ON [dbo].[patient_questionnaire]([template_questionnaire_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-175', '2.0.5', '3:6da3e9576a0895524f3287a515ad5848', 175)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-176::henrik (generated)::(Checksum: 3:d98604f387f0dc18294d6ffee1cb7d90)
CREATE INDEX [FK24149DE9385A86AB] ON [dbo].[patient_questionnaire]([monitoring_plan_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-176', '2.0.5', '3:d98604f387f0dc18294d6ffee1cb7d90', 176)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-177::henrik (generated)::(Checksum: 3:bc98a218d3f9a2587fdc0ac931ffb027)
CREATE INDEX [FK24149DE941C6E17A] ON [dbo].[patient_questionnaire]([patient_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-177', '2.0.5', '3:bc98a218d3f9a2587fdc0ac931ffb027', 177)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-178::henrik (generated)::(Checksum: 3:7c86a528b08f34c9fe4d21054d1e5a6d)
CREATE INDEX [FK24149DE98D386AB] ON [dbo].[patient_questionnaire]([start_node_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-178', '2.0.5', '3:7c86a528b08f34c9fe4d21054d1e5a6d', 178)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-179::henrik (generated)::(Checksum: 3:39bee02c799d99e023f0b413a2d66f9f)
CREATE INDEX [unique_revision] ON [dbo].[patient_questionnaire]([name], [patient_id], [revision])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-179', '2.0.5', '3:39bee02c799d99e023f0b413a2d66f9f', 179)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-180::henrik (generated)::(Checksum: 3:01825f07ec62d733270388e0965c80a0)
CREATE INDEX [FKC44D12981FCE3213] ON [dbo].[patient_questionnaire_node]([input_node_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-180', '2.0.5', '3:01825f07ec62d733270388e0965c80a0', 180)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-181::henrik (generated)::(Checksum: 3:a372da0bafc8c0006b8a13d15d011621)
CREATE INDEX [FKC44D12982DEB6871] ON [dbo].[patient_questionnaire_node]([template_questionnaire_node_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-181', '2.0.5', '3:a372da0bafc8c0006b8a13d15d011621', 181)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-182::henrik (generated)::(Checksum: 3:5eaa1210c6c7b997d2e63143ebce3e11)
CREATE INDEX [FKC44D129844D1DA0] ON [dbo].[patient_questionnaire_node]([next_fail_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-182', '2.0.5', '3:5eaa1210c6c7b997d2e63143ebce3e11', 182)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-183::henrik (generated)::(Checksum: 3:a9615c7e8f801457cc76cf628714b441)
CREATE INDEX [FKC44D129868687AF9] ON [dbo].[patient_questionnaire_node]([default_next_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-183', '2.0.5', '3:a9615c7e8f801457cc76cf628714b441', 183)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-184::henrik (generated)::(Checksum: 3:599374f272c10068cb86fa751e3ac62e)
CREATE INDEX [FKC44D12987397A065] ON [dbo].[patient_questionnaire_node]([alternative_next_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-184', '2.0.5', '3:599374f272c10068cb86fa751e3ac62e', 184)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-185::henrik (generated)::(Checksum: 3:575f790804bcf5b68e78e83b4c8329c6)
CREATE INDEX [FKC44D12989F1CA09] ON [dbo].[patient_questionnaire_node]([monica_measuring_time_input_node_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-185', '2.0.5', '3:575f790804bcf5b68e78e83b4c8329c6', 185)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-186::henrik (generated)::(Checksum: 3:ad79db4bed3d49d154f14eb6af75dd7f)
CREATE INDEX [FKC44D1298A0999325] ON [dbo].[patient_questionnaire_node]([questionnaire_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-186', '2.0.5', '3:ad79db4bed3d49d154f14eb6af75dd7f', 186)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-187::henrik (generated)::(Checksum: 3:c7238d475d71aa69623e6efa261a36b1)
CREATE INDEX [FKC44D1298CBF36DCD] ON [dbo].[patient_questionnaire_node]([meter_type_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-187', '2.0.5', '3:c7238d475d71aa69623e6efa261a36b1', 187)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-188::henrik (generated)::(Checksum: 3:5bbb91f2ea3e4815984d12ca1b650941)
CREATE INDEX [FK48C487B146BA7BA] ON [dbo].[patient_threshold]([threshold_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-188', '2.0.5', '3:5bbb91f2ea3e4815984d12ca1b650941', 188)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-189::henrik (generated)::(Checksum: 3:a08136cbf4e81ff556d80eac104f761c)
CREATE INDEX [FK48C487B17BBFF47D] ON [dbo].[patient_threshold]([patient_thresholds_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-189', '2.0.5', '3:a08136cbf4e81ff556d80eac104f761c', 189)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-190::henrik (generated)::(Checksum: 3:4f6f77c23c9bd947e54492e86f835701)
CREATE INDEX [FKC3610DA323FA7C5C] ON [dbo].[questionnaire]([creator_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-190', '2.0.5', '3:4f6f77c23c9bd947e54492e86f835701', 190)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-191::henrik (generated)::(Checksum: 3:92b516123ba8fa218a341e5eb2bb0496)
CREATE INDEX [FKC3610DA36F0DA35] ON [dbo].[questionnaire]([start_node_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-191', '2.0.5', '3:92b516123ba8fa218a341e5eb2bb0496', 191)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-192::henrik (generated)::(Checksum: 3:dbdcd66687a4a34e851a73ac0e8cc249)
CREATE INDEX [FK4148725FA3B23BAF] ON [dbo].[questionnaire2meter_type]([questionnaire_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-192', '2.0.5', '3:dbdcd66687a4a34e851a73ac0e8cc249', 192)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-193::henrik (generated)::(Checksum: 3:7bbc2841a93dd350816f9287d6d7919b)
CREATE INDEX [FK4148725FCBF36DCD] ON [dbo].[questionnaire2meter_type]([meter_type_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-193', '2.0.5', '3:7bbc2841a93dd350816f9287d6d7919b', 193)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-194::henrik (generated)::(Checksum: 3:ebaf72b1619e5cd1f1cc1bb69d0b1306)
CREATE INDEX [FK7BD3671E1DEB859D] ON [dbo].[questionnaire_node]([input_node_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-194', '2.0.5', '3:ebaf72b1619e5cd1f1cc1bb69d0b1306', 194)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-195::henrik (generated)::(Checksum: 3:fcd5b8dd9ee492b66266d663e3dcf375)
CREATE INDEX [FK7BD3671E26A712A] ON [dbo].[questionnaire_node]([next_fail_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-195', '2.0.5', '3:fcd5b8dd9ee492b66266d663e3dcf375', 195)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-196::henrik (generated)::(Checksum: 3:a4a84487a9ddbefc23e1501dc02afdd8)
CREATE INDEX [FK7BD3671E6685CE83] ON [dbo].[questionnaire_node]([default_next_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-196', '2.0.5', '3:a4a84487a9ddbefc23e1501dc02afdd8', 196)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-197::henrik (generated)::(Checksum: 3:1799d4d52d923e643f8c176b92c6aedd)
CREATE INDEX [FK7BD3671E71B4F3EF] ON [dbo].[questionnaire_node]([alternative_next_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-197', '2.0.5', '3:1799d4d52d923e643f8c176b92c6aedd', 197)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-198::henrik (generated)::(Checksum: 3:4a691273398bf0323d32bd9a6934e139)
CREATE INDEX [FK7BD3671E80F1D93] ON [dbo].[questionnaire_node]([monica_measuring_time_input_node_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-198', '2.0.5', '3:4a691273398bf0323d32bd9a6934e139', 198)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-199::henrik (generated)::(Checksum: 3:f4cc36c95553abd8641093d7e0646515)
CREATE INDEX [FK7BD3671EA3B23BAF] ON [dbo].[questionnaire_node]([questionnaire_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-199', '2.0.5', '3:f4cc36c95553abd8641093d7e0646515', 199)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-200::henrik (generated)::(Checksum: 3:5839f78993cf97e9388d0aca67b8a686)
CREATE INDEX [FK7BD3671ECBF36DCD] ON [dbo].[questionnaire_node]([meter_type_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-200', '2.0.5', '3:5839f78993cf97e9388d0aca67b8a686', 200)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-201::henrik (generated)::(Checksum: 3:a2a7d67493e7b732be75cb54b87b1aec)
CREATE INDEX [FK9B0C5DB3385A86AB] ON [dbo].[questionnaire_schedule]([monitoring_plan_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-201', '2.0.5', '3:a2a7d67493e7b732be75cb54b87b1aec', 201)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-202::henrik (generated)::(Checksum: 3:cd83b1b3ca1a2312975822ae1706d40d)
CREATE INDEX [FK9B0C5DB3E09BD51F] ON [dbo].[questionnaire_schedule]([patient_questionnaire_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-202', '2.0.5', '3:cd83b1b3ca1a2312975822ae1706d40d', 202)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-203::henrik (generated)::(Checksum: 3:70b4b5c898e191733bda44f4c987babb)
CREATE INDEX [unique_monitoring_plan_id] ON [dbo].[questionnaire_schedule]([patient_questionnaire_id], [monitoring_plan_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-203', '2.0.5', '3:70b4b5c898e191733bda44f4c987babb', 203)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-204::henrik (generated)::(Checksum: 3:771c4f2a0ffa0773e4ce6ca9b8582daa)
CREATE UNIQUE INDEX [authority_unique_1361794626904] ON [dbo].[role]([authority])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-204', '2.0.5', '3:771c4f2a0ffa0773e4ce6ca9b8582daa', 204)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-205::henrik (generated)::(Checksum: 3:7008e82d10c4b7c35191a6c82c761a80)
CREATE INDEX [FKBD40D5385C87581A] ON [dbo].[role_permission]([role_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-205', '2.0.5', '3:7008e82d10c4b7c35191a6c82c761a80', 205)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-206::henrik (generated)::(Checksum: 3:07505eb36fa084b7a47c2a31d167b323)
CREATE INDEX [FKBD40D538DE1B957A] ON [dbo].[role_permission]([permission_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-206', '2.0.5', '3:07505eb36fa084b7a47c2a31d167b323', 206)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-207::henrik (generated)::(Checksum: 3:9e845c1c3dfd32c7fbc0dce5bbb38bc4)
CREATE INDEX [FKEDFC247846BA7BA] ON [dbo].[standard_threshold_set_threshold]([threshold_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-207', '2.0.5', '3:9e845c1c3dfd32c7fbc0dce5bbb38bc4', 207)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-208::henrik (generated)::(Checksum: 3:6b086d0a333e09bd67e9ad3a6f3b31bf)
CREATE INDEX [FKEDFC24787F2FA653] ON [dbo].[standard_threshold_set_threshold]([standard_threshold_set_thresholds_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-208', '2.0.5', '3:6b086d0a333e09bd67e9ad3a6f3b31bf', 208)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-209::henrik (generated)::(Checksum: 3:395ee922f1b5ac1e61378f83da51508c)
CREATE INDEX [FKA3E1E46B14CAB36] ON [dbo].[threshold]([type_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-209', '2.0.5', '3:395ee922f1b5ac1e61378f83da51508c', 209)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-210::henrik (generated)::(Checksum: 3:2fb57e3d92435d659401a3a26d57031a)
CREATE INDEX [FK143BF46A1B21BFA] ON [dbo].[user_role]([user_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-210', '2.0.5', '3:2fb57e3d92435d659401a3a26d57031a', 210)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-211::henrik (generated)::(Checksum: 3:91f652d27c2e62573d06d7e9b1920118)
CREATE INDEX [FK143BF46A5C87581A] ON [dbo].[user_role]([role_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-211', '2.0.5', '3:91f652d27c2e62573d06d7e9b1920118', 211)
GO

-- Changeset 1_0_baseline.groovy::1361794626972-212::henrik (generated)::(Checksum: 3:c40941ad846f941423beaebdc8202327)
CREATE UNIQUE INDEX [username_unique_1361794626910] ON [dbo].[users]([username])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_0_baseline.groovy', '1361794626972-212', '2.0.5', '3:c40941ad846f941423beaebdc8202327', 212)
GO

-- Changeset 1_2_1_update.groovy::1361952929480-1::erikmejerhansen (generated)::(Checksum: 3:2fc3f52b67755200c1daead8d5e89a9b)
ALTER TABLE [dbo].[measurement] ADD [has_temperature_warning] bit
GO

ALTER TABLE [dbo].[measurement] ADD [is_after_meal] bit
GO

ALTER TABLE [dbo].[measurement] ADD [is_before_meal] bit
GO

ALTER TABLE [dbo].[measurement] ADD [is_control_measurement] bit
GO

ALTER TABLE [dbo].[measurement] ADD [is_out_of_bounds] bit
GO

ALTER TABLE [dbo].[measurement] ADD [other_information] bit
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('erikmejerhansen (generated)', '', GETDATE(), 'Add Column (x6)', 'EXECUTED', '1_2_1_update.groovy', '1361952929480-1', '2.0.5', '3:2fc3f52b67755200c1daead8d5e89a9b', 213)
GO

-- Changeset 1_2_1_update.groovy::1361795627835-1::henrik (generated)::(Checksum: 3:685001461c65a68de9265e3e770f798a)
ALTER TABLE [dbo].[measurement] ADD [exported] bit
GO

update measurement set exported = 'FALSE'
GO

ALTER TABLE [dbo].[measurement ] ALTER COLUMN [exported] bit NOT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Column, Custom SQL, Add Not-Null Constraint', 'EXECUTED', '1_2_1_update.groovy', '1361795627835-1', '2.0.5', '3:685001461c65a68de9265e3e770f798a', 214)
GO

-- Changeset 1_2_1_update.groovy::1361965007066-1::of (generated)::(Checksum: 3:ead8485ea85c412c3546691b68ef15ce)
ALTER TABLE [dbo].[measurement] ADD [mean_arterial_pressure] double precision
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('of (generated)', '', GETDATE(), 'Add Column', 'EXECUTED', '1_2_1_update.groovy', '1361965007066-1', '2.0.5', '3:ead8485ea85c412c3546691b68ef15ce', 215)
GO

-- Changeset 1_2_1_update.groovy::1362040391774-1::henrik (generated)::(Checksum: 3:35ed70bdd1402d8baf8a6a9ebf160023)
ALTER TABLE [dbo].[permission] ADD [created_by] NVARCHAR(1024)
GO

update permission set created_by = 'sprint11'
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Column, Custom SQL', 'EXECUTED', '1_2_1_update.groovy', '1362040391774-1', '2.0.5', '3:35ed70bdd1402d8baf8a6a9ebf160023', 216)
GO

-- Changeset 1_2_1_update.groovy::1362040391774-2::henrik (generated)::(Checksum: 3:beca42b3c6608fcca365a4967c5e4ee6)
ALTER TABLE [dbo].[permission] ADD [created_date] datetime2(7)
GO

update permission set created_date = getdate()
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Column, Custom SQL', 'EXECUTED', '1_2_1_update.groovy', '1362040391774-2', '2.0.5', '3:beca42b3c6608fcca365a4967c5e4ee6', 217)
GO

-- Changeset 1_2_1_update.groovy::1362040391774-3::henrik (generated)::(Checksum: 3:ce97cd95f321c05c4a3c3d28bf7ccb79)
ALTER TABLE [dbo].[permission] ADD [modified_by] NVARCHAR(1024)
GO

update permission set modified_by = 'sprint11'
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Column, Custom SQL', 'EXECUTED', '1_2_1_update.groovy', '1362040391774-3', '2.0.5', '3:ce97cd95f321c05c4a3c3d28bf7ccb79', 218)
GO

-- Changeset 1_2_1_update.groovy::1362040391774-4::henrik (generated)::(Checksum: 3:d5dd87b897a0e6667cc004ab94b80f56)
ALTER TABLE [dbo].[permission] ADD [modified_date] datetime2(7)
GO

update permission set modified_date = getdate()
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Add Column, Custom SQL', 'EXECUTED', '1_2_1_update.groovy', '1362040391774-4', '2.0.5', '3:d5dd87b897a0e6667cc004ab94b80f56', 219)
GO

-- Changeset 1_2_1_update.groovy::1362993116610-1::of::(Checksum: 3:e5e24977d0131182cf3ba28fe35b9501)
ALTER TABLE [dbo].[measurement] ADD [fetal_height] VARCHAR(max)
GO

ALTER TABLE [dbo].[measurement] ADD [signal_to_noise] VARCHAR(max)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('of', '', GETDATE(), 'Add Column (x2)', 'EXECUTED', '1_2_1_update.groovy', '1362993116610-1', '2.0.5', '3:e5e24977d0131182cf3ba28fe35b9501', 220)
GO

-- Changeset 1_2_1_update.groovy::1363083585912-1::msurrow (generated)::(Checksum: 3:396508e45b7eccd1d54b694da362726d)
ALTER TABLE [dbo].[numeric_threshold] ALTER COLUMN [alert_high] FLOAT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow (generated)', '', GETDATE(), 'Drop Not-Null Constraint', 'EXECUTED', '1_2_1_update.groovy', '1363083585912-1', '2.0.5', '3:396508e45b7eccd1d54b694da362726d', 221)
GO

-- Changeset 1_2_1_update.groovy::1363083585912-2::msurrow (generated)::(Checksum: 3:3b25e7e1e129da0cf956b4c9f6509a94)
ALTER TABLE [dbo].[numeric_threshold] ALTER COLUMN [alert_low] FLOAT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow (generated)', '', GETDATE(), 'Drop Not-Null Constraint', 'EXECUTED', '1_2_1_update.groovy', '1363083585912-2', '2.0.5', '3:3b25e7e1e129da0cf956b4c9f6509a94', 222)
GO

-- Changeset 1_2_1_update.groovy::1363083585912-3::msurrow (generated)::(Checksum: 3:65b74b8d33ab98ff7918f948e9b880e0)
ALTER TABLE [dbo].[numeric_threshold] ALTER COLUMN [warning_high] FLOAT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow (generated)', '', GETDATE(), 'Drop Not-Null Constraint', 'EXECUTED', '1_2_1_update.groovy', '1363083585912-3', '2.0.5', '3:65b74b8d33ab98ff7918f948e9b880e0', 223)
GO

-- Changeset 1_2_1_update.groovy::1363083585912-4::msurrow (generated)::(Checksum: 3:7bd0385012f561d63fb8a23aa943f390)
ALTER TABLE [dbo].[numeric_threshold] ALTER COLUMN [warning_low] FLOAT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow (generated)', '', GETDATE(), 'Drop Not-Null Constraint', 'EXECUTED', '1_2_1_update.groovy', '1363083585912-4', '2.0.5', '3:7bd0385012f561d63fb8a23aa943f390', 224)
GO

-- Changeset 1_2_1_update.groovy::1363083585912-5::msurrow (generated)::(Checksum: 3:d1b2209cc86c557979ee9bfa330b1a3b)
ALTER TABLE [dbo].[urine_threshold] ALTER COLUMN [alert_high] NVARCHAR(1024) NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow (generated)', '', GETDATE(), 'Drop Not-Null Constraint', 'EXECUTED', '1_2_1_update.groovy', '1363083585912-5', '2.0.5', '3:d1b2209cc86c557979ee9bfa330b1a3b', 225)
GO

-- Changeset 1_2_1_update.groovy::1363083585912-6::msurrow (generated)::(Checksum: 3:4874b7f773d0d458678160e9d3d48de0)
ALTER TABLE [dbo].[urine_threshold] ALTER COLUMN [alert_low] NVARCHAR(1024) NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow (generated)', '', GETDATE(), 'Drop Not-Null Constraint', 'EXECUTED', '1_2_1_update.groovy', '1363083585912-6', '2.0.5', '3:4874b7f773d0d458678160e9d3d48de0', 226)
GO

-- Changeset 1_2_1_update.groovy::1363083585912-7::msurrow (generated)::(Checksum: 3:c3298ab75b14f6d21f92a3cee392fe3b)
ALTER TABLE [dbo].[urine_threshold] ALTER COLUMN [warning_high] NVARCHAR(1024) NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow (generated)', '', GETDATE(), 'Drop Not-Null Constraint', 'EXECUTED', '1_2_1_update.groovy', '1363083585912-7', '2.0.5', '3:c3298ab75b14f6d21f92a3cee392fe3b', 227)
GO

-- Changeset 1_2_1_update.groovy::1363083585912-8::msurrow (generated)::(Checksum: 3:99eac51a70ff134363843e2056d83f5a)
ALTER TABLE [dbo].[urine_threshold] ALTER COLUMN [warning_low] NVARCHAR(1024) NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow (generated)', '', GETDATE(), 'Drop Not-Null Constraint', 'EXECUTED', '1_2_1_update.groovy', '1363083585912-8', '2.0.5', '3:99eac51a70ff134363843e2056d83f5a', 228)
GO

-- Changeset 1_2_1_update.groovy::1363083585912-9::msurrow (generated)::(Checksum: 3:d11eddc300564f6767cd67cb5da0f000)
ALTER TABLE [dbo].[blood_pressure_threshold] ALTER COLUMN [diastolic_alert_high] FLOAT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow (generated)', '', GETDATE(), 'Drop Not-Null Constraint', 'EXECUTED', '1_2_1_update.groovy', '1363083585912-9', '2.0.5', '3:d11eddc300564f6767cd67cb5da0f000', 229)
GO

-- Changeset 1_2_1_update.groovy::1363083585912-10::msurrow (generated)::(Checksum: 3:d32b6d702c38b014d68c502bf9f5b72b)
ALTER TABLE [dbo].[blood_pressure_threshold] ALTER COLUMN [diastolic_alert_low] FLOAT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow (generated)', '', GETDATE(), 'Drop Not-Null Constraint', 'EXECUTED', '1_2_1_update.groovy', '1363083585912-10', '2.0.5', '3:d32b6d702c38b014d68c502bf9f5b72b', 230)
GO

-- Changeset 1_2_1_update.groovy::1363083585912-11::msurrow (generated)::(Checksum: 3:f2d32f78456e65453e881bedd6291fd9)
ALTER TABLE [dbo].[blood_pressure_threshold] ALTER COLUMN [diastolic_warning_high] FLOAT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow (generated)', '', GETDATE(), 'Drop Not-Null Constraint', 'EXECUTED', '1_2_1_update.groovy', '1363083585912-11', '2.0.5', '3:f2d32f78456e65453e881bedd6291fd9', 231)
GO

-- Changeset 1_2_1_update.groovy::1363083585912-12::msurrow (generated)::(Checksum: 3:54bfe6ae458fb6c211978936a85e988e)
ALTER TABLE [dbo].[blood_pressure_threshold] ALTER COLUMN [diastolic_warning_low] FLOAT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow (generated)', '', GETDATE(), 'Drop Not-Null Constraint', 'EXECUTED', '1_2_1_update.groovy', '1363083585912-12', '2.0.5', '3:54bfe6ae458fb6c211978936a85e988e', 232)
GO

-- Changeset 1_2_1_update.groovy::1363083585912-13::msurrow (generated)::(Checksum: 3:7bdc0b79dc78a24c80c52e95bffa2326)
ALTER TABLE [dbo].[blood_pressure_threshold] ALTER COLUMN [systolic_alert_high] FLOAT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow (generated)', '', GETDATE(), 'Drop Not-Null Constraint', 'EXECUTED', '1_2_1_update.groovy', '1363083585912-13', '2.0.5', '3:7bdc0b79dc78a24c80c52e95bffa2326', 233)
GO

-- Changeset 1_2_1_update.groovy::1363083585912-14::msurrow (generated)::(Checksum: 3:a9d3ccba491ba3b897aefba6dbdc1980)
ALTER TABLE [dbo].[blood_pressure_threshold] ALTER COLUMN [systolic_alert_low] FLOAT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow (generated)', '', GETDATE(), 'Drop Not-Null Constraint', 'EXECUTED', '1_2_1_update.groovy', '1363083585912-14', '2.0.5', '3:a9d3ccba491ba3b897aefba6dbdc1980', 234)
GO

-- Changeset 1_2_1_update.groovy::1363083585912-15::msurrow (generated)::(Checksum: 3:046b107823af12c05a3bc94e0ddefb07)
ALTER TABLE [dbo].[blood_pressure_threshold] ALTER COLUMN [systolic_warning_high] FLOAT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow (generated)', '', GETDATE(), 'Drop Not-Null Constraint', 'EXECUTED', '1_2_1_update.groovy', '1363083585912-15', '2.0.5', '3:046b107823af12c05a3bc94e0ddefb07', 235)
GO

-- Changeset 1_2_1_update.groovy::1363083585912-16::msurrow (generated)::(Checksum: 3:a9d7fca576d28b922a0f5221f1661976)
ALTER TABLE [dbo].[blood_pressure_threshold] ALTER COLUMN [systolic_warning_low] FLOAT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow (generated)', '', GETDATE(), 'Drop Not-Null Constraint', 'EXECUTED', '1_2_1_update.groovy', '1363083585912-16', '2.0.5', '3:a9d7fca576d28b922a0f5221f1661976', 236)
GO

-- Changeset 1_2_1_update.groovy::1363086950247-1::msurrow (generated)::(Checksum: 3:4647e5263aee9ce6074cfbf35dbb4110)
ALTER TABLE [dbo].[patient] ADD [data_responsible_id] BIGINT
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow (generated)', '', GETDATE(), 'Add Column', 'EXECUTED', '1_2_1_update.groovy', '1363086950247-1', '2.0.5', '3:4647e5263aee9ce6074cfbf35dbb4110', 237)
GO

-- Changeset 1_4_0_update.groovy::1362907355500-1::erikmejerhansen (generated)::(Checksum: 3:b80fb1607b75d7afd255e44c1448e900)
ALTER TABLE [dbo].[questionnaire] ADD [editor_state] VARCHAR(max)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('erikmejerhansen (generated)', '', GETDATE(), 'Add Column', 'EXECUTED', '1_4_0_update.groovy', '1362907355500-1', '2.0.5', '3:b80fb1607b75d7afd255e44c1448e900', 238)
GO

-- Changeset 1_4_0_update.groovy::add_glucoseInUrine_to_measuremnt::erikmejerhansen::(Checksum: 3:9aa80eff791c3093c552fdc06bd7385d)
ALTER TABLE [dbo].[measurement] ADD [glucose_in_urine] VARCHAR(max)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('erikmejerhansen', '', GETDATE(), 'Add Column', 'EXECUTED', '1_4_0_update.groovy', 'add_glucoseInUrine_to_measuremnt', '2.0.5', '3:9aa80eff791c3093c552fdc06bd7385d', 239)
GO

-- Changeset 1_4_0_update.groovy::addUrineGlucoseTresholdTable::ErikMejerHansen::(Checksum: 3:d004720c72995ca1fb73ae24d953323a)
CREATE TABLE [dbo].[urine_glucose_threshold] ([id] BIGINT NOT NULL, [alert_high] NVARCHAR(1024), [alert_low] NVARCHAR(1024), [warning_high] NVARCHAR(1024), [warning_low] NVARCHAR(1024), CONSTRAINT [urine_glucose_threshoPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('ErikMejerHansen', '', GETDATE(), 'Create Table', 'EXECUTED', '1_4_0_update.groovy', 'addUrineGlucoseTresholdTable', '2.0.5', '3:d004720c72995ca1fb73ae24d953323a', 240)
GO

-- Changeset 20130513_add_delay_node.groovy::1368430157548-1::msurrow::(Checksum: 3:05fe810c4ad4567d0ef58fb8f3587a07)
ALTER TABLE [dbo].[patient_questionnaire_node] ADD [count_up] bit
GO

ALTER TABLE [dbo].[patient_questionnaire_node] ADD [count_time] INT
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow', '', GETDATE(), 'Add Column', 'EXECUTED', '20130513_add_delay_node.groovy', '1368430157548-1', '2.0.5', '3:05fe810c4ad4567d0ef58fb8f3587a07', 241)
GO

-- Changeset 20130513_add_delay_node.groovy::1368430157548-2::msurrow::(Checksum: 3:e36e958b1234f7ccdc77c51bebfc6b0e)
ALTER TABLE [dbo].[questionnaire_node] ADD [count_up] bit
GO

ALTER TABLE [dbo].[questionnaire_node] ADD [count_time] INT
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msurrow', '', GETDATE(), 'Add Column', 'EXECUTED', '20130513_add_delay_node.groovy', '1368430157548-2', '2.0.5', '3:e36e958b1234f7ccdc77c51bebfc6b0e', 242)
GO

