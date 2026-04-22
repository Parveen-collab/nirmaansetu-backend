package com.nirmaansetu.backend.modules.employer.service;

import com.nirmaansetu.backend.modules.employer.dto.EmployerResponseDto;
import com.nirmaansetu.backend.modules.employer.mapper.EmployerMapper;
import com.nirmaansetu.backend.modules.users.entity.EmployerProfile;
import com.nirmaansetu.backend.modules.users.repository.EmployerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployerService {

    @Autowired
    private EmployerProfileRepository employerProfileRepository;

    @Autowired
    private EmployerMapper employerMapper;

    public List<EmployerResponseDto> getAllEmployers() {
        return employerProfileRepository.findAll().stream()
                .map(employerMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public EmployerResponseDto getEmployerById(Long id) {
        EmployerProfile profile = employerProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employer not found with id: " + id));
        return employerMapper.toResponseDto(profile);
    }
}
