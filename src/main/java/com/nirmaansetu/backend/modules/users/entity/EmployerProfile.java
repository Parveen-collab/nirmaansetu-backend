package com.nirmaansetu.backend.modules.users.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "employer_profiles")
@Data
public class EmployerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String companyAddress;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}