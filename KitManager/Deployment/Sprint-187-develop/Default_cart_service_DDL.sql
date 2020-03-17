USE cart_service
GO

-- 	 KitSection = Tables
--KitManagerFileID=18111
--FileName=discounts.sql
--SubmittedBy=evan wei (ACTIVE\ewei)

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='discounts' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[discounts] (
        [id]                                BIGINT              IDENTITY (1, 1) NOT NULL,
        [identifier]                        UNIQUEIDENTIFIER    NOT NULL,
        [cart_id]                           BIGINT              NOT NULL,
        [name]                              NVARCHAR(255)       NULL,
        [description]                       NVARCHAR(255)       NULL,
        [amount]                            DECIMAL(19, 2)      NOT NULL,
        [amount_type]                       NVARCHAR(25)        NULL,
        [discount_id]                       BIGINT              NULL,
        [discount_type]                     NVARCHAR(25)        NOT NULL,
        [coupon_code]                       NVARCHAR(255)       NULL,
        [algorithm]                         NVARCHAR(25)        NULL,
        [apply_to_recurring_billing]        BIT                 NOT NULL,
        [discount_group_id]                 BIGINT              NULL,
        [origin]                            NVARCHAR(25)        NULL,
        [keyer_uuid]                        UNIQUEIDENTIFIER    NULL,
        [created_by]                        NVARCHAR(255)       NOT NULL,
        [created_dt]                        DATETIME            NOT NULL,
        [modified_by]                       NVARCHAR(255)       NOT NULL,
        [modified_dt]                       DATETIME            NOT NULL,
        CONSTRAINT [pk_discounts] PRIMARY KEY CLUSTERED ([id]) WITH (STATISTICS_NORECOMPUTE = ON)
    )
	 PRINT 'CREATE TABLE dbo.discounts'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.indexes i ON t.object_id = i.object_id AND i.is_primary_key = 1 WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='discounts' AND t.type = 'U')
BEGIN
	 ALTER TABLE dbo.discounts ADD CONSTRAINT [pk_discounts]  PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE)
	 PRINT 'Created primary key pk_discounts on table dbo.discounts'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_discounts_identifier'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'discounts' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_discounts_identifier] ON [dbo].[discounts] ([identifier])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_discounts_identifier to dbo.discounts.'
END

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'discount_group_id'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'discounts' AND t.[type] = 'U')
    BEGIN

        ALTER TABLE dbo.discounts ADD discount_group_id BIGINT NULL

        PRINT 'Added column discount_group_id to dbo.discounts'
    END
GO

IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'description'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'discounts' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.discounts ALTER COLUMN [description] NVARCHAR(255)  NULL;
	PRINT 'modify column description null, dbo.discounts'
END

IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'name'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'discounts' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.discounts ALTER COLUMN [name] NVARCHAR(255)  NULL;
	PRINT 'modify column name null, dbo.discounts'
END

IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'amount_type'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'discounts' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.discounts ALTER COLUMN [amount_type] NVARCHAR(25) NULL;
	PRINT 'modify column amount_type null, dbo.discounts'
END

IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'amount'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'discounts' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.discounts ALTER COLUMN [amount] DECIMAL(19, 2) NOT NULL;
	PRINT 'modify column amount not null, dbo.discounts'
END

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'keyer_uuid'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'discounts' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.discounts ADD keyer_uuid UNIQUEIDENTIFIER NULL
	PRINT 'add column keyer_uuid, dbo.discounts'
END

IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'apply_to_recurring_billing'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'discounts' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.discounts ALTER COLUMN [apply_to_recurring_billing] BIT NOT NULL;
	PRINT 'modify column apply_to_recurring_billing not null, dbo.discounts'
END

IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'discount_id'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'discounts' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.discounts ALTER COLUMN [discount_id] BIGINT NULL;
	PRINT 'modify column discount_id null, dbo.discounts'
END

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','cart_id'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'carts table id',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'discounts',
 @level2type = 'Column',
 @level2name = 'cart_id'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','discount_type'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'ACTIVE_ADVANTAGE, COUPON, MULTI, AD_HOC, MEMBERSHIP',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'discounts',
 @level2type = 'Column',
 @level2name = 'discount_type'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','apply_to_recurring_billing'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'is apply to recurring billing',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'discounts',
 @level2type = 'Column',
 @level2name = 'apply_to_recurring_billing'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','discount_id'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'discounts table id',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'discounts',
 @level2type = 'Column',
 @level2name = 'discount_id'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','discount_id'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'PERCENT, FLAT, FIXED_AMOUNT',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'discounts',
 @level2type = 'Column',
 @level2name = 'amount_type'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','discount_id'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'AUTOMATIC, OVERRIDE, AUTOMATIC_OVERRIDE, AD_HOC,ORDER_LINE_LEVEL_OVERRIDE, CARRY_OVER',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'discounts',
 @level2type = 'Column',
 @level2name = 'origin'
END
GO
GO
--/KitManagerFileID=18111
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4738,18111,'discounts.sql',0
GO

--KitManagerFileID=18169
--FileName=ad_hoc_discounts.sql
--SubmittedBy=shan li (ACTIVE\tli4)

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='ad_hoc_discounts' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[ad_hoc_discounts] (
        [id]                          BIGINT              IDENTITY (1, 1) NOT NULL,
        [identifier]                  UNIQUEIDENTIFIER    NOT NULL,
        [cart_item_id]                BIGINT              NOT NULL,
        [discount_name]               NVARCHAR(500)       NULL,
        [discount_keyer_id]           UNIQUEIDENTIFIER    NULL,
        [discount_amount]             DECIMAL(19, 2)      NOT NULL,
        [discount_coupon_code]        NVARCHAR(255)       NULL,
        [discount_group_id]           BIGINT              NULL,
        [created_by]                  NVARCHAR(255)       NOT NULL,
        [created_dt]                  DATETIME            NOT NULL,
        [modified_by]                 NVARCHAR(255)       NOT NULL,
        [modified_dt]                 DATETIME            NOT NULL
        CONSTRAINT [pk_ad_hoc_discounts] PRIMARY KEY CLUSTERED ([id]) WITH (STATISTICS_NORECOMPUTE = ON)
    )
	 PRINT 'CREATE TABLE dbo.ad_hoc_discounts'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.indexes i ON t.object_id = i.object_id AND i.is_primary_key = 1 WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='ad_hoc_discounts' AND t.type = 'U')
BEGIN
	 ALTER TABLE dbo.ad_hoc_discounts ADD CONSTRAINT [pk_ad_hoc_discounts]  PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE)
	 PRINT 'Created primary key pk_ad_hoc_discounts on table dbo.ad_hoc_discounts'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_ad_hoc_discounts_identifier'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'ad_hoc_discounts' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_ad_hoc_discounts_identifier] ON [dbo].[ad_hoc_discounts] ([identifier])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_ad_hoc_discounts_identifier to dbo.ad_hoc_discounts.'
END
GO
-- Table description
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', NULL, 'DC1', 'Store ad-hoc discount information';
-- Column comment for id
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'id', 'DC1', 'primary key';
GO
-- Column comment for cart_item_id
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'cart_item_id', 'DC1', 'cart_items table id';
GO
-- Column comment for discount_name
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'discount_name', 'DC1', 'ad-hoc discount name';
GO
-- Column comment for discount_keyer_id
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'discount_keyer_id', 'DC1', 'ad-hoc discount keyer id';
GO
-- Column comment for discount_amount
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'discount_amount', 'DC1', 'ad-hoc discount amount';
GO
-- Column comment for discount_coupon_code
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'discount_coupon_code', 'DC1', 'ad-hoc discount coupon code';
GO
-- Column comment for discount_group_id
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'discount_group_id', 'DC1', 'ad-hoc discount group id';
GO
-- Column comment for created_by
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'created_by', 'DC1', 'created_by information';
GO
-- Column comment for created_dt
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'created_dt', 'DC1', 'created_dt information';
GO
-- Column comment for modified_by
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'modified_by', 'DC1', 'modified_by information';
GO
-- Column comment for modified_dt
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'modified_dt', 'DC1', 'modified_dt information';
GO
GO
--/KitManagerFileID=18169
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4738,18169,'ad_hoc_discounts.sql',0
GO

--/ 	 KitSection = Tables
-- 	 KitSection = ForeignKeys
--KitManagerFileID=18168
--FileName=ad_hoc_discount_fk.sql
--SubmittedBy=shan li (ACTIVE\tli4)

IF NOT EXISTS(
        SELECT TOP 1 *
        FROM sys.foreign_keys
        WHERE name = 'fk_ad_hoc_discount_cart_item_id'
          AND OBJECT_NAME(parent_object_id) = 'ad_hoc_discounts' AND OBJECT_NAME(referenced_object_id) = 'cart_items'
    )
    BEGIN
        ALTER TABLE [dbo].[ad_hoc_discounts] ADD CONSTRAINT [fk_ad_hoc_discount_cart_item_id]
            FOREIGN KEY ([cart_item_id]) REFERENCES [dbo].[cart_items]([id]) ON DELETE CASCADE
        PRINT 'Added "fk_ad_hoc_discount_cart_item_id" Foreign Key to ad_hoc_discounts table';
    END
GO
GO
--/KitManagerFileID=18168
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4738,18168,'ad_hoc_discount_fk.sql',0
GO

