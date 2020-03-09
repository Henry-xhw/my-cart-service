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
        [modified_dt]               DATETIME            NOT NULL,
        CONSTRAINT [pk_cart_item_cart_item_fees] PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE),
        CONSTRAINT [uq_cart_item_cart_item_fees_identifier] UNIQUE ([identifier]) WITH (DATA_COMPRESSION= PAGE)
    )
	 PRINT 'CREATE TABLE dbo.cart_item_cart_item_fees'
END
GO

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
    CREATE NONCLUSTERED INDEX [ix_cart_item_fee_id] ON [dbo].[cart_item_cart_item_fees] ([cart_item_fee_id])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_item_fee_id to dbo.cart_item_cart_item_fees.'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_cart_item_fees', NULL, NULL))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'The relationship table between cart_items and cart_item_fees tables',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'cart_item_cart_item_fees'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_cart_item_fees','column','id'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'id',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'cart_item_cart_item_fees',
             @level2type = 'Column',
             @level2name = 'id'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_cart_item_fees','column','identifier'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'identifier',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'cart_item_cart_item_fees',
             @level2type = 'Column',
             @level2name = 'identifier'
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

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_cart_item_fees','column','created_by'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'created by',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'cart_item_cart_item_fees',
             @level2type = 'Column',
             @level2name = 'created_by'
    END
GO


IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_cart_item_fees','column','created_dt'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'created date time',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'cart_item_cart_item_fees',
             @level2type = 'Column',
             @level2name = 'created_dt'
    END
GO


IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_cart_item_fees','column','modified_by'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'modified by',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'cart_item_cart_item_fees',
             @level2type = 'Column',
             @level2name = 'modified_by'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_cart_item_fees','column','modified_dt'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'modified date time',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'cart_item_cart_item_fees',
             @level2type = 'Column',
             @level2name = 'modified_dt'
    END
GO