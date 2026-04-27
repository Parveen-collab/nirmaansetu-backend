package com.nirmaansetu.backend.modules.projects.service;

import com.nirmaansetu.backend.modules.auth.service.SmsService;
import com.nirmaansetu.backend.modules.notifications.service.NotificationService;
import com.nirmaansetu.backend.modules.projects.dto.ProjectRequestDto;
import com.nirmaansetu.backend.modules.projects.dto.ProjectResponseDto;
import com.nirmaansetu.backend.modules.projects.entity.Project;
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

        Project savedProject = projectRepository.save(project);
        
        // Trigger proximity notifications (5km = 5000 meters)
        notifyNearbyUsers(savedProject);

        return projectMapper.toProjectResponseDto(savedProject);
    }

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

    public List<ProjectResponseDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toProjectResponseDto)
                .collect(Collectors.toList());
    }

    public ProjectResponseDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return projectMapper.toProjectResponseDto(project);
    }

    @Transactional
    public ProjectResponseDto updateProject(Long id, ProjectRequestDto dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        validateOwnership(project);

        projectMapper.updateProjectFromDto(dto, project);
        // MapStruct expression in mapper might not handle bidirectional link for new roles properly during update
        if (project.getRoles() != null) {
            project.getRoles().forEach(role -> role.setProject(project));
        }

        Project updatedProject = projectRepository.save(project);
        return projectMapper.toProjectResponseDto(updatedProject);
    }

    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        
        validateOwnership(project);
        projectRepository.delete(project);
    }

    private void validateOwnership(Project project) {
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!project.getCreatedBy().getPhoneNumber().equals(phoneNumber)) {
            throw new RuntimeException("Unauthorized: Only the creator can modify this project");
        }
    }
}
