package com.enigma.wms_spring.service;

import com.enigma.wms_spring.entity.Role;

public interface RoleService {
    Role getOrSave(Role role);
}
