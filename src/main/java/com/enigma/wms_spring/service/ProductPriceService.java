package com.enigma.wms_spring.service;

import com.enigma.wms_spring.entity.ProductPrice;

public interface ProductPriceService {
    ProductPrice create(ProductPrice productPrice);
    ProductPrice getById(String id);
    ProductPrice findProductPriceIsActive(String productId, Boolean active);

    void delete(String id);

    ProductPrice createOrUpdate(ProductPrice productPrice);
}
