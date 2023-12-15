package com.enigma.wms_spring.controller;

import com.enigma.wms_spring.constant.AppPath;
import com.enigma.wms_spring.dto.request.AuthRequest;
import com.enigma.wms_spring.dto.respon.CommonRespon;
import com.enigma.wms_spring.dto.respon.LoginRespon;
import com.enigma.wms_spring.dto.respon.RegisterRespon;
import com.enigma.wms_spring.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.AUTH)
public class AuthController {
    private final AuthService authService;

    @PostMapping("/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody AuthRequest request) {
        RegisterRespon registerRespon= authService.registerAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonRespon.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully Regis")
                        .data(registerRespon)
                        .build());
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        LoginRespon loginRespon = authService.login(authRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonRespon.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully Login")
                        .data(loginRespon)
                        .build());
    }
}
