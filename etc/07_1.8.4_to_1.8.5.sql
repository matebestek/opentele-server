
--
-- Update CRP text (KIH-542)
--
UPDATE dbo.questionnaire_node
   SET text = 'Indtast værdi eller vælg <5'
 WHERE text in ('Mål C-reaktivt Protein (CRP) og indtast resultatet nedenfor', 'Indtast værdien. Hvis værdien er <5, indtast da venligst tallet 4.')
GO

UPDATE dbo.patient_questionnaire_node
   SET text = 'Indtast værdi eller vælg <5'
 WHERE text in ('Mål C-reaktivt Protein (CRP) og indtast resultatet nedenfor', 'Indtast værdien. Hvis værdien er <5, indtast da venligst tallet 4.')
GO

 UPDATE dbo.questionnaire_node
   SET text = 'Indtast resultatet af din crp-måling ved at vælge <5 eller skriv resultatet i indtastningsfeltet'
 WHERE text = 'Indtast værdi eller vælg <5'
GO

 UPDATE dbo.patient_questionnaire_node
   SET text = 'Indtast resultatet af din crp-måling ved at vælge <5 eller skriv resultatet i indtastningsfeltet'
 WHERE text = 'Indtast værdi eller vælg <5'
GO
