package com.nirmaansetu.backend.modules.shop.mapper;

import com.nirmaansetu.backend.modules.shop.dto.MaterialRequestDto;
import com.nirmaansetu.backend.modules.shop.dto.MaterialResponseDto;
import com.nirmaansetu.backend.modules.shop.dto.ShopRequestDto;
import com.nirmaansetu.backend.modules.shop.dto.ShopResponseDto;
import com.nirmaansetu.backend.modules.shop.entity.Material;
import com.nirmaansetu.backend.modules.shop.entity.Shop;
import com.nirmaansetu.backend.modules.users.mapper.UserMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShopMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "materials", ignore = true)
    Shop toShop(ShopRequestDto dto);

    ShopResponseDto toShopResponseDto(Shop shop);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shop", ignore = true)
    Material toMaterial(MaterialRequestDto dto);

    MaterialResponseDto toMaterialResponseDto(Material material);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "materials", ignore = true)
    void updateShopFromDto(ShopRequestDto dto, @MappingTarget Shop shop);
}
