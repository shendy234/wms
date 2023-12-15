package com.enigma.wms_spring.repository;

import com.enigma.wms_spring.constant.ERole;
import com.enigma.wms_spring.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
