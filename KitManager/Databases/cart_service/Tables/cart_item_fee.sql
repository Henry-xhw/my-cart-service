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
        [units]                     INT                 NOT NULL,
        [unit_price]                DECIMAL(19, 2)      NOT NULL,
        [discount_identifier]       UNIQUEIDENTIFIER    NULL,
        [surcharge_identifier]      UNIQUEIDENTIFIER    NULL,
        [due_amount]                DECIMAL(19, 2)      NULL,
        [created_by]                NVARCHAR(255)       NOT NULL,
        [created_dt]                DATETIME            NOT NULL,
        [modified_by]               NVARCHAR(255)       NOT NULL,
        [modified_dt]               DATETIME            NOT NULL,
        CONSTRAINT [pk_cart_item_fees] PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE),
        CONSTRAINT [uq_cart_item_fees_identifier] UNIQUE ([identifier]) WITH (DATA_COMPRESSION= PAGE)
    )
	 PRINT 'CREATE TABLE dbo.cart_item_fees'
END
GO

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

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees', NULL, NULL))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'cart item fee',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'cart_item_fees'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees','column','id'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'id',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'cart_item_fees',
             @level2type = 'Column',
             @level2name = 'id'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees','column','identifier'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'identifier',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'cart_item_fees',
             @level2type = 'Column',
             @level2name = 'identifier'
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
             @value = N'name',
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
             @value = N'type',
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
             @value = N'transaction_type',
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

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees','column','discount_identifier'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'discount identifier',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'cart_item_fees',
             @level2type = 'Column',
             @level2name = 'discount_identifier'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees','column','surcharge_identifier'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'surcharge identifier',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'cart_item_fees',
             @level2type = 'Column',
             @level2name = 'surcharge_identifier'
    END
GO


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

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees','column','created_by'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'created by',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'cart_item_fees',
             @level2type = 'Column',
             @level2name = 'created_by'
    END
GO


IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees','column','created_dt'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'created date time',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'cart_item_fees',
             @level2type = 'Column',
             @level2name = 'created_dt'
    END
GO


IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees','column','modified_by'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'modified by',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'cart_item_fees',
             @level2type = 'Column',
             @level2name = 'modified_by'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'cart_item_fees','column','modified_dt'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'modified date time',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'cart_item_fees',
             @level2type = 'Column',
             @level2name = 'modified_dt'
    END
GO
