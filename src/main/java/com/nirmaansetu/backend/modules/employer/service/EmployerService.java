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

    // Repository for accessing employer profile data
    @Autowired
    private EmployerProfileRepository employerProfileRepository;

    // Mapper to convert Employer entities to Response DTOs
    @Autowired
    private EmployerMapper employerMapper;

    /**
     * Retrieves all employers and maps them to DTOs
     * @return List of EmployerResponseDto
     */
    public List<EmployerResponseDto> getAllEmployers() {
        return employerProfileRepository.findAll().stream()
                .map(employerMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single employer by their ID
     * @param id The ID of the employer
     * @return EmployerResponseDto containing employer details
     * @throws RuntimeException if employer is not found
     */
    public EmployerResponseDto getEmployerById(Long id) {
        EmployerProfile profile = employerProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employer not found with id: " + id));
        return employerMapper.toResponseDto(profile);
    }
}
