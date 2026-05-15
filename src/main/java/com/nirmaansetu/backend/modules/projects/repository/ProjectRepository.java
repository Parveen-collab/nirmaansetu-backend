package com.nirmaansetu.backend.modules.projects.repository;

import com.nirmaansetu.backend.modules.projects.entity.Project;
import com.nirmaansetu.backend.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Project entities.
 * Extends JpaRepository to provide standard CRUD operations.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Finds users within a specified geographic radius from a given longitude and latitude.
     * Uses a native PostgreSQL/PostGIS query for spherical distance calculation.
     * 
     * @param latitude Center point latitude
     * @param longitude Center point longitude
     * @param radius Maximum distance in meters
     * @return List of users within the specified radius
     */
    @Query(value = "SELECT u.* FROM users u " +
            "JOIN addresses a ON u.id = a.user_id " +
            "WHERE a.type = 'CURRENT' " +
            "AND ST_Distance_Sphere(POINT(a.longitude, a.latitude), POINT(:longitude, :latitude)) <= :radius",
            nativeQuery = true)
    List<User> findUsersWithinRadius(@Param("latitude") Double latitude, 
                                     @Param("longitude") Double longitude, 
                                     @Param("radius") Double radius);

    /**
     * Retrieves all projects created by a specific user.
     * 
     * @param userId The ID of the creator
     * @return List of projects associated with the user
     */
    List<Project> findByCreatedById(Long userId);

    /**
     * Finds all projects that have at least one role with available spots.
     * A role is available if its occupiedCount is less than totalRequired.
     * 
     * @return List of projects with open positions
     */
    @Query("SELECT DISTINCT p FROM Project p JOIN p.roles r WHERE r.occupiedCount < r.totalRequired")
    List<Project> findAllWithAvailableRoles();
}
