package com.enigma.wms_spring.dto.request;

import com.enigma.wms_spring.dto.respon.BranchRespon;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductRequest {
    private String productId;
    private String productCode;
    private String productName;
    private Double price;
    private String branchId;
}
