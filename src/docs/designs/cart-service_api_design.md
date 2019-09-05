



### API

#### REST API definition

```java
	
    @PostMapping(value = "/carts", consumes = "application/vnd.active.cart-service.v1+json")
    public CreateCartsResp createCarts(@RequestBody @Valid CreateCartsReq request) {
        ...
        for (CartDto cartDto : request.getCartDtoList()) {
            for (CartItemDto cartItemDto : cartDto.getCartItemDtoList()) {
            CartItem cartItem = new CartItem();    
            List<Rule> prodRules = productRepo.findProductFeeRulesByProductId(cartItemDto.getProductId());
            CartItemFact cif = cartItemDto.getCartItemFact();
            cif.getKvFactPairs.add(new KVFactPair("currency", cart.getCurrency());
            cif.getKvFactPairs.add(new KVFactPair("priceDate", cart.getPriceDate());
            ProductFact fact = new ProductFact(cif.getKvFactPairs());
            ruleEngine.runRules(prodRules, fact);
            LOG.info("rule execution result: {}", fact.getResult());
            cartItem.setPrice(fact.getResult().getAmount());
            cartItem.setProductId(cartItemDto.getProductId());
            cartItem.setIdentifier();
        }
        return null;
        
    }
```



### Request

##### CreateCartsReq

```java
public class CreateCartsReq {
    
    private List<CartDto> cartDtoList;
}
```




##### CartDto

```java
public class CartDto {
    // the field can link cart with a order, it can be a orderId
    private String identifier;
    private List<CartItemDto> cartItemDtoList;
    private Currency currency;
    private DateTime priceDate;

}
```

##### CartItemDto

```java
public class CartItemDto {
    
    // the field can link cartItem with a orderLine, it can be a orderLineId
    private String identifier;
    
    
    private Long productId;
    
    
    private int quantity;
    
    
    private DateTime beginDateTime;
    
    private DateTime endDateTime;

    // it will take some dynamical properties.
    private CartItemFact cartItemFact;

}
```


##### CartItemFact

```java
public class CartItemFact {
    
    private List<KVFactPair> kvFactPairs;

}
```

##### KVFactPair

```java
public class KVFactPair {
    
    private String key;
    private Object value;

}
```

##### Cart

```java
public class Cart {
    private String id;
    private List<CartItem> cartItemList;
    private Currency currency;
    private DateTime priceDate;

}
```

##### CartItem

```java
public class CartItem {
    
    private String id;
    
    private Long productId;
    
    private int quantity;

    
    //
    private BigDecimal price;
    
    //
    private List<CartItemFee> cartItemFeeList;

}
```

##### CartItemFee

```java
public class CartItemFee {
    
    private String id;
    private String name;
    private String description;
    private BigDecimal amount;
    private CartItemFeeType;
    private FeeTransactionType;
    private CartItemFeeOrigin;

}
```


### Response

##### CreateCartsResp


```java
public class CreateCartsResp {
    
    private List<Cart> cartList;
}

```



#### Talking:


cart-service api的一些考虑：
1. 是否需要多个apis: createCarts, addItemToCart, removeItemFromCart。
只需要一个api: createCarts.
caller负责build add/remove之后的cart，然后call cart-service.createCarts

...


Facility:

	1. age:
		属于dynamic属性字段，可以放到key/value

		beginDate/endDate:
		可能属于公共字段，提出来放到cartItem
		

	2. booking相关的问题.

		用户想对2019.9.1-2019.9.3进行询价, 2$ per day, 2019.9.2 reservation:

		1. facility只面向 cart-service, cart-service内部call inventory-service:
			a. 询价失败
			b. check inventory-service之后，内部分片成 2019.9.1和2019.9.3, 最终price: 1*2+1*2=4
		2. facility先去check inventory-service, 后去call cart-service:
			after checking inventory-service, facility调整成两个cart items: 2019.9.1和2019.9.2， 最终price: cartItem[0].price: 1*2=2; cartItem[1].price: 1*2=2




CUI:

		1. cui call createCarts, link order with a cart, link orderLine with a cartItem.
			当询价出了问题，如何troubleshooting? 
			cart.identifier<-------->orderId, cartItem.identifier<------------->orderLineId
			options:
				1) log (prefer)
				2) db table:
					carts: id, uuid(orderId), priceDate, currency...
					cartItem: id, uuid(orderLineId), productId, cartId...
					好处: 比较清晰，好定位
					不好处: db资源消耗大，并且很多询价行为并不是真正的交易行为

		2. 引申: commit order success后，是否需要 persist cart/cartItem?
			options:
				1) 系统间需要track数据，以及对账等
				2) orderLine已经体现了，不需要再去persist cart/cartItem



		3. Multiple payment plan business.
		multiple payment plan不属于询价范畴，cui在call cart-service之后去进行multiple payment plan business构建, 最后commit order
		


OMS:

	  1. order/cart mapping, orderLine/cartItem mapping的一些考虑:
			1) 现有OMS中orderLine下可以挂sub-orderLine, OMS会进行最多只挂两层的限制，每次priceOrder之前进行扁平化处理，priceOrder之后unFlatten处理。 cart-service中cartItem没有父子关系, OMS扁平化order后 call cart-service进行询价

			2) override以及transfer业务不在cart-service范畴，只有当明确需要询价行为时，才会call cart-service。

            ...



	2. split考虑:
		cart定义一组cartItem,cart给出这组cartItem必须要遵守的规则，例如统一的currency，统一的priceDate等
		caller基于以下规则去定义一组carts,然后call cart-service.createCarts.
			1) currency
			2) 相同currency下的remittance account
			3) OrderLineSplitGroup(也就是customized split business)


	3. product pricing business:（VOLUME_BASED+has the includedAssociatedProductId）
				req:
					cartDtoList[0]:
						identifier:123
						currency:USD
						priceDate:2019-08-26 14:30
						cartItemDtoList[0]:
							identifier:456
							productId:789
							quantity:2
							cartItemFact:
								kvFactPairs[0]:
										includedAssociatedProductId:567
								kvFactPairs[1]:
										volume:13


				resp:
					carts[0]:
						identifier:123
						currency:USD
						priceDate:2019-08-26 14:30
						cartItems[0]:
							identifier:456
							productId:789
							quantity:2
							*price:300.00
							cartItemFeeList[0]:
								amount:150
								cartItemFeeType:PRICE
								feeTransactionType:DEBIT



		4. surcharge: