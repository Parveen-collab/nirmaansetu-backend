package com.nirmaansetu.backend.modules.reviews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private Long reviewerId;
    private String reviewerName;
    private Long reviewedUserId;
    private Integer rating;
    private String comment;
    private String sentiment;
    private Double sentimentScore;
    private boolean isFlagged;
    private String flagReason;
    private LocalDateTime createdAt;
}
