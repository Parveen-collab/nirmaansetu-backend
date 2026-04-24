package com.nirmaansetu.backend.modules.users.controller;

import com.nirmaansetu.backend.modules.users.dto.UserRequestDto;
import com.nirmaansetu.backend.modules.users.dto.UserResponseDto;
import com.nirmaansetu.backend.modules.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin APIs", description = "Operations related to administration")
public class AdminController {

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Create an admin or super admin user.",
            description = "Only Super Admin can create other admins or super admins.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/create-admin", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserResponseDto> createAdmin(
            @Valid @RequestPart("user") UserRequestDto request,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        return ResponseEntity.ok(userService.registerAdmin(request, photo));
    }
}
