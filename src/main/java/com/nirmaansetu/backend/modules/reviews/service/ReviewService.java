package com.nirmaansetu.backend.modules.reviews.service;

import com.nirmaansetu.backend.modules.reviews.dto.ReviewRequestDto;
import com.nirmaansetu.backend.modules.reviews.dto.ReviewResponseDto;
import com.nirmaansetu.backend.modules.reviews.dto.SentimentResultDto;
import com.nirmaansetu.backend.modules.reviews.entity.Review;
import com.nirmaansetu.backend.modules.reviews.repository.ReviewRepository;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final SentimentAnalysisService sentimentAnalysisService;

    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto request, String reviewerPhoneNumber) {
        User reviewer = userRepository.findByPhoneNumber(reviewerPhoneNumber)
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));

        User reviewedUser = userRepository.findById(request.getReviewedUserId())
                .orElseThrow(() -> new RuntimeException("Reviewed user not found"));

        if (reviewer.getId().equals(reviewedUser.getId())) {
            throw new RuntimeException("You cannot review yourself");
        }

        // Analyze sentiment
        SentimentResultDto sentimentResult = sentimentAnalysisService.analyzeSentiment(request.getComment());

        Review review = Review.builder()
                .reviewer(reviewer)
                .reviewedUser(reviewedUser)
                .rating(request.getRating())
                .comment(request.getComment())
                .sentiment(sentimentResult.getSentiment())
                .sentimentScore(sentimentResult.getScore())
                .isFlagged(sentimentResult.isFlagged())
                .flagReason(sentimentResult.getFlagReason())
                .build();

        Review savedReview = reviewRepository.save(review);
        log.info("Review created with ID: {}. Sentiment: {}. Flagged: {}", 
                savedReview.getId(), savedReview.getSentiment(), savedReview.isFlagged());

        return mapToResponseDto(savedReview);
    }

    public List<ReviewResponseDto> getReviewsForUser(Long reviewedUserId) {
        return reviewRepository.findByReviewedUserId(reviewedUserId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getFlaggedReviews() {
        return reviewRepository.findByIsFlaggedTrue().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private ReviewResponseDto mapToResponseDto(Review review) {
        return ReviewResponseDto.builder()
                .id(review.getId())
                .reviewerId(review.getReviewer().getId())
                .reviewerName(review.getReviewer().getName())
                .reviewedUserId(review.getReviewedUser().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .sentiment(review.getSentiment())
                .sentimentScore(review.getSentimentScore())
                .isFlagged(review.isFlagged())
                .flagReason(review.getFlagReason())
                .createdAt(LocalDateTime.from(review.getCreatedAt()))
                .build();
    }
}
