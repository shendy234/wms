package com.enigma.wms_spring.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BranchRequest {
    private String branchId;
    private String branchCode;
    private String branchName;
    private String address;
    private String phoneNumber;
}
