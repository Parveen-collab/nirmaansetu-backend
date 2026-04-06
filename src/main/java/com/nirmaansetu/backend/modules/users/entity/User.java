package com.nirmaansetu.backend.modules.users.entity;

import com.nirmaansetu.backend.modules.auth.globalNumberValidator.ValidPhoneNumber;
import com.nirmaansetu.backend.modules.users.entity.Role;
import com.nirmaansetu.backend.shared.utils.BaseEntity;
import com.nirmaansetu.backend.utility.EncryptionConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_phone_number", columnList = "phoneNumber"),
        @Index(name = "idx_aadhaar_number", columnList = "aadhaarNumber")
})
@Data
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE users SET deleted = true, deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted = false")
public class User extends BaseEntity implements UserDetails {

    @Schema(description = "Unique ID", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Phone number is required")
    @ValidPhoneNumber
    private String phoneNumber;

    @Schema(description = "User name", example = "Parveen", required = true)
    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Aadhaar number is required")
    @Convert(converter = EncryptionConverter.class)
    private String aadhaarNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Role is required")
    private Role role;

    private String profileImageUrl;

    @Column(unique = true)
    private String photoHash;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    private List<Address> addresses;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @Valid
    private EmployeeProfile employeeProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @Valid
    private EmployerProfile employerProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @Valid
    private SupplierProfile supplierProfile;

    private boolean deleted = false;

    private LocalDateTime deletedAt;

    public void addAddress(Address address) {
        if (addresses == null) {
            addresses = new java.util.ArrayList<>();
        }
        addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(Address address) {
        if (addresses != null) {
            addresses.remove(address);
            address.setUser(null);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return ""; // Since we use OTP or other methods, password is not used
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
