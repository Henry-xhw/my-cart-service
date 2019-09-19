



### API

#### REST API definition

```java
	
    @PostMapping(value = "/cart", consumes = "application/vnd.active.cart-service.v1+json")
    public CreateCartResp createCart(@RequestBody @Valid CreateCartReq request) {
        
        
    }
```



### Request

##### CreateCartReq

```java
public class CreateCartReq {
    
    private CartDto cart;
}
```




##### CartDto

```java
public class CartDto {
    
        /**
             * A specific string to mark the cart
             */
            @NotBlank
            @Size(min = 1, max = 255, message = "must be 1-255 chars")
            private String identifier;
        
            /**
             * The currency code used to represent a monetary values associated with the cart,
             * all cart items under the cart should use the same currency code.
             */
            @NotBlank
            @Size(min = 3, max = 3, message = "must be 3 chars")
            private String currency;
        
            private List<CartItemDto> cartItemDtos;
        
            /**
             * A specific pricing date for the cart
             */
            private LocalDateTime priceDate;

}
```

##### CartItemDto

```java
public class CartItemDto {

        /**
             * A specific string to mark the cart item
             */
            @NotBlank
            @Size(min = 1, max = 255, message = "must be 1-255 chars")
            private String identifier;
        
            /**
             * A organization identifier, it can be a agencyId, and so on.
             */
            @NotBlank
            private String orgIdentifier;
        
            @NotNull
            private Long productId;
        
            private Integer quantity;
        
            private CartItemOption option;
        
            /**
             * It can override the cartItem's price
             */
            private BigDecimal priceOverride;
        
            /**
             * It will take some dynamical properties
             */
            private CartItemFacts cartItemFacts;
        
            /**
             * It can indicate parent-child relationships
             */
            private List<CartItemDto> cartItemDtos;

}
```

##### CartItemOption

```java
public class CartItemOption {
    
    private List<BookingDuration> bookingDurations;

}
```

##### BookingDuration

```java
public class BookingDuration {
    
    private final LocalDateTime from;
    private final LocalDateTime to;

}
```

##### CartItemFacts

```java
public class CartItemFacts {
    private List<FactKVPair> factKVPairs;
}
```

##### FactKVPair

```java
public class FactKVPair {
    
    private String key;
    private Object value;

}
```


##### CartItemFee

```java
public class CartItemFee {
    
    private Long id;
        private String name;
        private String description;
        private CartItemFeeType feeType;
        private FeeTransactionType transactionType;
        private String cartItemFeeOrigin;
        private BigDecimal unitPrice;
        private Integer units;
        private BigDecimal subtotal;
    
        private List<CartItemFee> derivedFees;

}
```

##### CartItemFeeType

```java
/**
 * Enum representing types of fees that can be applied to a cart item.  These types are used in conjunction with
 * the {@link FeeTransactionType} for an {@link CartItemFee} to determine how the fee is applied.
 */ 
public enum CartItemFeeType {
    

    PRICE,
    PROCESSING_FLAT,
    PROCESSING_PERCENTAGE,
    TAX,
    SURCHARGE,
    DISCOUNT

}
```

##### FeeTransactionType

```java
public enum FeeTransactionType {
    
    
    DEBIT,
    
    
    CREDIT;

}
```

### Response

##### CreateCartResp


```java
public class CreateCartsResp {
    
    private CartResult cartResult;
}

```

##### CartResult

```java
public class CartResult {
    
    private String identifier;
        private String currency;
        private List<CartItemResult> cartItemResults;
        private LocalDateTime priceDate;
        private BigDecimal subtotal;
        private BigDecimal feeTotal;
        private BigDecimal taxTotal;

}
```

##### CartItemResult

```java
public class CartItemResult {

    private String identifier;
        private List<CartItemFeeResult> cartItemFeeResults;
        private List<CartItemResult> cartItemResults;
        private BigDecimal itemTotal;
        private BigDecimal feeTotal;
        private BigDecimal taxTotal;

}
```


#### REST API definition

```java
	
    @GetMapping(value = "/cart", consumes = "application/vnd.active.cart-service.v1+json")
        public GetCartResp getCart(@RequestBody @Valid GetCartReq request) {
            return null;
        }
```

### Request

##### GetCartReq

```java
public class GetCartReq {
    private String identifier;
}
```

### Request

##### GetCartResp

```java
public class GetCartResp {
    private CartResult cartResult;
}
```


#### REST API definition

```java
	
@PutMapping(value = "/cart", consumes = "application/vnd.active.cart-service.v1+json")
    public AddItemToCartResp addItemToCart(@RequestBody @Valid AddItemToCartReq request) {
        return null;
    }
```

### Request

##### AddItemToCartReq

```java
public class AddItemToCartReq {
    private String cartIdentifier;
    private CartItemDto cartItemDto;
}
```

### Response

##### AddItemToCartResp

```java
public class AddItemToCartResp {
    private CartResult cartResult;
}
```

#### REST API definition

```java
	
@DeleteMapping(value = "/cart", consumes = "application/vnd.active.cart-service.v1+json")
    public RemoveItemFromCartResp removeItemFromCart(@RequestBody @Valid RemoveItemFromCartReq request) {
        return null;
    }
```

### Request

##### RemoveItemFromCartReq

```java
public class RemoveItemFromCartReq {
    private String cartIdentifier;
    private String cartItemIdentifier;
}
```

### Response

##### RemoveItemFromCartResp

```java
public class RemoveItemFromCartResp {
    private CartResult cartResult;
}
```
#### REST API definition

```java
	
@DeleteMapping(value = "/cart", consumes = "application/vnd.active.cart-service.v1+json")
    public DeleteCartResp deleteCart(@RequestBody @Valid DeleteCartReq request) {
        return null;
    }
```

### Request

##### DeleteCartReq

```java
public class DeleteCartReq {
    private String cartIdentifier;
}
```

### Response

##### DeleteCartResp

```java
public class DeleteCartResp {
    private CartResult cartResult;
}
```