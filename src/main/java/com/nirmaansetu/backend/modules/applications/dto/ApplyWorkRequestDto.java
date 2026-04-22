package com.nirmaansetu.backend.modules.applications.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyWorkRequestDto {
    private Long projectRoleId;
    private String coverLetter;
}
