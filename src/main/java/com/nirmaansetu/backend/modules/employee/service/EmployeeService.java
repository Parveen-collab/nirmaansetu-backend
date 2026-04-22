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

    @Autowired
    private EmployeeProfileRepository employeeProfileRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    public List<EmployeeResponseDto> getAllEmployees() {
        return employeeProfileRepository.findAll().stream()
                .map(employeeMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
