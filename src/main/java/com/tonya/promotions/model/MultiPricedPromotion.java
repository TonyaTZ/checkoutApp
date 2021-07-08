package com.tonya.promotions.model;

import lombok.Getter;

@Getter
public class MultiPricedPromotion extends Promotion {

    private final int requiredAmount;

    public MultiPricedPromotion(int price, int requiredAmount) {
        super(price);
        this.requiredAmount = requiredAmount;
    }
}
