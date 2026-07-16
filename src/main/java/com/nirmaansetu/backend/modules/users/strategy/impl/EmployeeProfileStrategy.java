package com.nirmaansetu.backend.modules.users.strategy.impl;

import com.nirmaansetu.backend.modules.recommendation.service.RecommendationService;
import com.nirmaansetu.backend.modules.users.dto.UserRequestDto;
import com.nirmaansetu.backend.modules.users.entity.EmployeeProfile;
import com.nirmaansetu.backend.modules.users.entity.Role;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.mapper.UserMapper;
import com.nirmaansetu.backend.modules.users.repository.EmployeeProfileRepository;
import com.nirmaansetu.backend.modules.users.strategy.ProfileStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for creating, updating, verifying, and indexing Employee profiles whenever a user with the EMPLOYEE role registers or updates their information.
 * Strategy implementation for handling Employee Profile creation and updates.
 * Implements the ProfileStrategy interface for EMPLOYEE role users.
 */
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

    /**
     * Returns the role supported by this strategy.
     */
    @Override
    public Role getRole() {
        return Role.EMPLOYEE;
    }

    /**
     * Creates a new Employee Profile for the user.
     *
     * Steps:
     * 1. Map DTO to Entity.
     * 2. Set user and profile photo.
     * 3. Verify uploaded document using OCR.
     * 4. Save profile.
     * 5. Link profile to user.
     * 6. Index profile for recommendation engine.
     */
    @Override
    public void createProfile(User user, UserRequestDto request, String photoUrl) {
        if (request.getEmployeeProfile() != null) {

            // Convert request DTO into EmployeeProfile entity
            EmployeeProfile profile = userMapper.toEmployeeProfile(request.getEmployeeProfile());

            // Associate profile with user
            profile.setUser(user);

            // Store profile photo URL
            profile.setPhotoUrl(photoUrl);

            // Perform OCR verification if document is provided
            if (request.getEmployeeProfile().getVerificationDocumentUrl() != null) {
                verifyProfile(
                        profile,
                        user,
                        request.getEmployeeProfile().getVerificationDocumentUrl()
                );
            }

            // Save profile in database
            employeeProfileRepository.save(profile);

            // Link profile back to user entity
            user.setEmployeeProfile(profile);

            // Index profile for AI recommendation/search features
            recommendationService.indexEmployeeProfile(profile);
        }
    }

    /**
     * Verifies employee document using OCR.
     *
     * OCR extracts information from uploaded document and
     * matches it against user's name and Aadhaar number.
     *
     * @param profile Employee profile being verified
     * @param user Current user
     * @param documentUrl Uploaded document URL
     */
    private void verifyProfile(EmployeeProfile profile, User user, String documentUrl) {

        // Convert URL into actual file system path
        String filePath = fileService.getFileSystemPath(documentUrl);

        if (filePath != null) {

            // Run OCR verification
            OcrVerificationService.VerificationResult result =
                    ocrVerificationService.verifyDocument(
                            filePath,
                            user.getName(),
                            user.getAadhaarNumber()
                    );

            // Mark profile as verified if OCR validation succeeds
            profile.setVerified(result.isSuccess());
        }
    }

    /**
     * Updates an existing Employee Profile.
     *
     * Supports:
     * - Service Category update
     * - Service Speciality update
     * - Experience update
     * - Verification document update
     * - Profile photo update
     *
     * Re-runs OCR verification if a new document is uploaded.
     */
    @Override
    public void updateProfile(User user, UserRequestDto request, String photoUrl) {

        // Fetch existing profile
        EmployeeProfile profile = user.getEmployeeProfile();

        if (request.getEmployeeProfile() != null) {

            // Create profile if it does not exist
            if (profile == null) {
                profile = userMapper.toEmployeeProfile(request.getEmployeeProfile());
                profile.setUser(user);
                user.setEmployeeProfile(profile);
            } else {

                // Update service category if provided
                if (request.getEmployeeProfile().getServiceCategory() != null)
                    profile.setServiceCategory(
                            request.getEmployeeProfile().getServiceCategory()
                    );

                // Update service speciality if provided
                if (request.getEmployeeProfile().getServiceSpeciality() != null)
                    profile.setServiceSpeciality(
                            request.getEmployeeProfile().getServiceSpeciality()
                    );

                // Update years of experience if provided
                if (request.getEmployeeProfile().getExperienceYears() != 0)
                    profile.setExperienceYears(
                            request.getEmployeeProfile().getExperienceYears()
                    );

                // Update verification document URL if provided
                if (request.getEmployeeProfile().getVerificationDocumentUrl() != null)
                    profile.setVerificationDocumentUrl(
                            request.getEmployeeProfile().getVerificationDocumentUrl()
                    );
            }

            // Re-run OCR verification when document changes
            if (request.getEmployeeProfile().getVerificationDocumentUrl() != null) {
                verifyProfile(
                        profile,
                        user,
                        request.getEmployeeProfile().getVerificationDocumentUrl()
                );
            }
        }

        // Update profile photo if new photo is uploaded
        if (photoUrl != null) {

            // Create profile if missing
            if (profile == null) {
                profile = new EmployeeProfile();
                profile.setUser(user);
                user.setEmployeeProfile(profile);
            }

            profile.setPhotoUrl(photoUrl);
        }

        // Save updated profile and refresh recommendation index
        if (profile != null) {
            employeeProfileRepository.save(profile);
            recommendationService.indexEmployeeProfile(profile);
        }
    }
}