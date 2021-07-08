package com.tonya.promotions.api;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PromotionsResponse {

    private final List<PromotionAppliedSku> items;
}
