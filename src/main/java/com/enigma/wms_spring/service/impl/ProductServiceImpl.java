package com.enigma.wms_spring.service.impl;

import com.enigma.wms_spring.dto.request.ProductRequest;
import com.enigma.wms_spring.dto.respon.BranchRespon;
import com.enigma.wms_spring.dto.respon.ProductRespon;
import com.enigma.wms_spring.entity.Branch;
import com.enigma.wms_spring.entity.Product;
import com.enigma.wms_spring.entity.ProductPrice;
import com.enigma.wms_spring.repository.ProductRepository;
import com.enigma.wms_spring.service.BranchService;
import com.enigma.wms_spring.service.ProductPriceService;
import com.enigma.wms_spring.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final BranchService branchService;
    private final ProductPriceService productPriceService;

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }
    @Override
    public Product update(Product product) {
        return productRepository.save(product);
    }
    @Override
    public Product getByidProduct(String id) {
        return productRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductRespon createProductAndProductPrice(ProductRequest productRequest) {
        BranchRespon branchRespon = branchService.getByIdBranch(productRequest.getBranchId().getId());
        Branch branch = new Branch();
        Product product = Product.builder()
                .productCode(productRequest.getProductCode())
                .productName(productRequest.getProductName())
                .build();
        productRepository.saveAndFlush(product);
        ProductPrice productPrice = ProductPrice.builder()
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .isActive(true)
                .product(product)
                .branch(Branch.builder()
                        .id(branchRespon.getId())
                        .build())
                .build();
        productPriceService.create(productPrice);

        return ProductRespon.builder()
                .productId(product.getId())
                .productId(productPrice.getId())
                .productCode(product.getProductCode())
                .productName(product.getProductName())
                .price(productPrice.getPrice())
                .branch(BranchRespon.builder()
                        .Id(branchRespon.getId())
                        .branchCode(branchRespon.getBranchCode())
                        .branchName(branchRespon.getBranchName())
                        .address(branchRespon.getAddress())
                        .phoneNumber(branchRespon.getPhoneNumber())
                        .build())
                .build();
    }

    @Override
    public Page<ProductRespon> getAllByNameOrPrice(String productCode, String productName, Long minPrice, Long maxPrice, Integer page, Integer size) {
        Specification<Product> specification = (root, query, criteriaBuilder) ->{
            Join<Product, ProductPrice> productPrices = root.join("productPrice");
            List<Predicate> predicates = new ArrayList<>();
            if(productCode != null){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower((root.get("name"))), "%" + productCode.toLowerCase() + "%"));
            }
            if(productName != null){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower((root.get("name"))), "%" + productName.toLowerCase() + "%"));
            }
            if(maxPrice != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(productPrices.get("price"), maxPrice));
            }
            if(minPrice != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(productPrices.get("price"), minPrice));
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(specification,pageable);
        List<ProductRespon> productRespons = new ArrayList<>();
        for (Product product : products.getContent()){
            Optional<ProductPrice> productPrice = product.getProductPrice()
                    .stream()
                    .filter(ProductPrice::getIsActive).findFirst();
            if(productPrice.isEmpty()) continue;
            Branch branch = productPrice.get().getBranch();
            productRespons.add(toProductRespon( branch, product, productPrice.get()));
        }
        return new PageImpl<>(productRespons, pageable, products.getTotalElements());
    }

    private static ProductRespon toProductRespon( Branch branch,Product product, ProductPrice productPrice) {
        return ProductRespon.builder()
                .productId(product.getId())
                .productCode(product.getProductCode())
                .productName(product.getProductName())
                .price(productPrice.getPrice())
                .stock(productPrice.getStock())
                .branch(BranchRespon.builder()
                        .Id(branch.getId())
                        .branchCode(branch.getBranchCode())
                        .branchName(branch.getBranchName())
                        .address(branch.getAddress())
                        .phoneNumber(branch.getPhoneNumber())
                        .build())
                .build();
    }


    }

