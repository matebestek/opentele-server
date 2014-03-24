ALTER TABLE [dbo].[patient_group] ADD [show_gestational_age] bit
GO

update [dbo].patient_group set show_gestational_age = 'FALSE'
GO

ALTER TABLE [dbo].[patient_group] ALTER COLUMN [show_gestational_age] bit NOT NULL
GO


ALTER TABLE [dbo].[patient] ADD [due_date] datetime2(7)
GO