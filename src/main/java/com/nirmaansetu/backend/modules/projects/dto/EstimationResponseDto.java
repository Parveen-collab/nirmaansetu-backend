package com.nirmaansetu.backend.modules.projects.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstimationResponseDto {
    private List<MaterialEstimateDto> materials;
    private Double estimatedLaborCost;
    private Double totalEstimatedCost;
    private String aiSummary;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaterialEstimateDto {
        private String materialName;
        private Double quantity;
        private String unit;
        private Double unitPrice;
        private Double totalCost;
    }
}
