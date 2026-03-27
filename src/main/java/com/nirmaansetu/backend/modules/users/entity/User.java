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

