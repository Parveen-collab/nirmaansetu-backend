package com.nirmaansetu.backend.modules.projects.controller;

import com.nirmaansetu.backend.modules.projects.dto.ProjectRequestDto;
import com.nirmaansetu.backend.modules.projects.dto.ProjectResponseDto;
import com.nirmaansetu.backend.modules.projects.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@Tag(name = "Project APIs", description = "Operations related to construction projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Operation(
            summary = "Create a new project",
            description = "Creates a new construction project and triggers proximity notifications to nearby users.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody ProjectRequestDto request) {
        return ResponseEntity.ok(projectService.createProject(request));
    }

    @Operation(
            summary = "Get all projects",
            description = "Returns a list of all active construction projects.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @Operation(
            summary = "Get project by ID",
            description = "Returns details of a specific project by its unique identifier.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

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
