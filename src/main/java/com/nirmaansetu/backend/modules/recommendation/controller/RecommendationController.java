package com.nirmaansetu.backend.modules.recommendation.controller;

import com.nirmaansetu.backend.modules.recommendation.dto.RecommendationResponseDto;
import com.nirmaansetu.backend.modules.recommendation.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller responsible for handling AI-based recommendation requests.
 * Provides endpoints for:
 * 1. Employee recommendations
 * 2. Supplier recommendations
 * 3. Reindexing recommendation data
 */
@RestController
@RequestMapping("/api/v1/recommendations")
@Tag(name = "Recommendation", description = "Endpoints for AI-based matching and recommendations")
public class RecommendationController {

    /**
     * Service layer that contains recommendation business logic.
     */
    @Autowired
    private RecommendationService recommendationService;

    /**
     * Get recommended employees based on the provided search query.
     *
     * Example:
     * GET /api/v1/recommendations/employees?query=Electrician&limit=5
     *
     * @param query Search keyword entered by the user
     * @param limit Maximum number of recommendations to return
     * @return List of recommended employee profiles
     */
    @GetMapping("/employees")
    @Operation(summary = "Get recommended employees based on a query")
    public ResponseEntity<List<RecommendationResponseDto>> getRecommendedEmployees(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int limit) {

        return ResponseEntity.ok(
                recommendationService.recommendEmployees(query, limit)
        );
    }

    /**
     * Get recommended suppliers based on the provided search query.
     *
     * Example:
     * GET /api/v1/recommendations/suppliers?query=Cement Supplier&limit=5
     *
     * @param query Search keyword entered by the user
     * @param limit Maximum number of recommendations to return
     * @return List of recommended supplier profiles
     */
    @GetMapping("/suppliers")
    @Operation(summary = "Get recommended suppliers based on a query")
    public ResponseEntity<List<RecommendationResponseDto>> getRecommendedSuppliers(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int limit) {

        return ResponseEntity.ok(
                recommendationService.recommendSuppliers(query, limit)
        );
    }

    /**
     * Manually triggers reindexing of all employee and supplier profiles.
     *
     * Reindexing updates the recommendation engine's search index
     * so newly added or modified profiles become searchable.
     *
     * Example:
     * POST /api/v1/recommendations/reindex
     *
     * @return Success message after triggering reindexing
     */
    @PostMapping("/reindex")
    @Operation(summary = "Manually trigger reindexing of all profiles (Admin only)")
    public ResponseEntity<String> reindexAll() {

        recommendationService.reindexAll();

        return ResponseEntity.ok(
                "Reindexing triggered successfully"
        );
    }
}

//Flow of this Controller
//User sends a request to recommendation APIs.
//Controller receives the request.
//Controller extracts parameters (query, limit).
//Controller calls RecommendationService.
//Service performs AI-based matching/recommendation.
//Controller returns the recommended results to the frontend.
//Admin can call /reindex to rebuild the recommendation index when profiles are added or updated.