package com.nirmaansetu.backend.modules.employer.mapper;

import com.nirmaansetu.backend.modules.employer.dto.EmployerResponseDto;
import com.nirmaansetu.backend.modules.users.entity.EmployerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployerMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "profileImageUrl", source = "user.profileImageUrl")
    EmployerResponseDto toResponseDto(EmployerProfile profile);
}
