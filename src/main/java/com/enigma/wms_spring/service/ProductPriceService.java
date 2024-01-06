package com.enigma.wms_spring.service;

import com.enigma.wms_spring.entity.ProductPrice;

public interface ProductPriceService {
    ProductPrice create(ProductPrice productPrice);
    ProductPrice getById(String id);
    void delete(String id);
}
