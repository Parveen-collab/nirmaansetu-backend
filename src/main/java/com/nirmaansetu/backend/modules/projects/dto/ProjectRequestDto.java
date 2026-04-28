package com.nirmaansetu.backend.modules.projects.dto;

import com.nirmaansetu.backend.modules.projects.entity.ProjectStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location name is required")
    private String locationName;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    private ProjectStatus status;

    @Valid
    @NotNull(message = "At least one role is required")
    private List<ProjectRoleDto> roles;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectRoleDto {
        private Long id;

        @NotBlank(message = "Role name is required")
        private String roleName;

        @NotNull(message = "Total required count is required")
        @Min(value = 1, message = "At least 1 person is required for a role")
        private Integer totalRequired;

        private Integer occupiedCount;
    }
}
