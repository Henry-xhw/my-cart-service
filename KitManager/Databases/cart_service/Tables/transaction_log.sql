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
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_event_identifier_type'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'transaction_log' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_event_identifier_type] ON [dbo].[transaction_log] ([identifier], [type])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_event_identifier_type to dbo.transaction_log.'
END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'transaction_log', NULL, NULL))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'event',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'transaction_log'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'transaction_log','column','id'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'id',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'transaction_log',
             @level2type = 'Column',
             @level2name = 'id'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'transaction_log','column','identifier'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'identifier',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'transaction_log',
             @level2type = 'Column',
             @level2name = 'identifier'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'transaction_log','column','type'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'type',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'transaction_log',
             @level2type = 'Column',
             @level2name = 'type'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'transaction_log','column','payload'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'payload',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'transaction_log',
             @level2type = 'Column',
             @level2name = 'payload'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'transaction_log','column','created_by'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'created by',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'transaction_log',
             @level2type = 'Column',
             @level2name = 'created_by'
    END
GO


IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'transaction_log','column','created_dt'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'created date time',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'transaction_log',
             @level2type = 'Column',
             @level2name = 'created_dt'
    END
GO


IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'transaction_log','column','modified_by'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'modified by',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'transaction_log',
             @level2type = 'Column',
             @level2name = 'modified_by'
    END
GO

IF NOT EXISTS (SELECT name FROM :: fn_listextendedproperty (NULL, 'schema', 'dbo', 'table', 'transaction_log','column','modified_dt'))
    BEGIN
        EXEC sys.sp_addextendedproperty
             @name = N'MS_Description',
             @value = N'modified date time',
             @level0type = 'SCHEMA',
             @level0name = 'dbo',
             @level1type = 'TABLE',
             @level1name = 'transaction_log',
             @level2type = 'Column',
             @level2name = 'modified_dt'
    END
GO