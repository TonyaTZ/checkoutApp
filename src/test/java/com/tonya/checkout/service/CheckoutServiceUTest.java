package com.tonya.checkout.service;

import com.tonya.checkout.model.CheckoutRequest;
import com.tonya.checkout.model.Item;
import com.tonya.skus.api.InternalSkuProvider;
import com.tonya.skus.exception.MissingSkuException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceUTest {

    @Mock
    private InternalSkuProvider skuProvider;

    @InjectMocks
    private CheckoutService checkoutService;

    @Test
    void checkout_throwsException_whenSkuNotFound() {

        when(skuProvider.getSkus(anyList())).thenThrow(new MissingSkuException());

        List<Item> items = getItems();
        assertThrows(MissingSkuException.class, () -> checkoutService.checkout(new CheckoutRequest(items)));

        verify(skuProvider).getSkus(items.stream().map(Item::getId).collect(Collectors.toList()));
    }

    private List<Item> getItems() {
        return List.of(new Item(UUID.randomUUID().toString(), 1));
    }
}