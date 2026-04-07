package com.nirmaansetu.backend.modules.users.entity;

import com.nirmaansetu.backend.shared.utils.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_profiles")
@Data
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE employee_profiles SET deleted = true, deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted = false")
public class EmployeeProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Service category is required")
    private String serviceCategory;

    @Column(nullable = false)
    @NotBlank(message = "Service speciality is required")
    private String serviceSpeciality;

    @Column(nullable = false)
    @NotNull(message = "Experience years is required")
    @Min(value = 1, message = "Experience years must be greater than 0")
    private Integer experienceYears;
    private String photoUrl;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean deleted = false;

    private LocalDateTime deletedAt;
}
