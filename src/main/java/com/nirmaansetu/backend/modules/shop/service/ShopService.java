package com.nirmaansetu.backend.modules.shop.service;

import com.nirmaansetu.backend.modules.shop.dto.MaterialRequestDto;
import com.nirmaansetu.backend.modules.shop.dto.MaterialResponseDto;
import com.nirmaansetu.backend.modules.shop.dto.ShopRequestDto;
import com.nirmaansetu.backend.modules.shop.dto.ShopResponseDto;
import com.nirmaansetu.backend.modules.shop.entity.Material;
import com.nirmaansetu.backend.modules.shop.entity.Shop;
import com.nirmaansetu.backend.modules.shop.mapper.ShopMapper;
import com.nirmaansetu.backend.modules.shop.repository.MaterialRepository;
import com.nirmaansetu.backend.modules.shop.repository.ShopRepository;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopMapper shopMapper;

    @Transactional
    public ShopResponseDto createShop(ShopRequestDto dto) {
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        Shop shop = shopMapper.toShop(dto);
        shop.setOwner(currentUser);

        Shop savedShop = shopRepository.save(shop);
        return shopMapper.toShopResponseDto(savedShop);
    }

    public List<ShopResponseDto> getAllShops() {
        return shopRepository.findAll().stream()
                .map(shopMapper::toShopResponseDto)
                .collect(Collectors.toList());
    }

    public ShopResponseDto getShopById(Long id) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        return shopMapper.toShopResponseDto(shop);
    }

    @Transactional
    public MaterialResponseDto addMaterial(Long shopId, MaterialRequestDto dto) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        validateOwnership(shop);

        Material material = shopMapper.toMaterial(dto);
        material.setShop(shop);

        Material savedMaterial = materialRepository.save(material);
        return shopMapper.toMaterialResponseDto(savedMaterial);
    }

    private void validateOwnership(Shop shop) {
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!shop.getOwner().getPhoneNumber().equals(phoneNumber)) {
            throw new RuntimeException("Unauthorized: Only the owner can modify this shop");
        }
    }
}
