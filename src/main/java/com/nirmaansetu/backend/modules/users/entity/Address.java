package com.nirmaansetu.backend.modules.users.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "addresses")
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String state;
    private String district;
    private String wardNumber;
    private String landmark;
    private String pincode;
    private String areaVillage;
    private String building;

    @Enumerated(EnumType.STRING)
    private AddressType type; // PERMANENT / CURRENT

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
