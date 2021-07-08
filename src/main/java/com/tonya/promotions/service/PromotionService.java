package com.tonya.promotions.service;

import com.tonya.checkout.model.Item;
import com.tonya.promotions.api.InternalPromotionsApi;
import com.tonya.promotions.model.PromotionAppliedSku;
import com.tonya.promotions.model.PromotionsResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromotionService implements InternalPromotionsApi {

    @Override
    public PromotionsResponse calculatePromotions(List<Item> items) {
        //todo
        // #15 load promotions for items;
        // #8-11 apply promotions;
        return PromotionsResponse.builder().items(items.stream()
                .map(i -> PromotionAppliedSku.builder().build()).collect(Collectors.toList()))
                .build();
    }
}
