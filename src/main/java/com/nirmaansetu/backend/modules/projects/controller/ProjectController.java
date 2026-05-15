package com.nirmaansetu.backend.modules.projects.controller;

import com.nirmaansetu.backend.modules.projects.dto.ProjectRequestDto;
import com.nirmaansetu.backend.modules.projects.dto.ProjectResponseDto;
import com.nirmaansetu.backend.modules.projects.entity.ProjectStatus;
import com.nirmaansetu.backend.modules.projects.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing construction projects.
 * Provides endpoints for project lifecycle management including creation, retrieval, status updates, and deletion.
 */
@RestController
@RequestMapping("/api/v1/projects")
@Tag(name = "Project APIs", description = "Operations related to construction projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * Creates a new construction project.
     * 
     * @param request Project details from request body
     * @return ResponseEntity containing the created project details
     */
    @Operation(
            summary = "Create a new project",
            description = "Creates a new construction project and triggers proximity notifications to nearby users.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody ProjectRequestDto request) {
        return ResponseEntity.ok(projectService.createProject(request));
    }

    /**
     * Retrieves all active construction projects.
     * 
     * @return List of project response DTOs
     */
    @Operation(
            summary = "Get all projects",
            description = "Returns a list of all active construction projects.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    /**
     * Retrieves a specific project by its unique ID.
     * 
     * @param id The project ID
     * @return ResponseEntity with project details
     */
    @Operation(
            summary = "Get project by ID",
            description = "Returns details of a specific project by its unique identifier.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    /**
     * Updates the status (milestone) of a project.
     * Only the project creator is authorized to perform this update.
     * 
     * @param id The project ID
     * @param status The new status (PLANNING, IN_PROGRESS, etc.)
     * @return ResponseEntity with updated project details
     */
    @Operation(
            summary = "Update project status",
            description = "Allows the creator to update the project's current status (milestone). Notifications will be sent to applicants.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProjectResponseDto> updateProjectStatus(
            @PathVariable Long id,
            @RequestParam ProjectStatus status) {
        return ResponseEntity.ok(projectService.updateProjectStatus(id, status));
    }

    /**
     * Updates details of a project.
     * 
     * @param id The project ID
     * @param request Updated project details
     * @return ResponseEntity with updated project details
     */
    @Operation(
            summary = "Update project by ID",
            description = "Allows the creator to update project details. Only the creator can modify the project.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequestDto request) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    /**
     * Performs a soft delete on a project.
     * 
     * @param id The project ID to delete
     * @return No content response
     */
    @Operation(
            summary = "Delete project by ID",
            description = "Allows the creator to soft delete a project. Only the creator can delete the project.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
