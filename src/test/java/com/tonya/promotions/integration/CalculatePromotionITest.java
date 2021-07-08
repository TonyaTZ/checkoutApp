package com.tonya.promotions.integration;

import com.tonya.promotions.api.InternalPromotionsApi;
import com.tonya.promotions.api.ItemWithPrice;
import com.tonya.promotions.api.PromotionsResponse;
import com.tonya.promotions.model.MultiPricedPromotion;
import com.tonya.promotions.repository.PromotionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;


@SpringBootTest
public class CalculatePromotionITest {

    @Autowired
    private InternalPromotionsApi internalPromotionsApi;

    @MockBean
    private PromotionRepository promotionRepository;


    @Test
    void calculatePromotion_withMultiBuy() {

        List<ItemWithPrice> items = List.of(getItem(2, 10), getItem(3, 4));

        //TODO put promotions to db thru scripts.
        when(promotionRepository.getPromotions(anyList()))
                .thenReturn(Map.of(items.get(0).getId(), new MultiPricedPromotion(15, 2)));

        PromotionsResponse promotionsResponse = internalPromotionsApi.calculatePromotions(items);
        assertEquals(2, promotionsResponse.getItems().size());
        assertEquals(15, promotionsResponse.getItems().get(0).getTotalPrice());
        assertEquals(12, promotionsResponse.getItems().get(1).getTotalPrice());
    }

    private ItemWithPrice getItem(int quantity, int price) {
        return new ItemWithPrice(UUID.randomUUID().toString(), quantity, price);
    }
}
