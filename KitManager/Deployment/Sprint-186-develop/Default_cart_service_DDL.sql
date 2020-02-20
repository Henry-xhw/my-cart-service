USE cart_service
GO

-- 	 KitSection = Tables
--KitManagerFileID=17804
--FileName=cart.sql
--SubmittedBy=wade xu (ACTIVE\wxu)

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='carts' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[carts] (
        [id]                        BIGINT              IDENTITY (1, 1) NOT NULL,
        [identifier]                UNIQUEIDENTIFIER    NOT NULL,
        [owner_id]                  UNIQUEIDENTIFIER    NULL,
        [keyer_id]                  UNIQUEIDENTIFIER    NULL,
        [currency_code]             CHAR(3)             NOT NULL,
        [created_by]                NVARCHAR(255)       NOT NULL,
        [created_dt]                DATETIME            NOT NULL,
        [modified_by]               NVARCHAR(255)       NOT NULL,
        [modified_dt]               DATETIME            NOT NULL,
        [version]                   INT                 DEFAULT ((0)) NOT NULL,
        [price_version]             INT                 DEFAULT ((0)) NOT NULL,
        [is_lock]                   BIT                 DEFAULT ((0)) NOT NULL,
        [cart_status]               VARCHAR (255)       NOT NULL,
        [reservation_id]            UNIQUEIDENTIFIER    NULL,
        [coupon_codes]              NVARCHAR(MAX)       NULL
    )
	 PRINT 'CREATE TABLE dbo.carts'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'cart_status'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.[type] = 'U')
BEGIN

	ALTER TABLE dbo.carts ADD cart_status NVARCHAR(255) NOT NULL

	PRINT 'Added column cart_status to dbo.carts'
END

GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'version'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.[type] = 'U')
BEGIN

	ALTER TABLE dbo.carts ADD version INT DEFAULT ((0)) NOT NULL

	PRINT 'Added column version to dbo.carts'
END

GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'price_version'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.[type] = 'U')
BEGIN

	ALTER TABLE dbo.carts ADD price_version INT DEFAULT ((0)) NOT NULL

	PRINT 'Added column price_version to dbo.carts'
END

GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'is_lock'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.[type] = 'U')
BEGIN

	ALTER TABLE dbo.carts ADD is_lock BIT DEFAULT ((0)) NOT NULL

	PRINT 'Added column lock to dbo.carts'
END

GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.indexes i ON t.object_id = i.object_id AND i.is_primary_key = 1 WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='carts' AND t.type = 'U')
BEGIN
	 ALTER TABLE dbo.carts ADD CONSTRAINT [pk_cart]  PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE)
	 PRINT 'Created primary key pk_cart on table dbo.carts'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_identifier'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_identifier] ON [dbo].[carts] ([identifier])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_identifier to dbo.carts.'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_owner_id'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_owner_id] ON [dbo].[carts] ([owner_id])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_owner_id to dbo.carts.'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'reservation_id'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.[type] = 'U')
BEGIN

	ALTER TABLE dbo.carts ADD reservation_id UNIQUEIDENTIFIER NULL

	PRINT 'Added column reservation_id to dbo.carts'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'coupon_codes'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.[type] = 'U')
BEGIN

  ALTER TABLE dbo.carts ADD coupon_codes NVARCHAR(MAX) NULL

  PRINT 'Added column coupon_codes to dbo.carts'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','coupon_codes'))
BEGIN
  EXEC sys.sp_addextendedproperty
  @name = N'MS_Description',
  @value = N'coupon codes',
  @level0type = 'SCHEMA',
  @level0name = 'dbo',
  @level1type = 'TABLE',
  @level1name = 'carts',
  @level2type = 'Column',
  @level2name = 'coupon_codes'
END
GO
GO
--/KitManagerFileID=17804
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4704,17804,'cart.sql',0
GO

--KitManagerFileID=18092
--FileName=cart_discounts.sql
--SubmittedBy=wade xu (ACTIVE\wxu)

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='cart_discounts' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[cart_discounts] (
        [id]                                BIGINT              IDENTITY (1, 1) NOT NULL,
        [identifier]                        UNIQUEIDENTIFIER    NOT NULL,
        [cart_id]                           BIGINT              NOT NULL,
        [discount_type]                     NVARCHAR(25)        NOT NULL,
        [apply_to_recurring_billing]        BIT                 NULL,
        [discount_id]                       BIGINT              NULL,
        [keyer_uuid]                        UNIQUEIDENTIFIER    NULL,
        [discount_group_id]                 BIGINT              NULL,
        [amount_type]                       NVARCHAR(25)        NULL,
        [amount]                            DECIMAL(19, 2)      NULL,
        [origin]                            NVARCHAR(25)        NULL,
        [created_by]                        NVARCHAR(255)       NOT NULL,
        [created_dt]                        DATETIME            NOT NULL,
        [modified_by]                       NVARCHAR(255)       NOT NULL,
        [modified_dt]                       DATETIME            NOT NULL
    )
	 PRINT 'CREATE TABLE dbo.cart_discounts'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.indexes i ON t.object_id = i.object_id AND i.is_primary_key = 1 WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='cart_discounts' AND t.type = 'U')
BEGIN
	 ALTER TABLE dbo.cart_discounts ADD CONSTRAINT [pk_discounts]  PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE)
	 PRINT 'Created primary key pk_discounts on table dbo.discounts'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_discounts_identifier'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_discounts' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_discounts_identifier] ON [dbo].[cart_discounts] ([identifier])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_discounts_identifier to dbo.discounts.'
END

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_discounts','column','cart_id'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'carts table id',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_discounts',
 @level2type = 'Column',
 @level2name = 'cart_id'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_discounts','column','discount_type'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'ACTIVE_ADVANTAGE, COUPON, MULTI, AD_HOC, MEMBERSHIP',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_discounts',
 @level2type = 'Column',
 @level2name = 'discount_type'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_discounts','column','apply_to_recurring_billing'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'is apply to recurring billing',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_discounts',
 @level2type = 'Column',
 @level2name = 'apply_to_recurring_billing'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_discounts','column','discount_id'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'discounts table id',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_discounts',
 @level2type = 'Column',
 @level2name = 'discount_id'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_discounts','column','discount_id'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'keyer uuid',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_discounts',
 @level2type = 'Column',
 @level2name = 'keyer_uuid'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_discounts','column','discount_id'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'PERCENT, FLAT, FIXED_AMOUNT',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_discounts',
 @level2type = 'Column',
 @level2name = 'amount_type'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_discounts','column','discount_id'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'AUTOMATIC, OVERRIDE, AUTOMATIC_OVERRIDE, AD_HOC,ORDER_LINE_LEVEL_OVERRIDE, CARRY_OVER',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_discounts',
 @level2type = 'Column',
 @level2name = 'origin'
END
GO
GO
--/KitManagerFileID=18092
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4704,18092,'cart_discounts.sql',0
GO

--KitManagerFileID=17805
--FileName=cart_item.sql
--SubmittedBy=wade xu (ACTIVE\wxu)

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='cart_items' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[cart_items] (
        [id]                        BIGINT              IDENTITY (1, 1) NOT NULL,
        [cart_id]                   BIGINT              NOT NULL,
        [fee_volume_index]          BIGINT              NULL,
        [identifier]                UNIQUEIDENTIFIER    NOT NULL,
        [product_id]                BIGINT              NOT NULL,
        [product_name]              NVARCHAR(255)       NULL,
        [product_description]       NVARCHAR(MAX)       NULL,
        [booking_start_dt]          DATETIME            NULL,
        [booking_end_dt]            DATETIME            NULL,
        [trimmed_booking_start_dt]  DATETIME            NULL,
        [trimmed_booking_end_dt]    DATETIME            NULL,
        [quantity]                  BIGINT              NOT NULL,
        [unit_price]                DECIMAL(19, 2)      NULL,
        [grouping_identifier]       NVARCHAR(255)       NULL,
        [coupon_codes]              NVARCHAR(MAX)       NULL,
        [created_by]                NVARCHAR(255)       NOT NULL,
        [created_dt]                DATETIME            NOT NULL,
        [modified_by]               NVARCHAR(255)       NOT NULL,
        [modified_dt]               DATETIME            NOT NULL
    )
	 PRINT 'CREATE TABLE dbo.cart_items'
END
GO
/* OMS-10202 add column parent_id */
IF NOT EXISTS(SELECT 1
              FROM INFORMATION_SCHEMA.COLUMNS
              WHERE TABLE_NAME = 'cart_items'
                AND COLUMN_NAME = 'parent_id')
    BEGIN
        ALTER TABLE dbo.cart_items
            ADD parent_id BIGINT NULL;
        PRINT 'Added new column parent_id into table cart_items.';
    END
IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.indexes i ON t.object_id = i.object_id AND i.is_primary_key = 1 WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='cart_items' AND t.type = 'U')
BEGIN
	 ALTER TABLE dbo.cart_items ADD CONSTRAINT [pk_cart_item]  PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE)
	 PRINT 'Created primary key pk_cart_item on table dbo.cart_items'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_item_identifier'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_item_identifier] ON [dbo].[cart_items] ([identifier])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_item_identifier to dbo.cart.'
END

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_item_cart_id'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_item_cart_id] ON [dbo].[cart_items] ([cart_id])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_item_cart_id to dbo.cart_items.'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'fee_volume_index'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.[type] = 'U')
BEGIN

	ALTER TABLE dbo.cart_items ADD fee_volume_index BIGINT NULL

	PRINT 'Added column fee_volume_index to dbo.cart_items'
END
GO

IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'reservation_id'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.[type] = 'U')
BEGIN

	ALTER TABLE dbo.cart_items DROP COLUMN reservation_id

	PRINT 'Drop column reservation_id from dbo.cart_items'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'oversold'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.[type] = 'U')
    BEGIN

        ALTER TABLE dbo.cart_items ADD oversold BIT DEFAULT ((0)) NOT NULL

        PRINT 'Added column oversold to dbo.cart_items'
    END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'coupon_codes'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.[type] = 'U')
BEGIN

    ALTER TABLE dbo.cart_items ADD coupon_codes NVARCHAR(MAX) NULL

    PRINT 'Added column coupon_codes to dbo.cart_items'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'person_identifier'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.[type] = 'U')
    BEGIN

        ALTER TABLE dbo.cart_items ADD person_identifier NVARCHAR(50) NULL

        PRINT 'Added column person_identifier to dbo.cart_items'
    END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'coupon_mode'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.[type] = 'U')
    BEGIN

        ALTER TABLE dbo.cart_items ADD coupon_mode NVARCHAR(255) NOT NULL

        PRINT 'Added column coupon_mode to dbo.cart_items'
    END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'ignore_multi_discounts'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.[type] = 'U')
    BEGIN

        ALTER TABLE dbo.cart_items ADD ignore_multi_discounts BIT DEFAULT ((0)) NOT NULL

        PRINT 'Added column ignore_multi_discounts to dbo.cart_items'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_items','column','coupon_codes'))
BEGIN
    EXEC sys.sp_addextendedproperty
    @name = N'MS_Description',
    @value = N'coupon codes',
    @level0type = 'SCHEMA',
    @level0name = 'dbo',
    @level1type = 'TABLE',
    @level1name = 'cart_items',
    @level2type = 'Column',
    @level2name = 'coupon_codes'
END
GO
GO
--/KitManagerFileID=17805
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4704,17805,'cart_item.sql',0
GO

--KitManagerFileID=17876
--FileName=cart_item_fee.sql
--SubmittedBy=wade xu (ACTIVE\wxu)

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='cart_item_fees' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[cart_item_fees] (
        [id]                        BIGINT              IDENTITY (1, 1) NOT NULL,
        [identifier]                UNIQUEIDENTIFIER    NOT NULL,
        [parent_id]                 BIGINT              NULL,
        [name]                      NVARCHAR(255)       NULL,
        [description]               NVARCHAR(255)       NULL,
        [type]                      NVARCHAR(25)        NOT NULL,
        [transaction_type]          NVARCHAR(25)        NOT NULL,
        [units]                     BIGINT              NOT NULL,
        [unit_price]                DECIMAL(19, 2)      NOT NULL,
        [cart_discount_id]          BIGINT              NULL,
        [created_by]                NVARCHAR(255)       NOT NULL,
        [created_dt]                DATETIME            NOT NULL,
        [modified_by]               NVARCHAR(255)       NOT NULL,
        [modified_dt]               DATETIME            NOT NULL
    )
	 PRINT 'CREATE TABLE dbo.cart_item_fees'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.indexes i ON t.object_id = i.object_id AND i.is_primary_key = 1 WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='cart_item_fees' AND t.type = 'U')
BEGIN
	 ALTER TABLE dbo.cart_item_fees ADD CONSTRAINT [pk_cart_item_fee]  PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE)
	 PRINT 'Created primary key pk_cart_item_fee on table dbo.cart_item_fees'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_item_fee_identifier'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_item_fee_identifier] ON [dbo].[cart_item_fees] ([identifier])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_item_fee_identifier to dbo.cart_item_fees.'
END

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_item_fee_parent_id'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_item_fee_parent_id] ON [dbo].[cart_item_fees] ([parent_id])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_item_fee_parent_id to dbo.cart_item_fees.'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees','column','parent_id'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'parent id',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_item_fees',
 @level2type = 'Column',
 @level2name = 'parent_id'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees','column','name'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'cart item fees name',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_item_fees',
 @level2type = 'Column',
 @level2name = 'name'
END
GO
IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees','column','description'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'description',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_item_fees',
 @level2type = 'Column',
 @level2name = 'description'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees','column','type'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'type value PRICE, PRICE_ADJUSTMENT, PROCESSING_FLAT, PROCESSING_PERCENT, DISCOUNT, SURCHARGE',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_item_fees',
 @level2type = 'Column',
 @level2name = 'type'
END
GO
IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees','column','transaction_type'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'transaction type value DEBIT, CREDIT',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_item_fees',
 @level2type = 'Column',
 @level2name = 'transaction_type'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees','column','units'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'units',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_item_fees',
 @level2type = 'Column',
 @level2name = 'units'
END
GO
IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees','column','unit_price'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'unit price',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_item_fees',
 @level2type = 'Column',
 @level2name = 'unit_price'
END
GO

IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'name'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.cart_item_fees ALTER COLUMN [name] NVARCHAR(255)  NULL;
	PRINT 'modify column name null, dbo.cart_item_fees'
END

IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'description'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.cart_item_fees ALTER COLUMN [description] NVARCHAR(255)  NULL;
	PRINT 'modify column name null, dbo.cart_item_fees'
END

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'cart_discount_id'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.cart_item_fees ADD BIGINT BIT NULL;
	PRINT 'add column cart_discount_id on dbo.cart_item_fees'
END

GO
--/KitManagerFileID=17876
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4704,17876,'cart_item_fee.sql',0
GO

--/ 	 KitSection = Tables
-- 	 KitSection = ForeignKeys
--KitManagerFileID=18091
--FileName=cart_discount_fk.sql
--SubmittedBy=wade xu (ACTIVE\wxu)

IF NOT EXISTS(
    SELECT TOP 1 *
    FROM sys.foreign_keys
    WHERE name = 'fk_cart_discount_cart_id'
          AND OBJECT_NAME(parent_object_id) = 'cart_discounts' AND OBJECT_NAME(referenced_object_id) = 'carts'
)
BEGIN
     ALTER TABLE [dbo].[cart_discounts] ADD CONSTRAINT [fk_cart_discount_cart_id]
     FOREIGN KEY ([cart_id]) REFERENCES [dbo].[carts]([id]) ON DELETE CASCADE
     PRINT 'Added "fk_cart_discount_cart_id" Foreign Key to cart_discounts table';
END
GO

GO
--/KitManagerFileID=18091
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4704,18091,'cart_discount_fk.sql',0
GO

