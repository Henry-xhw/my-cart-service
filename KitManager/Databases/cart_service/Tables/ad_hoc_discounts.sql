IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='ad_hoc_discounts' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[ad_hoc_discounts] (
        [id]                          BIGINT              IDENTITY (1, 1) NOT NULL,
        [identifier]                  UNIQUEIDENTIFIER    NOT NULL,
        [cart_item_id]                BIGINT              NOT NULL,
        [adhoc_discount_name]         NVARCHAR(500)       NULL,
        [adhoc_discount_keyer_id]     UNIQUEIDENTIFIER    NULL,
        [adhoc_discount_amount]       DECIMAL(19, 2)      NOT NULL,
        [adhoc_discount_coupon_code]  NVARCHAR(255)       NULL,
        [adhoc_discount_group_id]     BIGINT              NULL,
        [created_by]                  NVARCHAR(255)       NOT NULL,
        [created_dt]                  DATETIME            NOT NULL,
        [modified_by]                 NVARCHAR(255)       NOT NULL,
        [modified_dt]                 DATETIME            NOT NULL
        CONSTRAINT [pk_ad_hoc_discounts] PRIMARY KEY CLUSTERED ([id]) WITH (STATISTICS_NORECOMPUTE = ON)
    )
	 PRINT 'CREATE TABLE dbo.ad_hoc_discounts'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.indexes i ON t.object_id = i.object_id AND i.is_primary_key = 1 WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='ad_hoc_discounts' AND t.type = 'U')
BEGIN
	 ALTER TABLE dbo.ad_hoc_discounts ADD CONSTRAINT [pk_ad_hoc_discounts]  PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE)
	 PRINT 'Created primary key pk_ad_hoc_discounts on table dbo.ad_hoc_discounts'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_ad_hoc_discounts_identifier'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'ad_hoc_discounts' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_ad_hoc_discounts_identifier] ON [dbo].[ad_hoc_discounts] ([identifier])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_ad_hoc_discounts_identifier to dbo.ad_hoc_discounts.'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'ad_hoc_discounts','column','cart_item_id'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'cart_items table id',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'ad_hoc_discounts',
 @level2type = 'Column',
 @level2name = 'cart_item_id'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'ad_hoc_discounts','column','adhoc_discount_name'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'ad-hoc discount name',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'ad_hoc_discounts',
 @level2type = 'Column',
 @level2name = 'adhoc_discount_name'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'ad_hoc_discounts','column','adhoc_discount_keyer_id'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'ad-hoc discount keyer id',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'ad_hoc_discounts',
 @level2type = 'Column',
 @level2name = 'adhoc_discount_keyer_id'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'ad_hoc_discounts','column','adhoc_discount_amount'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'ad-hoc discount amount',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'ad_hoc_discounts',
 @level2type = 'Column',
 @level2name = 'adhoc_discount_amount'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'ad_hoc_discounts','column','adhoc_discount_coupon_code'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'ad-hoc discount coupon code',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'ad_hoc_discounts',
 @level2type = 'Column',
 @level2name = 'adhoc_discount_coupon_code'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'ad_hoc_discounts','column','adhoc_discount_group_id'))
BEGIN
   EXEC sys.sp_addextendedproperty
 @name = N'MS_Description',
 @value = N'ad-hoc discount group id',
 @level0type = 'SCHEMA',
 @level0name = 'dbo',
 @level1type = 'TABLE',
 @level1name = 'ad_hoc_discounts',
 @level2type = 'Column',
 @level2name = 'adhoc_discount_group_id'
END
GO