USE cart_service
GO

-- 	 KitSection = Tables
--KitManagerFileID=18157
--FileName=drop_tables.sql
--SubmittedBy=  (ACTIVE\hxu)

IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES
           WHERE TABLE_NAME = 'events' and TABLE_TYPE = 'BASE TABLE' and TABLE_SCHEMA = 'dbo')
    BEGIN
        DROP TABLE events
        PRINT 'table events is dropped.'
    END

GO

IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES
           WHERE TABLE_NAME = 'transaction_log' and TABLE_TYPE = 'BASE TABLE' and TABLE_SCHEMA = 'dbo')
    BEGIN
        DROP TABLE transaction_log
        PRINT 'table transaction_log is dropped.'
    END

GO

IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES
           WHERE TABLE_NAME = 'discounts' and TABLE_TYPE = 'BASE TABLE' and TABLE_SCHEMA = 'dbo')
    BEGIN
        DROP TABLE discounts
        PRINT 'table discounts is dropped.'
    END

GO

IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES
           WHERE TABLE_NAME = 'cart_item_cart_item_fees' and TABLE_TYPE = 'BASE TABLE' and TABLE_SCHEMA = 'dbo')
    BEGIN
        DROP TABLE cart_item_cart_item_fees
        PRINT 'table cart_item_cart_item_fees is dropped.'
    END

GO

IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES
           WHERE TABLE_NAME = 'cart_items' and TABLE_TYPE = 'BASE TABLE' and TABLE_SCHEMA = 'dbo')
    BEGIN
        DROP TABLE cart_items
        PRINT 'table cart_items is dropped.'
    END

GO

IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES
           WHERE TABLE_NAME = 'cart_item_fees' and TABLE_TYPE = 'BASE TABLE' and TABLE_SCHEMA = 'dbo')
    BEGIN
        DROP TABLE cart_item_fees
        PRINT 'table cart_item_fees is dropped.'
    END

GO



IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES
           WHERE TABLE_NAME = 'carts' and TABLE_TYPE = 'BASE TABLE' and TABLE_SCHEMA = 'dbo')
    BEGIN
        DROP TABLE carts
        PRINT 'table carts is dropped.'
    END

GO





GO
--/KitManagerFileID=18157
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4738,18157,'drop_tables.sql',0
GO

--KitManagerFileID=17805
--FileName=cart_item.sql
--SubmittedBy=  (ACTIVE\hxu)

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

GO
--/KitManagerFileID=17805
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4738,17805,'cart_item.sql',0
GO

--KitManagerFileID=17804
--FileName=cart.sql
--SubmittedBy=  (ACTIVE\hxu)

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
        [coupon_codes]              NVARCHAR(MAX)       NULL,
        [reservation_group_guid]    UNIQUEIDENTIFIER    NULL,
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

exec sp_add_table_column_comment 'dbo', 'carts', 'coupon_codes', 'DC2', 'coupon codes';

exec sp_add_table_column_comment 'dbo', 'carts', 'sales_channel', 'DC2', 'sales channel';

exec sp_add_table_column_comment 'dbo', 'carts', 'reservation_group_guid', 'DC2', 'reservation group guid';

GO
--/KitManagerFileID=17804
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4738,17804,'cart.sql',0
GO

--KitManagerFileID=17876
--FileName=cart_item_fee.sql
--SubmittedBy=  (ACTIVE\hxu)

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


GO
--/KitManagerFileID=17876
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4738,17876,'cart_item_fee.sql',0
GO

--KitManagerFileID=17877
--FileName=cart_items_cart_item_fee.sql
--SubmittedBy=  (ACTIVE\hxu)

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
GO
--/KitManagerFileID=17877
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4738,17877,'cart_items_cart_item_fee.sql',0
GO

--KitManagerFileID=18111
--FileName=discounts.sql
--SubmittedBy=  (ACTIVE\hxu)

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


GO
--/KitManagerFileID=18111
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4738,18111,'discounts.sql',0
GO

--KitManagerFileID=18156
--FileName=transaction_log.sql
--SubmittedBy=  (ACTIVE\hxu)

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='transaction_log' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[transaction_log] (
        [id]                        BIGINT              IDENTITY (1, 1) NOT NULL,
        [identifier]                NVARCHAR(255)       NOT NULL,
        [type]                      NVARCHAR(255)       NOT NULL,
        [payload]                   NVARCHAR(MAX)       NULL,
        [created_by]                NVARCHAR(255)       CONSTRAINT df_transaction_log_created_by DEFAULT (('SYSTEM')) NOT NULL,
        [created_dt]                DATETIME            CONSTRAINT df_transaction_log_created_dt DEFAULT ((GETUTCDATE())) NOT NULL,
        [modified_by]               NVARCHAR(255)       CONSTRAINT df_transaction_log_modified_by DEFAULT (('SYSTEM')) NOT NULL,
        [modified_dt]               DATETIME            CONSTRAINT df_transaction_log_modified_dt DEFAULT ((GETUTCDATE())) NOT NULL
        CONSTRAINT [pk_transaction_log] PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE)
    )
	 PRINT 'CREATE TABLE dbo.transaction_log'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_transaction_log_identifier_type'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'transaction_log' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_transaction_log_identifier_type] ON [dbo].[transaction_log] ([identifier], [type])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_transaction_log_identifier_type to dbo.transaction_log.'
END
GO

exec sp_add_table_column_comment 'dbo', 'transaction_log', NULL, 'DC2', 'transaction log is used to track the history, the table is mapping to com.active.services.cart.common.Event';

exec sp_add_table_column_comment 'dbo', 'transaction_log', 'id', 'DC2', 'primary key';

exec sp_add_table_column_comment 'dbo', 'transaction_log', 'identifier', 'DC2', 'global unique id, represent a log';

exec sp_add_table_column_comment 'dbo', 'transaction_log', 'type', 'DC2', 'type';

exec sp_add_table_column_comment 'dbo', 'transaction_log', 'payload', 'DC2', 'payload';

exec sp_add_table_column_comment 'dbo', 'transaction_log', 'created_by', 'DC2', 'created by';

exec sp_add_table_column_comment 'dbo', 'transaction_log', 'created_dt', 'DC2', 'created date time';

exec sp_add_table_column_comment 'dbo', 'transaction_log', 'modified_by', 'DC2', 'modified by';

exec sp_add_table_column_comment 'dbo', 'transaction_log', 'modified_dt', 'DC2', 'modified date time';

GO
--/KitManagerFileID=18156
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4738,18156,'transaction_log.sql',0
GO

--KitManagerFileID=18169
--FileName=ad_hoc_discounts.sql
--SubmittedBy=  (ACTIVE\hxu)

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='ad_hoc_discounts' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[ad_hoc_discounts] (
        [id]                          BIGINT              IDENTITY (1, 1) NOT NULL,
        [identifier]                  UNIQUEIDENTIFIER    NOT NULL,
        [cart_item_id]                BIGINT              NOT NULL,
        [discount_name]               NVARCHAR(500)       NULL,
        [discount_keyer_id]           UNIQUEIDENTIFIER    NULL,
        [discount_amount]             DECIMAL(19, 2)      NOT NULL,
        [discount_coupon_code]        NVARCHAR(255)       NULL,
        [discount_group_id]           BIGINT              NULL,
        [created_by]                  NVARCHAR(255)       NOT NULL,
        [created_dt]                  DATETIME            NOT NULL,
        [modified_by]                 NVARCHAR(255)       NOT NULL,
        [modified_dt]                 DATETIME            NOT NULL
        CONSTRAINT [pk_ad_hoc_discounts] PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE)
        CONSTRAINT [uq_ad_hoc_discounts_identifier] UNIQUE ([identifier]) WITH (DATA_COMPRESSION= PAGE)
    )
	 PRINT 'CREATE TABLE dbo.ad_hoc_discounts'
END
GO
-- Table description
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', NULL, 'DC1', 'Store ad-hoc discount information';
-- Column comment for id
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'id', 'DC1', 'primary key';
GO
-- Column comment for cart_item_id
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'cart_item_id', 'DC1', 'cart_items table id';
GO
-- Column comment for discount_name
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'discount_name', 'DC1', 'ad-hoc discount name';
GO
-- Column comment for discount_keyer_id
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'discount_keyer_id', 'DC1', 'ad-hoc discount keyer id';
GO
-- Column comment for discount_amount
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'discount_amount', 'DC1', 'ad-hoc discount amount';
GO
-- Column comment for discount_coupon_code
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'discount_coupon_code', 'DC1', 'ad-hoc discount coupon code';
GO
-- Column comment for discount_group_id
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'discount_group_id', 'DC1', 'ad-hoc discount group id';
GO
-- Column comment for created_by
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'created_by', 'DC1', 'created_by information';
GO
-- Column comment for created_dt
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'created_dt', 'DC1', 'created_dt information';
GO
-- Column comment for modified_by
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'modified_by', 'DC1', 'modified_by information';
GO
-- Column comment for modified_dt
exec sp_add_table_column_comment 'dbo', 'ad_hoc_discounts', 'modified_dt', 'DC1', 'modified_dt information';
GO
GO
--/KitManagerFileID=18169
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4738,18169,'ad_hoc_discounts.sql',0
GO

--/ 	 KitSection = Tables
-- 	 KitSection = ForeignKeys
--KitManagerFileID=18168
--FileName=ad_hoc_discount_fk.sql
--SubmittedBy=  (ACTIVE\hxu)

IF NOT EXISTS(
        SELECT TOP 1 *
        FROM sys.foreign_keys
        WHERE name = 'fk_ad_hoc_discount_cart_item_id'
          AND OBJECT_NAME(parent_object_id) = 'ad_hoc_discounts' AND OBJECT_NAME(referenced_object_id) = 'cart_items'
    )
    BEGIN
        ALTER TABLE [dbo].[ad_hoc_discounts] ADD CONSTRAINT [fk_ad_hoc_discount_cart_item_id]
            FOREIGN KEY ([cart_item_id]) REFERENCES [dbo].[cart_items]([id]) ON DELETE CASCADE
        PRINT 'Added "fk_ad_hoc_discount_cart_item_id" Foreign Key to ad_hoc_discounts table';
    END
GO
GO
--/KitManagerFileID=18168
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4738,18168,'ad_hoc_discount_fk.sql',0
GO

--KitManagerFileID=17803
--FileName=cart_item_fk.sql
--SubmittedBy=  (ACTIVE\hxu)

IF NOT EXISTS(
    SELECT TOP 1 *
    FROM sys.foreign_keys
    WHERE name = 'fk_cart_item_cart_id'
          AND OBJECT_NAME(parent_object_id) = 'cart_items' AND OBJECT_NAME(referenced_object_id) = 'carts'
)
BEGIN
     ALTER TABLE [dbo].[cart_items] ADD CONSTRAINT [fk_cart_item_cart_id]
     FOREIGN KEY ([cart_id]) REFERENCES [dbo].[carts]([id]) ON DELETE CASCADE
     PRINT 'Added "fk_cart_item_cart_id" Foreign Key to cart_items table';
END
GO

GO
--/KitManagerFileID=17803
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4738,17803,'cart_item_fk.sql',0
GO

--KitManagerFileID=18110
--FileName=discount_fk.sql
--SubmittedBy=  (ACTIVE\hxu)

IF NOT EXISTS(
    SELECT TOP 1 *
    FROM sys.foreign_keys
    WHERE name = 'fk_cart_discount_cart_id'
          AND OBJECT_NAME(parent_object_id) = 'discounts' AND OBJECT_NAME(referenced_object_id) = 'carts'
)
BEGIN
     ALTER TABLE [dbo].[discounts] ADD CONSTRAINT [fk_cart_discount_cart_id]
     FOREIGN KEY ([cart_id]) REFERENCES [dbo].[carts]([id]) ON DELETE CASCADE
     PRINT 'Added "fk_cart_discount_cart_id" Foreign Key to discounts table';
END
GO

GO
--/KitManagerFileID=18110
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4738,18110,'discount_fk.sql',0
GO

--KitManagerFileID=17875
--FileName=fk_cart_items_cart_item_fee.sql
--SubmittedBy=  (ACTIVE\hxu)

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM sys.foreign_key_columns fk WITH(NOLOCK)
        JOIN sys.tables t WITH(NOLOCK) ON fk.parent_object_id = t.object_id 
        JOIN sys.columns c WITH(NOLOCK) ON fk.parent_object_id = c.object_id AND fk.parent_column_id = c.column_id
        JOIN sys.tables t2 WITH(NOLOCK) ON fk.referenced_object_id = t2.object_id
             AND SCHEMA_NAME(t2.schema_id) ='dbo' AND OBJECT_NAME(t2.object_id) ='cart_items' AND t2.[type] = 'U'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='cart_item_cart_item_fees' AND t.[type] = 'U')
BEGIN
    ALTER TABLE [dbo].[cart_item_cart_item_fees] ADD CONSTRAINT [fk_cart_item_id]
        FOREIGN KEY ([cart_item_id]) REFERENCES [dbo].[cart_items]([id]) ON DELETE CASCADE
END

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM sys.foreign_key_columns fk WITH(NOLOCK)
        JOIN sys.tables t WITH(NOLOCK) ON fk.parent_object_id = t.object_id 
        JOIN sys.columns c WITH(NOLOCK) ON fk.parent_object_id = c.object_id AND fk.parent_column_id = c.column_id
        JOIN sys.tables t2 WITH(NOLOCK) ON fk.referenced_object_id = t2.object_id
             AND SCHEMA_NAME(t2.schema_id) ='dbo' AND OBJECT_NAME(t2.object_id) ='cart_item_fees' AND t2.[type] = 'U'
WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='cart_item_cart_item_fees' AND t.[type] = 'U')
BEGIN
     ALTER TABLE [dbo].[cart_item_cart_item_fees] ADD CONSTRAINT [fk_cart_item_fee_id]
         FOREIGN KEY ([cart_item_fee_id]) REFERENCES [dbo].[cart_item_fees]([id]) ON DELETE CASCADE
END


GO
--/KitManagerFileID=17875
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4738,17875,'fk_cart_items_cart_item_fee.sql',0
GO

