package com.nirmaansetu.backend.modules.employer.controller;

import com.nirmaansetu.backend.modules.employer.dto.EmployerResponseDto;
import com.nirmaansetu.backend.modules.employer.service.EmployerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employers")
@Tag(name = "Employer APIs", description = "Operations related to employers")
public class EmployerController {

    @Autowired
    private EmployerService employerService;

    @Operation(
            summary = "Get all employers",
            description = "Returns a list of all registered employers with their profiles.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<List<EmployerResponseDto>> getAllEmployers() {
        return ResponseEntity.ok(employerService.getAllEmployers());
    }

    @Operation(
            summary = "Get employer by ID",
            description = "Returns a single employer profile by its ID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<EmployerResponseDto> getEmployerById(@PathVariable Long id) {
        return ResponseEntity.ok(employerService.getEmployerById(id));
    }
}
