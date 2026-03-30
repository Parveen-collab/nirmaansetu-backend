package com.nirmaansetu.backend.modules.users.mapper;

import com.nirmaansetu.backend.modules.users.dto.UserRequestDto;
import com.nirmaansetu.backend.modules.users.dto.UserResponseDto;
import com.nirmaansetu.backend.modules.users.entity.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addresses", expression = "java(mapAddresses(dto.getAddresses()))")
    User toUser(UserRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addresses", expression = "java(updateAddresses(user, dto))")
    void updateUserFromDto(UserRequestDto dto, @MappingTarget User user);

    default List<Address> updateAddresses(User user, UserRequestDto dto) {
        if (dto.getAddresses() == null) return user.getAddresses();
        // For simplicity, replacing addresses if provided in the update DTO
        List<Address> newAddresses = mapAddresses(dto.getAddresses());
        if (newAddresses != null) {
            newAddresses.forEach(address -> address.setUser(user));
        }
        return newAddresses;
    }

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

    @Mapping(target = "type", source = "type", qualifiedByName = "addressTypeToString")
    UserRequestDto.AddressDto toAddressDto(Address address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    EmployeeProfile toEmployeeProfile(UserRequestDto.EmployeeProfileDto dto);

    UserRequestDto.EmployeeProfileDto toEmployeeProfileDto(EmployeeProfile profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    EmployerProfile toEmployerProfile(UserRequestDto.EmployerProfileDto dto);

    UserRequestDto.EmployerProfileDto toEmployerProfileDto(EmployerProfile profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "shopType", source = "shopType", qualifiedByName = "stringToShopType")
    SupplierProfile toSupplierProfile(UserRequestDto.SupplierProfileDto dto);

    @Mapping(target = "shopType", source = "shopType", qualifiedByName = "shopTypeToString")
    UserRequestDto.SupplierProfileDto toSupplierProfileDto(SupplierProfile profile);

    @Mapping(target = "message", constant = "Success")
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

    @Named("addressTypeToString")
    default String addressTypeToString(AddressType type) {
        return type != null ? type.name() : null;
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

    @Named("shopTypeToString")
    default String shopTypeToString(ShopType type) {
        return type != null ? type.name() : null;
    }
}
