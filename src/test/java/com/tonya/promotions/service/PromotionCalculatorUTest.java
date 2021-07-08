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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        SkuWithPromotion currentSku = SkuWithPromotion.builder()
                .id(UUID.randomUUID().toString())
                .quantity(quantity)
                .usualPrice(usualPrice)
                .promotion(multiPricedPromotion)
                .build();

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
        SkuWithPromotion currentSku = SkuWithPromotion.builder()
                .id(UUID.randomUUID().toString())
                .quantity(quantity)
                .usualPrice(usualPrice)
                .promotion(getOneFreePromotion)
                .build();

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
        SkuWithPromotion skuX = SkuWithPromotion.builder()
                .id(idX)
                .quantity(quantityX)
                .usualPrice(usualPriceX)
                .promotion(mealDealPromotionX)
                .build();

        MealDealPromotion mealDealPromotionY = new MealDealPromotion(promotionPrice, List.of(idX));
        SkuWithPromotion skuY = SkuWithPromotion.builder()
                .id(idY)
                .quantity(quantityY)
                .usualPrice(usualPriceY)
                .promotion(mealDealPromotionY)
                .build();

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

        MealDealPromotion mealDealPromotionX = new MealDealPromotion(4, List.of(idY));
        SkuWithPromotion skuX = SkuWithPromotion.builder()
                .id(idX)
                .quantity(1)
                .usualPrice(2)
                .promotion(mealDealPromotionX)
                .build();

        PromotionAppliedSku promotionAppliedSkuX = processPromotion(skuX, List.of(skuX));

        assertNotNull(promotionAppliedSkuX);
        assertEquals(skuX.getId(), promotionAppliedSkuX.getId());
        assertEquals(2, promotionAppliedSkuX.getTotalPrice());
    }

    @Test
    void noPromotion_test() {
        SkuWithPromotion currentSku = SkuWithPromotion.builder()
                .id(UUID.randomUUID().toString())
                .quantity(2)
                .usualPrice(10)
                .build();
        PromotionAppliedSku promotionAppliedSku = processPromotion(currentSku, List.of(currentSku));

        assertNotNull(promotionAppliedSku);
        assertEquals(currentSku.getId(), promotionAppliedSku.getId());
        assertEquals(20, promotionAppliedSku.getTotalPrice());
    }
}