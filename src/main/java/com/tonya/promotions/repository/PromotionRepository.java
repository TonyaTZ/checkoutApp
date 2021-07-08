package com.tonya.promotions.repository;

import com.tonya.promotions.model.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Ascii.equalsIgnoreCase;
import static java.util.stream.Collectors.toMap;

@Service
public class PromotionRepository {

    public Map<String, Promotion> getPromotions(List<String> skuIds) {
        // select s.sku_id, p.type, p.price, p.requiredAmount
        // from sku_promotion s
        // left join promotion p on(p.id = s.promotion_id)
        // where s.id in(${skuIds});

        // TODO Build based on db response
        List<String> dbResponse = skuIds;

        return dbResponse.stream().collect(toMap(Function.identity(),
                id -> getPromotion(getRandomPromotionType(), getRandomPrice(), getRandomNumber(), id)));
    }

    private List<String> getAllSkusWithSamePromotion(String skuId) {
        // select s.sku_id
        // from sku_promotion s
        // where s.promotion_id = (select s2.promotion_id from sku_promotion s2 where s2.sku_id = ${skuId});

        // TODO Build based on db response
        List<String> dbResponse = List.of(skuId);

        return dbResponse.stream()
                .filter(id -> !equalsIgnoreCase(id, skuId)).collect(Collectors.toList());
    }

    private PromotionType getRandomPromotionType() {
        return PromotionType.values()[ThreadLocalRandom.current().nextInt(0, 3)];
    }

    private int getRandomPrice() {
        return ThreadLocalRandom.current().nextInt(0, 100);
    }

    private int getRandomNumber() {
        return ThreadLocalRandom.current().nextInt(0, 10);
    }

    private Promotion getPromotion(PromotionType promotionType, int price, int requiredAmount, String skuId) {
        switch (promotionType) {
            case MealDeal:
                return new MealDealPromotion(price, getAllSkusWithSamePromotion(skuId));
            case GetOneFree:
                return new GetOneFreePromotion(requiredAmount);
            case MultiPriced:
                return new MultiPricedPromotion(price, requiredAmount);
            default:
                throw new IllegalArgumentException("Can't resolve promotion type " + promotionType);
        }
    }
}
