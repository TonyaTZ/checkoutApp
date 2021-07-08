package com.tonya.skus.service;

import com.tonya.skus.api.InternalSkuProvider;
import com.tonya.skus.model.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class SkuService implements InternalSkuProvider {

    public List<Sku> getSkus(List<String> ids) {
        //todo #13 implement skus repository. Bulk load Skus; throw exception if sku doesn't exist
        return ids.stream()
                .map(this::getSkuById)
                .collect(Collectors.toList());
    }

    private Sku getSkuById(String id) {
        return Sku.builder()
                .id(id)
                .price(ThreadLocalRandom.current().nextInt(0))
                .build();
    }
}
