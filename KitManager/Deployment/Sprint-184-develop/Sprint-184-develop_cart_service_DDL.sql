USE cart_service
GO

-- 	 KitSection = Tables
--KitManagerFileID=17922
--FileName=delete.sql
--SubmittedBy=evan wei (ACTIVE\ewei)

IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='cart' AND type = 'U')
BEGIN
	 drop table dbo.cart
END
IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='cart_item' AND type = 'U')
BEGIN
	 drop table dbo.cart_item
END

IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='cart_items' AND type = 'U')
BEGIN
	 drop table dbo.cart_items
END

IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='cart_item_cart_item_fees' AND type = 'U')
BEGIN
	 drop table dbo.cart_item_cart_item_fees
END

IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='carts' AND type = 'U')
BEGIN
	 drop table dbo.carts
END

IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='cart_item_fees' AND type = 'U')
BEGIN
	 drop table dbo.cart_item_fees
END



GO
--/KitManagerFileID=17922
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4610,17922,'delete.sql',0
GO

