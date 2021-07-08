package com.tonya.promotions.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class SkuWithPromotion {
    private final String id;
    private final int quantity;
    private final int usualPrice;
    private final Promotion promotion;
    @Setter
    private boolean isPromotionApplied;
    @Setter
    private int priceAfterPromotion;
}
