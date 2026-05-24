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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private SmsService smsService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ProjectApplicationRepository applicationRepository;

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private User testUser;
    private Project testProject;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setPhoneNumber("1234567890");

        testProject = new Project();
        testProject.setId(1L);
        testProject.setTitle("Test Project");
        testProject.setCreatedBy(testUser);
        testProject.setStatus(ProjectStatus.PLANNING);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testCreateProject() {
        ProjectRequestDto dto = new ProjectRequestDto();
        dto.setTitle("Test Project");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("1234567890");
        when(userRepository.findByPhoneNumber("1234567890")).thenReturn(Optional.of(testUser));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);
        when(projectMapper.toProjectResponseDto(any(Project.class))).thenReturn(new ProjectResponseDto());

        ProjectResponseDto result = projectService.createProject(dto);

        assertNotNull(result);
        verify(projectRepository).save(any(Project.class));
        verify(projectRepository).findUsersWithinRadius(anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    void testGetProjectById_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectMapper.toProjectResponseDto(testProject)).thenReturn(new ProjectResponseDto());

        ProjectResponseDto result = projectService.getProjectById(1L);

        assertNotNull(result);
        verify(projectRepository).findById(1L);
    }

    @Test
    void testGetProjectById_NotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> projectService.getProjectById(1L));
    }

    @Test
    void testUpdateProjectStatus_Success() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("1234567890");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);
        when(projectMapper.toProjectResponseDto(any(Project.class))).thenReturn(new ProjectResponseDto());

        ProjectResponseDto result = projectService.updateProjectStatus(1L, ProjectStatus.COMPLETED);

        assertNotNull(result);
        assertEquals(ProjectStatus.COMPLETED, testProject.getStatus());
    }

    @Test
    void testDeleteProject_Success() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("1234567890");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        projectService.deleteProject(1L);

        verify(projectRepository).delete(testProject);
    }
}
