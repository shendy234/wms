package com.enigma.wms_spring.repository;

import com.enigma.wms_spring.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
}
