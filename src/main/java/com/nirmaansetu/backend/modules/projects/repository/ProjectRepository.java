package com.nirmaansetu.backend.modules.projects.repository;

import com.nirmaansetu.backend.modules.projects.entity.Project;
import com.nirmaansetu.backend.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value = "SELECT u.* FROM users u " +
            "JOIN addresses a ON u.id = a.user_id " +
            "WHERE a.type = 'CURRENT' " +
            "AND ST_Distance_Sphere(POINT(a.longitude, a.latitude), POINT(:longitude, :latitude)) <= :radius",
            nativeQuery = true)
    List<User> findUsersWithinRadius(@Param("latitude") Double latitude, 
                                     @Param("longitude") Double longitude, 
                                     @Param("radius") Double radius);

    List<Project> findByCreatedById(Long userId);

    @Query("SELECT DISTINCT p FROM Project p JOIN p.roles r WHERE r.occupiedCount < r.totalRequired")
    List<Project> findAllWithAvailableRoles();
}
