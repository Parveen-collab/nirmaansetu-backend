package com.nirmaansetu.backend.modules.recommendation.controller;

import com.nirmaansetu.backend.modules.recommendation.dto.RecommendationResponseDto;
import com.nirmaansetu.backend.modules.recommendation.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendations")
@Tag(name = "Recommendation", description = "Endpoints for AI-based matching and recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/employees")
    @Operation(summary = "Get recommended employees based on a query")
    public ResponseEntity<List<RecommendationResponseDto>> getRecommendedEmployees(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(recommendationService.recommendEmployees(query, limit));
    }

    @GetMapping("/suppliers")
    @Operation(summary = "Get recommended suppliers based on a query")
    public ResponseEntity<List<RecommendationResponseDto>> getRecommendedSuppliers(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(recommendationService.recommendSuppliers(query, limit));
    }

    @PostMapping("/reindex")
    @Operation(summary = "Manually trigger reindexing of all profiles (Admin only)")
    public ResponseEntity<String> reindexAll() {
        recommendationService.reindexAll();
        return ResponseEntity.ok("Reindexing triggered successfully");
    }
}
