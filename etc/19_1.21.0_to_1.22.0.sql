CREATE TABLE [dbo].[consultation] (
  [id] BIGINT IDENTITY NOT NULL,
  [patient_id] BIGINT NOT NULL,
  [clinician_id] BIGINT NOT NULL,
  [consultation_date] datetime2(7),
  [version] BIGINT NOT NULL,
  [created_by] NVARCHAR(1024),
  [created_date] datetime2(7),
  [modified_by] NVARCHAR(1024),
  [modified_date] datetime2(7),
  CONSTRAINT [consultation_PK] PRIMARY KEY ([id]))
GO

ALTER TABLE [dbo].[consultation]
  ADD CONSTRAINT [consultation_patient_FK]
  FOREIGN KEY ([patient_id])
  REFERENCES [dbo].[patient] ([id])
GO

ALTER TABLE [dbo].[consultation]
  ADD CONSTRAINT [consultation_clinician_FK]
  FOREIGN KEY ([clinician_id])
  REFERENCES [dbo].[clinician] ([id])
GO

ALTER TABLE [dbo].[measurement] ADD [consultation_id] BIGINT
GO

ALTER TABLE [dbo].[measurement]
  ADD CONSTRAINT [meas_consultation_FK]
  FOREIGN KEY ([consultation_id])
  REFERENCES [dbo].[consultation] ([id])
GO
