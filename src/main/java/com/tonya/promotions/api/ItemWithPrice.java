package com.tonya.promotions.api;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ItemWithPrice {

    private final String id;
    private final int quantity;
    private final int price;
}
