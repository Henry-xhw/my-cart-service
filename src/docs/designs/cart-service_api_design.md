




#### REST APIs definition

```java
	
    @GetMapping(value = "/carts/{identifier}") public CartResult getCart(@PathVariable UUID identifier) {
        return null;
    }

    @PostMapping(value = "/carts") public CartResult createCart(@RequestBody @Valid CartDto cart) {
        return null;
    }

    @PutMapping(value = "/carts/{identifier}")
    public CartResult addItemToCart(@PathVariable UUID identifier, @RequestBody @Valid CartItemDto item) {
        return null;
    }

    @PutMapping(value = "/carts/{identifier}/discount")
    public CartResult applyDiscountToCart(@PathVariable UUID identifier, @RequestBody List<String> coupons) {
        return null;
    }

    @PatchMapping(value = "/carts/{identifier}/{itemIdentifier}/{quantity}")
    public CartResult updateQuantity(@PathVariable UUID identifier, @PathVariable UUID itemIdentifier,
        @PathVariable Integer quantity) {
        return null;
    }

    @DeleteMapping(value = "/carts/{identifier}/{itemIdentifier}")
    public CartResult removeItemFromCart(@PathVariable UUID identifier, @PathVariable UUID itemIdentifier) {
        return null;
    }
```



### Request
##### CartDto

```java
public class CartDto {

    private String referenceId;

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

    private String referenceId;

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
    
    private LocalDateTime from;
    
    private LocalDateTime to;
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

### Response

##### CartResult

```java
public class CartResult {
    private UUID identifier;
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

    private UUID identifier;
    private List<CartItemFeeResult> cartItemFeeResults;
    private List<CartItemResult> cartItemResults;
    private boolean paymentOptionAvailable;
    private BigDecimal itemTotal;
    private BigDecimal feeTotal;
    private BigDecimal taxTotal;
}
```
##### CartItemFeeResult
```java
	
    public class CartItemFeeResult {
    private String name;
    private String description;
    private CartItemFeeType feeType;
    private FeeTransactionType transactionType;
    private String cartItemFeeOrigin;
    private BigDecimal unitPrice;
    private Integer units;
    private BigDecimal subtotal;

    private List<CartItemFeeResult> derivedFeeResults;
}
```

##### CartItemFeeType

```java
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
    CREDIT,
    DEBIT
}
```

