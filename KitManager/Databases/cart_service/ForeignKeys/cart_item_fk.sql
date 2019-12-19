IF NOT EXISTS(
    SELECT TOP 1 *
    FROM sys.foreign_keys
    WHERE name = 'fk_cart_item_cart_id'
          AND OBJECT_NAME(parent_object_id) = 'cart_item' AND OBJECT_NAME(referenced_object_id) = 'cart'
)
BEGIN
     ALTER TABLE [dbo].[cart_item] ADD CONSTRAINT [fk_cart_item_cart_id]
     FOREIGN KEY ([cart_id]) REFERENCES [dbo].[carts]([id]) ON DELETE CASCADE
     PRINT 'Added "fk_cart_item_cart_id" Foreign Key to cart_item table';
END
GO
