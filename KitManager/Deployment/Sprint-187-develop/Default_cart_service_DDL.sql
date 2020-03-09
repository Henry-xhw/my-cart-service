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
        [reservation_group_id]      UNIQUEIDENTIFIER    NULL,
        [coupon_codes]              NVARCHAR(MAX)       NULL,
        [sales_channel]             VARCHAR (255)       NULL,
        CONSTRAINT [pk_cart] PRIMARY KEY CLUSTERED ([id]) WITH (STATISTICS_NORECOMPUTE = ON)
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
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'coupon_codes'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.[type] = 'U')
BEGIN

  ALTER TABLE dbo.carts ADD coupon_codes NVARCHAR(MAX) NULL

  PRINT 'Added column coupon_codes to dbo.carts'
END
GO

IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'reservation_id'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.[type] = 'U')
BEGIN

  EXEC sys.sp_rename 'carts.reservation_id','reservation_group_id','column'

  PRINT 'Alter column reservation_id name to reservation_group_id'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'sales_channel'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.[type] = 'U')
BEGIN

  ALTER TABLE dbo.carts ADD sales_channel VARCHAR (255) NULL

  PRINT 'Added column sales_channel to dbo.carts'
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

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','sales_channel'))
BEGIN
  EXEC sys.sp_addextendedproperty
  @name = N'MS_Description',
  @value = N'sales channel',
  @level0type = 'SCHEMA',
  @level0name = 'dbo',
  @level1type = 'TABLE',
  @level1name = 'carts',
  @level2type = 'Column',
  @level2name = 'sales_channel'
END
GO
GO
--/KitManagerFileID=17804
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4738,17804,'cart.sql',0
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
        [override_price]            DECIMAL(19, 2)      NULL,
        [gross_price]               DECIMAL(19, 2)      NULL,
        [net_price]                 DECIMAL(19, 2)      NULL,
        [grouping_identifier]       NVARCHAR(255)       NULL,
        [coupon_codes]              NVARCHAR(MAX)       NULL,
        [reservation_id]            UNIQUEIDENTIFIER    NULL,
        [created_by]                NVARCHAR(255)       NOT NULL,
        [created_dt]                DATETIME            NOT NULL,
        [modified_by]               NVARCHAR(255)       NOT NULL,
        [modified_dt]               DATETIME            NOT NULL,
        CONSTRAINT [pk_cart_item] PRIMARY KEY CLUSTERED ([id]) WITH (STATISTICS_NORECOMPUTE = ON)
    )
	 PRINT 'CREATE TABLE dbo.cart_items'
END
GO

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
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.[type] = 'U')
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

        ALTER TABLE dbo.cart_items ADD coupon_mode NVARCHAR(255) NULL

        PRINT 'Added column coupon_mode to dbo.cart_items'
    END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'override_price'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.[type] = 'U')
    BEGIN

        ALTER TABLE dbo.cart_items ADD override_price DECIMAL(19, 2) NULL

        PRINT 'Added column override_price to dbo.cart_items'
    END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'gross_price'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.[type] = 'U')
    BEGIN

        ALTER TABLE dbo.cart_items ADD gross_price DECIMAL(19, 2) NULL

        PRINT 'Added column gross_price to dbo.cart_items'
    END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'net_price'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.[type] = 'U')
    BEGIN

        ALTER TABLE dbo.cart_items ADD net_price DECIMAL(19, 2) NULL

        PRINT 'Added column net_price to dbo.cart_items'
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

IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'unit_price'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.[type] = 'U')
    BEGIN

        ALTER TABLE dbo.cart_items DROP column unit_price

        PRINT 'Droped column unit_price to dbo.cart_items'
    END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'reservation_id'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.[type] = 'U')
BEGIN

    ALTER TABLE dbo.cart_items ADD reservation_id UNIQUEIDENTIFIER NULL

    PRINT 'Added column reservation_id to dbo.cart_items'
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

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_items','column','override_price'))
BEGIN
    EXEC sys.sp_addextendedproperty
    @name = N'MS_Description',
    @value = N'override price',
    @level0type = 'SCHEMA',
    @level0name = 'dbo',
    @level1type = 'TABLE',
    @level1name = 'cart_items',
    @level2type = 'Column',
    @level2name = 'override_price'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_items','column','gross_price'))
BEGIN
    EXEC sys.sp_addextendedproperty
    @name = N'MS_Description',
    @value = N'gross price',
    @level0type = 'SCHEMA',
    @level0name = 'dbo',
    @level1type = 'TABLE',
    @level1name = 'cart_items',
    @level2type = 'Column',
    @level2name = 'gross_price'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_items','column','net_price'))
BEGIN
    EXEC sys.sp_addextendedproperty
    @name = N'MS_Description',
    @value = N'net price',
    @level0type = 'SCHEMA',
    @level0name = 'dbo',
    @level1type = 'TABLE',
    @level1name = 'cart_items',
    @level2type = 'Column',
    @level2name = 'net_price'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_items','column','reservation_id'))
BEGIN
    EXEC sys.sp_addextendedproperty
    @name = N'MS_Description',
    @value = N'reservation id',
    @level0type = 'SCHEMA',
    @level0name = 'dbo',
    @level1type = 'TABLE',
    @level1name = 'cart_items',
    @level2type = 'Column',
    @level2name = 'reservation_id'
END
GO
GO
--/KitManagerFileID=17805
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4738,17805,'cart_item.sql',0
GO

