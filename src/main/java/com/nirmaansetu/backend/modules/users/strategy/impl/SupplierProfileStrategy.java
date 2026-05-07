package com.nirmaansetu.backend.modules.users.strategy.impl;

import com.nirmaansetu.backend.modules.recommendation.service.RecommendationService;
import com.nirmaansetu.backend.modules.users.dto.UserRequestDto;
import com.nirmaansetu.backend.modules.users.entity.Role;
import com.nirmaansetu.backend.modules.users.entity.SupplierProfile;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.mapper.UserMapper;
import com.nirmaansetu.backend.modules.users.repository.SupplierProfileRepository;
import com.nirmaansetu.backend.modules.users.strategy.ProfileStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SupplierProfileStrategy implements ProfileStrategy {

    @Autowired
    private SupplierProfileRepository supplierProfileRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private com.nirmaansetu.backend.shared.service.OcrVerificationService ocrVerificationService;

    @Autowired
    private com.nirmaansetu.backend.shared.service.FileService fileService;

    @Override
    public Role getRole() {
        return Role.SUPPLIER;
    }

    @Override
    public void createProfile(User user, UserRequestDto request, String photoUrl) {
        if (request.getSupplierProfile() != null) {
            SupplierProfile profile = userMapper.toSupplierProfile(request.getSupplierProfile());
            profile.setUser(user);
            profile.setPhotoUrl(photoUrl);
            
            // OCR Verification
            if (request.getSupplierProfile().getVerificationDocumentUrl() != null) {
                verifyProfile(profile, user, request.getSupplierProfile().getVerificationDocumentUrl());
            }
            
            supplierProfileRepository.save(profile);
            user.setSupplierProfile(profile);
            recommendationService.indexSupplierProfile(profile);
        }
    }

    private void verifyProfile(SupplierProfile profile, User user, String documentUrl) {
        String filePath = fileService.getFileSystemPath(documentUrl);
        if (filePath != null) {
            com.nirmaansetu.backend.shared.service.OcrVerificationService.VerificationResult result = ocrVerificationService.verifyDocument(
                    filePath, user.getName(), user.getAadhaarNumber());
            profile.setVerified(result.isSuccess());
        }
    }

    @Override
    public void updateProfile(User user, UserRequestDto request, String photoUrl) {
        SupplierProfile profile = user.getSupplierProfile();
        if (request.getSupplierProfile() != null) {
            if (profile == null) {
                profile = userMapper.toSupplierProfile(request.getSupplierProfile());
                profile.setUser(user);
                user.setSupplierProfile(profile);
            } else {
                userMapper.updateSupplierProfileFromDto(request.getSupplierProfile(), profile);
            }
            
            // Re-verify if document URL is updated
            if (request.getSupplierProfile().getVerificationDocumentUrl() != null) {
                verifyProfile(profile, user, request.getSupplierProfile().getVerificationDocumentUrl());
            }
        }
        if (photoUrl != null) {
            if (profile == null) {
                profile = new SupplierProfile();
                profile.setUser(user);
                user.setSupplierProfile(profile);
            }
            profile.setPhotoUrl(photoUrl);
        }
        if (profile != null) {
            supplierProfileRepository.save(profile);
            recommendationService.indexSupplierProfile(profile);
        }
    }
}
