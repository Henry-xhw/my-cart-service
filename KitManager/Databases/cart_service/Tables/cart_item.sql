IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='cart_items' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[cart_items] (
        [id]                        BIGINT              IDENTITY (1, 1) NOT NULL,
        [cart_id]                   BIGINT              NOT NULL,
        [fee_volume_index]          BIGINT              NULL,
        [identifier]                UNIQUEIDENTIFIER    NOT NULL,
        [product_id]                BIGINT              NOT NULL,
        [product_name]              NVARCHAR(255)       NULL,
        [product_description]       NVARCHAR(MAX)       NULL,
        [booking_start_dt]          DATETIME            NULL,
        [booking_end_dt]            DATETIME            NULL,
        [trimmed_booking_start_dt]  DATETIME            NULL,
        [trimmed_booking_end_dt]    DATETIME            NULL,
        [quantity]                  INT                 NOT NULL,
        [override_price]            DECIMAL(19, 2)      NULL,
        [gross_price]               DECIMAL(19, 2)      NULL,
        [net_price]                 DECIMAL(19, 2)      NULL,
        [grouping_identifier]       NVARCHAR(255)       NULL,
        [coupon_codes]              NVARCHAR(MAX)       NULL,
        [parent_id]                 BIGINT              NULL,
        [oversold]                  BIT                 CONSTRAINT df_cart_items_oversold DEFAULT ((0)) NOT NULL,
        [person_identifier]         NVARCHAR(50)        NULL,
        [coupon_mode]               NVARCHAR(255)       NULL,
        [ignore_multi_discounts]    BIT                 CONSTRAINT df_cart_items_ignore_multi_discounts DEFAULT ((0)) NOT NULL,
        [reservation_guid]          UNIQUEIDENTIFIER    NULL,
        [created_by]                NVARCHAR(255)       NOT NULL,
        [created_dt]                DATETIME            NOT NULL,
        [modified_by]               NVARCHAR(255)       NOT NULL,
        [modified_dt]               DATETIME            NOT NULL,
        CONSTRAINT [pk_cart_items] PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE),
        CONSTRAINT [uq_cart_items_identifier] UNIQUE ([identifier]) WITH (DATA_COMPRESSION= PAGE)
    )
	 PRINT 'CREATE TABLE dbo.cart_items'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_items_cart_id'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_items_cart_id] ON [dbo].[cart_items] ([cart_id])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_items_cart_id to dbo.cart_items.'
END
GO

IF NOT EXISTS(
        SELECT TOP 1 1
        FROM
            sys.tables t WITH(NOLOCK)
                JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_items_product_id'
        WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.type = 'U')
    BEGIN
        CREATE NONCLUSTERED INDEX [ix_cart_items_product_id] ON [dbo].[cart_items] ([product_id])
            WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
        PRINT 'Added index ix_cart_items_product_id to dbo.cart_items.'
    END
GO

IF NOT EXISTS(
        SELECT TOP 1 1
        FROM
            sys.tables t WITH(NOLOCK)
                JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_items_parent_id'
        WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.type = 'U')
    BEGIN
        CREATE NONCLUSTERED INDEX [ix_cart_items_parent_id] ON [dbo].[cart_items] ([parent_id])
            WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
        PRINT 'Added index ix_cart_items_parent_id to dbo.cart_items.'
    END
GO

exec sp_add_table_column_comment 'dbo', 'cart_items', NULL, 'DC2', 'cart item is a item for shopping behavior, and the table is mapping to com.active.services.cart.domain.CartItem';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'id', 'DC2', 'primary key';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'cart_id', 'DC2', 'cart id';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'fee_volume_index', 'DC2', 'fee volume index';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'identifier', 'DC2', 'global unique id, represent a cart item';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'product_id', 'DC2', 'product id';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'product_name', 'DC2', 'product name';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'product_description', 'DC2', 'product description';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'booking_start_dt', 'DC2', 'booking start date time';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'booking_end_dt', 'DC2', 'booking end date time';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'trimmed_booking_start_dt', 'DC2', 'trimmed booking start date time';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'trimmed_booking_end_dt', 'DC2', 'trimmed booking end date time';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'quantity', 'DC2', 'quantity';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'override_price', 'DC2', 'override price';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'gross_price', 'DC2', 'gross price';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'net_price', 'DC2', 'net price';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'grouping_identifier', 'DC2', 'grouping_identifier';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'coupon_codes', 'DC2', 'coupon codes';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'parent_id', 'DC2', 'parent id';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'oversold', 'DC2', 'oversold';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'person_identifier', 'DC2', 'person identifier';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'coupon_mode', 'DC2', 'coupon mode';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'ignore_multi_discounts', 'DC2', 'ignore multiple discounts';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'created_by', 'DC2', 'created by';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'created_dt', 'DC2', 'created date time';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'modified_by', 'DC2', 'modified by';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'modified_dt', 'DC2', 'modified date time';

exec sp_add_table_column_comment 'dbo', 'cart_items', 'reservation_guid', 'DC2', 'reservation guid';
