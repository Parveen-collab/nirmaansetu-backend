package com.nirmaansetu.backend.modules.employee.service;

import com.nirmaansetu.backend.modules.employee.dto.EmployeeResponseDto;
import com.nirmaansetu.backend.modules.employee.mapper.EmployeeMapper;
import com.nirmaansetu.backend.modules.users.repository.EmployeeProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    // Repository for accessing employee profile data
    @Autowired
    private EmployeeProfileRepository employeeProfileRepository;

    // Mapper to convert Employee entities to Response DTOs
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * Retrieves all employees and maps them to DTOs
     * @return List of EmployeeResponseDto
     */
    public List<EmployeeResponseDto> getAllEmployees() {
        return employeeProfileRepository.findAll().stream()
                .map(employeeMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
