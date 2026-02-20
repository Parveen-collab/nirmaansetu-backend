//a repository to handle database operations for the OTP.

package com.nirmaansetu.backend.repository;

import com.nirmaansetu.backend.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    Optional<OtpEntity> findByPhoneNumber(String phoneNumber);
}
