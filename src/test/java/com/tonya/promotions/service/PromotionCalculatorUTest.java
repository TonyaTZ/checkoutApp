package com.tonya.promotions.service;

import com.tonya.promotions.api.PromotionAppliedSku;
import com.tonya.promotions.model.GetOneFreePromotion;
import com.tonya.promotions.model.MealDealPromotion;
import com.tonya.promotions.model.MultiPricedPromotion;
import com.tonya.promotions.model.SkuWithPromotion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.tonya.promotions.service.PromotionCalculator.processPromotion;
import static org.junit.jupiter.api.Assertions.*;

class PromotionCalculatorUTest {


    private static Stream<Arguments> multiPricedTestCases() {
        return Stream.of(
                Arguments.of(2, 2, 3, 2, 3),
                Arguments.of(1, 2, 3, 2, 2),
                Arguments.of(5, 2, 3, 2, 8)
        );
    }

    @ParameterizedTest
    @MethodSource("multiPricedTestCases")
    void multiPriced_test(int quantity, int usualPrice, int promotionPrice, int requiredForPromotion, int expectedPrice) {
        MultiPricedPromotion multiPricedPromotion = new MultiPricedPromotion(promotionPrice, requiredForPromotion);
        SkuWithPromotion currentSku = new SkuWithPromotion(UUID.randomUUID().toString(),
                quantity, usualPrice, multiPricedPromotion);


        PromotionAppliedSku promotionAppliedSku = processPromotion(currentSku, List.of(currentSku));

        assertNotNull(promotionAppliedSku);
        assertEquals(currentSku.getId(), promotionAppliedSku.getId());
        assertEquals(expectedPrice, promotionAppliedSku.getTotalPrice());
    }


    private static Stream<Arguments> getOneFreeTestCases() {
        return Stream.of(
                Arguments.of(3, 1, 4, 3),
                Arguments.of(4, 1, 4, 3),
                Arguments.of(2, 1, 4, 2),
                Arguments.of(8, 1, 4, 6)
        );
    }

    @ParameterizedTest
    @MethodSource("getOneFreeTestCases")
    void getOneFree_test(int quantity, int usualPrice, int requiredForPromotion, int expectedPrice) {
        GetOneFreePromotion getOneFreePromotion = new GetOneFreePromotion(requiredForPromotion);
        SkuWithPromotion currentSku = new SkuWithPromotion(UUID.randomUUID().toString(),
                quantity, usualPrice, getOneFreePromotion);

        PromotionAppliedSku promotionAppliedSku = processPromotion(currentSku, List.of(currentSku));

        assertNotNull(promotionAppliedSku);
        assertEquals(currentSku.getId(), promotionAppliedSku.getId());
        assertEquals(expectedPrice, promotionAppliedSku.getTotalPrice());
    }


    private static Stream<Arguments> getMealDealTestCases() {
        return Stream.of(
                Arguments.of(1, 2, 1, 3, 4, 4, 0),
                Arguments.of(1, 2, 2, 3, 4, 4, 2),
                Arguments.of(2, 2, 2, 3, 4, 8, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("getMealDealTestCases")
    void mealDeal_test(int quantityX, int usualPriceX, int quantityY, int usualPriceY, int promotionPrice, int expectedPriceX, int expectedPriceY) {
        String idX = UUID.randomUUID().toString();
        String idY = UUID.randomUUID().toString();

        MealDealPromotion mealDealPromotionX = new MealDealPromotion(promotionPrice, List.of(idY));
        SkuWithPromotion skuX = new SkuWithPromotion(idX, quantityX, usualPriceX, mealDealPromotionX);

        MealDealPromotion mealDealPromotionY = new MealDealPromotion(promotionPrice, List.of(idX));
        SkuWithPromotion skuY = new SkuWithPromotion(idY, quantityY, usualPriceY, mealDealPromotionY);

        PromotionAppliedSku promotionAppliedSkuX = processPromotion(skuX, List.of(skuX, skuY));

        assertNotNull(promotionAppliedSkuX);
        assertEquals(skuX.getId(), promotionAppliedSkuX.getId());
        assertEquals(expectedPriceX, promotionAppliedSkuX.getTotalPrice());


        PromotionAppliedSku promotionAppliedSkuY = processPromotion(skuY, List.of(skuX, skuY));

        assertNotNull(promotionAppliedSkuY);
        assertEquals(skuY.getId(), promotionAppliedSkuY.getId());
        assertEquals(expectedPriceY, promotionAppliedSkuY.getTotalPrice());
    }


    @Test
    void mealDeal_only_sku_test() {
        String idX = UUID.randomUUID().toString();
        String idY = UUID.randomUUID().toString();
        SkuWithPromotion skuX;

        MealDealPromotion mealDealPromotionX = new MealDealPromotion(4, List.of(idY));
        skuX = new SkuWithPromotion(idX, 1, 2, mealDealPromotionX);

        PromotionAppliedSku promotionAppliedSkuX = processPromotion(skuX, List.of(skuX));

        assertNotNull(promotionAppliedSkuX);
        assertEquals(skuX.getId(), promotionAppliedSkuX.getId());
        assertEquals(2, promotionAppliedSkuX.getTotalPrice());
    }

    @Test
    void noPromotion_test() {
        SkuWithPromotion currentSku = new SkuWithPromotion(UUID.randomUUID().toString(), 2, 10, null);
        PromotionAppliedSku promotionAppliedSku = processPromotion(currentSku, List.of(currentSku));

        assertNotNull(promotionAppliedSku);
        assertEquals(currentSku.getId(), promotionAppliedSku.getId());
        assertEquals(20, promotionAppliedSku.getTotalPrice());
    }
}