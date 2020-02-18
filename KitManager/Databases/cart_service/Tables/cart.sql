IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='carts' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[carts] (
        [id]                        BIGINT              IDENTITY (1, 1) NOT NULL,
        [identifier]                UNIQUEIDENTIFIER    NOT NULL,
        [owner_id]                  UNIQUEIDENTIFIER    NULL,
        [keyer_id]                  UNIQUEIDENTIFIER    NULL,
        [currency_code]             CHAR(3)             NOT NULL,
        [created_by]                NVARCHAR(255)       NOT NULL,
        [created_dt]                DATETIME            NOT NULL,
        [modified_by]               NVARCHAR(255)       NOT NULL,
        [modified_dt]               DATETIME            NOT NULL,
        [version]                   INT                 DEFAULT ((0)) NOT NULL,
        [price_version]             INT                 DEFAULT ((0)) NOT NULL,
        [is_lock]                   BIT                 DEFAULT ((0)) NOT NULL,
        [cart_status]               VARCHAR (255)       NOT NULL,
        [reservation_id]            UNIQUEIDENTIFIER    NULL,
        [coupon_codes]              NVARCHAR(MAX)       NULL
    )
	 PRINT 'CREATE TABLE dbo.carts'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'cart_status'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.[type] = 'U')
BEGIN

	ALTER TABLE dbo.carts ADD cart_status NVARCHAR(255) NOT NULL

	PRINT 'Added column cart_status to dbo.carts'
END

GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'version'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.[type] = 'U')
BEGIN

	ALTER TABLE dbo.carts ADD version INT DEFAULT ((0)) NOT NULL

	PRINT 'Added column version to dbo.carts'
END

GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'price_version'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.[type] = 'U')
BEGIN

	ALTER TABLE dbo.carts ADD price_version INT DEFAULT ((0)) NOT NULL

	PRINT 'Added column price_version to dbo.carts'
END

GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'is_lock'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.[type] = 'U')
BEGIN

	ALTER TABLE dbo.carts ADD is_lock BIT DEFAULT ((0)) NOT NULL

	PRINT 'Added column lock to dbo.carts'
END

GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.indexes i ON t.object_id = i.object_id AND i.is_primary_key = 1 WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='carts' AND t.type = 'U')
BEGIN
	 ALTER TABLE dbo.carts ADD CONSTRAINT [pk_cart]  PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE)
	 PRINT 'Created primary key pk_cart on table dbo.carts'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_identifier'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_identifier] ON [dbo].[carts] ([identifier])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_identifier to dbo.carts.'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_cart_owner_id'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_cart_owner_id] ON [dbo].[carts] ([owner_id])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_cart_owner_id to dbo.carts.'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'reservation_id'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.[type] = 'U')
BEGIN

	ALTER TABLE dbo.carts ADD reservation_id UNIQUEIDENTIFIER NULL

	PRINT 'Added column reservation_id to dbo.carts'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.columns c WITH(NOLOCK) ON t.object_id = c.object_id AND c.name = 'coupon_codes'
WHERE SCHEMA_NAME(t.schema_id) LIKE 'dbo' AND OBJECT_NAME(t.object_id) = 'carts' AND t.[type] = 'U')
BEGIN

  ALTER TABLE dbo.carts ADD coupon_codes NVARCHAR(MAX) NULL

  PRINT 'Added column coupon_codes to dbo.carts'
END
GO