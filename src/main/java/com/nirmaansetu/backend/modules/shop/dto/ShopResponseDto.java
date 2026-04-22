package com.nirmaansetu.backend.modules.shop.dto;

import com.nirmaansetu.backend.modules.users.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopResponseDto {
    private Long id;
    private String name;
    private String address;
    private String contactNumber;
    private UserResponseDto owner;
    private List<MaterialResponseDto> materials;
    private Instant createdAt;
    private Instant updatedAt;
}
