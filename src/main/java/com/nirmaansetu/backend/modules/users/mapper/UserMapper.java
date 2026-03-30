package com.nirmaansetu.backend.modules.users.mapper;

import com.nirmaansetu.backend.modules.users.dto.UserRequestDto;
import com.nirmaansetu.backend.modules.users.dto.UserResponseDto;
import com.nirmaansetu.backend.modules.users.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addresses", expression = "java(mapAddresses(dto.getAddresses()))")
    User toUser(UserRequestDto dto);

    default List<Address> mapAddresses(List<UserRequestDto.AddressDto> addressDtos) {
        if (addressDtos == null) return null;
        return addressDtos.stream()
                .map(this::toAddress)
                .toList();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "type", source = "type", qualifiedByName = "stringToAddressType")
    Address toAddress(UserRequestDto.AddressDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    EmployeeProfile toEmployeeProfile(UserRequestDto.EmployeeProfileDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    EmployerProfile toEmployerProfile(UserRequestDto.EmployerProfileDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "shopType", source = "shopType", qualifiedByName = "stringToShopType")
    SupplierProfile toSupplierProfile(UserRequestDto.SupplierProfileDto dto);

    @Mapping(target = "message", constant = "User registered successfully")
    UserResponseDto toUserResponseDto(User user);

    @Named("stringToAddressType")
    default AddressType stringToAddressType(String type) {
        if (type == null) return null;
        try {
            return AddressType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Named("stringToShopType")
    default ShopType stringToShopType(String type) {
        if (type == null) return null;
        try {
            return ShopType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
