package com.nirmaansetu.backend.modules.enquiries.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnquiryResponseDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String message;
    private LocalDateTime createdAt;
}
