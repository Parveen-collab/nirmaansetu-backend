package com.nirmaansetu.backend.modules.users.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    private String name;
    private String email;

    private String aadhaarNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileImageUrl;

}


//mobile number, full name, aadhaar number
//permanenet address = state, district, ward number, landmark, pincode, area/village, building
//current address = state, district, ward number, landmark, pincode, area/village, building
//rolw = employee, employer, supplier/shopkeepr

//for employee role = service category, service speciality, experience in years

//for employer role = company name, company address, company's photos

//for shopkeeper/supplier role = shop name, shop category, shop speciality, select shop type (retail, bulk, retail & bulk), shop address

// upload live photo

