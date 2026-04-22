package com.nirmaansetu.backend.modules.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequestDto {

    @NotBlank(message = "Material name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    private Double price;

    @NotBlank(message = "Unit is required")
    private String unit;
}
