package com.tonya.promotions.integration;

import com.tonya.checkout.model.Item;
import com.tonya.promotions.api.InternalPromotionsApi;
import com.tonya.promotions.api.PromotionsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class CalculatePromotionITest {

    @Autowired
    private InternalPromotionsApi internalPromotionsApi;

    @Test
    void calculatePromotion() {
        PromotionsResponse promotionsResponse = internalPromotionsApi.calculatePromotions(List.of(getItem(2), getItem(3)));
        assertEquals(2, promotionsResponse.getItems().size());
    }

    private Item getItem(int quantity) {
        Item item = new Item();
        item.setId(UUID.randomUUID().toString());
        item.setQuantity(quantity);
        return item;
    }
}
