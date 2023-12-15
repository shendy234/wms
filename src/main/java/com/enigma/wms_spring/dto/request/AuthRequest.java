package com.enigma.wms_spring.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AuthRequest {
    private String username;
    private String password;
    private String name;
    private String mobilePhone;

}
