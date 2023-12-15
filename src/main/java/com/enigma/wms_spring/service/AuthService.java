package com.enigma.wms_spring.service;

import com.enigma.wms_spring.dto.request.AuthRequest;
import com.enigma.wms_spring.dto.respon.LoginRespon;
import com.enigma.wms_spring.dto.respon.RegisterRespon;

public interface AuthService {
    RegisterRespon registerAdmin(AuthRequest request);
    LoginRespon login(AuthRequest authRequest);
}