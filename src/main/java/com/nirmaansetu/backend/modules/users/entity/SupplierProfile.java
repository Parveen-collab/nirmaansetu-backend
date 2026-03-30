package com.nirmaansetu.backend.modules.users.entity;

import com.nirmaansetu.backend.shared.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "supplier_profiles")
@Data
@EqualsAndHashCode(callSuper = true)
public class SupplierProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shopName;
    private String shopCategory;
    private String shopSpeciality;

    @Enumerated(EnumType.STRING)
    private ShopType shopType;

    private String shopAddress;
    private String photoUrl;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}