package com.tonya.promotions.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PromotionsResponse {

    private List<PromotionAppliedSku> items;
}
