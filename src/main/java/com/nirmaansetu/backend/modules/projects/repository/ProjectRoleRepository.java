package com.nirmaansetu.backend.modules.projects.repository;

import com.nirmaansetu.backend.modules.projects.entity.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRoleRepository extends JpaRepository<ProjectRole, Long> {
}
