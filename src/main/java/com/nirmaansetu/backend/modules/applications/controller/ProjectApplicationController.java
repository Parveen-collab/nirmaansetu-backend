package com.nirmaansetu.backend.modules.applications.controller;

import com.nirmaansetu.backend.modules.applications.dto.ApplyWorkRequestDto;
import com.nirmaansetu.backend.modules.applications.service.ProjectApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/applications")
public class ProjectApplicationController {

    @Autowired
    private ProjectApplicationService projectApplicationService;

    @PostMapping("/apply")
    public ResponseEntity<String> applyForWork(@RequestBody ApplyWorkRequestDto dto) {
        projectApplicationService.applyForWork(dto);
        return ResponseEntity.ok("Application submitted successfully");
    }
}
