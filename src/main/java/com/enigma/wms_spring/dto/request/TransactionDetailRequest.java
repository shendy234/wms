package com.enigma.wms_spring.dto.request;

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
public class TransactionDetailRequest {
    private String productPriceId;
    private Integer quantity;

}
