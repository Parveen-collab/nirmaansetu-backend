package com.nirmaansetu.backend.modules.projects.service;

import com.nirmaansetu.backend.modules.projects.dto.EstimationResponseDto;
import com.nirmaansetu.backend.modules.shop.entity.Material;
import com.nirmaansetu.backend.modules.shop.repository.MaterialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EstimationService {

    private final ChatClient chatClient;
    private final MaterialRepository materialRepository;

    public EstimationService(ChatClient.Builder chatClientBuilder, MaterialRepository materialRepository) {
        this.chatClient = chatClientBuilder.build();
        this.materialRepository = materialRepository;
    }

    public EstimationResponseDto estimateProject(String description) {
        log.info("Generating project estimation for: {}", description);
        List<Material> availableMaterials = materialRepository.findAll();
        
        String materialsContext = availableMaterials.isEmpty() 
                ? "No specific material price data available. Please suggest based on market rates."
                : availableMaterials.stream()
                .map(m -> String.format("- %s: %.2f per %s", m.getName(), m.getPrice(), m.getUnit()))
                .collect(Collectors.joining("\n"));

        log.debug("Using materials context: {}", materialsContext);

        String template = """
                You are an expert construction cost and material estimator.
                Based on the project description provided by the employer, suggest the required materials, their quantities, and estimate the labor cost.
                Use the provided material prices to calculate the costs if available.
                
                Project Description: {description}
                
                Available Materials and Prices from local shops:
                {materialsContext}
                
                Instructions:
                1. Suggest realistic quantities for a project of this scale.
                2. If a required material is not in the list, suggest it with current market prices and mention it in the summary.
                3. Calculate the labor cost based on the complexity of the project.
                4. Ensure the response is a valid JSON matching the requested structure.
                """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(Map.of(
                "description", description,
                "materialsContext", materialsContext
        ));

        try {
            EstimationResponseDto response = chatClient.prompt(prompt)
                    .call()
                    .entity(new ParameterizedTypeReference<EstimationResponseDto>() {});
            
            log.info("Estimation generated successfully with total cost: {}", response.getTotalEstimatedCost());
            return response;
        } catch (Exception e) {
            log.error("Error generating AI estimation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate project estimation: " + e.getMessage());
        }
    }
}
