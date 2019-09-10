



### API

#### REST API definition

```java
	
    @PostMapping(value = "/carts", consumes = "application/vnd.active.cart-service.v1+json")
    public CreateCartsResp createCarts(@RequestBody @Valid CreateCartsReq request) {
        
        
    }
```



### Request

##### CreateCartsReq

```java
public class CreateCartsReq {
    
    private List<CartDto> carts;
}
```




##### CartDto

```java
public class CartDto {
    
    // a specific string to mark the cart
    private String identifier;
    private String currency;
    @Valid
    @NotEmpty
    private List<CartItemDto> cartItemDtos;
    // a organization identifier likes agencyId
    private String orgIdentifier;
    // a price date of the cart
    private LocalDateTime priceDate;

}
```

##### CartItemDto

```java
public class CartItemDto {

    
    // a specific string to mark the cart item.
    private String identifier;
    private Long productId;
    private int quantity;
    private CartItemOption option;
    // for pricing override
    private BigDecimal priceOverride;
    // it will take some dynamical properties
    private CartItemFacts cartItemFacts;
    // indicate parent-child relationships between cartItems
    private String parentIdentifier;

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
##### CartItemFeeOrigin

```java
public class CartItemFeeOrigin {
    
    
    ACL,

    TIMING,

    TIMING_CHIP,

    TIMER,

    VEB_PLAN,

    VEB_PLAN_ADD_ON,

    TAX_CONSUMER_ABSORBED,

    TAX_AGENCY_ABSORBED,

    TAX_ACTIVE_PRODUCT
    

}
```



### Response

##### CreateCartsResp


```java
public class CreateCartsResp {
    
    private List<CartResult> cartResults;
}

```

##### CartResult

```java
public class CartResult {
    
    private String identifier;
    private String currency;
    private List<CartItemResult> cartItemResults;
    private String orgIdentifier;
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
    private Long productId;
    private int quantity;
    private CartItemOption option;
    private BigDecimal priceOverride;
    private CartItemFacts cartItemFacts;
    private String parentIdentifier;
    private List<CartItemFee> cartItemFeeList;
    private BigDecimal itemTotal;
    private BigDecimal feeTotal;
    private BigDecimal taxTotal;

}
```
