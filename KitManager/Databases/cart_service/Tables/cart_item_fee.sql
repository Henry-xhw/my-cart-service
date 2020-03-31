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

exec sp_add_table_column_comment 'dbo', 'cart_item_fees', NULL, 'DC2', 'cart item fee is received according to some contracts, and is mapping to com.active.services.cart.domain.CartItemFee';

exec sp_add_table_column_comment 'dbo', 'cart_item_fees', 'id', 'DC2', 'primary key';

exec sp_add_table_column_comment 'dbo', 'cart_item_fees', 'identifier', 'DC2', 'global unique id, represent a cart item fee';

exec sp_add_table_column_comment 'dbo', 'cart_item_fees', 'parent_id', 'DC2', 'parent id';

exec sp_add_table_column_comment 'dbo', 'cart_item_fees', 'name', 'DC2', 'name';

exec sp_add_table_column_comment 'dbo', 'cart_item_fees', 'description', 'DC2', 'description';

exec sp_add_table_column_comment 'dbo', 'cart_item_fees', 'type', 'DC2', 'type';

exec sp_add_table_column_comment 'dbo', 'cart_item_fees', 'transaction_type', 'DC2', 'transaction type';

exec sp_add_table_column_comment 'dbo', 'cart_item_fees', 'units', 'DC2', 'units';

exec sp_add_table_column_comment 'dbo', 'cart_item_fees', 'unit_price', 'DC2', 'unit price';

exec sp_add_table_column_comment 'dbo', 'cart_item_fees', 'discount_identifier', 'DC2', 'discount identifier';

exec sp_add_table_column_comment 'dbo', 'cart_item_fees', 'surcharge_identifier', 'DC2', 'surcharge identifier';

exec sp_add_table_column_comment 'dbo', 'cart_item_fees', 'due_amount', 'DC2', 'due amount';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'created_by', 'DC2', 'created by';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'created_dt', 'DC2', 'created date time';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'modified_by', 'DC2', 'modified by';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'modified_dt', 'DC2', 'modified date time';

