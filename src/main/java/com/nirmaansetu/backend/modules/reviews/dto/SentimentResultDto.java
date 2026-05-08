package com.nirmaansetu.backend.modules.reviews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentimentResultDto {
    private String sentiment; // POSITIVE, NEGATIVE, NEUTRAL
    private Double score; // 0.0 to 1.0
    private boolean flagged;
    private String flagReason;
}
