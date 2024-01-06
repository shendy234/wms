package com.enigma.wms_spring.service.impl;

import com.enigma.wms_spring.entity.ProductPrice;
import com.enigma.wms_spring.repository.ProductPriceRepository;
import com.enigma.wms_spring.service.ProductPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductPriceServiceImpl implements ProductPriceService {
    private final ProductPriceRepository productPriceRepository;

    @Override
    public ProductPrice create(ProductPrice productPrice) {
        return productPriceRepository.save(productPrice);
    }
    @Override
    public ProductPrice getById(String id) {
        return productPriceRepository.findById(id).orElseThrow();
    }
    @Override
    public void delete(String id) {
        productPriceRepository.deleteById(id);
    }

    }
