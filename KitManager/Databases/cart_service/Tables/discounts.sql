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
        [apply_to_recurring_billing]        BIT                 CONSTRAINT df_discounts_apply_to_recurring_billing DEFAULT ((0)) NOT NULL,
        [discount_group_id]                 BIGINT              NULL,
        [origin]                            NVARCHAR(25)        NULL,
        [keyer_uuid]                        UNIQUEIDENTIFIER    NULL,
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

exec sp_add_table_column_comment 'dbo', 'discounts', NULL, 'DC2', 'discount is mapping to com.active.services.cart.domain.Discount';

exec sp_add_table_column_comment 'dbo', 'discounts', 'id', 'DC2', 'primary key';

exec sp_add_table_column_comment 'dbo', 'discounts', 'identifier', 'DC2', 'global unique id, represent a discount';

exec sp_add_table_column_comment 'dbo', 'discounts', 'cart_id', 'DC2', 'cart id';


exec sp_add_table_column_comment 'dbo', 'discounts', 'name', 'DC2', 'name';

exec sp_add_table_column_comment 'dbo', 'discounts', 'description', 'DC2', 'description';

exec sp_add_table_column_comment 'dbo', 'discounts', 'amount', 'DC2', 'amount';

exec sp_add_table_column_comment 'dbo', 'discounts', 'discount_type', 'DC2', 'discount type';

exec sp_add_table_column_comment 'dbo', 'discounts', 'coupon_code', 'DC2', 'coupon code';

exec sp_add_table_column_comment 'dbo', 'discounts', 'algorithm', 'DC2', 'algorithm';

exec sp_add_table_column_comment 'dbo', 'discounts', 'apply_to_recurring_billing', 'DC2', 'whether apply to recurring_billing';

exec sp_add_table_column_comment 'dbo', 'discounts', 'discount_group_id', 'DC2', 'discount group id';

exec sp_add_table_column_comment 'dbo', 'discounts', 'discount_id', 'DC2', 'discount id';

exec sp_add_table_column_comment 'dbo', 'discounts', 'amount_type', 'DC2', 'amount type';

exec sp_add_table_column_comment 'dbo', 'discounts', 'origin', 'DC2', 'origin';

exec sp_add_table_column_comment 'dbo', 'discounts', 'created_by', 'DC2', 'created by';

exec sp_add_table_column_comment 'dbo', 'discounts', 'created_dt', 'DC2', 'created date time';

exec sp_add_table_column_comment 'dbo', 'discounts', 'modified_by', 'DC2', 'modified by';

exec sp_add_table_column_comment 'dbo', 'discounts', 'modified_dt', 'DC2', 'modified date time';

