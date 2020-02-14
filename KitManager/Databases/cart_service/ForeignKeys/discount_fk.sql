IF NOT EXISTS(
    SELECT TOP 1 *
    FROM sys.foreign_keys
    WHERE name = 'fk_discount_cart_item_fee_id'
          AND OBJECT_NAME(parent_object_id) = 'discounts' AND OBJECT_NAME(referenced_object_id) = 'cart_item_fees'
)
BEGIN
     ALTER TABLE [dbo].[discounts] ADD CONSTRAINT [fk_discount_cart_item_fee_id]
     FOREIGN KEY ([cart_item_fee_id]) REFERENCES [dbo].[cart_item_fees]([id]) ON DELETE CASCADE
     PRINT 'Added "fk_discount_cart_item_fee_id" Foreign Key to discounts table';
END
GO
