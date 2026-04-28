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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectRole> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.PLANNING;

    private boolean deleted = false;

    private LocalDateTime deletedAt;

    public void addRole(ProjectRole role) {
        roles.add(role);
        role.setProject(this);
    }

    public void removeRole(ProjectRole role) {
        roles.remove(role);
        role.setProject(null);
    }
}
