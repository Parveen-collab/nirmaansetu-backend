package com.nirmaansetu.backend.modules.enquiries.repository;

import com.nirmaansetu.backend.modules.enquiries.entity.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Enquiry e WHERE e.createdAt < :expirationDate")
    void deleteEnquiriesOlderThan(LocalDateTime expirationDate);
}
