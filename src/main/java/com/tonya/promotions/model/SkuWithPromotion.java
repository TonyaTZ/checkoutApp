package com.tonya.promotions.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SkuWithPromotion {
    private String id;
    private Promotion promotion;
    private boolean isPromotionApplied;
}
