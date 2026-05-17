package com.nirmaansetu.backend.modules.applications.entity;

import com.nirmaansetu.backend.modules.applications.enums.ApplicationStatus;
import com.nirmaansetu.backend.modules.projects.entity.ProjectRole;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.shared.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing an application by a user for a specific role in a project.
 */
@Entity
@Table(name = "project_applications")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectApplication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who is applying for the role.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The specific project role being applied for.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_role_id", nullable = false)
    private ProjectRole projectRole;

    /**
     * Current status of the application (e.g., PENDING, ACCEPTED, REJECTED).
     */
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    /**
     * Optional message from the applicant to the employer.
     */
    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    /**
     * Flag indicating if this was a direct offer from the employer instead of a user-initiated application.
     */
    @Builder.Default
    private boolean isDirectOffer = false;
}
