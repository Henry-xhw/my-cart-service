USE cart_service
GO

-- 	 KitSection = Tables
--KitManagerFileID=17804
--FileName=cart.sql
--SubmittedBy=shan li (ACTIVE\tli4)

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='cart' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[cart] (
        [id]                        BIGINT              IDENTITY (1, 1) NOT NULL,
        [identifier]                UNIQUEIDENTIFIER    NOT NULL,
        [owner_id]                  UNIQUEIDENTIFIER    NULL,
        [keyer_id]                  UNIQUEIDENTIFIER    NULL,
        [currency_code]             CHAR(3)             NOT NULL,
        [created_by]                NVARCHAR(255)       NOT NULL,
        [created_dt]                DATETIME            NOT NULL,
        [modified_by]               NVARCHAR(255)       NOT NULL,
        [modified_dt]               DATETIME            NOT NULL
    )
	 PRINT 'CREATE TABLE dbo.payments'
END
GO
IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.indexes i ON t.object_id = i.object_id AND i.is_primary_key = 1 WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='cart' AND t.type = 'U')
BEGIN
	 ALTER TABLE dbo.cart ADD CONSTRAINT [pk_cart]  PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE)
	 PRINT 'Created primary key pk_cart on table dbo.cart'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_identifier'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_identifier] ON [dbo].[cart] ([identifier])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_identifier to dbo.cart.'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_owner_id'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_owner_id] ON [dbo].[cart] ([owner_id])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_owner_id to dbo.cart.'
END
GO

GO
--/KitManagerFileID=17804
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4531,17804,'cart.sql',0
GO

--KitManagerFileID=17805
--FileName=cart_item.sql
--SubmittedBy=shan li (ACTIVE\tli4)

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='cart_item' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[cart_item] (
        [id]                        BIGINT              IDENTITY (1, 1) NOT NULL,
        [cart_id]                   BIGINT              NOT NULL,
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
        [modified_dt]               DATETIME            NOT NULL
    )
	 PRINT 'CREATE TABLE dbo.cart_item'
END
GO
IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.indexes i ON t.object_id = i.object_id AND i.is_primary_key = 1 WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='cart_item' AND t.type = 'U')
BEGIN
	 ALTER TABLE dbo.cart_item ADD CONSTRAINT [pk_cart_item]  PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE)
	 PRINT 'Created primary key pk_cart_item on table dbo.cart_item'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_item_identifier'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_item_identifier] ON [dbo].[cart_item] ([identifier])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_item_identifier to dbo.cart.'
END

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_item_cart_id'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_item' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_item_cart_id] ON [dbo].[cart_item] ([cart_id])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_item_cart_id to dbo.cart_item.'
END
GO

GO
--/KitManagerFileID=17805
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4531,17805,'cart_item.sql',0
GO

--/ 	 KitSection = Tables
-- 	 KitSection = ForeignKeys
--KitManagerFileID=17803
--FileName=cart_item_fk.sql
--SubmittedBy=shan li (ACTIVE\tli4)

IF NOT EXISTS(
    SELECT TOP 1 *
    FROM sys.foreign_keys
    WHERE name = 'fk_cart_item_cart_id'
          AND OBJECT_NAME(parent_object_id) = 'cart_item' AND OBJECT_NAME(referenced_object_id) = 'cart'
)
BEGIN
     ALTER TABLE [dbo].[cart_item] ADD CONSTRAINT [fk_cart_item_cart_id]
     FOREIGN KEY ([cart_id]) REFERENCES [dbo].[cart]([id]) ON DELETE CASCADE
     PRINT 'Added "fk_cart_item_cart_id" Foreign Key to cart_item table';
END
GO

GO
--/KitManagerFileID=17803
if exists(select top 1 1  from msdb.INFORMATION_SCHEMA.ROUTINES where routine_name='p_KitFileApplicationHistory_ins_Info')
exec msdb.dbo.p_KitFileApplicationHistory_ins_Info 4531,17803,'cart_item_fk.sql',0
GO

