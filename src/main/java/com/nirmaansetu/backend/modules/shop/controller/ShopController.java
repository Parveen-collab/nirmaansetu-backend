package com.nirmaansetu.backend.modules.shop.controller;

import com.nirmaansetu.backend.modules.shop.dto.MaterialRequestDto;
import com.nirmaansetu.backend.modules.shop.dto.MaterialResponseDto;
import com.nirmaansetu.backend.modules.shop.dto.ShopRequestDto;
import com.nirmaansetu.backend.modules.shop.dto.ShopResponseDto;
import com.nirmaansetu.backend.modules.shop.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shops")
@RequiredArgsConstructor
@Tag(name = "Shop APIs", description = "Operations related to shops and materials")
public class ShopController {

    private final ShopService shopService;

    @Operation(
            summary = "Create a new shop",
            description = "Registers a new shop in the system. The authenticated user becomes the owner.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<ShopResponseDto> createShop(@Valid @RequestBody ShopRequestDto dto) {
        return ResponseEntity.ok(shopService.createShop(dto));
    }

    @Operation(
            summary = "Get all shops",
            description = "Returns a list of all registered shops.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<List<ShopResponseDto>> getAllShops() {
        return ResponseEntity.ok(shopService.getAllShops());
    }

    @Operation(
            summary = "Get shop by ID",
            description = "Returns details of a specific shop by its unique identifier.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<ShopResponseDto> getShopById(@PathVariable Long id) {
        return ResponseEntity.ok(shopService.getShopById(id));
    }

    @Operation(
            summary = "Add material to shop",
            description = "Adds a new material to an existing shop. Only the shop owner can perform this action.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/{shopId}/materials")
    public ResponseEntity<MaterialResponseDto> addMaterial(
            @PathVariable Long shopId,
            @Valid @RequestBody MaterialRequestDto dto) {
        return ResponseEntity.ok(shopService.addMaterial(shopId, dto));
    }
}
