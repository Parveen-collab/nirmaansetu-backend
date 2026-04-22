package com.nirmaansetu.backend.modules.notifications.controller;

import com.nirmaansetu.backend.modules.notifications.dto.NotificationResponseDto;
import com.nirmaansetu.backend.modules.notifications.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notification APIs", description = "Operations related to user notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Operation(
            summary = "Get all notifications",
            description = "Returns a list of all notifications for the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @Operation(
            summary = "Get notification by ID",
            description = "Returns details of a specific notification and marks it as read.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDto> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }
}
