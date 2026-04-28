package com.nirmaansetu.backend.modules.applications.repository;

import com.nirmaansetu.backend.modules.applications.entity.ProjectApplication;
import com.nirmaansetu.backend.modules.projects.entity.Project;
import com.nirmaansetu.backend.modules.projects.entity.ProjectRole;
import com.nirmaansetu.backend.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Long> {
    Optional<ProjectApplication> findByUserAndProjectRole(User user, ProjectRole projectRole);
    List<ProjectApplication> findByProjectRoleProject(Project project);
}
