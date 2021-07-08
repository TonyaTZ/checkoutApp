package com.tonya.promotions.repository;

import com.tonya.promotions.model.Promotion;
import com.tonya.promotions.model.PromotionType;
import com.tonya.promotions.model.SkuWithPromotion;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
public class PromotionRepository {

    public Map<String, Promotion> getPromotions(List<String> skuIds) {
        // select s.sku_id, p.type, p.price, p.requiredAmount
        // from sku_promotion s
        // left join promotion p on(p.id = s.promotion_id)
        // where s.id in(${skuIds});

        // TODO Build based on db response
        return skuIds.stream().collect(toMap(Function.identity(),
                id -> PromotionFactory.getPromotion(getRandomPromotionType(), getRandomPrice(), getRandomNumber())));
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
}
