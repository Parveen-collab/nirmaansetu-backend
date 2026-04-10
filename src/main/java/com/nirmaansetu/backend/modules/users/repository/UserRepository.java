package com.nirmaansetu.backend.modules.users.repository;


import com.nirmaansetu.backend.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findByRoleNot(com.nirmaansetu.backend.modules.users.entity.Role role);
    List<User> findByRole(com.nirmaansetu.backend.modules.users.entity.Role role);

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    boolean existsByAadhaarNumber(String aadhaarNumber);
    boolean existsByProfileImageUrl(String profileImageUrl);
    boolean existsByPhotoHash(String photoHash);
    boolean existsByPhotoHashAndIdNot(String photoHash, Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users WHERE deleted = true AND deleted_at <= :date", nativeQuery = true)
    void deleteSoftDeletedUsersOlderThan(@Param("date") LocalDateTime date);
}
