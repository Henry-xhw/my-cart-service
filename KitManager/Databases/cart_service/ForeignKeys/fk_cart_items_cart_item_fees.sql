IF NOT EXISTS(
    SELECT TOP 1 1
    FROM sys.foreign_key_columns fk WITH(NOLOCK)
        JOIN sys.tables t WITH(NOLOCK) ON fk.parent_object_id = t.object_id 
        JOIN sys.columns c WITH(NOLOCK) ON fk.parent_object_id = c.object_id AND fk.parent_column_id = c.column_id
        JOIN sys.tables t2 WITH(NOLOCK) ON fk.referenced_object_id = t2.object_id
             AND SCHEMA_NAME(t2.schema_id) ='dbo' AND OBJECT_NAME(t2.object_id) ='nexus' AND t2.[type] = 'U'
    WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='cart_items_cart_item_fees' AND t.[type] = 'U')
BEGIN
    ALTER TABLE [dbo].[cart_items_cart_item_fees] ADD CONSTRAINT [fk_cart_item_id]
        FOREIGN KEY ([cart_item_id]) REFERENCES [dbo].[cart_item]([id]) ON DELETE CASCADE ON UPDATE CASCADE
END

IF NOT EXISTS(
    SELECT TOP 1 1
    FROM sys.foreign_key_columns fk WITH(NOLOCK)
        JOIN sys.tables t WITH(NOLOCK) ON fk.parent_object_id = t.object_id 
        JOIN sys.columns c WITH(NOLOCK) ON fk.parent_object_id = c.object_id AND fk.parent_column_id = c.column_id
        JOIN sys.tables t2 WITH(NOLOCK) ON fk.referenced_object_id = t2.object_id
             AND SCHEMA_NAME(t2.schema_id) ='dbo' AND OBJECT_NAME(t2.object_id) ='exception_items' AND t2.[type] = 'U' 
WHERE SCHEMA_NAME(t.schema_id) = 'dbo' AND OBJECT_NAME(t.object_id) ='cart_items_cart_item_fees' AND t.[type] = 'U')
BEGIN
     ALTER TABLE [dbo].[cart_items_cart_item_fees] ADD CONSTRAINT [fk_cart_item_fee_id]
         FOREIGN KEY ([cart_item_fee_id]) REFERENCES [dbo].[cart_item_fees]([id]) ON DELETE CASCADE ON UPDATE CASCADE
END

