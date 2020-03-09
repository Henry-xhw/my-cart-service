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
        CONSTRAINT [uq_carts_identifier] UNIQUE ([identifier]) WITH (DATA_COMPRESSION= PAGE),
        CONSTRAINT [uq_carts_owner_guid] UNIQUE ([owner_guid]) WITH (DATA_COMPRESSION= PAGE)
    )
	 PRINT 'CREATE TABLE dbo.carts'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts', NULL, NULL))
BEGIN
    EXEC sys.sp_addextendedproperty
         @name = N'MS_Description',
         @value = N'cart',
         @level0type = 'SCHEMA',
         @level0name = 'dbo',
         @level1type = 'TABLE',
         @level1name = 'carts'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','id'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'id',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'carts',
             @level2type = 'Column',
             @level2name = 'id'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','identifier'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'The identifier is a unique identification, and is a reference id for external service',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'carts',
             @level2type = 'Column',
             @level2name = 'identifier'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','owner_guid'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'owner guid, also called epid',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'carts',
             @level2type = 'Column',
             @level2name = 'owner_guid'
    END
GO


IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','keyer_guid'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'keyer guid, also called keyer epid',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'carts',
             @level2type = 'Column',
             @level2name = 'keyer_guid'
    END
GO


IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','currency_code'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'currency code',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'carts',
             @level2type = 'Column',
             @level2name = 'currency_code'
    END
GO


IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','created_by'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'created by',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'carts',
             @level2type = 'Column',
             @level2name = 'created_by'
    END
GO


IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','created_dt'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'created date time',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'carts',
             @level2type = 'Column',
             @level2name = 'created_dt'
    END
GO


IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','modified_by'))
BEGIN
  EXEC sys.sp_addextendedproperty
  @name = N'MS_Description',
  @value = N'modified by',
  @level0type = 'SCHEMA',
  @level0name = 'dbo',
  @level1type = 'TABLE',
  @level1name = 'carts',
  @level2type = 'Column',
  @level2name = 'modified_by'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','modified_dt'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'modified date time',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'carts',
             @level2type = 'Column',
             @level2name = 'modified_dt'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','version'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'version',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'carts',
             @level2type = 'Column',
             @level2name = 'version'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','price_version'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'price version',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'carts',
             @level2type = 'Column',
             @level2name = 'price_version'
    END
GO


IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','is_lock'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'lock or not lock',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'carts',
             @level2type = 'Column',
             @level2name = 'is_lock'
    END
GO


IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','cart_status'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'cart status, as CREATED or FINALIZED',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'carts',
             @level2type = 'Column',
             @level2name = 'cart_status'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','reservation_guid'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'reservation guid',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'carts',
             @level2type = 'Column',
             @level2name = 'reservation_guid'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','coupon_codes'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'coupon codes',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'carts',
             @level2type = 'Column',
             @level2name = 'coupon_codes'
    END

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','sales_channel'))
BEGIN
  EXEC sys.sp_addextendedproperty
  @name = N'MS_Description',
  @value = N'sales channel',
  @level0type = 'SCHEMA',
  @level0name = 'dbo',
  @level1type = 'TABLE',
  @level1name = 'carts',
  @level2type = 'Column',
  @level2name = 'sales_channel'
END

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'carts','column','reservation_group_id'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'reservation group id',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'carts',
             @level2type = 'Column',
             @level2name = 'reservation_group_id'
    END

GO