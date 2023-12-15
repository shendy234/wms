package com.enigma.wms_spring.service.impl;

import com.enigma.wms_spring.constant.ERole;
import com.enigma.wms_spring.dto.request.AuthRequest;
import com.enigma.wms_spring.dto.respon.LoginRespon;
import com.enigma.wms_spring.dto.respon.RegisterRespon;
import com.enigma.wms_spring.entity.Admin;
import com.enigma.wms_spring.entity.AppUser;
import com.enigma.wms_spring.entity.Role;
import com.enigma.wms_spring.entity.UserCredential;
import com.enigma.wms_spring.repository.UserCredentialRepository;
import com.enigma.wms_spring.security.JwtUtil;
import com.enigma.wms_spring.service.AdminService;
import com.enigma.wms_spring.service.AuthService;
import com.enigma.wms_spring.service.RoleService;
import com.enigma.wms_spring.util.ValidationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminService adminService;
    private final RoleService roleService;
    private final JwtUtil jwtUtil;
    private final ValidationUtil validationUtil;
    private final AuthenticationManager authenticationManager;
    @Transactional(rollbackOn = Exception.class)
    @Override
    public RegisterRespon registerAdmin(AuthRequest request) {
        try {
            validationUtil.validate(request);

            //TODO 1 : Set Role
            Role role = Role.builder()
                    .name(ERole.ROLE_ADMIN)
                    .build();
            role = roleService.getOrSave(role);
            //TODO 2: Set Credetial
            UserCredential userCredential = UserCredential.builder()
                    .username(request.getUsername().toLowerCase())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(role)
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);
            //TODO 3 Set Admin
            Admin admin = Admin.builder()
                    .userCredential(userCredential)
                    .name(request.getUsername())
                    .phoneNumber(request.getMobilePhone())
                    .build();
            adminService.createNewAdmin(admin);

            return RegisterRespon.builder()
                    .username(userCredential.getUsername())
                    .role(userCredential.getRole().getName().toString())
                    .build();
        }catch (DataIntegrityViolationException e){
            throw  new ResponseStatusException(HttpStatus.CONFLICT,"User already exist");
        }
    }

    @Override
    public LoginRespon login(AuthRequest authRequest) {
        validationUtil.validate(authRequest);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUsername().toLowerCase(),
                authRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //object appUser
        AppUser appUser = (AppUser)  authentication.getPrincipal();
        String token = jwtUtil.generateToken(appUser);
        return LoginRespon.builder()
                .token(token)
                .role(appUser.getRole().name())
                .build();
    }
}
