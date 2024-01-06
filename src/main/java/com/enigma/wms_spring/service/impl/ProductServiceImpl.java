package com.enigma.wms_spring.service.impl;

import com.enigma.wms_spring.dto.request.ProductRequest;
import com.enigma.wms_spring.dto.respon.BranchRespon;
import com.enigma.wms_spring.dto.respon.ProductRespon;
import com.enigma.wms_spring.entity.Branch;
import com.enigma.wms_spring.entity.Product;
import com.enigma.wms_spring.entity.ProductPrice;
import com.enigma.wms_spring.repository.ProductPriceRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final BranchService branchService;
    private final ProductPriceRepository productPriceRepository;
    private final ProductPriceService productPriceService;


    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductRespon createProductAndProductPrice(ProductRequest productRequest) {
        BranchRespon branchRespon = branchService.getByIdBranch(productRequest.getBranchId());
        Product product = productRepository.save(Product.builder()
                .productCode(productRequest.getProductCode())
                .productName(productRequest.getProductName())
                .branch(Branch.builder()
                        .id(branchRespon.getId())
                        .branchCode(branchRespon.getBranchCode())
                        .branchName(branchRespon.getBranchName())
                        .address(branchRespon.getAddress())
                        .phoneNumber(branchRespon.getPhoneNumber())
                        .build())
                .build());
        ProductPrice productPrice = productPriceRepository.save(ProductPrice.builder()
                .price(productRequest.getPrice())
                .product(product)
                .build());
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
    public Page<ProductRespon> getAll(String productCode, String productName, Long minPrice, Long maxPrice, Integer page, Integer size, String branchId) {
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
        Page<Product> products = productRepository.findAll(specification, pageable);

        List<ProductRespon> productResponses = new ArrayList<>();
        products.getContent().forEach(product -> {
            Branch branch = product.getBranch();
            if (branch.getId().equals(branchId)){
                ProductRespon pr = ProductRespon.builder()
                        .productId(product.getId())
                        .productCode(product.getProductCode())
                        .productName(product.getProductName())
                        .price(product.getProductPrice().getPrice())
                        .branch(BranchRespon.builder()
                                .Id(branch.getId())
                                .branchCode(branch.getBranchCode())
                                .branchName(branch.getBranchName())
                                .address(branch.getAddress())
                                .phoneNumber(branch.getPhoneNumber())
                                .build())
                        .build();
                productResponses.add(pr);
            }
        });
        return new PageImpl<>(productResponses, pageable, products.getTotalElements());
    }

    private static ProductRespon toProductRespon( Branch branch,Product product, ProductPrice productPrice) {
        return ProductRespon.builder()
                .productId(product.getId())
                .productCode(product.getProductCode())
                .productName(product.getProductName())
                .price(productPrice.getPrice())
                .branch(BranchRespon.builder()
                        .Id(branch.getId())
                        .branchCode(branch.getBranchCode())
                        .branchName(branch.getBranchName())
                        .address(branch.getAddress())
                        .phoneNumber(branch.getPhoneNumber())
                        .build())
                .build();
    }

    @Override
    public List<ProductRespon> getByBranchId(String branchId) {
        List<Product> products = productRepository.findAllByBranchId(branchId);
        return products.stream()
                .map(product -> ProductRespon.builder()
                        .productId(product.getId())
                        .productId(product.getProductCode())
                        .productName(product.getProductName())
                        .price(product.getProductPrice().getPrice())
                        .branch(BranchRespon.builder()
                                .Id(product.getBranch().getId())
                                .branchCode(product.getBranch().getBranchCode())
                                .branchName(product.getBranch().getBranchName())
                                .address(product.getBranch().getAddress())
                                .phoneNumber(product.getBranch().getPhoneNumber())
                                .build())
                        .build()).collect(Collectors.toList());
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductRespon updateProduct(ProductRequest productRequest) {
        Product product = productRepository.findById(productRequest.getProductId()).orElseThrow(()-> new RuntimeException("productId doesn't exist"));
        BranchRespon branchRespon = branchService.getByIdBranch(productRequest.getProductId());

        product.setProductCode(productRequest.getProductCode());
        product.setProductName(productRequest.getProductName());
        product.setBranch(product.getBranch());

        product = productRepository.save(product);
        ProductPrice productPrice = (ProductPrice) product.getProductPrice();

        if (productRequest.getPrice().compareTo((product.getProductPrice()).getPrice()) == 0){
            productPrice = productPriceRepository.save(ProductPrice.builder()
                            .price(productRequest.getPrice())
                            .product(product)
                    .build());
        }
        return ProductRespon.builder()
                .productId(product.getId())
                .productCode(product.getProductCode())
                .productName(product.getProductName())
                .price(productRequest.getPrice())
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
    public void deleteProduct(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("product doesn't exist."));
        productPriceRepository.deleteById(product.getId());
        productRepository.deleteById(id);
    }
}
