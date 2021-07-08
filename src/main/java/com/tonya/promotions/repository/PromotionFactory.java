package com.tonya.promotions.repository;

import com.tonya.promotions.model.*;

public class PromotionFactory {

    public static Promotion getPromotion(PromotionType promotionType, int price, int requiredAmount) {
        switch (promotionType) {
            case MealDeal:
                //todo #20 meal deal table
                return new MealDealPromotion(price, null);
            case GetOneFree:
                return new GetOneFreePromotion(requiredAmount);
            case MultiPriced:
                return new MultiPricedPromotion(price, requiredAmount);
            default:
                throw new IllegalArgumentException("Can't resolve promotion type " + promotionType);
        }
    }
}
