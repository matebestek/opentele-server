-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog.groovy
-- Ran at: 09-08-13 14:16
-- Against: opentele153@jdbc:jtds:sqlserver://192.168.0.162:1433:opentele153
-- Liquibase version: 2.0.5
-- *********************************************************************

-- Lock Database

-- Changeset 1_8_0_update.groovy::fix_glucose_in_urine_1::Henrik (fix)::(Checksum: 3:3d29bb9a3f602c448bb5346f2fc34d85)
ALTER TABLE [dbo].[measurement] ALTER COLUMN [glucose_in_urine] NVARCHAR(1024)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik (fix)', '', GETDATE(), 'Modify data type', 'EXECUTED', '1_8_0_update.groovy', 'fix_glucose_in_urine_1', '2.0.5', '3:3d29bb9a3f602c448bb5346f2fc34d85', 245)
GO

-- Changeset 1_8_0_update.groovy::add_fev_to_measuremnt::of::(Checksum: 3:d971aa1648ece53af948c471a73cf9d8)
ALTER TABLE [dbo].[measurement] ADD [fev6] double precision
GO

ALTER TABLE [dbo].[measurement] ADD [fev1fev6ratio] double precision
GO

ALTER TABLE [dbo].[measurement] ADD [fef2575] double precision
GO

ALTER TABLE [dbo].[measurement] ADD [is_good_test] bit
GO

ALTER TABLE [dbo].[measurement] ADD [fev_software_version] INT
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('of', '', GETDATE(), 'Add Column', 'EXECUTED', '1_8_0_update.groovy', 'add_fev_to_measuremnt', '2.0.5', '3:d971aa1648ece53af948c471a73cf9d8', 246)
GO


-- Changeset 1_8_0_update.groovy::add_standard_schedules::mss::(Checksum: 3:bd2b98111b5d2d71ebb72b17be588a1c)
ALTER TABLE [dbo].[questionnaire] ADD [STANDARD_SCHEDULE_TYPE] NVARCHAR(1024)
GO

ALTER TABLE [dbo].[questionnaire] ADD [STANDARD_SCHEDULE_WEEKDAYS] NVARCHAR(1024)
GO

ALTER TABLE [dbo].[questionnaire] ADD [STANDARD_SCHEDULE_TIMES_OF_DAY] NVARCHAR(1024)
GO

ALTER TABLE [dbo].[questionnaire] ADD [STANDARD_SCHEDULE_DAYS_IN_MONTH] NVARCHAR(1024)
GO

ALTER TABLE [dbo].[questionnaire] ADD [STANDARD_SCHEDULE_INTERVAL_IN_DAYS] INT
GO

ALTER TABLE [dbo].[questionnaire] ADD [STANDARD_SCHEDULE_STARTING_DATE] datetime2(7)
GO

ALTER TABLE [dbo].[questionnaire] ADD [STANDARD_SCHEDULE_SPECIFIC_DATE] datetime2(7)
GO

ALTER TABLE [dbo].[questionnaire] ADD [STANDARD_SCHEDULE_REMINDER_START_MINUTES] INT
GO

ALTER TABLE [dbo].[questionnaire_schedule] ADD [SPECIFIC_DATE] datetime2(7)
GO

ALTER TABLE [dbo].[questionnaire_schedule] ADD [REMINDER_START_MINUTES] INT
GO



update [dbo].questionnaire set STANDARD_SCHEDULE_TYPE = 'UNSCHEDULED'
GO

update [dbo].questionnaire set STANDARD_SCHEDULE_WEEKDAYS = ''
GO

update [dbo].questionnaire set STANDARD_SCHEDULE_TIMES_OF_DAY = ''
GO

update [dbo].questionnaire set STANDARD_SCHEDULE_DAYS_IN_MONTH = ''
GO

update [dbo].questionnaire set STANDARD_SCHEDULE_INTERVAL_IN_DAYS = 2
GO

update [dbo].questionnaire set STANDARD_SCHEDULE_STARTING_DATE = getdate()
GO

update [dbo].questionnaire set STANDARD_SCHEDULE_SPECIFIC_DATE = getdate()
GO

update [dbo].questionnaire set STANDARD_SCHEDULE_REMINDER_START_MINUTES = 30
GO


ALTER TABLE [dbo].[questionnaire] ALTER COLUMN [STANDARD_SCHEDULE_TYPE] NVARCHAR(1024) NOT NULL
GO

update [dbo].questionnaire_schedule set REMINDER_START_MINUTES = '30'
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('mss', '', GETDATE(), 'Add Column (x10), Custom SQL (x8), Add Not-Null Constraint, Custom SQL', 'EXECUTED', '1_8_0_update.groovy', 'add_standard_schedules', '2.0.5', '3:bd2b98111b5d2d71ebb72b17be588a1c', 247)
GO



-- Changeset 1_8_0_update.groovy::add_video_username_password_to_clinician::emh::(Checksum: 3:5800f88f565f0203ecad9d0e10224819)
ALTER TABLE [dbo].[Clinician] ADD [video_user] NVARCHAR(1024)
GO

ALTER TABLE [dbo].[Clinician] ADD [video_password] NVARCHAR(1024)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('emh', '', GETDATE(), 'Add Column', 'EXECUTED', '1_8_0_update.groovy', 'add_video_username_password_to_clinician', '2.0.5', '3:5800f88f565f0203ecad9d0e10224819', 248)
GO


-- Changeset 1_8_0_update.groovy::add_exported_to_kih_to_measurement::hra::(Checksum: 3:73caf3c8c252e159087e2bfdb621cbff)
ALTER TABLE [dbo].[measurement] ADD [exported_to_kih] bit
GO

update [dbo].measurement set exported_to_kih = 'FALSE'
GO

ALTER TABLE [dbo].[measurement] ALTER COLUMN [exported_to_kih] bit NOT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('hra', '', GETDATE(), 'Add Column, Custom SQL, Add Not-Null Constraint', 'EXECUTED', '1_8_0_update.groovy', 'add_exported_to_kih_to_measurement', '2.0.5', '3:73caf3c8c252e159087e2bfdb621cbff', 249)
GO


-- Changeset 1_8_0_update.groovy::add_pending_conference_table::emh::(Checksum: 3:bce194e162e1c48bbb453012149bb01b)
CREATE TABLE [dbo].[pending_conference] ([id] BIGINT IDENTITY NOT NULL, [room_key] NVARCHAR(1024), [patient_id] BIGINT, [clinician_id] BIGINT, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), CONSTRAINT [pending_conference_PK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('emh', '', GETDATE(), 'Create Table', 'EXECUTED', '1_8_0_update.groovy', 'add_pending_conference_table', '2.0.5', '3:bce194e162e1c48bbb453012149bb01b', 250)
GO

-- Changeset 1_8_0_update.groovy::1371200701109-39::mark::(Checksum: 3:b4d1ce4221b1f33b45f56dc10788def8)
CREATE TABLE [dbo].[schedule_window] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] VARCHAR(255), [created_date] datetime2(7), [modified_by] VARCHAR(255), [modified_date] datetime2(7), [schedule_type] VARCHAR(255) NOT NULL, [window_size_minutes] INT NOT NULL, CONSTRAINT [schedule_windPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('mark', '', GETDATE(), 'Create Table', 'EXECUTED', '1_8_0_update.groovy', '1371200701109-39', '2.0.5', '3:b4d1ce4221b1f33b45f56dc10788def8', 251)
GO


-- Changeset 1_8_0_update.groovy::1370876709019-21::msu::(Checksum: 3:6a89623ca058cd06e32829f6fab08ee1)
CREATE TABLE [dbo].[passive_interval] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [patient_id] BIGINT, [created_by] VARCHAR(255), [created_date] datetime2(7), [interval_end_date] datetime2(7), [interval_start_date] datetime2(7), [comment] VARCHAR(2048), [modified_by] VARCHAR(255), [modified_date] datetime2(7), CONSTRAINT [passive_interPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msu', '', GETDATE(), 'Create Table', 'EXECUTED', '1_8_0_update.groovy', '1370876709019-21', '2.0.5', '3:6a89623ca058cd06e32829f6fab08ee1', 252)
GO

-- Changeset 1_8_0_update.groovy::1370876709019-22::mark (generated)::(Checksum: 3:a5533415b401993f1672e9bd23897ac8)
ALTER TABLE [dbo].[passive_interval] ADD CONSTRAINT [FK83F2DBBC167D75AE] FOREIGN KEY ([patient_id]) REFERENCES [dbo].[patient] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('mark (generated)', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_8_0_update.groovy', '1370876709019-22', '2.0.5', '3:a5533415b401993f1672e9bd23897ac8', 253)
GO

-- Changeset 1_8_0_update.groovy::1371200701111-0::Henrik::(Checksum: 3:fcd526a10673f2bf08b51bdd4fff51da)
ALTER TABLE [dbo].[patient_questionnaire] DROP CONSTRAINT [FK24149DE9385A86AB]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Drop Foreign Key Constraint', 'EXECUTED', '1_8_0_update.groovy', '1371200701111-0', '2.0.5', '3:fcd526a10673f2bf08b51bdd4fff51da', 254)
GO

-- Changeset 1_8_0_update.groovy::1371200701111-0.1::Henrik::(Checksum: 3:992a0c18498bb36b36207afca4d6c523)
-- TODO: Fandtes ikk epå staging. Hvad med prod???

-- DROP INDEX [dbo].[patient_questionnaire].[FK24149DE9385A86AB]
-- GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Drop Index', 'EXECUTED', '1_8_0_update.groovy', '1371200701111-0.1', '2.0.5', '3:992a0c18498bb36b36207afca4d6c523', 255)
GO


-- Changeset 1_8_0_update.groovy::1371200701111-1::Henrik + S�ren::(Checksum: 3:aeaf05d808ccd4de1410f64fd9940062)
ALTER TABLE [dbo].[patient_questionnaire] DROP COLUMN [monitoring_plan_id]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik + S�ren', '', GETDATE(), 'Drop Column', 'EXECUTED', '1_8_0_update.groovy', '1371200701111-1', '2.0.5', '3:aeaf05d808ccd4de1410f64fd9940062', 256)
GO

-- Changeset 1_8_0_update.groovy::custom-conversions-101::Henrik::(Checksum: 3:43ee573f1acf0690d853baddd863f081)
update [dbo].completed_questionnaire
            set patient_questionnaire_id = (select min(inn.TARGET_PQ) from (
                    select cq.patient_id,cq.id , cqq.id as CQ_Q, dist.qid as target_Q, cq.patient_questionnaire_id as CQ_PQ, dist.minpqid as TARGET_PQ
                    from [dbo].completed_questionnaire cq
                    inner join [dbo].patient_questionnaire cqpq on cq.patient_questionnaire_id = cqpq.id
                    inner join [dbo].questionnaire cqq on cqq.id = cqpq.template_questionnaire_id
                    inner join (
                    select distinct q.id as qid, min(pq.id) as minpqid
                    from [dbo].patient_questionnaire pq
                    inner join [dbo].questionnaire q on pq.template_questionnaire_id = q.id
                    group by q.id
            ) dist on dist.qid = cqq.id
            ) inn
            where inn.CQ_PQ = completed_questionnaire.patient_questionnaire_id
            )
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Custom SQL', 'EXECUTED', '1_8_0_update.groovy', 'custom-conversions-101', '2.0.5', '3:43ee573f1acf0690d853baddd863f081', 257)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-1::Henrik + S�ren::(Checksum: 3:3048300a94853e43549222531cc321fa)
CREATE TABLE [dbo].[questionnaire_header] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] VARCHAR(255), [created_date] datetime2(7), [modified_by] VARCHAR(255), [modified_date] datetime2(7), [name] VARCHAR(255) NOT NULL, [active_questionnaire_id] BIGINT, [draft_questionnaire_id] BIGINT, CONSTRAINT [questionnaire_header_PK] PRIMARY KEY ([id]), UNIQUE ([name]))
GO

-- Fix duplicate questionnaire names
update [dbo].questionnaire set name = '' + name + ' ' + revision where id in (
select q.id from [dbo].questionnaire q
inner join  (
select name, count(*) as antal 
from [dbo].questionnaire 
group by name
having count(*) > 1) dupes on dupes.name = q.name)
Go

INSERT INTO [dbo].questionnaire_header (version, created_by, created_date, modified_by, modified_date, name, active_questionnaire_id, draft_questionnaire_id)
            select 0, created_by, getdate(), modified_by, getdate(), name, id, null from [dbo].questionnaire
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik + S�ren', '', GETDATE(), 'Create Table, Custom SQL', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-1', '2.0.5', '3:3048300a94853e43549222531cc321fa', 258)
GO


-- Changeset 1_8_0_update.groovy::1371200712314-2::Henrik + S�ren::(Checksum: 3:1b0d8e37ce658ea818b943b84b0bac30)
ALTER TABLE [dbo].[questionnaire] ADD [questionnaire_header_id] BIGINT
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik + S�ren', '', GETDATE(), 'Add Column', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-2', '2.0.5', '3:1b0d8e37ce658ea818b943b84b0bac30', 259)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-2.1_MS::Henrik + S�ren::(Checksum: 3:1e021f3cb8be3cd0591fd1747bf725be)
UPDATE q
            SET q.questionnaire_header_id = qh.id
            FROM [dbo].questionnaire q
            INNER JOIN  [dbo].questionnaire_header qh ON qh.active_questionnaire_id = q.id
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik + S�ren', '', GETDATE(), 'Custom SQL', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-2.1_MS', '2.0.5', '3:1e021f3cb8be3cd0591fd1747bf725be', 260)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-2.2::Henrik + S�ren::(Checksum: 3:800ac31279bffae2c30d46a7a583cacd)
ALTER TABLE [dbo].[questionnaire] ALTER COLUMN [questionnaire_header_id] BIGINT NOT NULL
GO


INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik + S�ren', '', GETDATE(), 'Add Not-Null Constraint', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-2.2', '2.0.5', '3:800ac31279bffae2c30d46a7a583cacd', 261)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-3::Henrik + S�ren::(Checksum: 3:660e75d739781dcd592938b2e8185b04)
ALTER TABLE [dbo].[questionnaire] ADD CONSTRAINT [questionnaire_header_FK] FOREIGN KEY ([questionnaire_header_id]) REFERENCES [dbo].[questionnaire_header] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik + S�ren', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-3', '2.0.5', '3:660e75d739781dcd592938b2e8185b04', 262)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-3.1::Henrik + S�ren::(Checksum: 3:2dab72cdff8035f1a259fb954fa93e3a)
ALTER TABLE [dbo].[questionnaire_header] ADD CONSTRAINT [active_quest_PK] FOREIGN KEY ([active_questionnaire_id]) REFERENCES [dbo].[questionnaire] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik + S�ren', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-3.1', '2.0.5', '3:2dab72cdff8035f1a259fb954fa93e3a', 263)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-3.2::Henrik + S�ren::(Checksum: 3:92eb236f172fda9bfb945e8840d359eb)
ALTER TABLE [dbo].[questionnaire_header] ADD CONSTRAINT [draft_quest_PK] FOREIGN KEY ([draft_questionnaire_id]) REFERENCES [dbo].[questionnaire] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik + S�ren', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-3.2', '2.0.5', '3:92eb236f172fda9bfb945e8840d359eb', 264)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-5::Henrik::(Checksum: 3:a36a161cf171a6b33f85903716651460)
 -- DROP INDEX [dbo].[patient_questionnaire].[unique_revision]
 -- GO
-- Mod RN staging::
ALTER TABLE [dbo].[patient_questionnaire] DROP CONSTRAINT [UQ__patient___4DCD90EE35A7EF71]
Go

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Drop Index', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-5', '2.0.5', '3:a36a161cf171a6b33f85903716651460', 265)
GO

-- Changeset 1_8_0_update.groovy::1371200701111-7::Henrik::(Checksum: 3:43c541f9b12eb2cf9b07d2506cb9ec33)
ALTER TABLE [dbo].[patient_questionnaire] DROP CONSTRAINT [FK24149DE941C6E17A]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Drop Foreign Key Constraint', 'EXECUTED', '1_8_0_update.groovy', '1371200701111-7', '2.0.5', '3:43c541f9b12eb2cf9b07d2506cb9ec33', 266)
GO

-- Changeset 1_8_0_update.groovy::1371200701111-7.1::Henrik::(Checksum: 3:1af449e861bc2e16ff4281637a832ceb)
-- Probably Not NEEDED?
-- DROP INDEX [dbo].[patient_questionnaire].[FK24149DE941C6E17A]
-- GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Drop Index', 'EXECUTED', '1_8_0_update.groovy', '1371200701111-7.1', '2.0.5', '3:1af449e861bc2e16ff4281637a832ceb', 267)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-8::Henrik::(Checksum: 3:d2e1fe4251b68f81be0003afaf4d5b7c)
ALTER TABLE [dbo].[patient_questionnaire] DROP COLUMN [patient_id]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Drop Column', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-8', '2.0.5', '3:d2e1fe4251b68f81be0003afaf4d5b7c', 268)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-9::Henrik::(Checksum: 3:557626dfeb66713db7366f69d916c167)
ALTER TABLE [dbo].[patient_questionnaire] DROP COLUMN [deleted]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Drop Column', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-9', '2.0.5', '3:557626dfeb66713db7366f69d916c167', 269)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-10::Henrik::(Checksum: 3:b4fd99233545fd296917da897471f578)
ALTER TABLE [dbo].[patient_questionnaire] DROP COLUMN [revision]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Drop Column', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-10', '2.0.5', '3:b4fd99233545fd296917da897471f578', 270)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-11::Henrik::(Checksum: 3:ce5d41c309a48e1e2a6e6f7d0f8c5f4c)
ALTER TABLE [dbo].[questionnaire_schedule] ADD [questionnaire_header_id] BIGINT
GO

update [dbo].questionnaire_schedule set questionnaire_header_id = (select qh.id
            from [dbo].questionnaire_header qh
                    inner join [dbo].questionnaire q on qh.active_questionnaire_id = q.id
                    inner join [dbo].patient_questionnaire pq on pq.template_questionnaire_id = q.id
                    where pq.id = questionnaire_schedule.patient_questionnaire_id)
GO

ALTER TABLE [dbo].[questionnaire_schedule] ALTER COLUMN [questionnaire_header_id] BIGINT NOT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Add Column, Custom SQL, Add Not-Null Constraint', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-11', '2.0.5', '3:ce5d41c309a48e1e2a6e6f7d0f8c5f4c', 271)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-12::Henrik::(Checksum: 3:b364c6e8720e614b68c242602d7075b3)
ALTER TABLE [dbo].[questionnaire_schedule] ADD CONSTRAINT [q_header_qsch_FK] FOREIGN KEY ([questionnaire_header_id]) REFERENCES [dbo].[questionnaire_header] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-12', '2.0.5', '3:b364c6e8720e614b68c242602d7075b3', 272)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-13::henrik (generated)::(Checksum: 3:c773281a8c8370174f58fda0c2892e84)
-- Fails!!!
DROP INDEX [dbo].[questionnaire_schedule].[unique_monitoring_plan_id]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Drop Index', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-13', '2.0.5', '3:c773281a8c8370174f58fda0c2892e84', 273)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-14::Henrik::(Checksum: 3:ff9301c6a3232dea1c4c38f7bc1e5427)
ALTER TABLE [dbo].[questionnaire_schedule] DROP CONSTRAINT [FK9B0C5DB3E09BD51F]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Drop Foreign Key Constraint', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-14', '2.0.5', '3:ff9301c6a3232dea1c4c38f7bc1e5427', 274)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-14.1::henrik (generated)::(Checksum: 3:9f217bf9006fdfba3342f5c15971f9c4)
-- DROP INDEX [dbo].[questionnaire_schedule].[FK9B0C5DB3E09BD51F]
-- GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Drop Index', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-14.1', '2.0.5', '3:9f217bf9006fdfba3342f5c15971f9c4', 275)
GO

-- CUSTOM FOR RN STAGING:
-- ALTER TABLE [dbo].[questionnaire_schedule] DROP CONSTRAINT [UQ__question__66BF49281BD30ED5];

-- CUSTOM FOR RN PROD:
ALTER TABLE [dbo].[questionnaire_schedule] DROP CONSTRAINT [UQ__question__66BF4928505BE5AD];
-- Changeset 1_8_0_update.groovy::1371200712314-15::Henrik::(Checksum: 3:9a3e01efab096bfe2d8b7ea947738214)
ALTER TABLE [dbo].[questionnaire_schedule] DROP COLUMN [patient_questionnaire_id]
GO


INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Drop Column', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-15', '2.0.5', '3:9a3e01efab096bfe2d8b7ea947738214', 276)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-16::henrik (generated)::(Checksum: 3:6e419fc71cc6098c76c3f0c93a989391)
CREATE INDEX [unique_monplan_header] ON [dbo].[questionnaire_schedule]([questionnaire_header_id], [monitoring_plan_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('henrik (generated)', '', GETDATE(), 'Create Index', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-16', '2.0.5', '3:6e419fc71cc6098c76c3f0c93a989391', 277)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-17::Henrik::(Checksum: 3:61296d4a731d5df5c6ac575dd2ddcc3d)

ALTER TABLE [dbo].[completed_questionnaire] ADD [questionnaire_header_id] BIGINT
GO

-- Fejl???
-- ALTER TABLE [dbo].[choice_value] ADD [ordering] INT
-- GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Add Column (x2)', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-17', '2.0.5', '3:61296d4a731d5df5c6ac575dd2ddcc3d', 278)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-17.1::Henrik::(Checksum: 3:58cea50462c7e6fdd457235aa3af92b9)
UPDATE cq
                     SET cq.questionnaire_header_id = qh.id
                     FROM [dbo].completed_questionnaire cq
                     INNER JOIN [dbo].patient_questionnaire pq ON cq.patient_questionnaire_id = pq.id
                     INNER JOIN [dbo].questionnaire q ON pq.template_questionnaire_id = q.id
                     INNER JOIN [dbo].questionnaire_header qh on q.questionnaire_header_id = qh.id
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Custom SQL', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-17.1', '2.0.5', '3:58cea50462c7e6fdd457235aa3af92b9', 279)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-17.2::Henrik::(Checksum: 3:262e8742475dc8b1e5010841fad4a43c)
ALTER TABLE [dbo].[completed_questionnaire] ALTER COLUMN [questionnaire_header_id] BIGINT NOT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Add Not-Null Constraint', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-17.2', '2.0.5', '3:262e8742475dc8b1e5010841fad4a43c', 280)
GO

-- Changeset 1_8_0_update.groovy::1371200712314-18::Henrik::(Checksum: 3:2a173ea90d744818ca66f9b386cb5e70)
ALTER TABLE [dbo].[completed_questionnaire] ADD CONSTRAINT [q_header_comp_FK] FOREIGN KEY ([questionnaire_header_id]) REFERENCES [dbo].[questionnaire_header] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-18', '2.0.5', '3:2a173ea90d744818ca66f9b386cb5e70', 281)
GO

-- Changeset 1_8_0_update.groovy::147120071345-5::msu::(Checksum: 3:a0ac1f0f10846b9cd5c16f4bdc40c2de)
ALTER TABLE [dbo].[choice_value] ADD [ordering] INT
GO

update [dbo].choice_value set ordering = 1
GO

ALTER TABLE [dbo].[choice_value] ALTER COLUMN [ordering] INT NOT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msu', '', GETDATE(), 'Add Column, Custom SQL, Add Not-Null Constraint', 'EXECUTED', '1_8_0_update.groovy', '147120071345-5', '2.0.5', '3:a0ac1f0f10846b9cd5c16f4bdc40c2de', 282)
GO

-- Changeset 1_8_0_update.groovy::147120071345-6::msu::(Checksum: 3:d003c79973a67a32c3c3bbc2c71a0bc4)
ALTER TABLE [dbo].[patient_choice_value] ADD [ordering] INT
GO

update [dbo].patient_choice_value set ordering = 1
GO

ALTER TABLE [dbo].[patient_choice_value] ALTER COLUMN [ordering] INT NOT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msu', '', GETDATE(), 'Add Column, Custom SQL, Add Not-Null Constraint', 'EXECUTED', '1_8_0_update.groovy', '147120071345-6', '2.0.5', '3:d003c79973a67a32c3c3bbc2c71a0bc4', 283)
GO

-- Changeset 1_8_0_update.groovy::add_disable_messaging_to_patient_group::emh::(Checksum: 3:fd53d116acc4241b361270834a40242a)
ALTER TABLE [dbo].[patient_group] ADD [disable_messaging] bit
GO

update [dbo].patient_group set disable_messaging = 'FALSE'
GO

ALTER TABLE [dbo].[patient_group] ALTER COLUMN [disable_messaging] bit NOT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('emh', '', GETDATE(), 'Add Column, Custom SQL, Add Not-Null Constraint', 'EXECUTED', '1_8_0_update.groovy', 'add_disable_messaging_to_patient_group', '2.0.5', '3:fd53d116acc4241b361270834a40242a', 284)
GO




-- Changeset 1_8_0_update.groovy::add_conference::of::(Checksum: 3:279e176ff2586b04015b7b9d73d08a5f)
CREATE TABLE [dbo].[conference] ([id] BIGINT IDENTITY NOT NULL, [patient_id] BIGINT NOT NULL, [clinician_id] BIGINT NOT NULL, [completed] bit, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), CONSTRAINT [conference_PK] PRIMARY KEY ([id]))
GO

ALTER TABLE [dbo].[conference] ADD CONSTRAINT [conf_patient_FK] FOREIGN KEY ([patient_id]) REFERENCES [dbo].[patient] ([id])
GO

ALTER TABLE [dbo].[conference] ADD CONSTRAINT [conf_clinician_FK] FOREIGN KEY ([clinician_id]) REFERENCES [dbo].[clinician] ([id])
GO


CREATE TABLE [dbo].[conference_measurement_draft] ([id] BIGINT IDENTITY NOT NULL, [class] NVARCHAR(1024) NOT NULL, [included] bit NOT NULL, [conference_id] BIGINT NOT NULL, [weight] double precision, [fev1] double precision, [saturation] double precision, [pulse] INT, [systolic] INT, [diastolic] INT, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), CONSTRAINT [conference_measurement_draft_PK] PRIMARY KEY ([id]))
GO

ALTER TABLE [dbo].[conference_measurement_draft] ADD CONSTRAINT [confmeasdraft_conf_FK] FOREIGN KEY ([conference_id]) REFERENCES [dbo].[conference] ([id])
GO

ALTER TABLE [dbo].[measurement] ADD [conference_id] BIGINT
GO

ALTER TABLE [dbo].[measurement] ADD CONSTRAINT [meas_conf_FK] FOREIGN KEY ([conference_id]) REFERENCES [dbo].[conference] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('of', '', GETDATE(), 'Create Table, Add Foreign Key Constraint (x2), Create Table, Add Foreign Key Constraint, Add Column, Add Foreign Key Constraint', 'EXECUTED', '1_8_0_update.groovy', 'add_conference', '2.0.5', '3:279e176ff2586b04015b7b9d73d08a5f', 285)
GO


-- Changeset 1_8_0_update.groovy::147120071345-1::S�ren::(Checksum: 3:3b288c1e607ead4ce1d494b7c3e28f77)
CREATE TABLE [dbo].[questionnaire_group] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] VARCHAR(255), [created_date] datetime2(7), [modified_by] VARCHAR(255), [modified_date] datetime2(7), [name] VARCHAR(255) NOT NULL, CONSTRAINT [questionnaire_group_PK] PRIMARY KEY ([id]), UNIQUE ([name]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('S�ren', '', GETDATE(), 'Create Table', 'EXECUTED', '1_8_0_update.groovy', '147120071345-1', '2.0.5', '3:3b288c1e607ead4ce1d494b7c3e28f77', 286)
GO


-- Changeset 1_8_0_update.groovy::147120071345-2::S�ren::(Checksum: 3:c0e7cec87396dbed42c56893cf672e21)
CREATE TABLE [dbo].[questionnaire_group2questionnaire_header] ([id] BIGINT IDENTITY NOT NULL, [questionnaire_group_id] BIGINT NOT NULL, [questionnaire_header_id] BIGINT NOT NULL, [version] BIGINT NOT NULL, [created_by] VARCHAR(255), [created_date] datetime2(7), [modified_by] VARCHAR(255), [modified_date] datetime2(7), [STANDARD_SCHEDULE_TYPE] NVARCHAR(1024), [STANDARD_SCHEDULE_WEEKDAYS] NVARCHAR(1024), [STANDARD_SCHEDULE_TIMES_OF_DAY] NVARCHAR(1024), [STANDARD_SCHEDULE_DAYS_IN_MONTH] NVARCHAR(1024), [STANDARD_SCHEDULE_INTERVAL_IN_DAYS] INT, [STANDARD_SCHEDULE_STARTING_DATE] datetime2(7), [STANDARD_SCHEDULE_SPECIFIC_DATE] datetime2(7), [STANDARD_SCHEDULE_REMINDER_START_MINUTES] INT, [SPECIFIC_DATE] datetime2(7), [REMINDER_START_MINUTES] INT, CONSTRAINT [questionnaire_group2questionnaire_header_PK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('S�ren', '', GETDATE(), 'Create Table', 'EXECUTED', '1_8_0_update.groovy', '147120071345-2', '2.0.5', '3:c0e7cec87396dbed42c56893cf672e21', 287)
GO

-- Changeset 1_8_0_update.groovy::147120071345-3::S�ren::(Checksum: 3:9f2479a83da751eedcfc8e384aaa95b2)
ALTER TABLE [dbo].[questionnaire_group2questionnaire_header] ADD CONSTRAINT [questionnaire_groupFK] FOREIGN KEY ([questionnaire_group_id]) REFERENCES [dbo].[questionnaire_group] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('S�ren', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_8_0_update.groovy', '147120071345-3', '2.0.5', '3:9f2479a83da751eedcfc8e384aaa95b2', 288)
GO

-- Changeset 1_8_0_update.groovy::147120071345-4::S�ren::(Checksum: 3:b5aa615cdf51477a0c712d4a83fbf934)
ALTER TABLE [dbo].[questionnaire_group2questionnaire_header] ADD CONSTRAINT [questionnaire_headerFK] FOREIGN KEY ([questionnaire_header_id]) REFERENCES [dbo].[questionnaire_header] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('S�ren', '', GETDATE(), 'Add Foreign Key Constraint', 'EXECUTED', '1_8_0_update.groovy', '147120071345-4', '2.0.5', '3:b5aa615cdf51477a0c712d4a83fbf934', 289)
GO

-- Changeset 1_8_0_update.groovy::remove_name_from_questionnaire_and_patient_questionnaire::mss::(Checksum: 3:4ee4d95ffc53efc491c571e0b9a139ad)
ALTER TABLE [dbo].[questionnaire] DROP COLUMN [name]
GO

ALTER TABLE [dbo].[patient_questionnaire] DROP COLUMN [name]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('mss', '', GETDATE(), 'Drop Column (x2)', 'EXECUTED', '1_8_0_update.groovy', 'remove_name_from_questionnaire_and_patient_questionnaire', '2.0.5', '3:4ee4d95ffc53efc491c571e0b9a139ad', 290)
GO

-- Changeset 1_8_0_update.groovy::blue_alarm_check::mss::(Checksum: 3:166c3706e326d4e7929fda29776d4600)
CREATE TABLE [dbo].[blue_alarm_check] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [check_date] datetime2(7) NOT NULL, [created_by] NVARCHAR(1024), [created_date] datetime2(7), [modified_by] NVARCHAR(1024), [modified_date] datetime2(7), CONSTRAINT [blue_alarm_checkPK] PRIMARY KEY ([id]))
GO

CREATE INDEX [check_date_idx] ON [dbo].[blue_alarm_check]([check_date])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('mss', '', GETDATE(), 'Create Table, Create Index', 'EXECUTED', '1_8_0_update.groovy', 'blue_alarm_check', '2.0.5', '3:166c3706e326d4e7929fda29776d4600', 291)
GO

-- Changeset 1_8_0_update.groovy::20130710_add_badLoginAttemps_to_user::msu::(Checksum: 3:2778a5d0efab90c8ab5447ffc7a3bbfb)
ALTER TABLE [dbo].[Users] ADD [bad_login_attemps] INT
GO

update [dbo].users set bad_login_attemps = 0
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('msu', '', GETDATE(), 'Add Column, Custom SQL', 'EXECUTED', '1_8_0_update.groovy', '20130710_add_badLoginAttemps_to_user', '2.0.5', '3:2778a5d0efab90c8ab5447ffc7a3bbfb', 292)
GO

-- Release Database Lock

