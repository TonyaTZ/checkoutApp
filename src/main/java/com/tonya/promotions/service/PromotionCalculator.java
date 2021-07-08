package com.tonya.promotions.service;

import com.tonya.promotions.api.PromotionAppliedSku;
import com.tonya.promotions.model.MultiPricedPromotion;
import com.tonya.promotions.model.SkuWithPromotion;

import java.util.List;

public class PromotionCalculator {

    public static PromotionAppliedSku processPromotion(SkuWithPromotion currentItem, List<SkuWithPromotion> skuWithPromotionList) {
        if (currentItem.getPromotion() == null) {
            return PromotionAppliedSku.builder()
                    .id(currentItem.getId())
                    .totalPrice(currentItem.getUsualPrice() * currentItem.getQuantity())
                    .build();
        }
        if (currentItem.getPromotion() instanceof MultiPricedPromotion) {
            return applyMultiPricedPromotion(currentItem);
        }
        //TODO throw an exception
        return null;
    }

    private static PromotionAppliedSku applyMultiPricedPromotion(SkuWithPromotion currentItem) {
        MultiPricedPromotion promotion = (MultiPricedPromotion) currentItem.getPromotion();
        int priceByPromotion = (currentItem.getQuantity() / promotion.getRequiredAmount()) * promotion.getPrice();
        int priceWithoutPromotion = (currentItem.getQuantity() % promotion.getRequiredAmount()) * currentItem.getUsualPrice();

        currentItem.setPromotionApplied(true);
        return PromotionAppliedSku.builder()
                .id(currentItem.getId())
                .totalPrice(priceByPromotion + priceWithoutPromotion)
                .build();
    }
}
