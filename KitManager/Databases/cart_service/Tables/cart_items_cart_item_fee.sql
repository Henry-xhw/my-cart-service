IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='cart_item_cart_item_fees' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[cart_item_cart_item_fees] (
        [id]                        BIGINT              IDENTITY (1, 1) NOT NULL,
        [identifier]                UNIQUEIDENTIFIER    NOT NULL,
        [cart_item_id]              BIGINT              NOT NULL,
        [cart_item_fee_id]          BIGINT              NOT NULL,
        [created_by]                NVARCHAR(255)       NOT NULL,
        [created_dt]                DATETIME            NOT NULL,
        [modified_by]               NVARCHAR(255)       NOT NULL,
        [modified_dt]               DATETIME            NOT NULL
    )
	 PRINT 'CREATE TABLE dbo.cart_item_cart_item_fees'
END
GO
IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.indexes i ON t.object_id = i.object_id AND i.is_primary_key = 1 WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='cart_items_cart_item_fees' AND t.type = 'U')
BEGIN
	 ALTER TABLE dbo.cart_item_cart_item_fees ADD CONSTRAINT [pk_cart_item_cart_item_fee]  PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE)
	 PRINT 'Created primary key pk_cart_items_cart_item_fee on table dbo.cart_item_cart_item_fees'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_items_cart_item_fee_identifier'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_fees' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_items_cart_item_fee_identifier] ON [dbo].[cart_item_fees] ([identifier])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_items_cart_item_fee_identifier to dbo.cart_item_cart_item_fees.'
END

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_item_id'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_cart_item_fees' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_item_id] ON [dbo].[cart_item_cart_item_fees] ([cart_item_id])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_item_id to dbo.cart_item_cart_item_fees.'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_item_fee_id'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item_cart_item_fees' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_owner_id] ON [dbo].[cart_item_cart_item_fees] ([cart_item_fee_id])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_item_fee_id to dbo.cart_item_cart_item_fees.'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_cart_item_fees','column','cart_item_id'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'cart item id',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_item_cart_item_fees',
 @level2type = 'Column',
 @level2name = 'cart_item_id'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_cart_item_fees','column','cart_item_fee_id'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'cart item fee id',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'cart_item_cart_item_fees',
 @level2type = 'Column',
 @level2name = 'cart_item_fee_id'
END
GO
