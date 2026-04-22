package com.nirmaansetu.backend.modules.employee.mapper;

import com.nirmaansetu.backend.modules.employee.dto.EmployeeResponseDto;
import com.nirmaansetu.backend.modules.users.entity.EmployeeProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "profileImageUrl", source = "user.profileImageUrl")
    EmployeeResponseDto toResponseDto(EmployeeProfile profile);
}
