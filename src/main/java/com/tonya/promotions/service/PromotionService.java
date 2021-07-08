package com.tonya.promotions.service;

import com.tonya.promotions.api.InternalPromotionsApi;
import com.tonya.promotions.api.ItemWithPrice;
import com.tonya.promotions.api.PromotionAppliedSku;
import com.tonya.promotions.api.PromotionsResponse;
import com.tonya.promotions.model.Promotion;
import com.tonya.promotions.model.SkuWithPromotion;
import com.tonya.promotions.repository.PromotionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PromotionService implements InternalPromotionsApi {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public PromotionsResponse calculatePromotions(List<ItemWithPrice> items) {
        Map<String, Promotion> promotionsMap = promotionRepository.getPromotions(
                items.stream()
                        .map(ItemWithPrice::getId)
                        .collect(Collectors.toList())
        );

        List<SkuWithPromotion> skusWithPromotion = items.stream()
                .map(item -> SkuWithPromotion.builder()
                        .id(item.getId())
                        .quantity(item.getQuantity())
                        .usualPrice(item.getPrice())
                        .promotion(promotionsMap.get(item.getId()))
                        .build())
                .collect(Collectors.toList());

        List<PromotionAppliedSku> promotionAppliedSkus =
                skusWithPromotion.stream()
                        .map(skuWithPromotion -> PromotionCalculator.processPromotion(skuWithPromotion, skusWithPromotion))
                        .collect(Collectors.toList());

        return PromotionsResponse.builder()
                .items(promotionAppliedSkus)
                .build();
    }
}
