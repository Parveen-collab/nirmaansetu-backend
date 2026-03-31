package com.nirmaansetu.backend.modules.users.repository;


import com.nirmaansetu.backend.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    boolean existsByAadhaarNumber(String aadhaarNumber);
    boolean existsByProfileImageUrl(String profileImageUrl);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users WHERE deleted = true AND deleted_at <= :date", nativeQuery = true)
    void deleteSoftDeletedUsersOlderThan(@Param("date") LocalDateTime date);
}
