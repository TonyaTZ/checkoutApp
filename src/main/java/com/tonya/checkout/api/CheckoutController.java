package com.tonya.checkout.api;

import com.tonya.checkout.model.CheckoutRequest;
import com.tonya.checkout.model.CheckoutResponse;
import com.tonya.checkout.service.CheckoutService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("checkout")
@RestController
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    //TODO #3 add security
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest checkoutRequest) {
        return checkoutService.checkout(checkoutRequest);
    }
}
