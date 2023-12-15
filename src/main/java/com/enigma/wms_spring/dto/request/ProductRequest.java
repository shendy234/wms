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
    @NotBlank(message = "product name is required")
    private String productCode;
    @NotBlank(message = "product name is required")
    private String productName;
    @NotBlank(message = "product price is required")
    @Min(value = 0, message = "price must be greater than equal 0")
    private Double price;
    @NotBlank(message = "product stock is required")
    @Min(value = 0, message = "stock must be greater than equal 0")
    private Integer stock;

    private BranchRespon branchId;
}
