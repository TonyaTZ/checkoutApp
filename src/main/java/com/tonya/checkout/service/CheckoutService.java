package com.tonya.checkout.service;

import com.tonya.checkout.model.CheckoutRequest;
import com.tonya.checkout.model.CheckoutResponse;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {

    public CheckoutResponse checkout(CheckoutRequest checkoutRequest) {
        //todo
        // #6 load prices
        // #7 load promotions
        // #9-11 apply promotions
        return new CheckoutResponse();
    }
}
