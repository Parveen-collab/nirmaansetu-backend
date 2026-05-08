package com.nirmaansetu.backend.modules.reviews.repository;

import com.nirmaansetu.backend.modules.reviews.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReviewedUserId(Long reviewedUserId);
    List<Review> findByIsFlaggedTrue();
}
