package com.nirmaansetu.backend.modules.projects.dto;

import com.nirmaansetu.backend.modules.projects.entity.ProjectStatus;
import com.nirmaansetu.backend.modules.users.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDto {
    private Long id;
    private String title;
    private String description;
    private String locationName;
    private Double latitude;
    private Double longitude;
    private ProjectStatus status;
    private UserResponseDto createdBy;
    private List<ProjectRequestDto.ProjectRoleDto> roles;
    private Instant createdAt;
    private Instant updatedAt;
}
