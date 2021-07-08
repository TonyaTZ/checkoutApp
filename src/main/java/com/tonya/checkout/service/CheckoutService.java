package com.tonya.checkout.service;

import com.tonya.checkout.model.CheckoutRequest;
import com.tonya.checkout.model.CheckoutResponse;
import com.tonya.checkout.model.Item;
import com.tonya.skus.api.InternalSkuProvider;
import com.tonya.skus.model.Sku;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckoutService {

    private final InternalSkuProvider skuProvider;

    public CheckoutService(InternalSkuProvider skuProvider) {
        this.skuProvider = skuProvider;
    }

    /**
     * Calculates the total charge of client during the checkout process.
     *
     * @param checkoutRequest to be processed
     * @return response containing subtotal and total to be paid by client.
     */
    public CheckoutResponse checkout(CheckoutRequest checkoutRequest) {
        //todo request to contain only one item of each id.
        //todo
        List<Sku> skus = getSkus(checkoutRequest.getItems());
        // #7 load promotions
        // #9-11 apply promotions
        return new CheckoutResponse();
    }

    private List<Sku> getSkus(List<Item> items) {
        List<String> ids = items.stream().map(Item::getId).collect(Collectors.toList());
        return skuProvider.getSkus(ids);
    }
}
