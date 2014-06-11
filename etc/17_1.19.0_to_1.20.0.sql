ALTER TABLE [dbo].[patient_overview]
ADD [created_by] NVARCHAR(1024),
    [created_date] datetime2(7),
    [modified_by] NVARCHAR(1024),
    [modified_date] datetime2(7)
GO

update patient_overview
set created_by = 'sprint33',
    modified_by = 'sprint33',
    created_date = getdate(),
    modified_date = getdate()
GO