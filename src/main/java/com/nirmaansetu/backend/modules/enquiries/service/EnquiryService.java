package com.nirmaansetu.backend.modules.enquiries.service;

import com.nirmaansetu.backend.modules.enquiries.dto.EnquiryRequestDto;
import com.nirmaansetu.backend.modules.enquiries.dto.EnquiryResponseDto;
import com.nirmaansetu.backend.modules.enquiries.entity.Enquiry;
import com.nirmaansetu.backend.modules.enquiries.repository.EnquiryRepository;
import com.nirmaansetu.backend.modules.notifications.service.NotificationService;
import com.nirmaansetu.backend.modules.users.entity.Role;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnquiryService {

    private final EnquiryRepository enquiryRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public EnquiryResponseDto sendEnquiry(EnquiryRequestDto requestDto) {
        Enquiry enquiry = Enquiry.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .phone(requestDto.getPhone())
                .message(requestDto.getMessage())
                .build();

        Enquiry savedEnquiry = enquiryRepository.save(enquiry);

        // Notify all suppliers
        List<User> suppliers = userRepository.findByRole(Role.SUPPLIER);
        String title = "New Material Enquiry";
        String message = "A new enquiry has been received from " + savedEnquiry.getName() + ": " + savedEnquiry.getMessage();
        
        suppliers.forEach(supplier -> notificationService.createNotification(supplier, title, message));

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
