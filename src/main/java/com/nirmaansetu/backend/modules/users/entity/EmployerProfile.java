package com.nirmaansetu.backend.modules.users.entity;

import com.nirmaansetu.backend.shared.utils.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "employer_profiles")
@Data
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE employer_profiles SET deleted = true, deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted = false")
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

    private boolean deleted = false;

    private LocalDateTime deletedAt;
}