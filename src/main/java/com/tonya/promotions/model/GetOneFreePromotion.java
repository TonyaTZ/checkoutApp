package com.tonya.promotions.model;

import lombok.Getter;

@Getter
public class GetOneFreePromotion extends Promotion {
    private int requiredAmount;

    public GetOneFreePromotion(int price, int requiredAmount) {
        super(price);
        this.requiredAmount = requiredAmount;
    }
}
