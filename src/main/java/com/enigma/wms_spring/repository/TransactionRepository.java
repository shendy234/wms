package com.enigma.wms_spring.repository;

import com.enigma.wms_spring.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,String>, JpaSpecificationExecutor<Transaction> {
}
