package com.nirmaansetu.backend.modules.users.controller;

import com.nirmaansetu.backend.modules.users.dto.UserRequestDto;
import com.nirmaansetu.backend.modules.users.dto.UserResponseDto;
import com.nirmaansetu.backend.modules.users.entity.Role;
import com.nirmaansetu.backend.modules.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller for managing user-related operations including registration, retrieval, updates, and soft deletion.
 */
@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User APIs", description = "Operations related to users")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Registers a new user with a specific role and optional profile photo.
     * Expects a multipart request containing user details as JSON and an optional image file.
     */
    @Operation(
            summary = "Create user with a specific role.",
            description = "You can create user with a specific role and you have to provide role specific details too.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> register(
            @Valid @RequestPart("user") UserRequestDto request,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        return ResponseEntity.ok(userService.registerUser(request, photo));
    }

    /**
     * Retrieves detailed information for a specific user by their ID.
     */
    @Operation(
            summary = "Get users by id",
            description = "Returns only users where deleted = false. Requires USER or ADMIN role.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserDetails(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Lists all users, excluding those with the SUPER_ADMIN role.
     * Supports filtering by role and keyword search.
     */
    @Operation(
            summary = "Get all users except super admin",
            description = "Returns all users except those with SUPER_ADMIN role. Can filter by role.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(userService.getAllUsersExceptSuperAdmin(role, keyword));
    }

    /**
     * Updates an existing user's information.
     * Allows partial updates via PatchMapping.
     */
    @Operation(
            summary = "Update user by id",
            description = "You can update a particular field and can keep all other the same or you can update as many fields as you have passed.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> updateDetails(
        @PathVariable Long id,
        @Valid @RequestPart("user") UserRequestDto request,
        @RequestPart(value = "photo", required = false) MultipartFile photo) {
        return ResponseEntity.ok(userService.updateUser(id, request, photo));
    }

    /**
     * Performs a soft delete on a user by their ID.
     * Soft-deleted users can be restored by a Super Admin within 24 hours.
     */
    @Operation(
            summary = "Delete user by id",
            description = "You can soft delete the user, user can be restored within 24 hours by Super Admin only and after 24 hours data will be deleted permanently. then even Super Admin could not restore the data.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
