USE cart_service
GO

-- 	 KitSection = Tables
--KitManagerFileID=17922
--FileName=delete.sql
--SubmittedBy=evan wei (ACTIVE\ewei)


drop table dbo.carts
drop table dbo.cart_items
drop table dbo.cart_item_fees
drop table dbo.cart_item_cart_item_fees
GO
--/KitManagerFileID=17922
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4610,17922,'delete.sql',0
GO

