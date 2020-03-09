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
        [apply_to_recurring_billing]        BIT                 CONSTRAINT df_discounts_apply_to_recurring_billing DEFAULT ((0)) NOT NULL,
        [discount_group_id]                 BIGINT              NULL,
        [origin]                            NVARCHAR(25)        NULL,
        [created_by]                        NVARCHAR(255)       NOT NULL,
        [created_dt]                        DATETIME            NOT NULL,
        [modified_by]                       NVARCHAR(255)       NOT NULL,
        [modified_dt]                       DATETIME            NOT NULL,
        CONSTRAINT [pk_discounts] PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE),
        CONSTRAINT [uq_discounts_identifier] UNIQUE ([identifier]) WITH (DATA_COMPRESSION= PAGE)
    )
	 PRINT 'CREATE TABLE dbo.discounts'
END
GO


IF NOT EXISTS(
        SELECT TOP 1 1
        FROM
            sys.tables t WITH(NOLOCK)
                JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_discounts_cart_id'
        WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'discounts' AND t.type = 'U')
    BEGIN
        CREATE NONCLUSTERED INDEX [ix_discounts_cart_id] ON [dbo].[discounts] ([cart_id])
            WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
        PRINT 'Added index ix_discounts_cart_id to dbo.discounts.'
    END
GO


IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts', NULL, NULL))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'discount',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'discounts'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','id'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'id',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'discounts',
             @level2type = 'Column',
             @level2name = 'id'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','identifier'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'identifier',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'discounts',
             @level2type = 'Column',
             @level2name = 'identifier'
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

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','name'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'name',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'discounts',
             @level2type = 'Column',
             @level2name = 'name'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','description'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'description',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'discounts',
             @level2type = 'Column',
             @level2name = 'description'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','amount'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'amount',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'discounts',
             @level2type = 'Column',
             @level2name = 'amount'
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

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','coupon_code'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'coupon code',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'discounts',
             @level2type = 'Column',
             @level2name = 'coupon_code'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','algorithm'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'algorithm',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'discounts',
             @level2type = 'Column',
             @level2name = 'algorithm'
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

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','discount_group_id'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'discount group id',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'discounts',
             @level2type = 'Column',
             @level2name = 'discount_group_id'
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

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','amount_type'))
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

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','origin'))
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

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','created_by'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'created by',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'discounts',
             @level2type = 'Column',
             @level2name = 'created_by'
    END
GO


IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','created_dt'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'created date time',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'discounts',
             @level2type = 'Column',
             @level2name = 'created_dt'
    END
GO


IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','modified_by'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'modified by',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'discounts',
             @level2type = 'Column',
             @level2name = 'modified_by'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'discounts','column','modified_dt'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'modified date time',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'discounts',
             @level2type = 'Column',
             @level2name = 'modified_dt'
    END
GO