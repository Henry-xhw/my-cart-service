IF EXISTS(SELECT TOP 1 1 FROM sys.tables t WITH(NOLOCK)
WHERE SCHEMA_NAME(schema_id) = 'dbo' AND OBJECT_NAME(object_id) ='cart_discounts' AND type = 'U')
BEGIN
	 drop table dbo.cart_discounts
	 PRINT 'drop dbo.cart_discounts'
END
GO