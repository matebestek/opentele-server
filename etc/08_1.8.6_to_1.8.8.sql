--
-- Delete "CREATED" patient state
--
update patient
   set state = 'ACTIVE'
 where state = 'CREATED'