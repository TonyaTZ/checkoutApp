package com.tonya.promotions.service;

import com.tonya.promotions.api.PromotionAppliedSku;
import com.tonya.promotions.model.GetOneFreePromotion;
import com.tonya.promotions.model.MultiPricedPromotion;
import com.tonya.promotions.model.SkuWithPromotion;

import java.util.List;

public class PromotionCalculator {

    public static PromotionAppliedSku processPromotion(SkuWithPromotion currentItem, List<SkuWithPromotion> skuWithPromotionList) {
        if (currentItem.getPromotion() == null) {
            return noPromotions(currentItem);
        }
        if (currentItem.getPromotion() instanceof MultiPricedPromotion) {
            return applyMultiPricedPromotion(currentItem);
        }
        if (currentItem.getPromotion() instanceof GetOneFreePromotion) {
            return applyGetOneFreePromotion(currentItem);
        }
        //TODO throw an exception
        return null;
    }

    private static PromotionAppliedSku noPromotions(SkuWithPromotion currentItem) {
        return buildPromotionAppliedSku(currentItem, currentItem.getUsualPrice() * currentItem.getQuantity());
    }

    private static PromotionAppliedSku applyMultiPricedPromotion(SkuWithPromotion currentItem) {
        MultiPricedPromotion promotion = (MultiPricedPromotion) currentItem.getPromotion();
        int priceByPromotion = (currentItem.getQuantity() / promotion.getRequiredAmount()) * promotion.getPrice();
        int priceWithoutPromotion = (currentItem.getQuantity() % promotion.getRequiredAmount()) * currentItem.getUsualPrice();

        currentItem.setPromotionApplied(true);
        return buildPromotionAppliedSku(currentItem, priceByPromotion + priceWithoutPromotion);
    }

    private static PromotionAppliedSku applyGetOneFreePromotion(SkuWithPromotion currentItem) {
        GetOneFreePromotion promotion = (GetOneFreePromotion) currentItem.getPromotion();
        int priceByPromotion = (currentItem.getQuantity() / promotion.getRequiredAmount())
                * (currentItem.getUsualPrice() * promotion.getRequiredAmount() - 1);
        int priceWithoutPromotion = (currentItem.getQuantity() % promotion.getRequiredAmount()) * currentItem.getUsualPrice();

        currentItem.setPromotionApplied(true);
        return buildPromotionAppliedSku(currentItem, priceByPromotion + priceWithoutPromotion);
    }

    private static PromotionAppliedSku buildPromotionAppliedSku(SkuWithPromotion currentItem, int i) {
        return PromotionAppliedSku.builder()
                .id(currentItem.getId())
                .totalPrice(i)
                .build();
    }
}
