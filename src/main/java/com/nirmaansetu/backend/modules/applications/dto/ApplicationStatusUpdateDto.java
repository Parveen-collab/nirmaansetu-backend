package com.nirmaansetu.backend.modules.applications.dto;

import com.nirmaansetu.backend.modules.applications.enums.ApplicationStatus;
import lombok.Data;

@Data
public class ApplicationStatusUpdateDto {
    private ApplicationStatus status;
}
