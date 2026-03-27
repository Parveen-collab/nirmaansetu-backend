package com.nirmaansetu.backend.modules.users.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "supplier_profiles")
@Data
public class SupplierProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shopName;
    private String shopCategory;
    private String shopSpeciality;

    @Enumerated(EnumType.STRING)
    private ShopType shopType;

    private String shopAddress;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}