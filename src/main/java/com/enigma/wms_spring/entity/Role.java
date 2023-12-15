package com.enigma.wms_spring.entity;


import com.enigma.wms_spring.constant.ERole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_role")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private ERole name;
}
