package com.tonya.checkout.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class Item {

    @NotBlank
    private String id;

    @Positive
    private int quantity;

}