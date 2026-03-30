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

@Component
public class SupplierProfileStrategy implements ProfileStrategy {

    @Autowired
    private SupplierProfileRepository supplierProfileRepository;

    @Autowired
    private UserMapper userMapper;

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
            supplierProfileRepository.save(profile);
            user.setSupplierProfile(profile);
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
                if (request.getSupplierProfile().getShopName() != null)
                    profile.setShopName(request.getSupplierProfile().getShopName());
                if (request.getSupplierProfile().getShopCategory() != null)
                    profile.setShopCategory(request.getSupplierProfile().getShopCategory());
                if (request.getSupplierProfile().getShopSpeciality() != null)
                    profile.setShopSpeciality(request.getSupplierProfile().getShopSpeciality());
                if (request.getSupplierProfile().getShopAddress() != null)
                    profile.setShopAddress(request.getSupplierProfile().getShopAddress());
                if (request.getSupplierProfile().getShopType() != null)
                    profile.setShopType(userMapper.stringToShopType(request.getSupplierProfile().getShopType()));
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
        }
    }
}
