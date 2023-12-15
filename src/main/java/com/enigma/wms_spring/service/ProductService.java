package com.enigma.wms_spring.service;

import com.enigma.wms_spring.dto.request.ProductRequest;
import com.enigma.wms_spring.dto.respon.ProductRespon;
import com.enigma.wms_spring.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Product create(Product product);
    Product update(Product product);
    Product getByidProduct(String id);
    List<Product> getAllProduct();
    void deleteProduct(String id);
    ProductRespon createProductAndProductPrice(ProductRequest productRequest);
    Page<ProductRespon> getAllByNameOrPrice(String productCode, String productName, Long minPrice, Long maxPrice, Integer page, Integer size);
}
