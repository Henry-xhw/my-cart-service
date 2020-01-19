IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='events' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[events] (
        [id]                        BIGINT              IDENTITY (1, 1) NOT NULL,
        [identifier]                NVARCHAR(255)       NOT NULL,
        [type]                      NVARCHAR(255)       NOT NULL,
        [payload]                   NVARCHAR(MAX)       NULL
    )
	 PRINT 'CREATE TABLE dbo.events'
END
GO

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM
        sys.tables t WITH(NOLOCK)
        JOIN sys.indexes i WITH(NOLOCK) ON t.object_id = i.object_id AND i.name = 'ix_event_identifier_type'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) = 'events' AND t.type = 'U')
BEGIN
    CREATE NONCLUSTERED INDEX [ix_event_identifier_type] ON [dbo].[events] ([identifier], [type])
    WITH (DATA_COMPRESSION= PAGE, ONLINE=ON, MAXDOP=0)
    PRINT 'Added index ix_event_identifier_type to dbo.events.'
END
GO
