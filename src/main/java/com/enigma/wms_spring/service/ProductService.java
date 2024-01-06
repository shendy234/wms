package com.enigma.wms_spring.service;

import com.enigma.wms_spring.dto.request.ProductRequest;
import com.enigma.wms_spring.dto.respon.ProductRespon;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    void deleteProduct(String id);
    ProductRespon createProductAndProductPrice(ProductRequest productRequest);
    Page<ProductRespon> getAll(String productCode, String productName, Long minPrice, Long maxPrice, Integer page, Integer size, String branchId);
    List<ProductRespon> getByBranchId(String branchId);
    ProductRespon updateProduct(ProductRequest productRequest);
}
