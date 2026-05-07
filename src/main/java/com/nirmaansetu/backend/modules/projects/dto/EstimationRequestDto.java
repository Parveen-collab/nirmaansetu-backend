package com.nirmaansetu.backend.modules.projects.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstimationRequestDto {
    @NotBlank(message = "Project description is required")
    private String description;
}
