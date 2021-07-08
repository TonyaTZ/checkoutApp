package com.tonya.promotions.service;

import com.tonya.checkout.model.Item;
import com.tonya.promotions.api.InternalPromotionsApi;
import com.tonya.promotions.api.PromotionAppliedSku;
import com.tonya.promotions.api.PromotionsResponse;
import com.tonya.promotions.model.SkuWithPromotion;
import com.tonya.promotions.repository.PromotionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromotionService implements InternalPromotionsApi {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public PromotionsResponse calculatePromotions(List<Item> items) {
        //todo
        List<SkuWithPromotion> skusWithPromotions = promotionRepository.getPromotions(
                items.stream()
                        .map(Item::getId)
                        .collect(Collectors.toList())
        );
        // #8-11 apply promotions;
        return PromotionsResponse.builder().items(items.stream()
                .map(i -> PromotionAppliedSku.builder().build()).collect(Collectors.toList()))
                .build();
    }
}
