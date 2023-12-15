package com.enigma.wms_spring.controller;

import com.enigma.wms_spring.constant.AppPath;
import com.enigma.wms_spring.dto.request.ProductRequest;
import com.enigma.wms_spring.dto.respon.CommonRespon;
import com.enigma.wms_spring.dto.respon.PagingRespon;
import com.enigma.wms_spring.dto.respon.ProductRespon;
import com.enigma.wms_spring.entity.Product;
import com.enigma.wms_spring.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.PRODUCT)
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest){
        ProductRespon productResponse = productService.createProductAndProductPrice(productRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonRespon.<ProductRespon>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully created new product")
                        .data(productResponse)
                        .build());
    }

    @GetMapping
    public List<Product> getAllProduct(){
        return productService.getAllProduct();
    }

    @GetMapping(value = "/page")
    public ResponseEntity<?> getALlproductPage(
            @RequestParam(name = "productCode", required = false) String productCode,
            @RequestParam(name = "productName", required = false) String productName,
            @RequestParam(name = "minPrice", required = false) Long minPrice,
            @RequestParam(name = "maxPrice", required = false) Long maxPrice,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ){
        Page<ProductRespon> productRespons = productService.getAllByNameOrPrice(productCode,productName,minPrice,maxPrice,page,size);
        PagingRespon pagingRespon = PagingRespon.builder()
                .currentPage(page)
                .totalPage(productRespons.getTotalPages())
                .size(size)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonRespon.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successgully get all product")
                        .data(productRespons.getContent())
                        .paging(pagingRespon)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductAndPrice(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonRespon.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully Delete Product and Price")
                        .data(HttpStatus.OK)
                        .build());
    }
}
