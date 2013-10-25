-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog.groovy
-- Ran at: 29-08-13 11:17
-- Against: opentele@jdbc:jtds:sqlserver://192.168.0.199:1433:opentele
-- Liquibase version: 2.0.5
-- *********************************************************************

-- Lock Database
-- Changeset 1_8_0_update.groovy::fix_glucose_in_urine_1::Henrik (fix)::(Checksum: 3:3d29bb9a3f602c448bb5346f2fc34d85)
/*
ALTER TABLE [dbo].[measurement] ALTER COLUMN [glucose_in_urine] NVARCHAR(1024)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik (fix)', '', GETDATE(), 'Modify data type', 'EXECUTED', '1_8_0_update.groovy', 'fix_glucose_in_urine_1', '2.0.5', '3:3d29bb9a3f602c448bb5346f2fc34d85', 288)
GO
 */


-- Changeset 1_8_0_update.groovy::1371200712314-17.2::Henrik::(Checksum: 3:262e8742475dc8b1e5010841fad4a43c)
/*ALTER TABLE [dbo].[completed_questionnaire] ALTER COLUMN [questionnaire_header_id] BIGINT NOT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('Henrik', '', GETDATE(), 'Add Not-Null Constraint', 'EXECUTED', '1_8_0_update.groovy', '1371200712314-17.2', '2.0.5', '3:262e8742475dc8b1e5010841fad4a43c', 290)
GO
*/

-- CUSTOM Changes
ALTER TABLE [dbo].[measurement] ALTER COLUMN [fev_software_version] INT
GO



-- Changeset 1_8_4_update.groovy::add_requires_manual_inspection_to_questionnaire_header::mby::(Checksum: 3:3ae17d9383a753d7a954a1b9963ddcc3)
ALTER TABLE [dbo].[questionnaire_header] ADD [requires_manual_inspection] bit
GO

UPDATE [dbo].[questionnaire_header] SET [requires_manual_inspection] = 'FALSE' WHERE requires_manual_inspection IS NULL
GO

ALTER TABLE [dbo].[questionnaire_header] ALTER COLUMN [requires_manual_inspection] bit NOT NULL
GO

-- Changeset questionnaire_node_add_headline.groovy::questionnaire_node_add_headline::of::(Checksum: 3:f7751592cbc13316665de29405b0c923)
ALTER TABLE [dbo].[questionnaire_node] ADD [headline] VARCHAR(max)
GO

ALTER TABLE [dbo].[patient_questionnaire_node] ADD [headline] VARCHAR(max)
GO

-- Release Database Lock
