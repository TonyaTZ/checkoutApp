package com.tonya.promotions.api;

import com.tonya.promotions.model.PromotionType;

import java.util.List;

public interface InternalPromotionsApi {

    /**
     * Calculates the price of each item after promotion is applied.
     * Supported promotions are provided by {@link PromotionType}
     * @param items selected by user. Should contain id of the sku, quantity and the usual price.
     * @return the list of skus with the calculated total price per sku.
     */
    PromotionsResponse calculatePromotions(List<ItemWithPrice> items);
}
