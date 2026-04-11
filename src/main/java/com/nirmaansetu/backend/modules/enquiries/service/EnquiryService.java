package com.nirmaansetu.backend.modules.enquiries.service;

import com.nirmaansetu.backend.modules.enquiries.dto.EnquiryRequestDto;
import com.nirmaansetu.backend.modules.enquiries.dto.EnquiryResponseDto;
import com.nirmaansetu.backend.modules.enquiries.entity.Enquiry;
import com.nirmaansetu.backend.modules.enquiries.repository.EnquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnquiryService {

    private final EnquiryRepository enquiryRepository;

    public EnquiryResponseDto sendEnquiry(EnquiryRequestDto requestDto) {
        Enquiry enquiry = Enquiry.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .phone(requestDto.getPhone())
                .message(requestDto.getMessage())
                .build();

        Enquiry savedEnquiry = enquiryRepository.save(enquiry);
        return mapToResponseDto(savedEnquiry);
    }

    public List<EnquiryResponseDto> getAllEnquiries() {
        return enquiryRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private EnquiryResponseDto mapToResponseDto(Enquiry enquiry) {
        return EnquiryResponseDto.builder()
                .id(enquiry.getId())
                .name(enquiry.getName())
                .email(enquiry.getEmail())
                .phone(enquiry.getPhone())
                .message(enquiry.getMessage())
                .createdAt(enquiry.getCreatedAt())
                .build();
    }
}
