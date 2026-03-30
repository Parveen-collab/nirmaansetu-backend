package com.nirmaansetu.backend.modules.users.entity;

import com.nirmaansetu.backend.shared.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "employee_profiles")
@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeProfile extends BaseEntity {

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
