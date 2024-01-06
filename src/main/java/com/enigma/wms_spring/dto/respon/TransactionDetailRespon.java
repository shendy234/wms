package com.enigma.wms_spring.dto.respon;

import com.enigma.wms_spring.entity.ProductPrice;
import com.enigma.wms_spring.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TransactionDetailRespon {
    private String transactionDetailId;
    private String billDetailId;
    private ProductRespon product;
    private Integer quantity;
    private Double totalSales;
}
