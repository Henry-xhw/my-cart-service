IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='events' AND type = 'U')
BEGIN
	 CREATE TABLE [dbo].[events] (
        [id]                        BIGINT              IDENTITY (1, 1) NOT NULL,
        [identifier]                NVARCHAR(255)       NOT NULL,
        [type]                      NVARCHAR(255)       NOT NULL,
        [payload]                   NVARCHAR(MAX)       NULL,
        [created_by]                NVARCHAR(255)       NOT NULL,
        [created_dt]                DATETIME            NOT NULL,
        [modified_by]               NVARCHAR(255)       NOT NULL,
        [modified_dt]               DATETIME            NOT NULL
        CONSTRAINT [pk_events] PRIMARY KEY CLUSTERED ([id]) WITH (STATISTICS_NORECOMPUTE = ON)
    )
	 PRINT 'CREATE TABLE dbo.events'
END
GO

IF NOT EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
JOIN sys.indexes i ON t.object_id = i.object_id AND i.is_primary_key = 1 WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='events' AND t.type = 'U')
BEGIN
	 ALTER TABLE dbo.events ADD CONSTRAINT [pk_events]  PRIMARY KEY CLUSTERED ([id]) WITH (DATA_COMPRESSION= PAGE)
	 PRINT 'Created primary key pk_events on table dbo.events'
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

IF NOT EXISTS( SELECT 1
                FROM information_schema.COLUMNS
                WHERE table_name = 'events'
                AND column_name = 'created_by'
                AND TABLE_SCHEMA = 'dbo')
BEGIN
    ALTER TABLE dbo.events ADD [created_by] NVARCHAR(255) NOT NULL DEFAULT 'SYSTEM'

    PRINT 'Add created_by to events'
END
GO


IF NOT EXISTS( SELECT 0
                FROM information_schema.COLUMNS
                WHERE table_name = 'events'
                AND column_name = 'modified_by'
                AND TABLE_SCHEMA = 'dbo')
BEGIN
    ALTER TABLE dbo.events ADD [modified_by] NVARCHAR(255) NOT NULL DEFAULT 'SYSTEM'

    PRINT 'Add modified_by to events'
END
GO


IF NOT EXISTS( SELECT 0
                FROM information_schema.COLUMNS
                WHERE table_name = 'events'
                AND column_name = 'created_dt'
                AND TABLE_SCHEMA = 'dbo')
BEGIN
    ALTER TABLE dbo.events ADD [created_dt] DATETIME NOT NULL DEFAULT GETUTCDATE()

    PRINT 'Add created_dt to events'
END
GO


IF NOT EXISTS( SELECT 0
                FROM information_schema.COLUMNS
                WHERE table_name = 'events'
                AND column_name = 'modified_dt'
                AND TABLE_SCHEMA = 'dbo')
BEGIN
    ALTER TABLE dbo.events ADD [modified_dt] DATETIME NOT NULL DEFAULT GETUTCDATE()

    PRINT 'Add modified_dt to events'
END
GO

