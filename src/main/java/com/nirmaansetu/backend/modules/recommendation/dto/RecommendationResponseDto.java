package com.nirmaansetu.backend.modules.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponseDto {
    private Long userId;
    private String name;
    private String phoneNumber;
    private String serviceCategory;
    private String serviceSpeciality;
    private Integer experienceYears;
    private String location; // Detailed location string
    private Double score; // Relevance score from vector store
}
