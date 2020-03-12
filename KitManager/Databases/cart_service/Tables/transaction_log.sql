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
