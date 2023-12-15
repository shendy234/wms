package com.enigma.wms_spring.service;

import com.enigma.wms_spring.dto.respon.AdminRespon;
import com.enigma.wms_spring.entity.Admin;

public interface AdminService {
    AdminRespon createNewAdmin(Admin request);
}
