package com.enigma.wms_spring.service;

import com.enigma.wms_spring.dto.request.TransactionRequest;
import com.enigma.wms_spring.dto.respon.TotalSalesRespon;
import com.enigma.wms_spring.dto.respon.TransactionRespon;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {
    TransactionRespon createNewTransaction(TransactionRequest transactionRequest);
    TransactionRespon getTransactionById(String id);
    Page<TransactionRespon> getTransactions(
            Integer page,
            Integer size,
            String receiptNumber,
            String startDate,
            String endDate,
            String transType,
            String productName);
    TotalSalesRespon getTotalSales(String startDate, String endDate);
}
