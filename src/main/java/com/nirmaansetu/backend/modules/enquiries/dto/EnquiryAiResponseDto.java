package com.nirmaansetu.backend.modules.enquiries.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnquiryAiResponseDto {
    private String workType;
    private String urgency;
}
