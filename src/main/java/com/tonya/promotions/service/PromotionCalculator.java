package com.tonya.promotions.service;

import com.tonya.promotions.api.PromotionAppliedSku;
import com.tonya.promotions.model.GetOneFreePromotion;
import com.tonya.promotions.model.MealDealPromotion;
import com.tonya.promotions.model.MultiPricedPromotion;
import com.tonya.promotions.model.SkuWithPromotion;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

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

        if (currentItem.getPromotion() instanceof MealDealPromotion) {
            if (currentItem.isPromotionApplied()) {
                return buildPromotionAppliedSku(currentItem, currentItem.getPriceAfterPromotion());
            }
            return applyMealDealPromotion(currentItem, skuWithPromotionList);
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

    private static PromotionAppliedSku applyMealDealPromotion(SkuWithPromotion currentItem, List<SkuWithPromotion> skuWithPromotionList) {

        MealDealPromotion promotion = (MealDealPromotion) currentItem.getPromotion();
        List<String> requiredItems = promotion.getRequiredItems();

        List<SkuWithPromotion> otherRequired = skuWithPromotionList.stream()
                .filter(sku -> requiredItems.contains(sku.getId()))
                .collect(Collectors.toList());

        if (otherRequired.size() != requiredItems.size()) {
            return noPromotions(currentItem);
        }

        int packedDealsNumber =  Math.min(currentItem.getQuantity(), otherRequired.stream().mapToInt(SkuWithPromotion::getQuantity).min().orElse(0));

        int priceByPromotion = packedDealsNumber * promotion.getPrice();
        int priceWithoutPromotion = (currentItem.getQuantity() - packedDealsNumber) * currentItem.getUsualPrice();

        otherRequired.forEach(skuWithPromotion -> {
            int priceAfterPromotion = (skuWithPromotion.getQuantity() - packedDealsNumber) * currentItem.getUsualPrice();
            skuWithPromotion.setPriceAfterPromotion(priceAfterPromotion);
            skuWithPromotion.setPromotionApplied(true);
        });
        currentItem.setPromotionApplied(true);
        return buildPromotionAppliedSku(currentItem, priceByPromotion + priceWithoutPromotion);
    }

    private static PromotionAppliedSku buildPromotionAppliedSku(SkuWithPromotion currentItem, int price) {
        return PromotionAppliedSku.builder()
                .id(currentItem.getId())
                .totalPrice(price)
                .build();
    }
}
