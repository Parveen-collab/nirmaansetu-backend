package com.nirmaansetu.backend.modules.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Data Transfer Object for Employee Response.
 * Contains both employee-specific profile information and basic user details.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String serviceCategory;
    private String serviceSpeciality;
    private Integer experienceYears;
    private String photoUrl;

    // The following fields are mapped from the associated User entity
    private String name;
    private String phoneNumber;
    private String email;
    private String profileImageUrl;
}
