package com.nirmaansetu.backend.modules.enquiries.controller;

import com.nirmaansetu.backend.modules.enquiries.dto.EnquiryRequestDto;
import com.nirmaansetu.backend.modules.enquiries.dto.EnquiryResponseDto;
import com.nirmaansetu.backend.modules.enquiries.service.EnquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enquiries")
@RequiredArgsConstructor
@Tag(name = "Enquiry APIs", description = "Operations related to enquiries")
public class EnquiryController {

    private final EnquiryService enquiryService;

    @Operation(summary = "Send an enquiry")
    @PostMapping
    public ResponseEntity<EnquiryResponseDto> sendEnquiry(@Valid @RequestBody EnquiryRequestDto requestDto) {
        return ResponseEntity.ok(enquiryService.sendEnquiry(requestDto));
    }

    @Operation(
            summary = "Get all enquiries",
            description = "Only accessible by ADMIN or SUPER_ADMIN roles",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<EnquiryResponseDto>> getAllEnquiries() {
        return ResponseEntity.ok(enquiryService.getAllEnquiries());
    }
}
