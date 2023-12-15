package com.enigma.wms_spring.entity;


import com.enigma.wms_spring.constant.ETransactionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_transaction_type")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TransactionType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private ETransactionType name;
}
