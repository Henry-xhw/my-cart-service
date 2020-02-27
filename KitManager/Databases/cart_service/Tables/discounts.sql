IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='discounts' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[discounts] (
        [id]                                BIGINT              IDENTITY (1, 1) NOT NULL,
        [identifier]                        UNIQUEIDENTIFIER    NOT NULL,
        [cart_id]                           BIGINT              NOT NULL,
        [name]                              NVARCHAR(255)       NULL,
        [description]                       NVARCHAR(255)       NULL,
        [amount]                            DECIMAL(19, 2)      NULL,
        [amount_type]                       NVARCHAR(25)        NULL,
        [discount_id]                       BIGINT              NOT NULL,
        [discount_type]                     NVARCHAR(25)        NOT NULL,
        [coupon_code]                       NVARCHAR(255)       NULL,
        [algorithm]                         NVARCHAR(25)        NULL,
        [apply_to_recurring_billing]        BIT                 NULL,
        [discount_group_id]                 BIGINT              NULL,
        [origin]                            NVARCHAR(25)        NULL,
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
    ALTER TABLE dbo.discounts ALTER COLUMN [amount_type] NVARCHAR(25) NOT NULL;
	PRINT 'modify column amount_type not null, dbo.discounts'
END

IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'amount'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'discounts' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.discounts ALTER COLUMN [amount] DECIMAL(19, 2) NOT NULL;
	PRINT 'modify column amount not null, dbo.discounts'
END