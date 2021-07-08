package com.tonya.checkout.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CheckoutRequest {

    @NotEmpty
    @Valid
    private List<Item> items;
}
