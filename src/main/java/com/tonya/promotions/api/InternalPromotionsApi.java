package com.tonya.promotions.api;

import com.tonya.checkout.model.Item;

import java.util.List;

public interface InternalPromotionsApi {

    PromotionsResponse calculatePromotions(List<ItemWithPrice> items);
}
