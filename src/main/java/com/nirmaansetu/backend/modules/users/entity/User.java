package com.nirmaansetu.backend.modules.users.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return ""; // Since we use OTP or other methods, password is not used
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


//mobile number, full name, aadhaar number
//permanenet address = state, district, ward number, landmark, pincode, area/village, building
//current address = state, district, ward number, landmark, pincode, area/village, building
//rolw = employee, employer, supplier/shopkeepr

//for employee role = service category, service speciality, experience in years

//for employer role = company name, company address, company's photos

//for shopkeeper/supplier role = shop name, shop category, shop speciality, select shop type (retail, bulk, retail & bulk), shop address

// upload live photo

//request body after hitting the /api/user/register
//{
//  "phoneNumber": "string",
//  "name": "string",
//  "email": "string",
//  "aadhaarNumber": "string",
//  "role": "EMPLOYEE | EMPLOYER | SUPPLIER",
//  "profileImageUrl": "string",
//  "addresses": [
//    {
//      "type": "PERMANENT | CURRENT",
//      "state": "string",
//      "district": "string",
//      "wardNumber": "string",
//      "landmark": "string",
//      "pincode": "string",
//      "areaVillage": "string",
//      "building": "string"
//    }
//  ],
//  "employeeProfile": {
//    "serviceCategory": "string",
//    "serviceSpeciality": "string",
//    "experienceYears": number
//  },
//  "employerProfile": {
//    "companyName": "string",
//    "companyAddress": "string",
//    "companyPhotos": ["string"]
//  },
//  "supplierProfile": {
//    "shopName": "string",
//    "shopCategory": "string",
//    "shopSpeciality": "string",
//    "shopType": "RETAIL | BULK | BOTH",
//    "shopAddress": "string"
//  }
//}

//response after hitting the /api/user/register
//{
//  "id": number,
//  "phoneNumber": "string",
//  "name": "string",
//  "role": "string",
//  "message": "User registered successfully"
//}

//corrected request body format
//{
//  "phoneNumber": "string",
//  "name": "string",
//  "email": "string",
//  "aadhaarNumber": "string",
//  "role": "EMPLOYEE | EMPLOYER | SUPPLIER",
//  "profileImageUrl": "string",
//  "addresses": [
//    {
//      "type": "PERMANENT | CURRENT",
//      "state": "string",
//      "district": "string",
//      "wardNumber": "string",
//      "landmark": "string",
//      "pincode": "string",
//      "areaVillage": "string",
//      "building": "string"
//    }
//  ],
//  // Include ONLY the one that matches the role:
//  "employeeProfile": { ... },
//  "employerProfile": { ... },
//  "supplierProfile": { ... }
//}

