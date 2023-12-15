package com.enigma.wms_spring.repository;

import com.enigma.wms_spring.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, String> {
}
