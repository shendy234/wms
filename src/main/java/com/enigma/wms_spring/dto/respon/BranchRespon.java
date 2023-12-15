package com.enigma.wms_spring.dto.respon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BranchRespon {
    private String Id;
    private String branchCode;
    private String branchName;
    private String address;
    private String phoneNumber;
}
