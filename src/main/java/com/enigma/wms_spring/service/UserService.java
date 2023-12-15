package com.enigma.wms_spring.service;

import com.enigma.wms_spring.entity.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    AppUser loadUserByUserId(String id);
}
