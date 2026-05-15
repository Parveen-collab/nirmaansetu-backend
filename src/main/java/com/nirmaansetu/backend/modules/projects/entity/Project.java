package com.nirmaansetu.backend.modules.projects.entity;

import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.shared.utils.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a construction project in the system.
 * Stores core project information including location, creator, and available roles.
 * Supports soft delete via the deleted flag.
 */
@Entity
@Table(name = "projects", indexes = {
        @Index(name = "idx_project_location", columnList = "latitude, longitude")
})
@Data
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE projects SET deleted = true, deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted = false")
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Title is required")
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Description is required")
    private String description;

    @Column(nullable = false)
    @NotBlank(message = "Location name is required")
    private String locationName;

    @Column(nullable = false)
    @NotNull(message = "Latitude is required")
    private Double latitude;

    @Column(nullable = false)
    @NotNull(message = "Longitude is required")
    private Double longitude;

    /**
     * The user who created this project.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    /**
     * List of roles (positions) available or required for this project.
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectRole> roles = new ArrayList<>();

    /**
     * Current status of the project (e.g., PLANNING, IN_PROGRESS, COMPLETED).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.PLANNING;

    /**
     * Flag for soft deletion.
     */
    private boolean deleted = false;

    /**
     * Timestamp when the project was soft-deleted.
     */
    private LocalDateTime deletedAt;

    /**
     * Helper method to add a role to the project and maintain bidirectional relationship.
     * @param role The role to add
     */
    public void addRole(ProjectRole role) {
        roles.add(role);
        role.setProject(this);
    }

    /**
     * Helper method to remove a role from the project.
     * @param role The role to remove
     */
    public void removeRole(ProjectRole role) {
        roles.remove(role);
        role.setProject(null);
    }
}
