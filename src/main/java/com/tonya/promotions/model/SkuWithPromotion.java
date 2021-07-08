package com.tonya.promotions.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class SkuWithPromotion {
    private String id;
    private int quantity;
    private int usualPrice;
    private Promotion promotion;
    @Setter
    private boolean isPromotionApplied;
}
