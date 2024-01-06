package com.enigma.wms_spring.service.impl;

import com.enigma.wms_spring.constant.ETransactionType;
import com.enigma.wms_spring.dto.request.TransactionDetailRequest;
import com.enigma.wms_spring.dto.request.TransactionRequest;
import com.enigma.wms_spring.dto.respon.*;
import com.enigma.wms_spring.entity.*;
import com.enigma.wms_spring.repository.ProductPriceRepository;
import com.enigma.wms_spring.repository.TransactionDetailRepository;
import com.enigma.wms_spring.repository.TransactionRepository;
import com.enigma.wms_spring.service.TransactionService;
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

import java.sql.Date;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionDetailRepository transactionDetailRepository;
    private final ProductPriceRepository productPriceRepository;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public TransactionRespon createNewTransaction(TransactionRequest transactionRequest) {
        String transactionType = transactionRequest.getTransactionType().trim();
        if (!(transactionType.equals("1") ||
                transactionType.equals("2") ||
                transactionType.equals("3"))) {
            throw new RuntimeException("Invalid transactionType");
        }
        if (transactionRequest.getTransactionDetails().isEmpty()) {
            throw new RuntimeException("Please buy something");
        }
        ProductPrice initialProductPrice = productPriceRepository
                .findById(transactionRequest.getTransactionDetails().get(0).getProductPriceId())
                .orElseThrow(() -> new RuntimeException("productPriceId doesn't exist"));

        String branchCode = initialProductPrice.getProduct().getBranch().getBranchCode();
        String thisYear = Year.now().toString();

        String transType = null;
        switch (transactionRequest.getTransactionType()) {
            case "1" -> transType = ETransactionType.EAT_IN.name();
            case "2" -> transType = ETransactionType.ONLINE.name();
            case "3" -> transType = ETransactionType.TAKE_AWAY.name();
        }

        Transaction transaction = Transaction.builder()
                .transType(transType)
                .transDate(Date.valueOf(LocalDate.now()))
                .receiptNumber("%s-%s-%d".formatted(branchCode, thisYear, transactionRepository.count() + 1))
                .build();
        transaction = transactionRepository.save(transaction);

        List<TransactionDetailRespon> transactionDetailRespons = new ArrayList<>();
        for (TransactionDetailRequest transactionDetails : transactionRequest.getTransactionDetails()) {
            ProductPrice productPrice = productPriceRepository.findById(transactionDetails.getProductPriceId())
                    .orElseThrow(() -> new RuntimeException("productPriceId doesn't exist"));

            TransactionDetail savedTransactionDetail = TransactionDetail.builder()
                    .transaction(transaction)
                    .quantity(transactionDetails.getQuantity())
                    .totalSales(productPrice.getPrice()*(Double.valueOf(transactionDetails.getQuantity())))
                    .product(productPrice.getProduct())
                    .build();
            savedTransactionDetail = transactionDetailRepository.save(savedTransactionDetail);

            transactionDetailRespons.add(TransactionDetailRespon.builder()
                    .transactionDetailId(savedTransactionDetail.getId())
                    .billDetailId(savedTransactionDetail.getTransaction().getId())
                            .product(ProductRespon.builder()
                                    .productId(savedTransactionDetail.getProduct().getId())
                                    .productCode(savedTransactionDetail.getProduct().getProductCode())
                                    .productName(savedTransactionDetail.getProduct().getProductName())
                                    .price(savedTransactionDetail.getProduct().getProductPrice().getPrice())
                                    .branch(BranchRespon.builder()
                                            .Id(savedTransactionDetail.getProduct().getBranch().getId())
                                            .branchCode(savedTransactionDetail.getProduct().getBranch().getBranchCode())
                                            .branchName(savedTransactionDetail.getProduct().getBranch().getBranchName())
                                            .address(savedTransactionDetail.getProduct().getBranch().getAddress())
                                            .phoneNumber(savedTransactionDetail.getProduct().getBranch().getPhoneNumber())
                                            .build())
                                    .build())
                    .quantity(transactionDetails.getQuantity())
                    .totalSales(savedTransactionDetail.getTotalSales())
                    .build());
        }

        return TransactionRespon.builder()
                .transactionId(transaction.getId())
                .receiptNumber(transaction.getReceiptNumber())
                .transDate(transaction.getTransDate().toString())
                .transactionType(transaction.getTransType())
                .transactionDetails(transactionDetailRespons)
                .build();
    }

    @Override
    public TransactionRespon getTransactionById(String id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("bill id doesn't exist"));

        List<TransactionDetailRespon> transactionDetailRespons = new ArrayList<>();
        for (TransactionDetail transactionDetail : transaction.getTransactionDetails()) {
            transactionDetailRespons.add(TransactionDetailRespon.builder()
                            .transactionDetailId(transactionDetail.getId())
                            .billDetailId(transactionDetail.getId())
                            .quantity(transactionDetail.getQuantity())
                            .totalSales(transactionDetail.getTotalSales())
                            .product(ProductRespon.builder()
                                    .productId(transactionDetail.getProduct().getId())
                                    .productCode(transactionDetail.getProduct().getProductCode())
                                    .productName(transactionDetail.getProduct().getProductName())
                                    .price(transactionDetail.getProduct().getProductPrice().getPrice())
                                    .branch(BranchRespon.builder()
                                            .Id(transactionDetail.getProduct().getBranch().getId())
                                            .branchCode(transactionDetail.getProduct().getBranch().getBranchCode())
                                            .branchName(transactionDetail.getProduct().getBranch().getBranchName())
                                            .address(transactionDetail.getProduct().getBranch().getAddress())
                                            .phoneNumber(transactionDetail.getProduct().getBranch().getPhoneNumber())
                                            .build())
                                    .build())
                    .build());
        }
        return TransactionRespon.builder()
                .transactionId(transaction.getId())
                .receiptNumber(transaction.getReceiptNumber())
                .transDate(transaction.getTransDate().toString())
                .transactionType(transaction.getTransType())
                .transactionDetails(transactionDetailRespons)
                .build();
    }

    @Override
    public Page<TransactionRespon> getTransactions(
            Integer page,
            Integer size,
            String receiptNumber,
            String startDate,
            String endDate,
            String transType,
            String productName) {

        Specification<Transaction> specification = (root, query, criteriaBuilder) -> {
            Join<Transaction, TransactionDetail> transactionDetail = root.join("transactionDetail");
            Join<TransactionDetail, Product> products = transactionDetail.join("product");
            List<Predicate> predicates = new ArrayList<>();
            if (receiptNumber != null) {
                predicates.add(criteriaBuilder.like(root.get("receiptNumber"), "%" + receiptNumber + "%"));
            }
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("transDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("transDate"), endDate));
            }
            if (transType != null) {
                predicates.add(criteriaBuilder.like(root.get("transType"), "%" + transType + "%"));
            }
            if (productName != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(products.get("name")), "%" + productName.toLowerCase() + "%"));
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionRepository.findAll(specification, pageable);

        List<TransactionRespon> transactionResponses = new ArrayList<>();
        transactions.getContent().forEach(transaction -> {
            List<TransactionDetailRespon> transactionDetailRespons = new ArrayList<>();
            for (TransactionDetail transactionDetail : transaction.getTransactionDetails()) {
                transactionDetailRespons.add(TransactionDetailRespon.builder()
                        .transactionDetailId(transactionDetail.getId())
                        .billDetailId(transactionDetail.getId())
                        .quantity(transactionDetail.getQuantity())
                        .totalSales(transactionDetail.getTotalSales())
                        .product(ProductRespon.builder()
                                        .productId(transactionDetail.getProduct().getId())
                                        .productCode(transactionDetail.getProduct().getProductCode())
                                        .productName(transactionDetail.getProduct().getProductName())
                                        .price(transactionDetail.getProduct().getProductPrice().getPrice())
                                        .branch(BranchRespon.builder()
                                                .Id(transactionDetail.getProduct().getBranch().getId())
                                                .branchCode(transactionDetail.getProduct().getBranch().getBranchCode())
                                                .branchName(transactionDetail.getProduct().getBranch().getBranchName())
                                                .address(transactionDetail.getProduct().getBranch().getAddress())
                                                .phoneNumber(transactionDetail.getProduct().getBranch().getPhoneNumber())
                                                .build())
                                        .build())
                        .build());
            }
            transactionResponses.add(TransactionRespon.builder()
                    .transactionId(transaction.getId())
                    .receiptNumber(transaction.getReceiptNumber())
                    .transDate(transaction.getTransDate().toString())
                    .transactionType(transaction.getTransType())
                    .transactionDetails(transactionDetailRespons)
                    .build());
        });
        return new PageImpl<>(transactionResponses, pageable, transactions.getTotalElements());
    }


    @Override
    public TotalSalesRespon getTotalSales(String startDate, String endDate) {
        Specification<Transaction> specification = (root, query, criteriaBuilder) -> {
            Join<Transaction, TransactionDetail> transactionDetails = root.join("transactionDetails");
            Join<TransactionDetail, Product> products = transactionDetails.join("products");
            Join<Product, ProductPrice> productPrices = products.join("productPrices");
            List<Predicate> predicates = new ArrayList<>();
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("transDate"), endDate));
            }
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("transDate"), startDate));
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        List<Transaction> transactions = transactionRepository.findAll(specification);
        Double eatIn = Double.valueOf(0);
        Double takeAway = Double.valueOf(0);
        Double online = Double.valueOf(0);

        for (Transaction transaction : transactions) {
            if (ETransactionType.EAT_IN.name().equals(transaction.getTransType())) {
                Double sum = Double.valueOf(0);
                for (TransactionDetail transactionDetail : transaction.getTransactionDetails()) {
                    sum = (transactionDetail.getTotalSales());
                };
                eatIn += sum;
            }
            if (ETransactionType.ONLINE.name().equals(transaction.getTransType())) {
                Double sum = Double.valueOf(0);
                for (TransactionDetail transactionDetail : transaction.getTransactionDetails()) {
                    sum = (transactionDetail.getTotalSales());
                };
                takeAway = takeAway + (sum);

            }
            if (ETransactionType.TAKE_AWAY.name().equals(transaction.getTransType())) {
                Double sum = Double.valueOf(0);
                for (TransactionDetail transactionDetail : transaction.getTransactionDetails()) {
                    sum = (transactionDetail.getTotalSales());
                };
                online = online + (sum);
            }
        }

        return TotalSalesRespon.builder()
                .eatIn(eatIn)
                .online(online)
                .takeAway(takeAway)
                .build();
    }
    }
