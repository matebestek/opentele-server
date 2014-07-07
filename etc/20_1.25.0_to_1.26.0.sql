ALTER TABLE [dbo].[patient_group] ADD [show_running_ctg_messaging] bit
GO

update [dbo].patient_group set show_running_ctg_messaging = 'FALSE'
GO

ALTER TABLE [dbo].[patient_group] ALTER COLUMN [show_running_ctg_messaging] bit NOT NULL
GO
