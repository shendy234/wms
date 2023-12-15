package com.enigma.wms_spring.dto.respon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AdminRespon {
    private String id;
    private String name;
    private String phoneNumber;

}
