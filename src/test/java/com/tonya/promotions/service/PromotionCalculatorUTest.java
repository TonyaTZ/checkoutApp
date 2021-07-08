package com.tonya.promotions.service;

import com.tonya.promotions.api.PromotionAppliedSku;
import com.tonya.promotions.model.GetOneFreePromotion;
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
                quantity, usualPrice, multiPricedPromotion, false);


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
                quantity, usualPrice, getOneFreePromotion, false);

        PromotionAppliedSku promotionAppliedSku = processPromotion(currentSku, List.of(currentSku));

        assertNotNull(promotionAppliedSku);
        assertEquals(currentSku.getId(), promotionAppliedSku.getId());
        assertEquals(expectedPrice, promotionAppliedSku.getTotalPrice());
    }

    @Test
    void noPromotion_test() {
        SkuWithPromotion currentSku = new SkuWithPromotion(UUID.randomUUID().toString(), 2, 10, null, false);
        PromotionAppliedSku promotionAppliedSku = processPromotion(currentSku, List.of(currentSku));

        assertNotNull(promotionAppliedSku);
        assertEquals(currentSku.getId(), promotionAppliedSku.getId());
        assertEquals(20, promotionAppliedSku.getTotalPrice());
    }
}