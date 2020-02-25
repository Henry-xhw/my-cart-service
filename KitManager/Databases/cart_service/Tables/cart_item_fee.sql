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
        [related_identifier]        BIGINT              NULL,
        [created_by]                NVARCHAR(255)       NOT NULL,
        [created_dt]                DATETIME            NOT NULL,
        [modified_by]               NVARCHAR(255)       NOT NULL,
        [modified_dt]               DATETIME            NOT NULL,
        CONSTRAINT [pk_cart_item_fee] PRIMARY KEY CLUSTERED ([id]) WITH (STATISTICS_NORECOMPUTE = ON)
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
IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees','column','related_identifier'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'discount identifier, surcharge identifier',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_item_fees',
 @level2type = 'Column',
 @level2name = 'related_identifier'
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
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'due_amount'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.cart_item_fees ADD [due_amount] DECIMAL(19, 2) NOT NULL DEFAULT 0;
	PRINT 'add column due_amount, dbo.cart_item_fees'
END

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees','column','due_amount'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'due amount',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_item_fees',
 @level2type = 'Column',
 @level2name = 'due_amount'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'related_identifier'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.cart_item_fees ADD related_identifier BIGINT NULL;
	PRINT 'add column cart_discount_id on dbo.cart_item_fees'
END
GO