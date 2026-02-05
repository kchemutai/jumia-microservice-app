package com.jumia.ProductService.service;

import com.jumia.ProductService.model.ProductRequest;
import com.jumia.ProductService.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
