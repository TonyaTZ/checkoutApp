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

    /**
     * Calculates multi-priced promotion, where N items with the same SKU cost special price.
     *
     * @param currentItem to process
     * @return sku with the applied promotion
     */
    private static PromotionAppliedSku applyMultiPricedPromotion(SkuWithPromotion currentItem) {
        MultiPricedPromotion promotion = (MultiPricedPromotion) currentItem.getPromotion();

        int priceWithPromotion = (currentItem.getQuantity() / promotion.getRequiredAmount())
                * promotion.getPrice();
        int priceWithoutPromotion = (currentItem.getQuantity() % promotion.getRequiredAmount())
                * currentItem.getUsualPrice();

        currentItem.setPromotionApplied(true);
        return buildPromotionAppliedSku(currentItem, priceWithPromotion + priceWithoutPromotion);
    }

    /**
     * Calculates get one free promotion, where by buying N+1 items with the same SKU, users pay only for N of them.
     *
     * @param currentItem to process
     * @return sku with the applied promotion
     */
    private static PromotionAppliedSku applyGetOneFreePromotion(SkuWithPromotion currentItem) {
        GetOneFreePromotion promotion = (GetOneFreePromotion) currentItem.getPromotion();

        int priceWithPromotion = (currentItem.getQuantity() / promotion.getRequiredAmount())
                * (currentItem.getUsualPrice() * (promotion.getRequiredAmount() - 1));
        int priceWithoutPromotion = (currentItem.getQuantity() % promotion.getRequiredAmount()) * currentItem.getUsualPrice();

        currentItem.setPromotionApplied(true);
        return buildPromotionAppliedSku(currentItem, priceWithPromotion + priceWithoutPromotion);
    }

    /**
     * Calculates meal deal promotion, where by buying all items from the list, users pay special price.
     * It specifies the full meal deal price on the first item in the list and updates the rest of the meal deal items with the price of 0.
     *
     * @param currentItem to process
     * @return sku with the applied promotion
     */
    private static PromotionAppliedSku applyMealDealPromotion(SkuWithPromotion currentItem, List<SkuWithPromotion> skuWithPromotionList) {

        MealDealPromotion promotion = (MealDealPromotion) currentItem.getPromotion();
        List<String> requiredByPromotion = promotion.getRequiredItems();

        List<SkuWithPromotion> requiredExistingInOrder = skuWithPromotionList.stream()
                .filter(sku -> requiredByPromotion.contains(sku.getId()))
                .collect(Collectors.toList());

        if (requiredExistingInOrder.size() != requiredByPromotion.size()) {
            return noPromotions(currentItem);
        }

        int packedDealsAmount = getAmountOfMealDealsInOrder(currentItem, requiredExistingInOrder);

        int priceByPromotion = packedDealsAmount * promotion.getPrice();
        int priceWithoutPromotion = (currentItem.getQuantity() - packedDealsAmount) * currentItem.getUsualPrice();

        requiredExistingInOrder.forEach(skuWithPromotion -> {
            int priceAfterPromotion = (skuWithPromotion.getQuantity() - packedDealsAmount) * currentItem.getUsualPrice();
            skuWithPromotion.setPriceAfterPromotion(priceAfterPromotion);
            skuWithPromotion.setPromotionApplied(true);
        });

        currentItem.setPromotionApplied(true);
        return buildPromotionAppliedSku(currentItem, priceByPromotion + priceWithoutPromotion);
    }

    private static int getAmountOfMealDealsInOrder(SkuWithPromotion currentItem, List<SkuWithPromotion> requiredExistingInOrder) {
        return Math.min(currentItem.getQuantity(),
                    requiredExistingInOrder.stream()
                            .mapToInt(SkuWithPromotion::getQuantity)
                            .min()
                            .orElse(0));
    }

    private static PromotionAppliedSku buildPromotionAppliedSku(SkuWithPromotion currentItem, int price) {
        return PromotionAppliedSku.builder()
                .id(currentItem.getId())
                .totalPrice(price)
                .build();
    }
}
