package com.nirmaansetu.backend.modules.reviews.controller;

import com.nirmaansetu.backend.modules.reviews.dto.ReviewRequestDto;
import com.nirmaansetu.backend.modules.reviews.dto.ReviewResponseDto;
import com.nirmaansetu.backend.modules.reviews.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Review APIs", description = "Operations related to user reviews and sentiment analysis")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "Create a new review",
            description = "Creates a review for a user and automatically performs sentiment analysis. Flags reviews with high negative sentiment.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(
            @Valid @RequestBody ReviewRequestDto request,
            Principal principal) {
        return ResponseEntity.ok(reviewService.createReview(request, principal.getName()));
    }

    @Operation(
            summary = "Get reviews for a user",
            description = "Returns all reviews received by a specific user.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/user/{id}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsForUser(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewsForUser(id));
    }

    @Operation(
            summary = "Get flagged reviews",
            description = "Returns all reviews that have been automatically flagged for admin review. Requires ADMIN or SUPER_ADMIN role.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/flagged")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<ReviewResponseDto>> getFlaggedReviews() {
        return ResponseEntity.ok(reviewService.getFlaggedReviews());
    }
}
