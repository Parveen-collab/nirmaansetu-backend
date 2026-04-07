package com.nirmaansetu.backend.modules.users.task;

import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserCleanupTask {

    @Autowired
    private UserRepository userRepository;

    @Value("${app.cleanup.user.retention-hours:24}")
    private int retentionHours;

    @Scheduled(cron = "${app.cleanup.user.cron:0 0 * * * *}")
    public void cleanupDeletedUsers() {
        LocalDateTime expirationDate = LocalDateTime.now().minusHours(retentionHours);
        userRepository.deleteSoftDeletedUsersOlderThan(expirationDate);
    }
}
