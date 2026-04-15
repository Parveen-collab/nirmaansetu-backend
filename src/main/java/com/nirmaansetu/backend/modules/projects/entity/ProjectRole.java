package com.nirmaansetu.backend.modules.projects.entity;

import com.nirmaansetu.backend.shared.utils.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "project_roles")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Role name is required")
    private String roleName;

    @Column(nullable = false)
    @NotNull(message = "Total required count is required")
    @Min(value = 1, message = "At least 1 person is required for a role")
    private Integer totalRequired;

    @Column(nullable = false)
    private Integer occupiedCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public boolean isOccupied() {
        return occupiedCount >= totalRequired;
    }
}
