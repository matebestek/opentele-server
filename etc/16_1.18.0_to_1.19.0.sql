ALTER TABLE [dbo].[audit_log_entry] ADD [user_agent] nvarchar(1024)
GO

ALTER TABLE [dbo].[questionnaire_node] ADD [next_fail_severity] nvarchar(1024)
GO

ALTER TABLE [dbo].[patient_questionnaire_node] ADD [next_fail_severity] nvarchar(1024)
GO

CREATE TABLE [dbo].[continuous_blood_sugar_measurement] (
  [id] BIGINT IDENTITY NOT NULL,
  [version] BIGINT NOT NULL,
  [created_by] NVARCHAR(1024),
  [created_date] datetime2(7),
  [modified_by] NVARCHAR(1024),
  [modified_date] datetime2(7),
  [measurement_id] BIGINT NOT NULL,
  [record_number] int NOT NULL,
  [time] datetime2(7) NOT NULL,
  [value] double precision NOT NULL,
  CONSTRAINT [continuous_blood_sugar_measurement_PK] PRIMARY KEY ([id]))
GO

ALTER TABLE [dbo].[continuous_blood_sugar_measurement]
  ADD CONSTRAINT [FK_CGM_MEASUREMENT]
  FOREIGN KEY ([measurement_id])
  REFERENCES [dbo].[measurement] ([id])
GO

ALTER TABLE [dbo].[measurement] add [cgm_graphs_created] bit
GO

UPDATE [dbo].[measurement] set [cgm_graphs_created]=0;
GO

CREATE TABLE [dbo].[cgm_graphs] (
  [id] BIGINT IDENTITY NOT NULL,
  [version] BIGINT NOT NULL,
  [created_by] NVARCHAR(1024),
  [created_date] datetime2(7),
  [modified_by] NVARCHAR(1024),
  [modified_date] datetime2(7),
  [patient_id] BIGINT NOT NULL,
  [normal_graph] varbinary(max) NOT NULL,
  [average_graph] varbinary(max) NOT NULL,
  CONSTRAINT [cgm_graphs_PK] PRIMARY KEY ([id]))
GO

ALTER TABLE [dbo].[cgm_graphs]
  ADD CONSTRAINT [FK_CGM_GRAPHS_PATIENT]
  FOREIGN KEY ([patient_id])
  REFERENCES [dbo].[patient] ([id])
GO
