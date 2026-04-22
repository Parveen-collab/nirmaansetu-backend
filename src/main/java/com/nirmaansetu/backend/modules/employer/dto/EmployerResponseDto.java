package com.nirmaansetu.backend.modules.employer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployerResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String companyName;
    private String state;
    private String district;
    private String wardNumber;
    private String landmark;
    private String pincode;
    private String areaVillage;
    private String building;
    private String photoUrl;
    private String name;
    private String phoneNumber;
    private String email;
    private String profileImageUrl;
}
