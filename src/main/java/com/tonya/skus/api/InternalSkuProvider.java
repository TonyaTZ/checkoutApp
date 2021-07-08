package com.tonya.skus.api;

import com.tonya.skus.exception.MissingSkuException;
import com.tonya.skus.model.Sku;

import java.util.List;

public interface InternalSkuProvider {

    /**
     * Returns bulk of Skus by provided ids.
     *
     * @param ids of the requested Skus.
     * @return list of Skus containing id and price for each item.
     * @throws MissingSkuException when any id is not found.
     */
    List<Sku> getSkus(List<String> ids);
}
