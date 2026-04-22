package com.nirmaansetu.backend.modules.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialResponseDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String unit;
    private Instant createdAt;
    private Instant updatedAt;
}
