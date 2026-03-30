package com.nirmaansetu.backend.modules.users.task;

import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserCleanupTask {

    @Autowired
    private UserRepository userRepository;

    // Run every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanupDeletedUsers() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        userRepository.deleteSoftDeletedUsersOlderThan(thirtyDaysAgo);
    }
}
