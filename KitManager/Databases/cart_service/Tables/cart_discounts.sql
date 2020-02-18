IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='cart_discounts' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[cart_discounts] (
        [id]                                BIGINT              IDENTITY (1, 1) NOT NULL,
        [identifier]                        UNIQUEIDENTIFIER    NOT NULL,
        [cart_id]                           BIGINT              NOT NULL,
        [discount_type]                     NVARCHAR(25)        NOT NULL,
        [apply_to_recurring_billing]        BIT                 NULL,
        [discount_id]                       BIGINT              NULL,
        [keyer_uuid]                        UNIQUEIDENTIFIER    NULL,
        [discount_group_id]                 BIGINT              NULL,
        [amount_type]                       NVARCHAR(25)        NULL,
        [amount]                            DECIMAL(19, 2)      NULL,
        [origin]                            NVARCHAR(25)        NULL,
        [has_same_discount_id]              BIT                 NULL,
        [created_by]                        NVARCHAR(255)       NOT NULL,
        [created_dt]                        DATETIME            NOT NULL,
        [modified_by]                       NVARCHAR(255)       NOT NULL,
        [modified_dt]                       DATETIME            NOT NULL
    )
	 PRINT 'CREATE TABLE dbo.cart_discounts'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.indexes i ON t.object_id = i.object_id AND i.is_primary_key = 1 WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='cart_discounts' AND t.type = 'U')
BEGIN
	 ALTER TABLE dbo.cart_discounts ADD CONSTRAINT [pk_discounts]  PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE)
	 PRINT 'Created primary key pk_discounts on table dbo.discounts'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_discounts_identifier'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'cart_discounts' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_discounts_identifier] ON [dbo].[cart_discounts] ([identifier])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_discounts_identifier to dbo.discounts.'
END