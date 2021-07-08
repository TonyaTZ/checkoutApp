package com.tonya.promotions.api;

import com.tonya.checkout.model.Item;
import com.tonya.promotions.model.PromotionsResponse;

import java.util.List;

public interface InternalPromotionsApi {

    PromotionsResponse calculatePromotions(List<Item> items);
}
