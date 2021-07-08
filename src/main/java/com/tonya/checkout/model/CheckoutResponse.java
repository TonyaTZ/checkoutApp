package com.tonya.checkout.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CheckoutResponse {

    private String id;
    private final int subTotal;
    private final int savings;
    private final int total;
}
