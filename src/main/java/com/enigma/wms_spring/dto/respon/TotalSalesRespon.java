package com.enigma.wms_spring.dto.respon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TotalSalesRespon {
    private Double eatIn;
    private Double takeAway;
    private Double online;
}
