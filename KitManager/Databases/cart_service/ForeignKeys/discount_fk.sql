IF NOT EXISTS(
    SELECT TOP 1 *
    FROM sys.foreign_keys
    WHERE name = 'fk_cart_discount_cart_id'
          AND OBJECT_NAME(parent_object_id) = 'discounts' AND OBJECT_NAME(referenced_object_id) = 'carts'
)
BEGIN
     ALTER TABLE [dbo].[discounts] ADD CONSTRAINT [fk_cart_discount_cart_id]
     FOREIGN KEY ([cart_id]) REFERENCES [dbo].[carts]([id]) ON DELETE CASCADE
     PRINT 'Added "fk_cart_discount_cart_id" Foreign Key to discounts table';
END
GO
