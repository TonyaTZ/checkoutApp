package com.tonya.checkout.model;

import lombok.Data;

@Data
public class CheckoutResponse {

    private int subTotal;
    private int savings;
    private int total;
}
