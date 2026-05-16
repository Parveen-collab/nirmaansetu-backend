package com.nirmaansetu.backend.modules.employee.mapper;

import com.nirmaansetu.backend.modules.employee.dto.EmployeeResponseDto;
import com.nirmaansetu.backend.modules.users.entity.EmployeeProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for converting {@link EmployeeProfile} entity to {@link EmployeeResponseDto}.
 * This mapper uses MapStruct to handle the conversion logic, including flattening
 * nested user details.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    /**
     * Converts an EmployeeProfile to an EmployeeResponseDto.
     * Maps nested fields from the associated User entity (name, phoneNumber, email, profileImageUrl)
     * directly to the DTO's top-level fields.
     *
     * @param profile The source employee profile entity
     * @return The populated response DTO
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "profileImageUrl", source = "user.profileImageUrl")
    EmployeeResponseDto toResponseDto(EmployeeProfile profile);
}
