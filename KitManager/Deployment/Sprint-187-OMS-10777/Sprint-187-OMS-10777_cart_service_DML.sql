USE cart_service
GO

-- 	 KitSection = DataScripts
--KitManagerFileID=18155
--FileName=drop_tables.sql
--SubmittedBy=  (ACTIVE\hxu)

USE cart_service
GO

IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES
           WHERE TABLE_NAME = 'events' and TABLE_TYPE = 'BASE TABLE' and TABLE_SCHEMA = 'dbo')
    BEGIN
        DROP TABLE events
        PRINT 'table events is dropped.'
    END

GO

IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES
           WHERE TABLE_NAME = 'discounts' and TABLE_TYPE = 'BASE TABLE' and TABLE_SCHEMA = 'dbo')
    BEGIN
        DROP TABLE discounts
        PRINT 'table discounts is dropped.'
    END

GO

IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES
           WHERE TABLE_NAME = 'cart_item_cart_item_fees' and TABLE_TYPE = 'BASE TABLE' and TABLE_SCHEMA = 'dbo')
    BEGIN
        DROP TABLE cart_item_cart_item_fees
        PRINT 'table cart_item_cart_item_fees is dropped.'
    END

GO

IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES
           WHERE TABLE_NAME = 'cart_item_fees' and TABLE_TYPE = 'BASE TABLE' and TABLE_SCHEMA = 'dbo')
    BEGIN
        DROP TABLE cart_item_fees
        PRINT 'table cart_item_fees is dropped.'
    END

GO

IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES
           WHERE TABLE_NAME = 'cart_items' and TABLE_TYPE = 'BASE TABLE' and TABLE_SCHEMA = 'dbo')
    BEGIN
        DROP TABLE cart_items
        PRINT 'table cart_items is dropped.'
    END

GO

IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES
           WHERE TABLE_NAME = 'carts' and TABLE_TYPE = 'BASE TABLE' and TABLE_SCHEMA = 'dbo')
    BEGIN
        DROP TABLE carts
        PRINT 'table carts is dropped.'
    END

GO














GO
--/KitManagerFileID=18155
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4750,18155,'drop_tables.sql',0
GO

