IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='carts' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[carts] (
        [id]                        BIGINT              IDENTITY (1, 1) NOT NULL,
        [identifier]                UNIQUEIDENTIFIER    NOT NULL,
        [owner_guid]                UNIQUEIDENTIFIER    NULL,
        [keyer_guid]                UNIQUEIDENTIFIER    NULL,
        [currency_code]             CHAR(3)             NOT NULL,
        [created_by]                NVARCHAR(255)       NOT NULL,
        [created_dt]                DATETIME            NOT NULL,
        [modified_by]               NVARCHAR(255)       NOT NULL,
        [modified_dt]               DATETIME            NOT NULL,
        [version]                   INT                 CONSTRAINT df_carts_version DEFAULT ((0)) NOT NULL,
        [price_version]             INT                 CONSTRAINT df_carts_price_version DEFAULT ((0)) NOT NULL,
        [is_lock]                   BIT                 CONSTRAINT df_carts_is_lock DEFAULT ((0)) NOT NULL,
        [cart_status]               VARCHAR (255)       NOT NULL,
        [reservation_guid]          UNIQUEIDENTIFIER    NULL,
        [coupon_codes]              NVARCHAR(MAX)       NULL,
        [reservation_group_id]      UNIQUEIDENTIFIER    NULL,
        [sales_channel]             VARCHAR (255)       NULL,
        CONSTRAINT [pk_carts] PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE),
        CONSTRAINT [uq_carts_identifier] UNIQUE ([identifier]) WITH (DATA_COMPRESSION= PAGE)
    )
	 PRINT 'CREATE TABLE dbo.carts'
END
GO

IF NOT EXISTS(
        SELECT TOP 1 1
        FROM
            sys.tables t WITH(NOLOCK)
                JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_owner_guid'
        WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.type = 'U')
    BEGIN
        CREATE NONCLUSTERED INDEX [ix_cart_owner_guid] ON [dbo].[carts] ([owner_guid])
            WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
        PRINT 'Added index ix_cart_owner_guid to dbo.carts.'
    END
GO

exec sp_add_table_column_comment 'dbo', 'carts', NULL, 'DC2', 'cart is a shopping behavior, the table is mapping to com.active.services.cart.domain.Cart';

exec sp_add_table_column_comment 'dbo', 'carts', 'id', 'DC2', 'primary key';

exec sp_add_table_column_comment 'dbo', 'carts', 'identifier', 'DC2', 'global unique id, represent a cart';

exec sp_add_table_column_comment 'dbo', 'carts', 'owner_guid', 'DC2', 'owner guid, also called epid';

exec sp_add_table_column_comment 'dbo', 'carts', 'keyer_guid', 'DC2', 'keyer guid, also called keyer epid';

exec sp_add_table_column_comment 'dbo', 'carts', 'currency_code', 'DC2', 'currency code';

exec sp_add_table_column_comment 'dbo', 'carts', 'created_by', 'DC2', 'created by';

exec sp_add_table_column_comment 'dbo', 'carts', 'created_dt', 'DC2', 'created date time';

exec sp_add_table_column_comment 'dbo', 'carts', 'modified_by', 'DC2', 'modified by';

exec sp_add_table_column_comment 'dbo', 'carts', 'modified_dt', 'DC2', 'modified date time';

exec sp_add_table_column_comment 'dbo', 'carts', 'version', 'DC2', 'version';

exec sp_add_table_column_comment 'dbo', 'carts', 'price_version', 'DC2', 'price version';

exec sp_add_table_column_comment 'dbo', 'carts', 'is_lock', 'DC2', 'lock or not lock';

exec sp_add_table_column_comment 'dbo', 'carts', 'cart_status', 'DC2', 'cart status, as CREATED or FINALIZED';

exec sp_add_table_column_comment 'dbo', 'carts', 'reservation_guid', 'DC2', 'reservation guid';

exec sp_add_table_column_comment 'dbo', 'carts', 'coupon_codes', 'DC2', 'coupon codes';

exec sp_add_table_column_comment 'dbo', 'carts', 'sales_channel', 'DC2', 'sales channel';

exec sp_add_table_column_comment 'dbo', 'carts', 'reservation_group_id', 'DC2', 'reservation group id';
