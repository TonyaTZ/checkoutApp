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
     * Calculates the total charge of client during the checkout process.
     *
     * @param checkoutRequest to be processed
     * @return response containing subtotal and total to be paid by client.
     */
    public CheckoutResponse checkout(CheckoutRequest checkoutRequest) {
        //todo request to contain only one item of each id.

        Map<String, Integer> priceMap = getPriceMap(checkoutRequest.getItems());
        PromotionsResponse priceAfterPromotions = getPriceAfterPromotions(checkoutRequest, priceMap);


        int priceBeforePromotion = getPriceBeforePromotion(checkoutRequest, priceMap);

        return calculateTotals(priceAfterPromotions, priceBeforePromotion);
    }

    private int getPriceBeforePromotion(CheckoutRequest checkoutRequest, Map<String, Integer> priceMap) {
        return checkoutRequest.getItems().stream().mapToInt(item -> item.getQuantity() * priceMap.get(item.getId())).sum();
    }

    private CheckoutResponse calculateTotals(PromotionsResponse priceAfterPromotions, int priceBeforePromotion) {

        int totalAfterPromotion = priceAfterPromotions.getItems()
                .stream()
                .mapToInt(PromotionAppliedSku::getTotalPrice)
                .sum();

        int savings = priceBeforePromotion - totalAfterPromotion;

        return new CheckoutResponse(priceBeforePromotion, savings, totalAfterPromotion);
    }

    private PromotionsResponse getPriceAfterPromotions(CheckoutRequest checkoutRequest, Map<String, Integer> priceMap) {
        List<ItemWithPrice> itemsWithPrices = checkoutRequest.getItems().stream().map(item ->
                new ItemWithPrice(item.getId(), item.getQuantity(),
                        priceMap.get(item.getId()))).collect(Collectors.toList());

        return promotionsApi.calculatePromotions(itemsWithPrices);
    }

    private Map<String, Integer> getPriceMap(List<Item> items) {
        List<String> ids = items.stream().map(Item::getId).collect(Collectors.toList());
        return skuProvider.getSkus(ids).stream().collect(Collectors.toMap(Sku::getId, Sku::getPrice));
    }
}
