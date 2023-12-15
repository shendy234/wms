package com.enigma.wms_spring.service.impl;

import com.enigma.wms_spring.dto.respon.AdminRespon;
import com.enigma.wms_spring.entity.Admin;
import com.enigma.wms_spring.repository.AdminRepository;
import com.enigma.wms_spring.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    @Override
    public AdminRespon createNewAdmin(Admin request) {
        Admin admin = adminRepository.saveAndFlush(request);
        return AdminRespon.builder()
                .id(admin.getId())
                .name(admin.getName())
                .phoneNumber(admin.getPhoneNumber())
                .build();
    }
}