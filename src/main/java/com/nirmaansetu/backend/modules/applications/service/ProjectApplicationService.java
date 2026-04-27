package com.nirmaansetu.backend.modules.applications.service;

import com.nirmaansetu.backend.modules.applications.dto.ApplicationStatusUpdateDto;
import com.nirmaansetu.backend.modules.applications.dto.ApplyWorkRequestDto;
import com.nirmaansetu.backend.modules.applications.entity.ProjectApplication;
import com.nirmaansetu.backend.modules.applications.enums.ApplicationStatus;
import com.nirmaansetu.backend.modules.applications.repository.ProjectApplicationRepository;
import com.nirmaansetu.backend.modules.notifications.service.NotificationService;
import com.nirmaansetu.backend.modules.projects.entity.Project;
import com.nirmaansetu.backend.modules.projects.entity.ProjectRole;
import com.nirmaansetu.backend.modules.projects.repository.ProjectRoleRepository;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectApplicationService {

    @Autowired
    private ProjectApplicationRepository projectApplicationRepository;

    @Autowired
    private ProjectRoleRepository projectRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public void applyForWork(ApplyWorkRequestDto dto) {
        User currentUser = getCurrentUser();
        ProjectRole role = projectRoleRepository.findById(dto.getProjectRoleId())
                .orElseThrow(() -> new RuntimeException("Project role not found"));

        if (projectApplicationRepository.findByUserAndProjectRole(currentUser, role).isPresent()) {
            throw new RuntimeException("You have already applied for this role");
        }

        ProjectApplication application = ProjectApplication.builder()
                .user(currentUser)
                .projectRole(role)
                .coverLetter(dto.getCoverLetter())
                .status(ApplicationStatus.PENDING)
                .build();

        projectApplicationRepository.save(application);

        // Notify the project creator
        notifyProjectCreator(application);
    }

    @Transactional
    public void updateApplicationStatus(Long id, ApplicationStatusUpdateDto dto) {
        ProjectApplication application = projectApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Validate if current user is the project creator
        User currentUser = getCurrentUser();
        if (!application.getProjectRole().getProject().getCreatedBy().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized: Only the project creator can update application status");
        }

        application.setStatus(dto.getStatus());
        projectApplicationRepository.save(application);

        // Notify the applicant
        notifyApplicant(application);
    }

    private void notifyApplicant(ProjectApplication application) {
        User applicant = application.getUser();
        String projectName = application.getProjectRole().getProject().getTitle();
        ApplicationStatus status = application.getStatus();

        String title = "Application Status Updated";
        String message;

        if (status == ApplicationStatus.APPROVED) {
            message = String.format("Congratulations! Your application for '%s' has been approved.", projectName);
        } else if (status == ApplicationStatus.REJECTED) {
            message = String.format("Update: Your application for '%s' was not selected.", projectName);
        } else {
            message = String.format("Your application for '%s' is now %s.", projectName, status);
        }

        notificationService.createNotification(applicant, title, message);
    }

    private void notifyProjectCreator(ProjectApplication application) {
        Project project = application.getProjectRole().getProject();
        User creator = project.getCreatedBy();
        
        String title = "New Job Application";
        String message = String.format("User %s has applied for the role of %s in your project '%s'.",
                application.getUser().getFullName(),
                application.getProjectRole().getRoleName(),
                project.getTitle());

        notificationService.createNotification(creator, title, message);
    }

    private User getCurrentUser() {
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }
}
