package com.nirmaansetu.backend.modules.projects.controller;

import com.nirmaansetu.backend.modules.projects.dto.EstimationRequestDto;
import com.nirmaansetu.backend.modules.projects.dto.EstimationResponseDto;
import com.nirmaansetu.backend.modules.projects.service.EstimationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/projects/estimation")
@RequiredArgsConstructor
@Tag(name = "Project Estimation", description = "AI-powered project cost and material estimation")
public class ProjectEstimationController {

    private final EstimationService estimationService;

    @PostMapping
    @Operation(summary = "Get AI-powered estimation for a project")
    public ResponseEntity<EstimationResponseDto> getEstimation(@Valid @RequestBody EstimationRequestDto request) {
        return ResponseEntity.ok(estimationService.estimateProject(request.getDescription()));
    }
}
