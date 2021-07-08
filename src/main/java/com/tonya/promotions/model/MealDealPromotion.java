package com.tonya.promotions.model;

import lombok.Getter;

import java.util.List;

@Getter
public class MealDealPromotion extends Promotion {

    private final List<String> requiredItems;

    public MealDealPromotion(int price, List<String> requiredItems) {
        super(price);
        this.requiredItems = requiredItems;
    }

}
