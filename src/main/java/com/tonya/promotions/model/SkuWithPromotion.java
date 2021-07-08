package com.tonya.promotions.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
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
