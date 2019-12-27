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
        [quantity]                  BIGINT              NOT NULL,
        [unit_price]                DECIMAL(19, 2)      NULL,
        [grouping_identifier]       NVARCHAR(255)       NULL,
        [created_by]                NVARCHAR(255)       NOT NULL,
        [created_dt]                DATETIME            NOT NULL,
        [modified_by]               NVARCHAR(255)       NOT NULL,
        [modified_dt]               DATETIME            NOT NULL,
        [reservation_id]            UNIQUEIDENTIFIER    NULL
    )
	 PRINT 'CREATE TABLE dbo.cart_items'
END
GO
/* OMS-10202 add column parent_id */
IF NOT EXISTS(SELECT 1
              FROM INFORMATION_SCHEMA.COLUMNS
              WHERE TABLE_NAME = 'cart_items'
                AND COLUMN_NAME = 'parent_id')
    BEGIN
        ALTER TABLE dbo.cart_items
            ADD parent_id BIGINT NULL;
        PRINT 'Added new column parent_id into table cart_items.';
    END
IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.indexes i ON t.object_id = i.object_id AND i.is_primary_key = 1 WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='cart_items' AND t.type = 'U')
BEGIN
	 ALTER TABLE dbo.cart_items ADD CONSTRAINT [pk_cart_item]  PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE)
	 PRINT 'Created primary key pk_cart_item on table dbo.cart_items'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_item_identifier'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_item_identifier] ON [dbo].[cart_items] ([identifier])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_item_identifier to dbo.cart.'
END

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_item_cart_id'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_item_cart_id] ON [dbo].[cart_items] ([cart_id])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_item_cart_id to dbo.cart_items.'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'fee_volume_index'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.[type] = 'U')
BEGIN

	ALTER TABLE dbo.cart_items ADD fee_volume_index BIGINT NULL

	PRINT 'Added column fee_volume_index to dbo.cart_items'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'reservation_id'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_items' AND t.[type] = 'U')
BEGIN

	ALTER TABLE dbo.cart_items ADD reservation_id UNIQUEIDENTIFIER NULL

	PRINT 'Added column reservation_id to dbo.cart_items'
END
GO