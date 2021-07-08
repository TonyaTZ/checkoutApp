package com.tonya.checkout.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@Getter
public class Item {

    @NotBlank
    private final String id;

    @Positive
    private final int quantity;

}