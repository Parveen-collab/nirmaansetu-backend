package com.nirmaansetu.backend.modules.users.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "employee_profiles")
@Data
public class EmployeeProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceCategory;
    private String serviceSpeciality;
    private int experienceYears;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
