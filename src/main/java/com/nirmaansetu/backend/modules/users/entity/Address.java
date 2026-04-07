package com.nirmaansetu.backend.modules.users.entity;

import com.nirmaansetu.backend.shared.utils.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
@Data
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE addresses SET deleted = true, deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted = false")
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "State is required")
    private String state;

    @Column(nullable = false)
    @NotBlank(message = "District is required")
    private String district;

    private String wardNumber;
    private String landmark;

    @Column(nullable = false)
    @NotBlank(message = "Pincode is required")
    private String pincode;

    @Column(nullable = false)
    @NotBlank(message = "Area/Village is required")
    private String areaVillage;

    private String building;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Address type is required")
    private AddressType type; // PERMANENT / CURRENT

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean deleted = false;

    private LocalDateTime deletedAt;
}
