package com.nirmaansetu.backend.modules.projects.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nirmaansetu.backend.modules.projects.dto.ProjectRequestDto;
import com.nirmaansetu.backend.modules.projects.dto.ProjectResponseDto;
import com.nirmaansetu.backend.modules.projects.service.EstimationService;
import com.nirmaansetu.backend.modules.projects.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private EstimationService estimationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateProject_Success() throws Exception {
        ProjectRequestDto request = new ProjectRequestDto();
        request.setTitle("Test Project");

        when(projectService.createProject(any())).thenReturn(new ProjectResponseDto());

        mockMvc.perform(post("/api/v1/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetAllProjects_Success() throws Exception {
        when(projectService.getAllProjects()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProjectById_Success() throws Exception {
        when(projectService.getProjectById(1L)).thenReturn(new ProjectResponseDto());

        mockMvc.perform(get("/api/v1/projects/1"))
                .andExpect(status().isOk());
    }
}
