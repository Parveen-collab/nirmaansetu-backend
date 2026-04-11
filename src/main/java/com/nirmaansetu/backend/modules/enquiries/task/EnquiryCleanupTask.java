package com.nirmaansetu.backend.modules.enquiries.task;

import com.nirmaansetu.backend.modules.enquiries.repository.EnquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnquiryCleanupTask {

    private final EnquiryRepository enquiryRepository;

    // Run every hour to check for expired enquiries
    @Scheduled(cron = "0 0 * * * *")
    public void cleanupExpiredEnquiries() {
        LocalDateTime expirationDate = LocalDateTime.now().minusHours(48);
        log.info("Starting cleanup of enquiries created before {}", expirationDate);
        enquiryRepository.deleteEnquiriesOlderThan(expirationDate);
        log.info("Enquiry cleanup completed");
    }
}
