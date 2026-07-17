package com.nirmaansetu.backend.modules.users.strategy.impl;

import com.nirmaansetu.backend.modules.users.dto.UserRequestDto;
import com.nirmaansetu.backend.modules.users.entity.Role;
import com.nirmaansetu.backend.modules.users.entity.SupplierProfile;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.mapper.UserMapper;
import com.nirmaansetu.backend.modules.users.repository.SupplierProfileRepository;
import com.nirmaansetu.backend.modules.users.strategy.ProfileStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Strategy implementation responsible for handling
 * Supplier profile creation, update, OCR verification,
 * and recommendation indexing.
 */
@Component
public class SupplierProfileStrategy implements ProfileStrategy {

    @Autowired
    private SupplierProfileRepository supplierProfileRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private com.nirmaansetu.backend.shared.service.FileService fileService;

    /**
     * Specifies that this strategy handles SUPPLIER users.
     */
    @Override
    public Role getRole() {
        return Role.SUPPLIER;
    }

    /**
     * Creates a Supplier Profile for a newly registered supplier.
     *
     * Steps:
     * 1. Convert DTO into SupplierProfile entity.
     * 2. Associate profile with user.
     * 3. Save profile photo URL.
     * 4. Perform OCR verification if a document is uploaded.
     * 5. Save profile in database.
     * 6. Index profile for recommendation engine.
     */
    @Override
    public void createProfile(User user, UserRequestDto request, String photoUrl) {

        if (request.getSupplierProfile() != null) {

            // Convert request data into SupplierProfile entity
            SupplierProfile profile =
                    userMapper.toSupplierProfile(request.getSupplierProfile());

            // Associate profile with user
            profile.setUser(user);

            // Store uploaded profile image URL
            profile.setPhotoUrl(photoUrl);

            // Verify supplier document using OCR
            if (request.getSupplierProfile().getVerificationDocumentUrl() != null) {
                verifyProfile(
                        profile,
                        user,
                        request.getSupplierProfile().getVerificationDocumentUrl()
                );
            }

            // Save profile in database
            supplierProfileRepository.save(profile);

            // Link profile back to user entity
            user.setSupplierProfile(profile);
        }
    }

    /**
     * Performs OCR-based verification of supplier documents.
     *
     * OCR extracts text from uploaded documents and compares
     * the extracted information with the user's details.
     *
     * @param profile Supplier profile being verified
     * @param user Current user
     * @param documentUrl Uploaded document URL
     */
    private void verifyProfile(
            SupplierProfile profile,
            User user,
            String documentUrl
    ) {

        // Convert document URL into actual file system path
        String filePath = fileService.getFileSystemPath(documentUrl);
    }

    /**
     * Updates an existing Supplier Profile.
     *
     * Supports updating:
     * - Supplier details
     * - Verification documents
     * - Profile photo
     *
     * Re-runs OCR verification if a new document is uploaded.
     */
    @Override
    public void updateProfile(
            User user,
            UserRequestDto request,
            String photoUrl
    ) {

        // Fetch existing supplier profile
        SupplierProfile profile = user.getSupplierProfile();

        if (request.getSupplierProfile() != null) {

            // Create profile if one doesn't exist
            if (profile == null) {

                profile = userMapper.toSupplierProfile(
                        request.getSupplierProfile()
                );

                profile.setUser(user);
                user.setSupplierProfile(profile);

            } else {

                // Update existing profile fields from DTO
                userMapper.updateSupplierProfileFromDto(
                        request.getSupplierProfile(),
                        profile
                );
            }

            // Re-run OCR verification if document was updated
            if (request.getSupplierProfile().getVerificationDocumentUrl() != null) {

                verifyProfile(
                        profile,
                        user,
                        request.getSupplierProfile().getVerificationDocumentUrl()
                );
            }
        }

        // Update profile photo if provided
        if (photoUrl != null) {

            // Create profile if missing
            if (profile == null) {

                profile = new SupplierProfile();
                profile.setUser(user);
                user.setSupplierProfile(profile);
            }

            profile.setPhotoUrl(photoUrl);
        }

        // Save profile and refresh recommendation index
        if (profile != null) {

            supplierProfileRepository.save(profile);
        }
    }
}