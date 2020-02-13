IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='cart_item_fees' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[cart_item_fees] (
        [id]                                BIGINT              IDENTITY (1, 1) NOT NULL,
        [identifier]                        UNIQUEIDENTIFIER    NOT NULL,
        [parent_id]                         BIGINT              NULL,
        [name]                              NVARCHAR(255)       NULL,
        [description]                       NVARCHAR(255)       NULL,
        [type]                              NVARCHAR(25)        NOT NULL,
        [transaction_type]                  NVARCHAR(25)        NOT NULL,
        [units]                             BIGINT              NOT NULL,
        [unit_price]                        DECIMAL(19, 2)      NOT NULL,
        [discount_type]                     NVARCHAR(25)        NULL,
        [is_apply_to_becurring_billing]     BIT                 NULL,
        [discount_id]                       BIGINT              NULL,
        [keyer_uuid]                        UNIQUEIDENTIFIER    NULL,
        [discount_group_id]                 BIGINT              NULL,
        [amount_type]                       NVARCHAR(25)        NULL,
        [amount]                            DECIMAL(19, 2)      NULL,
        [origin]                            NVARCHAR(25)        NULL,
        [has_same_discount_id]              BIT                 NULL,
        [is_cancelled]                      BIT                 NULL,
        [created_by]                        NVARCHAR(255)       NOT NULL,
        [created_dt]                        DATETIME            NOT NULL,
        [modified_by]                       NVARCHAR(255)       NOT NULL,
        [modified_dt]                       DATETIME            NOT NULL
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
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'discount_type'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.cart_item_fees ADD discount_type NVARCHAR(25) NULL;
	PRINT 'add column discount_type on dbo.cart_item_fees'
END

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'is_apply_to_becurring_billing'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.cart_item_fees ADD is_apply_to_becurring_billing BIT NULL;
	PRINT 'add column is_apply_to_becurring_billing on dbo.cart_item_fees'
END

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'discount_id'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.cart_item_fees ADD discount_id BIGINT NULL;
	PRINT 'add column discount_id on dbo.cart_item_fees'
END

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'keyer_uuid'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.cart_item_fees ADD keyer_uuid UNIQUEIDENTIFIER NULL;
	PRINT 'add column keyer_uuid on dbo.cart_item_fees'
END

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'discount_group_id'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.cart_item_fees ADD discount_group_id BIGINT NULL;
	PRINT 'add column discount_group_id on dbo.cart_item_fees'
END

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'amount_type'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.cart_item_fees ADD amount_type NVARCHAR(25) NULL;
	PRINT 'add column amount_type on dbo.cart_item_fees'
END

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'amount'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.cart_item_fees ADD amount DECIMAL(19, 2) NULL;
	PRINT 'add column amount on dbo.cart_item_fees'
END

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'origin'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.cart_item_fees ADD origin NVARCHAR(25) NULL;
	PRINT 'add column origin on dbo.cart_item_fees'
END

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'has_same_discount_id'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.cart_item_fees ADD has_same_discount_id BIT NULL;
	PRINT 'add column has_same_discount_id on dbo.cart_item_fees'
END

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'is_cancelled'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.[type] = 'U')
BEGIN
    ALTER TABLE dbo.cart_item_fees ADD is_cancelled BIT NULL;
	PRINT 'add column is_cancelled on dbo.cart_item_fees'
END