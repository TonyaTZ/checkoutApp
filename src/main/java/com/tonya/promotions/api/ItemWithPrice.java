package com.tonya.promotions.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ItemWithPrice {

    private String id;
    private int quantity;
    private int price;
}
