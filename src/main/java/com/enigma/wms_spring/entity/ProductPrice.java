package com.enigma.wms_spring.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "m_product_price")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "stock", columnDefinition = "int check (stock > 0)")
    private Integer stock;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "price")
    private Double price;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

}
