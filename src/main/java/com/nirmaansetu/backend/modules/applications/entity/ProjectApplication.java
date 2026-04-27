package com.nirmaansetu.backend.modules.applications.entity;

import com.nirmaansetu.backend.modules.applications.enums.ApplicationStatus;
import com.nirmaansetu.backend.modules.projects.entity.ProjectRole;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.shared.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_role_id", nullable = false)
    private ProjectRole projectRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    private boolean isDirectOffer = false;
}
