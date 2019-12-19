package com.active.services.cart.controller.v1;

public class Constants {
    public static final String V1_MEDIA = "application/vnd.active.cart-service.v1+json";

    public static final String ID_PARAM = "id";

    public static final String ID_PARAM_PATH = "/{" + ID_PARAM + "}";

    public static final String CART_ID_PARAM = "cart-id";

    public static final String CART_ITEM_ID_PARAM = "cart-item-id";

    public static final String CART_ITEM_ID_PATH = "/{" + CART_ITEM_ID_PARAM + "}";

    public static final String OWNER_ID_PARAM = "owner-id";

    public static final String OWNERS_PARAM = "owners";

    public static final String OWNER_PATH = "/" + OWNERS_PARAM + "/{" + OWNER_ID_PARAM + "}";

    public static final String QUOTE_PATH = "/{" + CART_ID_PARAM + "}" + "/quote";
}
