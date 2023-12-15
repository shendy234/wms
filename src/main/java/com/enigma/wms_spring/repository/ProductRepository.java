package com.enigma.wms_spring.repository;

import com.enigma.wms_spring.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {

}
