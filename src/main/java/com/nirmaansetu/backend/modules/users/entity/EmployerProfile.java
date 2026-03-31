package com.nirmaansetu.backend.modules.users.entity;

import com.nirmaansetu.backend.shared.utils.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "employer_profiles")
@Data
@EqualsAndHashCode(callSuper = true)
public class EmployerProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Company name is required")
    private String companyName;

    @Column(nullable = false)
    @NotBlank(message = "Company address is required")
    private String companyAddress;

    private String photoUrl;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}