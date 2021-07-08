package com.tonya.skus.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Sku {

    private String id;
    private int price;

}
