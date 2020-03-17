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
        CONSTRAINT [uq_cart_item_cart_item_fees_identifier] UNIQUE ([identifier]) WITH (DATA_COMPRESSION= PAGE),
        CONSTRAINT [uq_cart_item_id_cart_item_fee_id] UNIQUE ([cart_item_id], [cart_item_fee_id]) WITH (DATA_COMPRESSION= PAGE)
    )
	 PRINT 'CREATE TABLE dbo.cart_item_cart_item_fees'
END
GO

exec sp_add_table_column_comment 'dbo', 'cart_item_cart_item_fees', NULL, 'DC2', 'The relationship table between cart_items and cart_item_fees tables';

exec sp_add_table_column_comment 'dbo', 'cart_item_cart_item_fees', 'id', 'DC2', 'primary key';

exec sp_add_table_column_comment 'dbo', 'cart_item_cart_item_fees', 'identifier', 'DC2', 'global unique id, represent a relationship between cart_item and cart_item_fees';

exec sp_add_table_column_comment 'dbo', 'cart_item_cart_item_fees', 'cart_item_id', 'DC2', 'cart item id';

exec sp_add_table_column_comment 'dbo', 'cart_item_cart_item_fees', 'cart_item_fee_id', 'DC2', 'cart item fee id';

exec sp_add_table_column_comment 'dbo', 'cart_item_cart_item_fees', 'created_by', 'DC2', 'created by';

exec sp_add_table_column_comment 'dbo', 'cart_item_cart_item_fees', 'created_dt', 'DC2', 'created date time';

exec sp_add_table_column_comment 'dbo', 'cart_item_cart_item_fees', 'modified_by', 'DC2', 'modified by';

exec sp_add_table_column_comment 'dbo', 'cart_item_cart_item_fees', 'modified_dt', 'DC2', 'modified date time';