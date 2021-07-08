package com.tonya.promotions.api;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PromotionAppliedSku {

    private final String id;
    private final int totalPrice;
    //todo #5 add missing promotion info
}
