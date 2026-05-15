package com.nirmaansetu.backend.modules.projects.service;

import com.nirmaansetu.backend.modules.applications.repository.ProjectApplicationRepository;
import com.nirmaansetu.backend.modules.auth.service.SmsService;
import com.nirmaansetu.backend.modules.notifications.service.NotificationService;
import com.nirmaansetu.backend.modules.projects.dto.ProjectRequestDto;
import com.nirmaansetu.backend.modules.projects.dto.ProjectResponseDto;
import com.nirmaansetu.backend.modules.projects.entity.Project;
import com.nirmaansetu.backend.modules.projects.entity.ProjectStatus;
import com.nirmaansetu.backend.modules.projects.mapper.ProjectMapper;
import com.nirmaansetu.backend.modules.projects.repository.ProjectRepository;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing project-related business logic.
 * Handles project creation, updates, deletions, and notifications for nearby users.
 */
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private SmsService smsService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ProjectApplicationRepository applicationRepository;

    /**
     * Creates a new project and triggers notifications for nearby users.
     * 
     * @param dto Data transfer object containing project details
     * @return Created project details as a response DTO
     */
    @Transactional
    public ProjectResponseDto createProject(ProjectRequestDto dto) {
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        Project project = new Project();
        projectMapper.updateProjectFromDto(dto, project);
        project.setCreatedBy(currentUser);
        
        // Ensure bidirectional relationship for roles
        if (project.getRoles() != null) {
            project.getRoles().forEach(role -> role.setProject(project));
        }

        if (dto.getStatus() != null) {
            project.setStatus(dto.getStatus());
        }

        Project savedProject = projectRepository.save(project);
        
        // Trigger proximity notifications (5km = 5000 meters)
        notifyNearbyUsers(savedProject);

        return projectMapper.toProjectResponseDto(savedProject);
    }

    /**
     * Finds users within a 5km radius of the project and sends them SMS and in-app notifications.
     * 
     * @param project The newly created project
     */
    private void notifyNearbyUsers(Project project) {
        List<User> nearbyUsers = projectRepository.findUsersWithinRadius(
                project.getLatitude(), project.getLongitude(), 5000.0);

        String title = "New Project Nearby";
        String message = String.format("New construction project near you: %s at %s. Apply now on NirmaanSetu!",
                project.getTitle(), project.getLocationName());

        nearbyUsers.forEach(user -> {
            if (!user.getId().equals(project.getCreatedBy().getId())) {
                smsService.sendSms(user.getPhoneNumber(), message);
                notificationService.createNotification(user, title, message);
            }
        });
    }

    /**
     * Updates the status of an existing project.
     * 
     * @param id Project ID
     * @param status New status to apply
     * @return Updated project details
     */
    @Transactional
    public ProjectResponseDto updateProjectStatus(Long id, ProjectStatus status) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        validateOwnership(project);

        ProjectStatus oldStatus = project.getStatus();
        if (oldStatus == status) {
            return projectMapper.toProjectResponseDto(project);
        }

        project.setStatus(status);
        Project savedProject = projectRepository.save(project);

        // Notify applicants about milestone/status change
        notifyApplicantsOfStatusChange(savedProject, oldStatus);

        return projectMapper.toProjectResponseDto(savedProject);
    }

    /**
     * Notifies all users who have applied for roles in the project about a status change.
     * 
     * @param project The updated project
     * @param oldStatus Previous status
     */
    private void notifyApplicantsOfStatusChange(Project project, ProjectStatus oldStatus) {
        applicationRepository.findByProjectRoleProject(project).stream()
                .map(application -> application.getUser())
                .distinct()
                .forEach(user -> {
                    String title = "Project Milestone Reached";
                    String message = String.format("Project '%s' status updated from %s to %s.",
                            project.getTitle(), oldStatus, project.getStatus());
                    notificationService.createNotification(user, title, message);
                });
    }

    /**
     * Retrieves all projects in the system.
     * 
     * @return List of all project response DTOs
     */
    public List<ProjectResponseDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toProjectResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a project by its ID.
     * 
     * @param id Project ID
     * @return Project details
     */
    public ProjectResponseDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return projectMapper.toProjectResponseDto(project);
    }

    /**
     * Updates project details. Only the creator can perform this action.
     * 
     * @param id Project ID
     * @param dto New project data
     * @return Updated project details
     */
    @Transactional
    public ProjectResponseDto updateProject(Long id, ProjectRequestDto dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        validateOwnership(project);

        ProjectStatus oldStatus = project.getStatus();
        projectMapper.updateProjectFromDto(dto, project);
        
        // MapStruct expression in mapper might not handle bidirectional link for new roles properly during update
        if (project.getRoles() != null) {
            project.getRoles().forEach(role -> role.setProject(project));
        }

        if (dto.getStatus() != null) {
            project.setStatus(dto.getStatus());
        }

        Project updatedProject = projectRepository.save(project);

        if (oldStatus != updatedProject.getStatus()) {
            notifyApplicantsOfStatusChange(updatedProject, oldStatus);
        }

        return projectMapper.toProjectResponseDto(updatedProject);
    }

    /**
     * Soft deletes a project by ID. Only the creator can perform this action.
     * 
     * @param id Project ID
     */
    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        
        validateOwnership(project);
        projectRepository.delete(project);
    }

    /**
     * Validates that the current authenticated user is the one who created the project.
     * 
     * @param project Project to validate against
     */
    private void validateOwnership(Project project) {
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!project.getCreatedBy().getPhoneNumber().equals(phoneNumber)) {
            throw new RuntimeException("Unauthorized: Only the creator can modify this project");
        }
    }
}
