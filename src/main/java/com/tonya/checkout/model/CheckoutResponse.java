package com.tonya.checkout.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CheckoutResponse {

    private int subTotal;
    private int savings;
    private int total;
}
