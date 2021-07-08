package com.tonya.checkout.integration;


import com.tonya.checkout.model.CheckoutRequest;
import com.tonya.checkout.model.CheckoutResponse;
import com.tonya.checkout.model.Item;
import com.tonya.checkout.service.CheckoutService;
import com.tonya.promotions.api.InternalPromotionsApi;
import com.tonya.promotions.api.ItemWithPrice;
import com.tonya.promotions.api.PromotionAppliedSku;
import com.tonya.promotions.api.PromotionsResponse;
import com.tonya.skus.api.InternalSkuProvider;
import com.tonya.skus.model.Sku;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CheckoutITest {

    private static final String ID_1 = UUID.randomUUID().toString();
    private static final String ID_2 = UUID.randomUUID().toString();
    private static final String ID_3 = UUID.randomUUID().toString();
    private static final String ID_4 = UUID.randomUUID().toString();
    private static final String ID_5 = UUID.randomUUID().toString();

    private static final Map<String, Integer> quantityMap = new HashMap<>() {{
        put(ID_1, 1);
        put(ID_2, 5);
        put(ID_3, 3);
        put(ID_4, 2);
        put(ID_5, 7);
    }};

    private static final Map<String, Integer> priceMap = new HashMap<>() {{
        put(ID_1, 100);
        put(ID_2, 700);
        put(ID_3, 500);
        put(ID_4, 10000);
        put(ID_5, 138);
    }};

    private static final Map<String, Integer> promotionsMap = new HashMap<>() {{
        put(ID_1, 100);
        put(ID_2, 1000);
        put(ID_3, 1300);
        put(ID_4, 10000);
        put(ID_5, 999);
    }};

    @MockBean
    private InternalPromotionsApi promotionsApi;

    @MockBean
    private InternalSkuProvider skuProvider;

    @Autowired
    private CheckoutService checkoutService;

    //todo replace with MockMvc
    @Test
    void checkout() {

        mockSkuProvider();
        mockPromotions();

        CheckoutResponse checkoutResponse = checkoutService.checkout(getCheckoutRequest());

        assertEquals(13399, checkoutResponse.getTotal());
        assertEquals(26066, checkoutResponse.getSubTotal());
        assertEquals(12667, checkoutResponse.getSavings());

        verifyPromotionsCalled();
        verifySkuApiCalled();
    }

    private void verifySkuApiCalled() {
        ArgumentCaptor<List<String>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(skuProvider).getSkus(argumentCaptor.capture());

        List<String> arguments = argumentCaptor.getValue();
        assertEquals(5, arguments.size());
        assertTrue(arguments.contains(ID_1));
        assertTrue(arguments.contains(ID_2));
        assertTrue(arguments.contains(ID_3));
        assertTrue(arguments.contains(ID_4));
        assertTrue(arguments.contains(ID_5));
    }

    private void verifyPromotionsCalled() {
        ArgumentCaptor<List<ItemWithPrice>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(promotionsApi).calculatePromotions(argumentCaptor.capture());

        List<ItemWithPrice> arguments = argumentCaptor.getValue();

        assertEquals(5, arguments.size());

        arguments.sort(Comparator.comparing(ItemWithPrice::getQuantity));

        checkItem(arguments.get(0), ID_1);
        checkItem(arguments.get(1), ID_4);
        checkItem(arguments.get(2), ID_3);
        checkItem(arguments.get(3), ID_2);
        checkItem(arguments.get(4), ID_5);
    }

    private void checkItem(ItemWithPrice actual, String id) {
        assertEquals(id, actual.getId());
        assertEquals(priceMap.get(id), actual.getPrice());
        assertEquals(quantityMap.get(id), actual.getQuantity());
    }

    private void mockPromotions() {

        List<PromotionAppliedSku> promotionAppliedSkus = promotionsMap.entrySet()
                .stream()
                .map(entry ->
                        PromotionAppliedSku.builder()
                                .id(entry.getKey())
                                .totalPrice(entry.getValue())
                                .build()
                ).collect(Collectors.toList());

        when(promotionsApi.calculatePromotions(anyList()))
                .thenReturn(PromotionsResponse.builder()
                        .items(promotionAppliedSkus)
                        .build());
    }

    private void mockSkuProvider() {

        List<Sku> skus = priceMap.entrySet()
                .stream()
                .map(entry ->
                        Sku.builder()
                                .id(entry.getKey())
                                .price(entry.getValue())
                                .build()
                ).collect(Collectors.toList());

        when(skuProvider.getSkus(anyList())).thenReturn(skus);
    }

    private CheckoutRequest getCheckoutRequest() {
        List<Item> items = quantityMap.entrySet()
                .stream()
                .map(entry -> {
                            Item item = new Item();
                            item.setId(entry.getKey());
                            item.setQuantity(entry.getValue());
                            return item;
                        }
                ).collect(Collectors.toList());

        CheckoutRequest checkoutRequest = new CheckoutRequest();
        checkoutRequest.setItems(items);
        return checkoutRequest;
    }
}
