package com.nirmaansetu.backend.modules.users.strategy.impl;

import com.nirmaansetu.backend.modules.recommendation.service.RecommendationService;
import com.nirmaansetu.backend.modules.users.dto.UserRequestDto;
import com.nirmaansetu.backend.modules.users.entity.EmployeeProfile;
import com.nirmaansetu.backend.modules.users.entity.Role;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.mapper.UserMapper;
import com.nirmaansetu.backend.modules.users.repository.EmployeeProfileRepository;
import com.nirmaansetu.backend.modules.users.strategy.ProfileStrategy;
import com.nirmaansetu.backend.shared.service.OcrVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeProfileStrategy implements ProfileStrategy {

    @Autowired
    private EmployeeProfileRepository employeeProfileRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private OcrVerificationService ocrVerificationService;

    @Autowired
    private com.nirmaansetu.backend.shared.service.FileService fileService;

    @Override
    public Role getRole() {
        return Role.EMPLOYEE;
    }

    @Override
    public void createProfile(User user, UserRequestDto request, String photoUrl) {
        if (request.getEmployeeProfile() != null) {
            EmployeeProfile profile = userMapper.toEmployeeProfile(request.getEmployeeProfile());
            profile.setUser(user);
            profile.setPhotoUrl(photoUrl);
            
            // OCR Verification
            if (request.getEmployeeProfile().getVerificationDocumentUrl() != null) {
                verifyProfile(profile, user, request.getEmployeeProfile().getVerificationDocumentUrl());
            }
            
            employeeProfileRepository.save(profile);
            user.setEmployeeProfile(profile);
            recommendationService.indexEmployeeProfile(profile);
        }
    }

    private void verifyProfile(EmployeeProfile profile, User user, String documentUrl) {
        String filePath = fileService.getFileSystemPath(documentUrl);
        if (filePath != null) {
            OcrVerificationService.VerificationResult result = ocrVerificationService.verifyDocument(
                    filePath, user.getName(), user.getAadhaarNumber());
            profile.setVerified(result.isSuccess());
        }
    }

    @Override
    public void updateProfile(User user, UserRequestDto request, String photoUrl) {
        EmployeeProfile profile = user.getEmployeeProfile();
        if (request.getEmployeeProfile() != null) {
            if (profile == null) {
                profile = userMapper.toEmployeeProfile(request.getEmployeeProfile());
                profile.setUser(user);
                user.setEmployeeProfile(profile);
            } else {
                if (request.getEmployeeProfile().getServiceCategory() != null)
                    profile.setServiceCategory(request.getEmployeeProfile().getServiceCategory());
                if (request.getEmployeeProfile().getServiceSpeciality() != null)
                    profile.setServiceSpeciality(request.getEmployeeProfile().getServiceSpeciality());
                if (request.getEmployeeProfile().getExperienceYears() != 0)
                    profile.setExperienceYears(request.getEmployeeProfile().getExperienceYears());
                if (request.getEmployeeProfile().getVerificationDocumentUrl() != null)
                    profile.setVerificationDocumentUrl(request.getEmployeeProfile().getVerificationDocumentUrl());
            }
            
            // Re-verify if document URL is updated
            if (request.getEmployeeProfile().getVerificationDocumentUrl() != null) {
                verifyProfile(profile, user, request.getEmployeeProfile().getVerificationDocumentUrl());
            }
        }
        if (photoUrl != null) {
            if (profile == null) {
                profile = new EmployeeProfile();
                profile.setUser(user);
                user.setEmployeeProfile(profile);
            }
            profile.setPhotoUrl(photoUrl);
        }
        if (profile != null) {
            employeeProfileRepository.save(profile);
            recommendationService.indexEmployeeProfile(profile);
        }
    }
}
