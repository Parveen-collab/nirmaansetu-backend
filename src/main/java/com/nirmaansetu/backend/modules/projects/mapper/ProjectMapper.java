package com.nirmaansetu.backend.modules.projects.mapper;

import com.nirmaansetu.backend.modules.projects.dto.ProjectRequestDto;
import com.nirmaansetu.backend.modules.projects.dto.ProjectResponseDto;
import com.nirmaansetu.backend.modules.projects.entity.Project;
import com.nirmaansetu.backend.modules.projects.entity.ProjectRole;
import com.nirmaansetu.backend.modules.users.mapper.UserMapper;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "roles", expression = "java(mapRoles(dto.getRoles(), project))")
    Project toProject(ProjectRequestDto dto, @Context Project project);

    @Mapping(target = "roles", expression = "java(mapRolesToDto(project.getRoles()))")
    ProjectResponseDto toProjectResponseDto(Project project);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    ProjectRole toProjectRole(ProjectRequestDto.ProjectRoleDto dto);

    ProjectRequestDto.ProjectRoleDto toProjectRoleDto(ProjectRole role);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "roles", expression = "java(updateRoles(project, dto))")
    void updateProjectFromDto(ProjectRequestDto dto, @MappingTarget Project project);

    default List<ProjectRole> mapRoles(List<ProjectRequestDto.ProjectRoleDto> dtos, @Context Project project) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            ProjectRole role = toProjectRole(dto);
            role.setProject(project);
            return role;
        }).collect(Collectors.toList());
    }

    default List<ProjectRequestDto.ProjectRoleDto> mapRolesToDto(List<ProjectRole> roles) {
        if (roles == null) return null;
        return roles.stream().map(this::toProjectRoleDto).collect(Collectors.toList());
    }

    default List<ProjectRole> updateRoles(Project project, ProjectRequestDto dto) {
        if (dto.getRoles() == null) return project.getRoles();
        
        project.getRoles().clear();
        List<ProjectRole> newRoles = mapRoles(dto.getRoles(), project);
        if (newRoles != null) {
            project.getRoles().addAll(newRoles);
        }
        return project.getRoles();
    }
}
