package com.nirmaansetu.backend.modules.notifications.service;

import com.nirmaansetu.backend.modules.notifications.dto.NotificationResponseDto;
import com.nirmaansetu.backend.modules.notifications.entity.Notification;
import com.nirmaansetu.backend.modules.notifications.repository.NotificationRepository;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all notifications for the currently authenticated user, ordered by creation date (newest first).
     * @return List of NotificationResponseDto
     */
    public List<NotificationResponseDto> getAllNotifications() {
        User currentUser = getCurrentUser();
        return notificationRepository.findByUserOrderByCreatedAtDesc(currentUser).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific notification by ID, marks it as read, and ensures it belongs to the current user.
     * @param id The notification ID
     * @return NotificationResponseDto of the updated notification
     * @throws RuntimeException if notification is not found or user is unauthorized
     */
    @Transactional
    public NotificationResponseDto getNotificationById(Long id) {
        User currentUser = getCurrentUser();
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized: This notification does not belong to you");
        }

        notification.setRead(true);
        notificationRepository.save(notification);

        return toDto(notification);
    }

    /**
     * Creates and saves a new notification for a specific user.
     * @param user The recipient of the notification
     * @param title The subject of the notification
     * @param message The content body
     */
    @Transactional
    public void createNotification(User user, String title, String message) {
        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }

    /**
     * Helper method to fetch the User entity for the currently logged-in user session.
     * @return The User entity
     */
    private User getCurrentUser() {
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    /**
     * Maps Notification entity to its DTO representation.
     * @param notification The entity to map
     * @return The resulting DTO
     */
    private NotificationResponseDto toDto(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
