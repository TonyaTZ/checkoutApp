package com.tonya.promotions.model;

import lombok.Getter;

@Getter
public class GetOneFreePromotion extends Promotion {
    private int requiredAmount;

    public GetOneFreePromotion(int requiredAmount) {
        super(0);
        this.requiredAmount = requiredAmount;
    }

    public int getPrice() {
        throw new UnsupportedOperationException("Get one free promotion does not have price");
    }
}
