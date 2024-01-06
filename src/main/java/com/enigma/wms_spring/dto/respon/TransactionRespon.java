package com.enigma.wms_spring.dto.respon;

import com.enigma.wms_spring.entity.TransactionDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TransactionRespon {
    private String transactionId;
    private String receiptNumber;
    private String transDate;
    private String transactionType;
    private List<TransactionDetailRespon> transactionDetails;
}
