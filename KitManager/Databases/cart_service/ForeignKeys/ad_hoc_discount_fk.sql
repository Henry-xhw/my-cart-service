IF NOT EXISTS(
        SELECT TOP 1 *
        FROM sys.foreign_keys
        WHERE name = 'fk_ad_hoc_discount_cart_item_id'
          AND OBJECT_NAME(parent_object_id) = 'ad_hoc_discounts' AND OBJECT_NAME(referenced_object_id) = 'cart_items'
    )
    BEGIN
        ALTER TABLE [dbo].[ad_hoc_discounts] ADD CONSTRAINT [fk_ad_hoc_discount_cart_item_id]
            FOREIGN KEY ([cart_item_id]) REFERENCES [dbo].[cart_items]([id]) ON DELETE CASCADE
        PRINT 'Added "fk_ad_hoc_discount_cart_item_id" Foreign Key to ad_hoc_discounts table';
    END
GO