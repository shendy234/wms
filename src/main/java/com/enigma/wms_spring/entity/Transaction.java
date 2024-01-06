package com.enigma.wms_spring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "t_transantion")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String receiptNumber;
    private Date transDate;
    private String transType;

    @OneToMany(mappedBy = "transaction")
    private List<TransactionDetail> transactionDetails;
}
