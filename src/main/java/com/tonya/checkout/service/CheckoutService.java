package com.tonya.checkout.service;

import com.tonya.checkout.model.CheckoutRequest;
import com.tonya.checkout.model.CheckoutResponse;
import com.tonya.checkout.model.Item;
import com.tonya.promotions.api.InternalPromotionsApi;
import com.tonya.promotions.api.ItemWithPrice;
import com.tonya.promotions.api.PromotionAppliedSku;
import com.tonya.promotions.api.PromotionsResponse;
import com.tonya.skus.api.InternalSkuProvider;
import com.tonya.skus.model.Sku;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CheckoutService {

    private final InternalSkuProvider skuProvider;
    private final InternalPromotionsApi promotionsApi;

    public CheckoutService(InternalSkuProvider skuProvider, InternalPromotionsApi promotionsApi) {
        this.skuProvider = skuProvider;
        this.promotionsApi = promotionsApi;
    }

    /**
     * Calculates the total amount that user has to pay for the order.
     *
     * @param checkoutRequest containing the scanned items
     * @return response containing subtotal and total to be paid by client.
     */
    public CheckoutResponse checkout(CheckoutRequest checkoutRequest) {
        //todo request to contain only one item of each id.

        Map<String, Integer> priceMap = getPriceMap(checkoutRequest.getItems());
        PromotionsResponse totalAfterPromotions = getTotalAfterPromotions(checkoutRequest.getItems(), priceMap);

        int totalBeforePromotion = getTotalBeforePromotion(checkoutRequest.getItems(), priceMap);

        return buildResponse(totalAfterPromotions, totalBeforePromotion);
    }

    /**
     * @param items    in the basket
     * @param priceMap loaded from sku service
     * @return total amount of the basket using full prices
     */
    private int getTotalBeforePromotion(List<Item> items, Map<String, Integer> priceMap) {
        return items.stream()
                .mapToInt(item -> item.getQuantity() * priceMap.get(item.getId()))
                .sum();
    }

    private CheckoutResponse buildResponse(PromotionsResponse priceAfterPromotions, int totalBeforePromotion) {

        int totalAfterPromotion = priceAfterPromotions.getItems()
                .stream()
                .mapToInt(PromotionAppliedSku::getTotalPrice)
                .sum();

        int savings = totalBeforePromotion - totalAfterPromotion;

        return new CheckoutResponse(totalBeforePromotion, savings, totalAfterPromotion);
    }

    private PromotionsResponse getTotalAfterPromotions(List<Item> items, Map<String, Integer> priceMap) {
        List<ItemWithPrice> itemsWithPrices = items.stream()
                .map(item -> ItemWithPrice.builder()
                        .id(item.getId())
                        .price(priceMap.get(item.getId()))
                        .quantity(item.getQuantity())
                        .build()
                ).collect(Collectors.toList());

        return promotionsApi.calculatePromotions(itemsWithPrices);
    }

    private Map<String, Integer> getPriceMap(List<Item> items) {
        List<String> itemIds = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        return skuProvider.getSkus(itemIds).stream()
                .collect(Collectors.toMap(Sku::getId, Sku::getPrice));
    }
}
