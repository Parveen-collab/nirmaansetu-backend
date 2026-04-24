package com.nirmaansetu.backend.modules.users.entity;

import com.nirmaansetu.backend.shared.utils.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_profiles")
@Data
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE supplier_profiles SET deleted = true, deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted = false")
public class SupplierProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Shop name is required")
    private String shopName;

    @Column(nullable = false)
    @NotBlank(message = "Shop category is required")
    private String shopCategory;

    @Column(nullable = false)
    @NotBlank(message = "Shop speciality is required")
    private String shopSpeciality;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Shop type is required")
    private ShopType shopType;

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

    private Double latitude;
    private Double longitude;

    private String photoUrl;

    @OneToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    private boolean deleted = false;

    private LocalDateTime deletedAt;
}