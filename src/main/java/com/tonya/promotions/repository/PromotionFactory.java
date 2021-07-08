package com.tonya.promotions.repository;

import com.tonya.promotions.model.*;

public class PromotionFactory {

    public static Promotion getPromotion(PromotionType promotionType, int price, int requiredAmount) {
        switch (promotionType) {
            case MealDeal:
                return new MealDealPromotion(price);
            case GetOneFree:
                return new GetOneFreePromotion(price, requiredAmount);
            case MultiPriced:
                return new MultiPricedPromotion(price, requiredAmount);
            default:
                throw new IllegalArgumentException("Can't resolve promotion type " + promotionType);
        }
    }
}
