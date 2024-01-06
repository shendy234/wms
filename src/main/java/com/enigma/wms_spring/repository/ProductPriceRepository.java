package com.enigma.wms_spring.repository;

import com.enigma.wms_spring.entity.Product;
import com.enigma.wms_spring.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, String> {

}
